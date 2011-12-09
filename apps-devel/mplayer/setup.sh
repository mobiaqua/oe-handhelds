#!/bin/sh

echo
echo "Checking mplayer prebuilded..."
echo
cd ${OE_BASE}/build-${DISTRO} && source env.source && ${OE_BASE}/bb/bin/bitbake mplayer && {
	cd ${OE_BASE}/apps-devel/mplayer
	if [ ! -e src ]; then
		mkdir src
		echo
		echo "Copy mplayer sources..."
		echo
		cp -R ${OE_BASE}/build-${DISTRO}/tmp/work/armv7a-linux-gnueabi/mplayer-*/trunk/*  src/
	fi
	cp _config.sh _compile.sh _debug.sh _env.sh src/
	chmod +x src/_config.sh src/_compile.sh src/_debug.sh
	echo ;echo "--- Setup done ---"; echo
}
