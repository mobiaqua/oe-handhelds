require xorg-lib-common.inc
DESCRIPTION = "X11 Input extension library"
DEPENDS += "libxext inputproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "78ee882e1ff3b192cf54070bdb19938e"
SRC_URI[archive.sha256sum] = "f2e3627d7292ec5eff488ab58867fba14a62f06e72a8d3337ab6222c09873109"

XORG_PN = "libXi"
