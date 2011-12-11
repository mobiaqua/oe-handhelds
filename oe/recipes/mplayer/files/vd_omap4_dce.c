/*
 * OMAP 4 ducati hardware decoding for codecs:
 * h264, h263, mpeg4, divx 4-5, xvid, 3ivx, mpeg1, mpeg2, wmv 1-3, vc1
 *
 * Copyright (C) 2011 Pawel Kolodziejski <aquadran at users.sourceforge.net>
 *
 * Some parts inspired by omapfbplay code written by Mans Rullgard
 *
 * This file is part of MPlayer.
 *
 * MPlayer is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * MPlayer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with MPlayer; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>

#include "config.h"

#include "mp_msg.h"
#include "help_mp.h"

#include "vd_internal.h"
#include "dec_video.h"

static const vd_info_t info = {
	"OMAP4 ducati video codecs",
	"omap4_dce",
	"",
	"",
	""
};

LIBVD_EXTERN(omap4_dce)

static int control(sh_video_t *sh, int cmd, void *arg, ...) {
	switch (cmd) {
	case VDCTRL_QUERY_FORMAT:
		{
			int format = (*((int *)arg));
			if (format == IMGFMT_NV12) {
				return CONTROL_TRUE;
			} else {
				return CONTROL_FALSE;
			}
		}
		case VDCTRL_RESYNC_STREAM:
		{
			// ???
			return CONTROL_UNKNOWN;
		}
		case VDCTRL_QUERY_UNSEEN_FRAMES:
		{
			// for now no delay
			return 0;
		}
		case VDCTRL_QUERY_MAX_PP_LEVEL:
		{
			return 9; // ???
		}
		case VDCTRL_SET_PP_LEVEL:
		{
			int level = (*((int*)arg));
			if (!sh->context)
				return CONTROL_ERROR;
			return CONTROL_OK;
		}
	}

	return CONTROL_UNKNOWN;
}

static int init(sh_video_t *sh) {
	unsigned int out_fmt = sh->codec->outfmt[0];

	return 1;
}

static void uninit(sh_video_t *sh) {

}

static mp_image_t *decode(sh_video_t *sh, void *data, int len, int flags) {
	mp_image_t *mpi;

	if (len <= 0)
		return NULL; // skipped frame




	return mpi;
}
