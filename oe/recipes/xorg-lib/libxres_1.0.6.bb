require xorg-lib-common.inc
DESCRIPTION = "X11 Resource extension library"
DEPENDS += "libxext resourceproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "80d0c6d8522fa7a645e4f522e9a9cd20"
SRC_URI[archive.sha256sum] = "ff8661c925e8b182f98ae98f02bbd93c55259ef7f34a92c1a126b6074ebde890"

XORG_PN = "libXres"
