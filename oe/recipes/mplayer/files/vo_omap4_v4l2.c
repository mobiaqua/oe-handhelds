/*
 * video output for OMAP4 V4L2 NV12
 *
 * Copyright (C) 2011 Pawel Kolodziejski
 *
 * Inspired by omapfbplay created by Mans Rullgard
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */
 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include <sys/mman.h>
#include <sys/ioctl.h>
#include <linux/videodev2.h>
#include <linux/fb.h>

#include "config.h"
#include "video_out.h"
#include "video_out_internal.h"
#include "fastmemcpy.h"
#include "sub/sub.h"
#include "mp_msg.h"

#include "libswscale/swscale.h"
#include "libmpcodecs/vf_scale.h"
#include "libavcodec/avcodec.h"

#include "aspect.h"

#include "subopt-helper.h"

static vo_info_t info = {
	"omap4_v4l2 video driver",
	"omap4_v4l2",
	"",
	""
};

LIBVO_EXTERN(omap4_v4l2)

#define ALIGN(value, align) (((value) + ((align) - 1)) & ~((align) - 1))

static struct fb_var_screeninfo display_info;
static int v4l2_handle = -1;
static struct v4l2_format v4l2_vout_format;
static struct v4l2_crop v4l2_vout_crop;
static int v4l2_num_buffers;
static int v4l2_offset[3], v4l2_stride[3];
static struct v4l2_buffer tmp_v4l2_buffer;

static struct v4l2_buf {
	struct v4l2_buffer buffer;
	unsigned char *plane[3];
} *v4l2_buffers = NULL;

static struct frame_info {
	unsigned w;
	unsigned h;
	unsigned dx;
	unsigned dy;
	unsigned dw;
	unsigned dh;
	unsigned y_stride;
	unsigned uv_stride;
	enum PixelFormat format;
} yuv420_frame_info, v4l2_frame_info;

extern int yuv420_to_nv12_convert(unsigned char *vdst[3], unsigned char *vsrc[3], unsigned char *, unsigned char *);
extern void yuv420_to_nv12_open(struct frame_info *dst, struct frame_info *src);

static int preinit(const char *arg)
{
	int fb_handle;
	struct v4l2_capability v4l2_cap;
	struct v4l2_fmtdesc v4l2_formatdesc;

	fb_handle = open("/dev/fb0", O_RDONLY);
	if (fb_handle == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error open /dev/fb0\n");
		return -1;
	}
	if (ioctl(fb_handle, FBIOGET_VSCREENINFO, &display_info) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error get FB screen info (FBIOGET_VSCREENINFO)\n");
		return -1;
	}
	close(fb_handle);

	v4l2_handle = open("/dev/video1", O_RDONLY);
	if (v4l2_handle == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error open /dev/video1\n");
		return -1;
	}

	if (ioctl(v4l2_handle, VIDIOC_QUERYCAP, &v4l2_cap) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error query capabilities (VIDIOC_QUERYCAP)\n");
		return -1;
	}
	v4l2_cap.capabilities &= (V4L2_CAP_STREAMING | V4L2_CAP_VIDEO_OUTPUT);
	if (v4l2_cap.capabilities != (V4L2_CAP_STREAMING | V4L2_CAP_VIDEO_OUTPUT)) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error V4L2 missing support for video streaming\n");
		return -1;
	}

	v4l2_formatdesc.index = 0;
	v4l2_formatdesc.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	while (!ioctl(v4l2_handle, VIDIOC_ENUM_FMT, &v4l2_formatdesc)) {
		if (v4l2_formatdesc.pixelformat == V4L2_PIX_FMT_NV12)
			break;
		v4l2_formatdesc.index++;
	}
	if (v4l2_formatdesc.pixelformat != V4L2_PIX_FMT_NV12) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error V4L2 missing NV12 format support\n");
		return -1;
	}

	v4l2_num_buffers = 2;

	return 0;
}

