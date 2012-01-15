/****************************************************************************
**
** Copyright (C) 2011 Nokia Corporation and/or its subsidiary(-ies).
** All rights reserved.
** Contact: Nokia Corporation (qt-info@nokia.com)
**
** This file is part of the plugins of the Qt Toolkit.
**
** $QT_BEGIN_LICENSE:LGPL$
** GNU Lesser General Public License Usage
** This file may be used under the terms of the GNU Lesser General Public
** License version 2.1 as published by the Free Software Foundation and
** appearing in the file LICENSE.LGPL included in the packaging of this
** file. Please review the following information to ensure the GNU Lesser
** General Public License version 2.1 requirements will be met:
** http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html.
**
** In addition, as a special exception, Nokia gives you certain additional
** rights. These rights are described in the Nokia Qt LGPL Exception
** version 1.1, included in the file LGPL_EXCEPTION.txt in this package.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU General
** Public License version 3.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of this
** file. Please review the following information to ensure the GNU General
** Public License version 3.0 requirements will be met:
** http://www.gnu.org/copyleft/gpl.html.
**
** Other Usage
** Alternatively, this file may be used in accordance with the terms and
** conditions contained in a signed written agreement between you and Nokia.
**
**
**
**
**
** $QT_END_LICENSE$
**
****************************************************************************/
/*
 * Later changes, and modification for omap fb implemention:
 * Copyright (C) 2012 Pawel Kolodziejski <aquadran at users.sourceforge.net>
 *
 */

#include "pvrlinuxfbdrawable_p.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <linux/fb.h>
#include <fcntl.h>
#include <unistd.h>

PvrLinuxFBDisplay pvrLinuxFBDisplay;

static void pvrLinuxFBDestroyDrawableForced(PvrLinuxFBDrawable *drawable);

