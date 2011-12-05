#!/bin/sh

/usr/bin/killall syslink_daemon
/usr/bin/syslink_daemon /lib/firmware/omap4/Notify_MPUSYS_reroute_Test_Core0.xem3 /lib/firmware/omap4/dce_app_m3.xem3