static int config(uint32_t width, uint32_t height, uint32_t d_width, uint32_t d_height, uint32_t flags, char *title, uint32_t format)
{
	int i, k, stream_on_off;
	struct v4l2_requestbuffers request_buf;
	unsigned char *ptr_mmap;
	struct v4l2_format overlay_format = { 0 };

	stream_on_off = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	ioctl(v4l2_handle, VIDIOC_STREAMOFF, &stream_on_off);

	if (format != IMGFMT_YV12) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error wrong pixel format at config()\n");
		return -1;
	}

	yuv420_frame_info.w = ALIGN(width, 32);
	yuv420_frame_info.h = ALIGN(height, 32);

	v4l2_vout_format.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	v4l2_vout_format.fmt.pix.width = yuv420_frame_info.w;
	v4l2_vout_format.fmt.pix.height = yuv420_frame_info.h;
	v4l2_vout_format.fmt.pix.pixelformat = V4L2_PIX_FMT_NV12;
	v4l2_vout_format.fmt.pix.field = V4L2_FIELD_NONE;
	if (ioctl(v4l2_handle, VIDIOC_S_FMT, &v4l2_vout_format) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error setup video format (VIDIOC_S_FMT)\n");
		return -1;
	}

	v4l2_offset[0] = 0;
	v4l2_stride[0] = v4l2_vout_format.fmt.pix.bytesperline;
	v4l2_offset[1] = v4l2_vout_format.fmt.pix.height * v4l2_vout_format.fmt.pix.bytesperline;
	v4l2_stride[1] = v4l2_vout_format.fmt.pix.bytesperline;
	v4l2_offset[2] = v4l2_stride[2] = 0;

	yuv420_frame_info.dx = 0;
	yuv420_frame_info.dy = 0;
	yuv420_frame_info.dw = width;
	yuv420_frame_info.dh = height;
	yuv420_frame_info.y_stride = yuv420_frame_info.w;
	yuv420_frame_info.uv_stride = yuv420_frame_info.w;
	yuv420_frame_info.format = PIX_FMT_YUV420P;
	v4l2_frame_info.w = ALIGN(width, 32);
	v4l2_frame_info.h = ALIGN(height, 32);
	v4l2_frame_info.dx = 0;
	v4l2_frame_info.dy = 0;
	v4l2_frame_info.dw = width;
	v4l2_frame_info.dh = height;
	v4l2_frame_info.y_stride = v4l2_stride[0];
	v4l2_frame_info.uv_stride = v4l2_stride[1];
	v4l2_frame_info.format = PIX_FMT_NV12;
	yuv420_to_nv12_open(&yuv420_frame_info, &v4l2_frame_info);

	request_buf.count = v4l2_num_buffers;
	request_buf.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	request_buf.memory = V4L2_MEMORY_MMAP;
	if (ioctl(v4l2_handle, VIDIOC_REQBUFS, &request_buf) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error request buffers setup video format (VIDIOC_S_FMT)\n");
		return -1;
	}
	v4l2_num_buffers = request_buf.count;
	v4l2_buffers = malloc(v4l2_num_buffers * sizeof(struct v4l2_buf));
	if (!v4l2_buffers) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error allocate v4l2 info buffers\n");
		return -1;
	}

	for (i = 0; i < v4l2_num_buffers; i++) {
		v4l2_buffers[i].buffer.index = i;
		v4l2_buffers[i].buffer.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
		v4l2_buffers[i].buffer.memory = V4L2_MEMORY_MMAP;
		if (ioctl(v4l2_handle, VIDIOC_QUERYBUF, &v4l2_buffers[i].buffer) == -1) {
			mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error query buffers (VIDIOC_QUERYBUF)\n");
			return -1;
		}
		ptr_mmap = mmap(NULL, v4l2_buffers[i].buffer.length, PROT_READ | PROT_WRITE, MAP_SHARED, v4l2_handle, v4l2_buffers[i].buffer.m.offset);
		if (ptr_mmap == MAP_FAILED) {
			mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error mmap video buffer\n");
			return -1;
		}
		memset(ptr_mmap, 0, v4l2_buffers[i].buffer.length);
		for (k = 0; k < 3; k++) {
			v4l2_buffers[i].plane[k] = ptr_mmap + v4l2_offset[k];
		}
		if (ioctl(v4l2_handle, VIDIOC_QBUF, &v4l2_buffers[i].buffer) == -1) {
			mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error queue buffer (VIDIOC_QBUF)\n");
			return -1;
		}
	}

	v4l2_vout_crop.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	v4l2_vout_crop.c.left = 0;
	v4l2_vout_crop.c.top = 0;
	v4l2_vout_crop.c.width = yuv420_frame_info.dw;
	v4l2_vout_crop.c.height = yuv420_frame_info.dh;
	if (ioctl(v4l2_handle, VIDIOC_S_CROP, &v4l2_vout_crop) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error crop video output (VIDIOC_S_CROP)\n");
		return -1;
	}

	overlay_format.type = V4L2_BUF_TYPE_VIDEO_OVERLAY;
	overlay_format.fmt.win.w.left = 0;
	overlay_format.fmt.win.w.top = 0;
	overlay_format.fmt.win.w.width = display_info.xres;
	overlay_format.fmt.win.w.height = display_info.yres;
	overlay_format.fmt.win.field = V4L2_FIELD_NONE;
	overlay_format.fmt.win.global_alpha = 255;
	if (ioctl(v4l2_handle, VIDIOC_S_FMT, &overlay_format) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error configure overlay (VIDIOC_S_FMT)\n");
		return -1;
	}

	stream_on_off = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	if (ioctl(v4l2_handle, VIDIOC_STREAMON, &stream_on_off) == -1) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error turn on streaming (VIDIOC_STREAMON)\n");
		return -1;
	}

	return 0;
}

