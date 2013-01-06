require xorg-lib-common.inc
DESCRIPTION = "X11 miscellaneous extension library"
DEPENDS += "xproto virtual/libx11 xextproto libxau"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "71251a22bc47068d60a95f50ed2ec3cf"
SRC_URI[archive.sha256sum] = "56229c617eb7bfd6dec40d2805bc4dfb883dfe80f130d99b9a2beb632165e859"

BBCLASSEXTEND = "native nativesdk"

XORG_PN = "libXext"
