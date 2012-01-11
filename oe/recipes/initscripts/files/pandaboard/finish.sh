#!/bin/sh

if ! test -e /etc/.configured; then
	> /etc/.configured
fi

#MobiAqua: temporary hack
echo 0 > /sys/devices/platform/omapdss/manager1/alpha_blending_enabled
echo 0 > /sys/devices/platform/omapdss/manager2/alpha_blending_enabled
dd if=/dev/zero of=/dev/fb0
echo "74250,1920/148/638/44,1080/36/4/5" > /sys/devices/platform/omapdss/display0/timings
modprobe videobuf-core
modprobe videobuf-dma-contig
modprobe omap-vout
