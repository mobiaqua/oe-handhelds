#!/bin/sh

echo
echo "Checking mplayer prebuilded..."
echo
cd ${OE_BASE}/build-${DISTRO} && source env.source && ${OE_BASE}/bb/bin/bitbake mplayer && {
	cd ${OE_BASE}/apps-devel/mplayer
	if [ ! -e src ]; then
		echo
		echo "Copy mplayer sources..."
		echo
		if [ ! -e ${OE_BASE}/build-${DISTRO}/tmp/work/armv7a-linux-gnueabi/mplayer-*/trunk/ ]; then
			echo "Error: mplayer OE build dir not exist, you must disable INHERIT = \"rm_work\","
			echo "       rebuild mplayer and try again."
			echo
			exit 1
		fi
		mkdir src
		cp -R ${OE_BASE}/build-${DISTRO}/tmp/work/armv7a-linux-gnueabi/mplayer-*/trunk/*  src/
		cd src
		make distclean
		sed -i '' -e "s,-O2 \$_march \$_mcpu \$_pipe \$_debug \$_profile,-O0 \$_march \$_mcpu \$_pipe \$_debug \$_profile," configure
		sed -i '' -e '/^include/a\
		CXXFLAGS = \$WARNFLAGS \$CXXFLAGS \$extra_cflags \$extra_cxxflags -g0 -O3' configure
		sed -i '' -e '/^include/a\
		CFLAGS   = \$WARNFLAGS \$WARN_CFLAGS \$CFLAGS \$extra_cflags -g0 -O3' configure
		sed -i '' -e 's|\$(BUILD_CC)|gcc|' Makefile
		cd ..
	fi
	cp _config.sh _compile.sh _debug.sh _env.sh src/
	chmod +x src/_config.sh src/_compile.sh src/_debug.sh
	echo ;echo "--- Setup done ---"; echo
}