static int pvrLinuxFBInitFbScreen(void) {
    struct fb_var_screeninfo var;
    struct fb_fix_screeninfo fix;
    unsigned long start;
    unsigned long length;
    int width, height, stride;
    PVR2DFORMAT format;
    void *mapped;
    int fd, bytesPerPixel;
    PVR2DMEMINFO *memInfo;
    unsigned long pageAddresses[2];

    if (pvrLinuxFBDisplay.screen.initialized)
        return 1;

    fd = open("/dev/fb0", O_RDONLY, 0);
    if (fd < 0) {
        perror("LINUXFBWSEGL: error open /dev/fb0");
        return 0;
    }
    if (ioctl(fd, FBIOGET_VSCREENINFO, &var) < 0) {
        perror("LINUXFBWSEGL: FBIOGET_VSCREENINFO");
        close(fd);
        return 0;
    }
    if (ioctl(fd, FBIOGET_FSCREENINFO, &fix) < 0) {
        perror("LINUXFBWSEGL: FBIOGET_FSCREENINFO");
        close(fd);
        return 0;
    }
    width = var.xres;
    height = var.yres;
    bytesPerPixel = var.bits_per_pixel / 8;
    stride = fix.line_length;
    if (var.bits_per_pixel == 16) {
        if (var.red.length == 5 && var.green.length == 6 &&
            var.blue.length == 5 && var.red.offset == 11 &&
            var.green.offset == 5 && var.blue.offset == 0) {
            format = PVR2D_RGB565;
        }
        if (var.red.length == 4 && var.green.length == 4 &&
            var.blue.length == 4 && var.transp.length == 4 &&
            var.red.offset == 8 && var.green.offset == 4 &&
            var.blue.offset == 0 && var.transp.offset == 12) {
            format = PVR2D_ARGB4444;
        }
    } else if (var.bits_per_pixel == 32) {
        if (var.red.length == 8 && var.green.length == 8 &&
            var.blue.length == 8 && var.transp.length == 8 &&
            var.red.offset == 16 && var.green.offset == 8 &&
            var.blue.offset == 0 && var.transp.offset == 24) {
            format = PVR2D_ARGB8888;
        }
    } else {
        fprintf(stderr, "LINUXFBWSEGL: %s: could not find a suitable PVR2D pixel format\n", "/dev/fb0");
        close(fd);
        return 0;
    }
    start = fix.smem_start;
    length = var.xres_virtual * var.yres_virtual * bytesPerPixel;

    mapped = mmap(0, length, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (!mapped || mapped == (void *)(-1)) {
        perror("LINUXFBWSEGL: mmap");
        close(fd);
        return 0;
    }

    /* Allocate a PVR2D memory region for the framebuffer */
    memInfo = 0;
    if (pvrLinuxFBDisplay.context) {
        pageAddresses[0] = start & 0xFFFFF000;
        pageAddresses[1] = 0;
        if (PVR2DMemWrap(pvrLinuxFBDisplay.context, mapped, PVR2D_WRAPFLAG_CONTIGUOUS, length, pageAddresses, &memInfo) != PVR2D_OK) {
            munmap(mapped, length);
            close(fd);
            return 0;
        }
    }

    /* We don't need the file descriptor any more */
    close(fd);

    /* The framebuffer is ready, so initialize the PvrLinuxFBScreenInfo */
    pvrLinuxFBDisplay.screen.screenRect.x = 0;
    pvrLinuxFBDisplay.screen.screenRect.y = 0;
    pvrLinuxFBDisplay.screen.screenRect.width = width;
    pvrLinuxFBDisplay.screen.screenRect.height = height;
    pvrLinuxFBDisplay.screen.screenStride = stride;
    pvrLinuxFBDisplay.screen.pixelFormat = format;
    pvrLinuxFBDisplay.screen.bytesPerPixel = bytesPerPixel;
    pvrLinuxFBDisplay.screen.screenDrawable = 0;
    if (mapped) {
        pvrLinuxFBDisplay.screen.frameBuffer = memInfo;
        pvrLinuxFBDisplay.screen.mapped = mapped;
    }
    pvrLinuxFBDisplay.screen.mappedLength = length;
    pvrLinuxFBDisplay.screen.screenStart = start;
    pvrLinuxFBDisplay.screen.needsUnmap = (mapped != 0);
    pvrLinuxFBDisplay.screen.initialized = 1;
    return 1;
}

/* Called when a new drawable is added to ensure that we have a
   PVR2D context and framebuffer PVR2DMEMINFO blocks */
static int pvrLinuxFBAddDrawable(void) {
    int numDevs, screen;
    PVR2DDEVICEINFO *devs;
    unsigned long devId;
    unsigned long pageAddresses[2];
    PVR2DMEMINFO *memInfo;
    PVR2DDISPLAYINFO displayInfo;

    /* Bail out early if this is not the first drawable */
    if (pvrLinuxFBDisplay.numDrawables > 0) {
        ++(pvrLinuxFBDisplay.numDrawables);
        return 1;
    }

    /* Find the first PVR2D device in the system and open it */
    numDevs = PVR2DEnumerateDevices(0);
    if (numDevs <= 0)
        return 0;
    devs = (PVR2DDEVICEINFO *)malloc(sizeof(PVR2DDEVICEINFO) * numDevs);
    if (!devs)
        return 0;
    if (PVR2DEnumerateDevices(devs) != PVR2D_OK) {
        free(devs);
        return 0;
    }
    devId = devs[0].ulDevID;
    free(devs);
    if (PVR2DCreateDeviceContext(devId, &pvrLinuxFBDisplay.context, 0) != PVR2D_OK)
        return 0;
    pvrLinuxFBDisplay.numFlipBuffers = 0;
    pvrLinuxFBDisplay.flipChain = 0;
    if (PVR2DGetDeviceInfo(pvrLinuxFBDisplay.context, &displayInfo) == PVR2D_OK) {
        if (displayInfo.ulMaxFlipChains > 0 && displayInfo.ulMaxBuffersInChain > 0)
            pvrLinuxFBDisplay.numFlipBuffers = displayInfo.ulMaxBuffersInChain;
        if (pvrLinuxFBDisplay.numFlipBuffers > PVRLINUXFB_MAX_FLIP_BUFFERS)
            pvrLinuxFBDisplay.numFlipBuffers = PVRLINUXFB_MAX_FLIP_BUFFERS;
    }

    if (!pvrLinuxFBDisplay.screen.mapped) {
        PVR2DDestroyDeviceContext(pvrLinuxFBDisplay.context);
        pvrLinuxFBDisplay.context = 0;
        return 0;
    }

    /* Create a flip chain for the screen if supported by the hardware */
    pvrLinuxFBDisplay.usePresentBlit = 0;
    if (pvrLinuxFBDisplay.numFlipBuffers > 0) {
        long stride = 0;
        unsigned long flipId = 0;
        unsigned long numBuffers;
        if (PVR2DCreateFlipChain(pvrLinuxFBDisplay.context, 0,
                                 //PVR2D_CREATE_FLIPCHAIN_SHARED |
                                 //PVR2D_CREATE_FLIPCHAIN_QUERY,
                                 pvrLinuxFBDisplay.numFlipBuffers, pvrLinuxFBDisplay.screen.screenRect.width,
                                 pvrLinuxFBDisplay.screen.screenRect.height, pvrLinuxFBDisplay.screen.pixelFormat,
                                 &stride, &flipId, &(pvrLinuxFBDisplay.flipChain)) == PVR2D_OK) {
            pvrLinuxFBDisplay.screen.screenStride = stride;
            PVR2DGetFlipChainBuffers(pvrLinuxFBDisplay.context, pvrLinuxFBDisplay.flipChain, &numBuffers, pvrLinuxFBDisplay.flipBuffers);
        } else {
            pvrLinuxFBDisplay.flipChain = 0;
            pvrLinuxFBDisplay.numFlipBuffers = 0;
        }

        /* PVR2DPresentBlt is a little more reliable than PVR2DBlt
           when flip chains are present, even if we cannot create a
           flip chain at the moment */
        pvrLinuxFBDisplay.usePresentBlit = 1;
    }

    /* The context is ready to go */
    ++(pvrLinuxFBDisplay.numDrawables);
    return 1;
}

/* Called when the last drawable is destroyed.  The PVR2D context
   will be destroyed but the raw framebuffer memory will stay mapped */
static void pvrLinuxFBDestroyContext(void) {
    if (pvrLinuxFBDisplay.screen.frameBuffer) {
        PVR2DMemFree(pvrLinuxFBDisplay.context, pvrLinuxFBDisplay.screen.frameBuffer);
            pvrLinuxFBDisplay.screen.frameBuffer = 0;
    }

    if (pvrLinuxFBDisplay.numFlipBuffers > 0)
        PVR2DDestroyFlipChain(pvrLinuxFBDisplay.context, pvrLinuxFBDisplay.flipChain);
    PVR2DDestroyDeviceContext(pvrLinuxFBDisplay.context);
    pvrLinuxFBDisplay.context = 0;
    pvrLinuxFBDisplay.flipChain = 0;
    pvrLinuxFBDisplay.numFlipBuffers = 0;
    pvrLinuxFBDisplay.usePresentBlit = 0;
}

int pvrLinuxFBDisplayOpen(void) {
    if (pvrLinuxFBDisplay.refCount > 0) {
        ++(pvrLinuxFBDisplay.refCount);
        return 1;
    }

    /* Open the framebuffer and map it directly */
    if (!pvrLinuxFBInitFbScreen()) {
        --(pvrLinuxFBDisplay.refCount);
        return 0;
    }

    /* The display is open and ready */
    ++(pvrLinuxFBDisplay.refCount);
    return 1;
}

void pvrLinuxFBDisplayClose(void) {
    if (pvrLinuxFBDisplay.refCount == 0)
        return;
    if (--(pvrLinuxFBDisplay.refCount) > 0)
        return;

    /* Prevent pvrQwsDestroyContext from being called for the time being */
    ++pvrLinuxFBDisplay.numDrawables;

    PvrLinuxFBScreenInfo *info = &(pvrLinuxFBDisplay.screen);
    if (info->screenDrawable)
        pvrLinuxFBDestroyDrawableForced(info->screenDrawable);
    if (info->frameBuffer)
        PVR2DMemFree(pvrLinuxFBDisplay.context, info->frameBuffer);
    if (info->mapped && info->needsUnmap)
        munmap(info->mapped, info->mappedLength);

    /* Now it is safe to destroy the PVR2D context */
    --pvrLinuxFBDisplay.numDrawables;
    if (pvrLinuxFBDisplay.context)
        PVR2DDestroyDeviceContext(pvrLinuxFBDisplay.context);

    memset(&pvrLinuxFBDisplay, 0, sizeof(pvrLinuxFBDisplay));
}

int pvrLinuxFBDisplayIsOpen(void) {
    return (pvrLinuxFBDisplay.refCount > 0);
}

/* Ensure that a specific screen has been initialized */
static int pvrLinuxFBEnsureScreen(int screen) {
    if (screen != 0)
        return 0;
    return pvrLinuxFBInitFbScreen();
}

PvrLinuxFBDrawable *pvrLinuxFBScreenWindow(int screen) {
    PvrLinuxFBDrawable *drawable;

    if (!pvrLinuxFBEnsureScreen(screen))
        return 0;

    drawable = pvrLinuxFBDisplay.screen.screenDrawable;
    if (drawable)
        return drawable;

    drawable = (PvrLinuxFBDrawable *)calloc(1, sizeof(PvrLinuxFBDrawable));
    if (!drawable)
        return 0;

    drawable->type = PvrLinuxFBScreen;
    drawable->screen = screen;
    drawable->pixelFormat = pvrLinuxFBDisplay.screen.pixelFormat;
    drawable->rect = pvrLinuxFBDisplay.screen.screenRect;
    drawable->visibleRects[0] = drawable->rect;
    drawable->numVisibleRects = 1;
    drawable->isFullScreen = 1;

    if (!pvrLinuxFBAddDrawable()) {
        free(drawable);
        return 0;
    }

    pvrLinuxFBDisplay.screen.screenDrawable = drawable;

    return drawable;
}

PvrLinuxFBDrawable *pvrLinuxFBCreateWindow(int screen, long winId, const PvrLinuxFBRect *rect) {
    PvrLinuxFBDrawable *drawable;

    if (!pvrLinuxFBEnsureScreen(screen))
        return 0;

    drawable = (PvrLinuxFBDrawable *)calloc(1, sizeof(PvrLinuxFBDrawable));
    if (!drawable)
        return 0;

    drawable->type = PvrLinuxFBWindow;
    drawable->winId = winId;
    drawable->refCount = 1;
    drawable->screen = screen;
    drawable->pixelFormat = pvrLinuxFBDisplay.screen.pixelFormat;
    drawable->rect = *rect;

    if (!pvrLinuxFBAddDrawable()) {
        free(drawable);
        return 0;
    }

    drawable->nextWinId = pvrLinuxFBDisplay.firstWinId;
    pvrLinuxFBDisplay.firstWinId = drawable;

    return drawable;
}

PvrLinuxFBDrawable *pvrLinuxFBFetchWindow(long winId) {
    PvrLinuxFBDrawable *drawable = pvrLinuxFBDisplay.firstWinId;
    while (drawable != 0 && drawable->winId != winId)
        drawable = drawable->nextWinId;

    if (drawable)
        ++(drawable->refCount);
    return drawable;
}

int pvrLinuxFBReleaseWindow(PvrLinuxFBDrawable *drawable) {
    if (drawable->type == PvrLinuxFBWindow)
        return (--(drawable->refCount) <= 0);
    else
        return 0;
}

PvrLinuxFBDrawable *pvrLinuxFBCreatePixmap(int width, int height, int screen) {
    PvrLinuxFBDrawable *drawable;

    if (!pvrLinuxFBEnsureScreen(screen))
        return 0;

    drawable = (PvrLinuxFBDrawable *)calloc(1, sizeof(PvrLinuxFBDrawable));
    if (!drawable)
        return 0;

    drawable->type = PvrLinuxFBPixmap;
    drawable->screen = screen;
    drawable->pixelFormat = pvrLinuxFBDisplay.screen.pixelFormat;
    drawable->rect.x = 0;
    drawable->rect.y = 0;
    drawable->rect.width = width;
    drawable->rect.height = height;

    if (!pvrLinuxFBAddDrawable()) {
        free(drawable);
        return 0;
    }

    return drawable;
}

static void pvrLinuxFBDestroyDrawableForced(PvrLinuxFBDrawable *drawable) {
    /* Remove the drawable from the display's winId list */
    PvrLinuxFBDrawable *current = pvrLinuxFBDisplay.firstWinId;
    PvrLinuxFBDrawable *prev = 0;
    while (current != 0 && current != drawable) {
        prev = current;
        current = current->nextWinId;
    }
    if (current != 0) {
        if (prev)
            prev->nextWinId = current->nextWinId;
        else
            pvrLinuxFBDisplay.firstWinId = current->nextWinId;
    }

    pvrLinuxFBFreeBuffers(drawable);
    free(drawable);

    --pvrLinuxFBDisplay.numDrawables;
    if (pvrLinuxFBDisplay.numDrawables == 0)
        pvrLinuxFBDestroyContext();
}

void pvrLinuxFBDestroyDrawable(PvrLinuxFBDrawable *drawable) {
    if (drawable && drawable->type != PvrLinuxFBScreen)
        pvrLinuxFBDestroyDrawableForced(drawable);
}

PvrLinuxFBDrawableType pvrLinuxFBGetDrawableType(PvrLinuxFBDrawable *drawable) {
    return drawable->type;
}

void pvrLinuxFBSetVisibleRegion(PvrLinuxFBDrawable *drawable, const PvrLinuxFBRect *rects, int numRects) {
    int index, indexOut;
    PvrLinuxFBRect *rect;
    PvrLinuxFBRect *screenRect;

    /* Visible regions don't make sense for pixmaps */
    if (drawable->type == PvrLinuxFBPixmap)
        return;

    /* Restrict the number of rectangles to prevent buffer overflow */
    if (numRects > PVRLINUXFB_MAX_VISIBLE_RECTS)
        numRects = PVRLINUXFB_MAX_VISIBLE_RECTS;
    if (numRects > 0)
        memcpy(drawable->visibleRects, rects, numRects * sizeof(PvrLinuxFBRect));

    /* Convert the rectangles into screen-relative co-ordinates and
       then clamp them to the screen boundaries.  If any of the
       clamped rectangles are empty, remove them from the list */
    screenRect = &(pvrLinuxFBDisplay.screen.screenRect);
    indexOut = 0;
    for (index = 0, rect = drawable->visibleRects; index < numRects; ++index, ++rect) {
        if (rect->x < 0) {
            rect->width += rect->x;
            rect->x = 0;
            if (rect->width < 0)
                rect->width = 0;
        } else if (rect->x >= screenRect->width) {
            rect->x = screenRect->width;
            rect->width = 0;
        }
        if ((rect->x + rect->width) > screenRect->width) {
            rect->width = screenRect->width - rect->x;
        }
        if (rect->y < 0) {
            rect->height += rect->y;
            rect->y = 0;
            if (rect->height < 0)
                rect->height = 0;
        } else if (rect->y >= screenRect->height) {
            rect->y = screenRect->height;
            rect->height = 0;
        }
        if ((rect->y + rect->height) > screenRect->height) {
            rect->height = screenRect->height - rect->y;
        }
        if (rect->width > 0 && rect->height > 0) {
            if (index != indexOut)
                drawable->visibleRects[indexOut] = *rect;
            ++indexOut;
        }
    }
    drawable->numVisibleRects = indexOut;
}

void pvrLinuxFBClearVisibleRegion(PvrLinuxFBDrawable *drawable) {
    if (drawable->type != PvrLinuxFBPixmap)
        drawable->numVisibleRects = 0;
}

void pvrLinuxFBSetGeometry(PvrLinuxFBDrawable *drawable, const PvrLinuxFBRect *rect) {
    /* We can only change the geometry of window drawables */
    if (drawable->type != PvrLinuxFBWindow)
        return;

    /* If the position has changed, then clear the visible region */
    if (drawable->rect.x != rect->x || drawable->rect.y != rect->y) {
        drawable->rect.x = rect->x;
        drawable->rect.y = rect->y;
        drawable->numVisibleRects = 0;
    }

    /* If the size has changed, then clear the visible region and
       invalidate the drawable's buffers.  Invalidating the buffers
       will force EGL to recreate the drawable, which will then
       allocate new buffers for the new size */
    if (drawable->rect.width != rect->width ||
            drawable->rect.height != rect->height) {
        drawable->rect.width = rect->width;
        drawable->rect.height = rect->height;
        drawable->numVisibleRects = 0;
        pvrLinuxFBInvalidateBuffers(drawable);
    }
}

void pvrLinuxFBGetGeometry(PvrLinuxFBDrawable *drawable, PvrLinuxFBRect *rect) {
    *rect = drawable->rect;
}

void pvrLinuxFBSetRotation(PvrLinuxFBDrawable *drawable, int angle) {
    if (drawable->rotationAngle != angle) {
        drawable->rotationAngle = angle;

        /* Force the buffers to be recreated if the rotation angle changes */
        pvrLinuxFBInvalidateBuffers(drawable);
    }
}

int pvrLinuxFBGetStride(PvrLinuxFBDrawable *drawable) {
    if (drawable->backBuffersValid)
        return drawable->strideBytes;
    else
        return 0;
}

PvrLinuxFBPixelFormat pvrLinuxFBGetPixelFormat(PvrLinuxFBDrawable *drawable) {
    return (PvrLinuxFBPixelFormat)(drawable->pixelFormat);
}

void *pvrLinuxFBGetRenderBuffer(PvrLinuxFBDrawable *drawable) {
    if (drawable->backBuffersValid)
        return drawable->backBuffers[drawable->currentBackBuffer]->pBase;
    else
        return 0;
}

int pvrLinuxFBAllocBuffers(PvrLinuxFBDrawable *drawable) {
    int index;
    int numBuffers = PVRLINUXFB_MAX_BACK_BUFFERS;
    if (drawable->type == PvrLinuxFBPixmap)
        numBuffers = 1;
    if (drawable->backBuffers[0]) {
        if (drawable->backBuffersValid)
            return 1;
        if (!drawable->usingFlipBuffers) {
            for (index = 0; index < numBuffers; ++index)
                PVR2DMemFree(pvrLinuxFBDisplay.context, drawable->backBuffers[index]);
        }
    }
    drawable->stridePixels = (drawable->rect.width + 31) & ~31;
    drawable->strideBytes = drawable->stridePixels * pvrLinuxFBDisplay.screen.bytesPerPixel;
    drawable->usingFlipBuffers = (pvrLinuxFBDisplay.numFlipBuffers > 0 && drawable->isFullScreen);
    if (drawable->usingFlipBuffers) {
        if (numBuffers > (int)(pvrLinuxFBDisplay.numFlipBuffers))
            numBuffers = pvrLinuxFBDisplay.numFlipBuffers;
        for (index = 0; index < numBuffers; ++index)
            drawable->backBuffers[index] = pvrLinuxFBDisplay.flipBuffers[index];
    } else {
        for (index = 0; index < numBuffers; ++index) {
            if (PVR2DMemAlloc(pvrLinuxFBDisplay.context, drawable->strideBytes * drawable->rect.height,
                              128, 0, &(drawable->backBuffers[index])) != PVR2D_OK) {
                while (--index >= 0)
                    PVR2DMemFree(pvrLinuxFBDisplay.context, drawable->backBuffers[index]);
                memset(drawable->backBuffers, 0, sizeof(drawable->backBuffers));
                drawable->backBuffersValid = 0;
                return 0;
            }
        }
    }
    for (index = numBuffers; index < PVRLINUXFB_MAX_BACK_BUFFERS; ++index) {
        drawable->backBuffers[index] = drawable->backBuffers[0];
    }
    drawable->backBuffersValid = 1;
    drawable->currentBackBuffer = 0;
    return 1;
}

void pvrLinuxFBFreeBuffers(PvrLinuxFBDrawable *drawable) {
    int index;
    int numBuffers = PVRLINUXFB_MAX_BACK_BUFFERS;
    if (drawable->type == PvrLinuxFBPixmap)
        numBuffers = 1;
    if (!drawable->usingFlipBuffers) {
        for (index = 0; index < numBuffers; ++index) {
            if (drawable->backBuffers[index])
                PVR2DMemFree(pvrLinuxFBDisplay.context, drawable->backBuffers[index]);
        }
    }
    memset(drawable->backBuffers, 0, sizeof(drawable->backBuffers));
    drawable->backBuffersValid = 0;
    drawable->usingFlipBuffers = 0;
}

void pvrLinuxFBInvalidateBuffers(PvrLinuxFBDrawable *drawable) {
    drawable->backBuffersValid = 0;
}

int pvrLinuxFBGetBuffers(PvrLinuxFBDrawable *drawable, PVR2DMEMINFO **source, PVR2DMEMINFO **render) {
    if (!drawable->backBuffersValid)
        return 0;
    *render = drawable->backBuffers[drawable->currentBackBuffer];
    *source = drawable->backBuffers[(drawable->currentBackBuffer + PVRLINUXFB_MAX_BACK_BUFFERS - 1) % PVRLINUXFB_MAX_BACK_BUFFERS];
    return 1;
}

int pvrLinuxFBSwapBuffers(PvrLinuxFBDrawable *drawable, int repaintOnly) {
    PVR2DMEMINFO *buffer;
    PvrLinuxFBRect *rect;
    int index;

    /* Bail out if the back buffers have been invalidated */
    if (!drawable->backBuffersValid)
        return 0;

    /* If there is a swap function, then use that instead */
    if (drawable->swapFunction) {
        (*(drawable->swapFunction))(drawable, drawable->userData, repaintOnly);
        if (!repaintOnly) {
            drawable->currentBackBuffer = (drawable->currentBackBuffer + 1) % PVRLINUXFB_MAX_BACK_BUFFERS;
        }
        return 1;
    }

    /* Iterate through the visible rectangles and blit them to the screen */
    if (!repaintOnly) {
        index = drawable->currentBackBuffer;
    } else {
        index = (drawable->currentBackBuffer + PVRLINUXFB_MAX_BACK_BUFFERS - 1) % PVRLINUXFB_MAX_BACK_BUFFERS;
    }
    buffer = drawable->backBuffers[index];
    rect = drawable->visibleRects;
    if (drawable->usingFlipBuffers) {
        PVR2DPresentFlip(pvrLinuxFBDisplay.context, pvrLinuxFBDisplay.flipChain, buffer, 0);
    } else if (pvrLinuxFBDisplay.usePresentBlit && drawable->numVisibleRects > 0) {
        PVR2DRECT pvrRects[PVRLINUXFB_MAX_VISIBLE_RECTS];
        for (index = 0; index < drawable->numVisibleRects; ++index, ++rect) {
            pvrRects[index].left = rect->x;
            pvrRects[index].top = rect->y;
            pvrRects[index].right = rect->x + rect->width;
            pvrRects[index].bottom = rect->y + rect->height;
        }
        for (index = 0; index < drawable->numVisibleRects; index += 4) {
            int numClip = drawable->numVisibleRects - index;
            if (numClip > 4)    /* No more than 4 clip rects at a time */
                numClip = 4;
            PVR2DSetPresentBltProperties(pvrLinuxFBDisplay.context,
                 PVR2D_PRESENT_PROPERTY_SRCSTRIDE |
                 PVR2D_PRESENT_PROPERTY_DSTSIZE |
                 PVR2D_PRESENT_PROPERTY_DSTPOS |
                 PVR2D_PRESENT_PROPERTY_CLIPRECTS,
                 drawable->strideBytes,
                 drawable->rect.width, drawable->rect.height,
                 drawable->rect.x, drawable->rect.y,
                 numClip, pvrRects + index, 0);
            PVR2DPresentBlt(pvrLinuxFBDisplay.context, buffer, 0);
        }
        PVR2DQueryBlitsComplete(pvrLinuxFBDisplay.context, buffer, 1);
    } else {
        /* TODO: use PVR2DBltClipped for faster transfers of clipped windows */
        PVR2DBLTINFO blit;
        for (index = 0; index < drawable->numVisibleRects; ++index, ++rect) {
            memset(&blit, 0, sizeof(blit));

            blit.CopyCode = PVR2DROPcopy;
            blit.BlitFlags = PVR2D_BLIT_DISABLE_ALL;

            blit.pSrcMemInfo = buffer;
            blit.SrcStride = drawable->strideBytes;
            blit.SrcX = rect->x - drawable->rect.x;
            blit.SrcY = rect->y - drawable->rect.y;
            blit.SizeX = rect->width;
            blit.SizeY = rect->height;
            blit.SrcFormat = drawable->pixelFormat;

            blit.pDstMemInfo = pvrLinuxFBDisplay.screen.frameBuffer;
            blit.DstStride = pvrLinuxFBDisplay.screen.screenStride;
            blit.DstX = rect->x;
            blit.DstY = rect->y;
            blit.DSizeX = rect->width;
            blit.DSizeY = rect->height;
            blit.DstFormat = pvrLinuxFBDisplay.screen.pixelFormat;

            PVR2DBlt(pvrLinuxFBDisplay.context, &blit);
        }
    }

    /* Swap the buffers */
    if (!repaintOnly) {
        drawable->currentBackBuffer = (drawable->currentBackBuffer + 1) % PVRLINUXFB_MAX_BACK_BUFFERS;
    }
    return 1;
}

void pvrLinuxFBSetSwapFunction(PvrLinuxFBDrawable *drawable, PvrLinuxFBSwapFunction func, void *userData) {
    drawable->swapFunction = func;
    drawable->userData = userData;
}
