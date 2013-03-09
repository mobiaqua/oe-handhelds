/*
 * MobiAqua Media Player
 *
 * Copyright (C) 2013 Pawel Kolodziejski <aquadran at users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <time.h>
#include <signal.h>

#include <libavutil/pixfmt.h>
#include <libavformat/avformat.h>

#include "types.h"
#include "log.h"

static AVFormatContext *afc = NULL;
static AVStream *st = NULL;
static AVFrame *picture = NULL;
static AVPacket pk = {};
static AVCodec *codec = NULL;

int init_decode(void) {
	int err;

	codec = avcodec_find_decoder(codec_id);

	avc = avcodec_alloc_context();
	avc->width = st->codec->width;
	avc->height = st->codec->height;
	avc->time_base = st->codec->time_base;
	avc->extradata = st->codec->extradata;
	avc->extradata_size = st->codec->extradata_size;
	err = avcodec_open2(avc, codec, NULL);
	if (err) {
		fprintf(stderr, "avcodec_open: %d\n", err);
		return err;
	}

}

int init_display(void) {
	return S_OK;
}

int decode(void) {
	int gp;

	if (avcodec_decode_video2(avc, picture, &gp, &pk) < 0) {
		printf("error decoding\n");
		return -1;
	}
	if (gp) {
		// TODO
		return 0;
	}

	return -1;
}

int display(int buf_id) {
	return S_OK;
}

int main(int argc, char **argv) {
	char *filename;
	int i, err, disp_w, disp_h, codec_id;
	struct timeval last_time, cur_time;

	if (argc < 2) {
		printf("missing input video param\n");
		exit(1);
	}
	filename = argv[1];

	err = av_open_input_file(&afc, filename, NULL, 0, NULL);
	if (!err)
		err = av_find_stream_info(afc);

	if (err < 0) {
		fprintf(stderr, "%s: lavf error %d\n", filename, err);
		exit(1);
	}

	av_dump_format(afc, 0, filename, 0);

	picture = avcodec_alloc_frame();

	for (i = 0; i < afc->nb_streams; i++) {
		if (afc->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO && !st)
			st = afc->streams[i];
		else
			afc->streams[i]->discard = AVDISCARD_ALL;
	}

	disp_w = st->codec->width;
	disp_h = st->codec->height;
	codec_id = st->codec->codec_id;

	if (!init_display(disp_w, disp_h, codec_id)) {
		exit(1);
	}
	if (!init_decode()) {
		exit(1);
	}

	gettimeofday(&last_time, NULL);

	while (!av_read_frame(afc, &pk)) {
		double t1, t2;

		if (pk.stream_index == st->index) {
			int display_buf_id = decode();
			if (display_buf_id != -1)
				display(display_buf_id);
		}
		av_free_packet(&pk);

		gettimeofday(&cur_time, NULL);
		t1 = last_time.tv_sec * 1000000 + (last_time.tv_usec);
		t2 = cur_time.tv_sec * 1000000 + (cur_time.tv_usec);
		usleep((41 * 1000) - (t1 - t2));
		last_time = cur_time;
	}

	av_free(picture);
	avcodec_close(codec);
	av_close_input_file(afc);

	return 0;
}
