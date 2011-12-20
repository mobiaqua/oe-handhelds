/*
 * OMAP 4 ducati hardware decoding for following video codecs:
 * h264, h263, MPEG4, DivX 4/5, XviD, 3ivx, MPEG1, MPEG2, WMV9, VC-1
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

#include <xdc/std.h>
#include <ti/sdo/ce/Engine.h>
#include <ti/sdo/ce/video3/viddec3.h>
#include <ti/sdo/codecs/h264dec/ih264vdec.h>
#include <ti/sdo/codecs/mpeg4dec/impeg4vdec.h>
#include <ti/sdo/codecs/mpeg2vdec/impeg2vdec.h>
#include <ti/sdo/codecs/vc1vdec/ivc1vdec.h>
#include <timemmgr/tilermem.h>
#include <timemmgr/memmgr.h>
#include <dce.h>

#include <linux/videodev2.h>

#include "config.h"

#include "mp_msg.h"
#include "help_mp.h"

#include "vd_internal.h"
#include "dec_video.h"
#include "vd_omap4_dce.h"
#include "../mp_core.h"
#include "../libmpdemux/parse_es.h"
#include "../libvo/video_out.h"
#include "../ffmpeg/libavcodec/avcodec.h"

static const vd_info_t info = {
	"OMAP4 ducati video codecs decoder",
	"omap4_dce",
	"",
	"",
	""
};

LIBVD_EXTERN(omap4_dce)

static Engine_Handle codec_engine;
static VIDDEC3_Handle codec_handle;
static VIDDEC3_Params *codec_params;
static VIDDEC3_DynamicParams *codec_dynamic_params;
static VIDDEC3_Status *codec_status;
static XDM2_BufDesc *codec_input_buffers;
static XDM2_BufDesc *codec_output_buffers;
static VIDDEC3_InArgs *codec_input_args;
static VIDDEC3_OutArgs *codec_output_args;
static int codec_error;

static uint8_t *input_buf;
static uint32_t input_phys;
static int input_size;

static int frame_id;
static int frame_width, frame_height;

struct omap4_dce_priv omap4_dce_priv_t;

#define ALIGN2(value, align) (((value) + ((1 << (align)) - 1)) & ~((1 << (align)) - 1))

void codec_engine_close(void);

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
			// flush codec engine
			codec_error = VIDDEC3_control(codec_handle, XDM_FLUSH, codec_dynamic_params, codec_status);
			if (codec_error != VIDDEC3_EOK) {
				mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: VIDDEC3_control(XDM_FLUSH) failed %d\n", codec_error);
				return CONTROL_ERROR;
			}
			codec_input_args->inputID = 0;
			codec_input_buffers->numBufs = 0;
			codec_input_buffers->descs[0].bufSize.bytes = 0;
			do {
				codec_error = VIDDEC3_process(codec_handle, codec_input_buffers, codec_output_buffers,
							codec_input_args , codec_output_args);
			} while (codec_error != XDM_EFAIL);
			if (omap4_dce_priv_t.reset_buffers)
				if (omap4_dce_priv_t.reset_buffers() == VO_TRUE)
					return CONTROL_OK;
			return CONTROL_ERROR;
		}
		case VDCTRL_QUERY_UNSEEN_FRAMES:
		{
			return 0; // FIXME
		}
	}

	return CONTROL_UNKNOWN;
}

static int init(sh_video_t *sh) {
	unsigned int codec_id = CODEC_ID_NONE;

	sh->context = &omap4_dce_priv_t;

	switch (sh->format) {
	case 0x10000005:
	case mmioFOURCC('H','2','6','4'):
		codec_id = CODEC_ID_H264;
		break;
	case 0x10000004:
	case mmioFOURCC('F','M','P','4'):
	case mmioFOURCC('X','V','I','D'):
	case mmioFOURCC('D','X','5','0'):
	case mmioFOURCC('D','X','G','M'):
		codec_id = CODEC_ID_MPEG4;
		break;
	case 0x10000002:
	case mmioFOURCC('m','p','g','2'):
	case mmioFOURCC('M','P','G','2'):
	case mmioFOURCC('M','7','0','1'):
		codec_id = CODEC_ID_MPEG2VIDEO;
		break;
	case 0x10000001:
	case 0x00000001:
	case mmioFOURCC('m','p','g','1'):
	case mmioFOURCC('M','P','G','1'):
		codec_id = CODEC_ID_MPEG1VIDEO;
		break;
	case mmioFOURCC('W','V','C','1'):
		codec_id = CODEC_ID_VC1;
		break;
	case mmioFOURCC('W','M','V','3'):
		codec_id = CODEC_ID_WMV3; // not working - fatal error
		break;
	default:
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] ------ Unsupported codec id: %08x, tag: '%04s' ------\n", sh->format, &sh->format);
		return 0;
	}
//	mp_msg(MSGT_DECVIDEO, MSGL_INFO, "[vd_omap4_dce] codec id: %08x, tag: '%04s' ------\n", sh->format, &sh->format);

	omap4_dce_priv_t.codec_id = codec_id;
	omap4_dce_priv_t.reset_buffers = NULL;

	frame_width = ALIGN2(sh->disp_w, 4);
	frame_height = ALIGN2(sh->disp_h, 4);

	codec_engine = Engine_open("ivahd_vidsvr", NULL, NULL);
	if (!codec_engine) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: Engine_open() failed\n");
		return 0;
	}

	switch (codec_id) {
	case CODEC_ID_H264:
		codec_params = dce_alloc(sizeof(IH264VDEC_Params));
		break;
	case CODEC_ID_MPEG4:
		codec_params = dce_alloc(sizeof(IMPEG4VDEC_Params));
		break;
	case CODEC_ID_MPEG1VIDEO:
	case CODEC_ID_MPEG2VIDEO:
		codec_params = dce_alloc(sizeof(IMPEG2VDEC_Params));
		break;
	case CODEC_ID_WMV3:
	case CODEC_ID_VC1:
		codec_params = dce_alloc(sizeof(IVC1VDEC_Params));
		break;
	default:
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Unsupported codec %08x\n", codec_id);
		goto fail;
	}

	if (!codec_params) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error allocation with dce_alloc()\n");
		goto fail;
	}

	codec_params->maxWidth = frame_width;
	codec_params->maxHeight = frame_height;
	codec_params->maxFrameRate = 30000;
	codec_params->maxBitRate = 10000000;
	codec_params->dataEndianness = XDM_BYTE;
	codec_params->forceChromaFormat = XDM_YUV_420SP;
	codec_params->operatingMode = IVIDEO_DECODE_ONLY;
	codec_params->displayDelay = IVIDDEC3_DISPLAY_DELAY_AUTO;
	codec_params->displayBufsMode = IVIDDEC3_DISPLAYBUFS_EMBEDDED;
	codec_params->inputDataMode = IVIDEO_ENTIREFRAME;
	codec_params->outputDataMode = IVIDEO_ENTIREFRAME;
	codec_params->numInputDataUnits = 0;
	codec_params->numOutputDataUnits = 0;
	codec_params->metadataType[0] = IVIDEO_METADATAPLANE_NONE;
	codec_params->metadataType[1] = IVIDEO_METADATAPLANE_NONE;
	codec_params->metadataType[2] = IVIDEO_METADATAPLANE_NONE;
	codec_params->errorInfoMode = IVIDEO_ERRORINFO_OFF;

	switch (codec_id) {
	case CODEC_ID_H264:
		frame_width = ALIGN2(frame_width + (32 * 2), 7);
		frame_height = frame_height + 4 * 24;
		codec_params->size = sizeof(IH264VDEC_Params);
		((IH264VDEC_Params *)codec_params)->maxNumRefFrames = IH264VDEC_DPB_NUMFRAMES_AUTO;
		((IH264VDEC_Params *)codec_params)->pConstantMemory = 0;
		((IH264VDEC_Params *)codec_params)->presetLevelIdc = IH264VDEC_LEVEL41;
		((IH264VDEC_Params *)codec_params)->errConcealmentMode = IH264VDEC_APPLY_CONCEALMENT;
		((IH264VDEC_Params *)codec_params)->temporalDirModePred = TRUE;
		codec_handle = VIDDEC3_create(codec_engine, "ivahd_h264dec", codec_params);
		mp_msg(MSGT_DECVIDEO, MSGL_INFO, "[vd_omap4_dce] using ivahd_h264dec\n");
		break;
	case CODEC_ID_MPEG4:
		frame_width = ALIGN2(frame_width + 32, 7);
		frame_height = frame_height + 32;
		codec_params->size = sizeof(IMPEG4VDEC_Params);
		((IMPEG4VDEC_Params *)codec_params)->outloopDeBlocking = TRUE;
		((IMPEG4VDEC_Params *)codec_params)->sorensonSparkStream = FALSE;
		((IMPEG4VDEC_Params *)codec_params)->ErrorConcealmentON = FALSE;
		codec_handle = VIDDEC3_create(codec_engine, "ivahd_mpeg4dec", codec_params);
		mp_msg(MSGT_DECVIDEO, MSGL_INFO, "[vd_omap4_dce] using ivahd_mpeg4dec\n");
		break;
	case CODEC_ID_MPEG1VIDEO:
	case CODEC_ID_MPEG2VIDEO:
		codec_params->size = sizeof(IMPEG2VDEC_Params);
		codec_params->displayDelay = IVIDDEC3_DISPLAY_DELAY_1;
		codec_handle = VIDDEC3_create(codec_engine, "ivahd_mpeg2vdec", codec_params);
		mp_msg(MSGT_DECVIDEO, MSGL_INFO, "[vd_omap4_dce] using ivahd_mpeg2vdec\n");
		break;
	case CODEC_ID_WMV3:
	case CODEC_ID_VC1:
		frame_width = ALIGN2(frame_width + (32 * 2), 7);
		frame_height = (ALIGN2(frame_height / 2, 4) * 2) + 2 * 40;
		codec_params->size = sizeof(IVC1VDEC_Params);
		codec_params->maxBitRate = 45000000;
		((IVC1VDEC_Params *)codec_params)->FrameLayerDataPresentFlag = FALSE;
		((IVC1VDEC_Params *)codec_params)->ErrorConcealmentON = 1;
		codec_handle = VIDDEC3_create(codec_engine, "ivahd_vc1vdec", codec_params);
		mp_msg(MSGT_DECVIDEO, MSGL_INFO, "[vd_omap4_dce] using ivahd_vc1dec\n");
		break;
	default:
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Unsupported codec %08x\n", codec_id);
		goto fail;
	}

	if (!codec_handle) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: VIDDEC3_create() failed\n");
		goto fail;
	}

	switch (codec_id) {
	case CODEC_ID_MPEG1VIDEO:
	case CODEC_ID_MPEG2VIDEO:
		codec_status = dce_alloc(sizeof(IMPEG2VDEC_Status));
		break;
	default:
		codec_status = dce_alloc(sizeof(VIDDEC3_Status));
	}

	codec_dynamic_params = dce_alloc(sizeof(VIDDEC3_DynamicParams));
	codec_input_buffers = dce_alloc(sizeof(XDM2_BufDesc));
	codec_output_buffers = dce_alloc(sizeof(XDM2_BufDesc));
	codec_input_args = dce_alloc(sizeof(VIDDEC3_InArgs));
	codec_output_args = dce_alloc(sizeof(VIDDEC3_OutArgs));
	if (!codec_dynamic_params || !codec_status || !codec_input_buffers ||
			!codec_output_buffers || !codec_input_args || !codec_output_args) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error allocation with dce_alloc()\n");
		goto fail;
	}

	codec_dynamic_params->size = sizeof(VIDDEC3_DynamicParams);
	codec_dynamic_params->decodeHeader = XDM_DECODE_AU;
	codec_dynamic_params->displayWidth = 0;
	codec_dynamic_params->frameSkipMode = IVIDEO_NO_SKIP;
	codec_dynamic_params->newFrameFlag = XDAS_TRUE;
	if (codec_id == CODEC_ID_MPEG4 || codec_id == CODEC_ID_VC1 || codec_id == CODEC_ID_WMV3) {
		codec_dynamic_params->lateAcquireArg = -1;
	}

	codec_status->size = sizeof(VIDDEC3_Status);
	codec_input_args->size = sizeof(VIDDEC3_InArgs);
	codec_output_args->size = sizeof(VIDDEC3_OutArgs);

	codec_error = VIDDEC3_control(codec_handle, XDM_SETPARAMS, codec_dynamic_params, codec_status);
	if (codec_error != VIDDEC3_EOK) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: VIDDEC3_control(XDM_SETPARAMS) failed %d\n", codec_error);
		goto fail;
	}
	codec_error = VIDDEC3_control(codec_handle, XDM_GETBUFINFO, codec_dynamic_params, codec_status);
	if (codec_error != VIDDEC3_EOK) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: VIDDEC3_control(XDM_GETBUFINFO) failed %d\n", codec_error);
		goto fail;
	}

	vo_directrendering = 1;
	frame_id = -1;

	return mpcodecs_config_vo(sh, sh->disp_w, sh->disp_h, sh->format);
fail:
	codec_engine_close();
	return 0;
}

void codec_engine_close(void) {
	if (codec_handle) {
		VIDDEC3_delete(codec_handle);
		codec_handle = NULL;
	}
	if (codec_engine) {
		Engine_close(codec_engine);
		codec_engine = NULL;
	}
	if (codec_params) {
		dce_free(codec_params);
		codec_params = NULL;
	}
	if (codec_dynamic_params) {
		dce_free(codec_dynamic_params);
		codec_dynamic_params = NULL;
	}
	if (codec_status) {
		dce_free(codec_status);
		codec_status = NULL;
	}
	if (codec_input_buffers) {
		dce_free(codec_input_buffers);
		codec_input_buffers = NULL;
	}
	if (codec_output_buffers) {
		dce_free(codec_output_buffers);
		codec_output_buffers = NULL;
	}
	if (codec_input_args) {
		dce_free(codec_input_args);
		codec_input_args = NULL;
	}
	if (codec_output_args) {
		dce_free(codec_output_args);
		codec_output_args = NULL;
	}
	if (input_buf) {
		MemMgr_Free(input_buf);
		input_buf = NULL;
	}
}

static void uninit(sh_video_t *sh) {
	codec_engine_close();
}

static mp_image_t *decode(sh_video_t *sh, void *data, int len, int flags) {
	MemAllocBlock mablk = { 0 };
	mp_image_t *mpi;
	XDM_Rect *r;
	int i;

	if (len <= 0)
		return NULL; // skipped frame

	if (!input_buf) {
		int size = VIDEOBUFFER_SIZE + MP_INPUT_BUFFER_PADDING_SIZE;
		if (len > size) {
			mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: allocation buffer bigger than allowed\n");
			return NULL;
		}

		size = ALIGN2(size * 5 / 4, 13);
		mablk.pixelFormat = PIXEL_FMT_PAGE;
		mablk.dim.len = size;

		input_buf = MemMgr_Alloc(&mablk, 1);
		if (!input_buf) {
			mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error allocation Tiled buffer\n");
			return NULL;
		}

		input_phys = TilerMem_VirtToPhys(input_buf);
		input_size = size;
	}

	memcpy(input_buf, data, len);

	mpi = mpcodecs_get_image(sh, MP_IMGTYPE_TEMP, MP_IMGFLAG_ACCEPT_STRIDE | MP_IMGFLAG_DIRECT, frame_width, frame_height);

	if (!mpi) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: mpcodecs_get_image() failed\n");
		return NULL;
	}

	memset(codec_output_args->outputID, 0, sizeof(codec_output_args->outputID));
	memset(codec_output_args->freeBufID, 0, sizeof(codec_output_args->freeBufID));

	codec_input_args->inputID = (XDAS_Int32)mpi->priv;
	codec_input_args->numBytes = len;

	codec_input_buffers->numBufs = 1;
	codec_input_buffers->descs[0].memType = XDM_MEMTYPE_RAW;
	codec_input_buffers->descs[0].buf = (int8_t *)input_phys;
	codec_input_buffers->descs[0].bufSize.bytes = len;

	codec_output_buffers->numBufs = 2;
	codec_output_buffers->descs[0].memType = XDM_MEMTYPE_RAW;
	codec_output_buffers->descs[0].buf = (int8_t *)((struct v4l2_buf *)mpi->priv)->plane_p[0];
	codec_output_buffers->descs[0].bufSize.bytes = frame_width * frame_height;
	codec_output_buffers->descs[1].memType = XDM_MEMTYPE_RAW;
	codec_output_buffers->descs[1].buf = (int8_t *)((struct v4l2_buf *)mpi->priv)->plane_p[1];
	codec_output_buffers->descs[1].bufSize.bytes = (frame_width * frame_height) / 2;

	frame_id++;
	codec_error = VIDDEC3_process(codec_handle, codec_input_buffers, codec_output_buffers, codec_input_args, codec_output_args);
	if (codec_error != VIDDEC3_EOK) {
		mp_msg(MSGT_DECVIDEO, MSGL_ERR, "[vd_omap4_dce] Error: VIDDEC3_process() status: %d, extendedError: %08x, frame: %d\n",
				codec_error, codec_output_args->extendedError, frame_id);
		if (XDM_ISFATALERROR(codec_output_args->extendedError)) {
			exit_player(EXIT_QUIT);
		}
	}

	for (i = 0; codec_output_args->freeBufID[i]; i++) {
		((struct v4l2_buf *)codec_output_args->freeBufID[i])->to_free = 1;
	}

	if (codec_output_args->outputID[0]) {
		mpi->priv = codec_output_args->outputID[0];
		r = &codec_output_args->displayBufs.bufDesc[0].activeFrameRegion;
		mpi->x = r->topLeft.x;
		mpi->y = r->topLeft.y;
		return mpi;
	}

	mpi->priv = NULL;
	return NULL;
}
