require xorg-lib-common.inc
DESCRIPTION = "X11 XFree86 video mode extension library"
DEPENDS += "libxext xf86vidmodeproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "ffd93bcedd8b2b5aeabf184e7b91f326"
SRC_URI[archive.sha256sum] = "a564172fb866b1b587bbccb7d041088931029845245e0d15c32ca7f1bb48fc84"

XORG_PN = "libXxf86vm"
