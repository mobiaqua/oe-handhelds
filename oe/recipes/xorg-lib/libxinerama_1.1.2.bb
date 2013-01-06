require xorg-lib-common.inc
DESCRIPTION = "X11 Xinerama extension library"
DEPENDS += "libxext xineramaproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "cb45d6672c93a608f003b6404f1dd462"
SRC_URI[archive.sha256sum] = "a4e77c2fd88372e4ae365f3ca0434a23613da96c5b359b1a64bf43614ec06aac"

XORG_PN = "libXinerama"
