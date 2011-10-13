#!/bin/sh

/usr/bin/killall syslink_daemon.out
/usr/bin/syslink_daemon.out /lib/firmware/omap4/Ipc_MPUSYS_reroute_core0.xem3 /lib/firmware/omap4/dce_app_m3.xem3
