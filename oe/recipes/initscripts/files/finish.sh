#!/bin/sh

if ! test -e /etc/.configured; then
	> /etc/.configured
fi

#MobiAqua: temporary hack
echo 0 > /sys/devices/platform/omapdss/overlay0/global_alpha
modprobe videobuf-core
modprobe videobuf-dma-contig
modprobe omap-vout
