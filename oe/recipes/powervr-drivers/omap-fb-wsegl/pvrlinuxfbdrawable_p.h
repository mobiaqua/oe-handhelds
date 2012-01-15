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

#ifndef PVRLINUXFBDRAWABLE_P_H
#define PVRLINUXFBDRAWABLE_P_H

#include <pvr2d/pvr2d.h>
#include "pvrlinuxfbdrawable.h"

#ifdef __cplusplus
extern "C" {
#endif

#define PVRLINUXFB_MAX_VISIBLE_RECTS    32
#define PVRLINUXFB_MAX_BACK_BUFFERS     3
#define PVRLINUXFB_MAX_FLIP_BUFFERS     3

typedef struct {
    PvrLinuxFBRect      screenRect;
    int                 screenStride;
    PVR2DFORMAT         pixelFormat;
    int                 bytesPerPixel;
    PVR2DMEMINFO       *frameBuffer;
    PvrLinuxFBDrawable *screenDrawable;
    void               *mapped;
    int                 mappedLength;
    unsigned long       screenStart;
    int                 needsUnmap;
    int                 initialized;
} PvrLinuxFBScreenInfo;

typedef struct {
    int                 refCount;
    PvrLinuxFBScreenInfo screen;
    PVR2DCONTEXTHANDLE  context;
    int                 numDrawables;
    unsigned long       numFlipBuffers;
    PVR2DFLIPCHAINHANDLE flipChain;
    PVR2DMEMINFO       *flipBuffers[PVRLINUXFB_MAX_FLIP_BUFFERS];
    int                 usePresentBlit;
    PvrLinuxFBDrawable  *firstWinId;

} PvrLinuxFBDisplay;

extern PvrLinuxFBDisplay pvrLinuxFBDisplay;

struct _PvrLinuxFBDrawable {
    PvrLinuxFBDrawableType  type;
    long                winId;
    int                 refCount;
    PvrLinuxFBRect      rect;
    int                 screen;
    PVR2DFORMAT         pixelFormat;
    PvrLinuxFBRect      visibleRects[PVRLINUXFB_MAX_VISIBLE_RECTS];
    int                 numVisibleRects;
    PVR2DMEMINFO       *backBuffers[PVRLINUXFB_MAX_BACK_BUFFERS];
    int                 currentBackBuffer;
    int                 backBuffersValid;
    int                 usingFlipBuffers;
    int                 isFullScreen;
    int                 strideBytes;
    int                 stridePixels;
    int                 rotationAngle;
    PvrLinuxFBSwapFunction  swapFunction;
    void               *userData;
    PvrLinuxFBDrawable *nextWinId;
};

/* Get the current source and render buffers for a drawable */
int pvrLinuxFBGetBuffers(PvrLinuxFBDrawable *drawable, PVR2DMEMINFO **source, PVR2DMEMINFO **render);

#ifdef __cplusplus
};
#endif

#endif
