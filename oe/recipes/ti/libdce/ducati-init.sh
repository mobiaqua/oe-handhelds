#!/bin/sh

kernel=`uname -r | cut -c 1-8`
if [ "$kernel" != "2.6.35.3" ]; then
	echo "not loading Ducati firmware"
	exit 0
fi

/usr/bin/killall syslink_daemon.out
/usr/bin/syslink_daemon.out /lib/firmware/omap4/Ipc_MPUSYS_reroute_core0.xem3 /lib/firmware/omap4/dce_app_m3.xem3
