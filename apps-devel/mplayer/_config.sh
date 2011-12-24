#!/bin/sh

. _env.sh

./configure --prefix=/usr --target=arm-linux-gnueabi --disable-mencoder --disable-gui --disable-lirc --disable-lircc --disable-joystick \
	--disable-vm --disable-xf86keysym --disable-tv --enable-networking --enable-rtc --disable-smb --enable-live \
	--disable-dvdnav --disable-dvdread --disable-dvdread-internal --disable-libdvdcss-internal --disable-cdparanoia \
	--enable-freetype --enable-menu --enable-sortsub --disable-fribidi --disable-enca --disable-ftp --disable-vstream \
	--disable-gif --disable-png --disable-jpeg --disable-libcdio --disable-liblzo --disable-qtx --disable-xanim \
	--disable-real --disable-xvid --disable-x264 --disable-ffmpeg_so --disable-speex --disable-theora --disable-faac \
	--disable-faad --disable-ladspa --disable-libdv --disable-mad --disable-toolame --disable-twolame --disable-xmms \
	--disable-mp3lib --enable-mpg123 --disable-libmpeg2 --disable-musepack --disable-gl --disable-vesa --disable-svga \
	--disable-sdl --disable-aa --disable-caca --disable-ggi --disable-ggiwmh --disable-directx --disable-dxr2 --disable-dxr3 \
	--disable-directfb --disable-zr --disable-bl --disable-tdfxvid --disable-tga --disable-pnm --disable-md5sum --disable-xss \
	--disable-dga1 --disable-dga2 --enable-alsa --disable-ossaudio --disable-arts --disable-esd --disable-pulse --disable-jack \
	--disable-openal --disable-nas --disable-sgiaudio --disable-sunaudio --disable-win32waveout --enable-select \
	--disable-fbdev --enable-omap4 --disable-v4l2 --disable-dvb --disable-yuv4mpeg --disable-vcd \
	--enable-protocol='file_protocol pipe_protocol http_protocol' --disable-decoder=vorbis_decoder --disable-encoder=vorbis_encoder \
	--enable-armv6 --enable-neon --extra-libs-mplayer="-lstdc++" \
	--extra-cflags="-march=armv7-a -mtune=cortex-a9 -mfpu=neon -mfloat-abi=softfp -mno-thumb-interwork -mno-thumb $DCE_CFLAGS" \
	--extra-libs="-lliveMedia -lBasicUsageEnvironment -lgroupsock -lUsageEnvironment -lstdc++ -lmpg123 $DCE_LIBS" \
	--ar=$AR --disable-termcap \
	--enable-debug=3