static void draw_alpha(int x0, int y0, int w, int h, unsigned char *src, unsigned char *srca, int stride)
{
//    vo_draw_alpha_yuy2(w, h, src, srca, stride, fb_pages[page].buf + y0 * finfo.line_length + x0 * 2, finfo.line_length);
}

static void draw_osd(void)
{
//    vo_draw_text(sinfo.xres, sinfo.yres, draw_alpha);
}

static int draw_frame(uint8_t *src[])
{
	return 1;
}

static int draw_slice(uint8_t *src[], int stride[], int w, int h, int x, int y)
{
	if ((x != 0) || (y != 0)) {
		mp_msg(MSGT_VO, MSGL_FATAL, "[omap4l2] Error offsets are not 0x0\n");
		return 0;
	}

	tmp_v4l2_buffer.type = V4L2_BUF_TYPE_VIDEO_OUTPUT;
	tmp_v4l2_buffer.memory = V4L2_MEMORY_MMAP;
	ioctl(v4l2_handle, VIDIOC_DQBUF, &tmp_v4l2_buffer);
	yuv420_to_nv12_convert(v4l2_buffers[tmp_v4l2_buffer.index].plane, src, NULL, NULL);

	return 0;
}

static void flip_page(void)
{
	ioctl(v4l2_handle, VIDIOC_QBUF, &v4l2_buffers[tmp_v4l2_buffer.index].buffer);
}

static int query_format(uint32_t format)
{
	if (format != IMGFMT_YV12)
		return 0;

	return VFCAP_CSP_SUPPORTED | VFCAP_CSP_SUPPORTED_BY_HW | VFCAP_OSD | VFCAP_SWSCALE | VFCAP_ACCEPT_STRIDE;
}

static void uninit()
{
	int i, stream_off;

	if (vo_config_count > 0) {
		stream_off = V4L2_BUF_TYPE_VIDEO_OUTPUT;
		ioctl(v4l2_handle, VIDIOC_STREAMOFF, &stream_off);

		if (v4l2_buffers) {
			for (i = 0; i < v4l2_num_buffers; i++) {
				if (v4l2_buffers[i].plane[0]) {
					munmap(v4l2_buffers[i].plane[0], v4l2_buffers[i].buffer.length);
				}
			}
			free(v4l2_buffers);
			v4l2_buffers = NULL;
		}
	}
	if (v4l2_handle != -1) {
		close(v4l2_handle);
		v4l2_handle = -1;
	}
}

static int control(uint32_t request, void *data)
{
	switch (request) {
	case VOCTRL_QUERY_FORMAT:
		return query_format(*((uint32_t*)data));
	case VOCTRL_FULLSCREEN:
		{
			if (WinID > 0)
				return VO_FALSE;
			return VO_TRUE;
		}
	case VOCTRL_UPDATE_SCREENINFO:
		return VO_TRUE;
	}

	return VO_NOTIMPL;
}

static void check_events(void)
{
}
