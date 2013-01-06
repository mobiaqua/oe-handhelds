require xorg-lib-common.inc
DESCRIPTION = "X Test Extension: client side library"
DEPENDS += "libxext recordproto inputproto libxi"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "e8abc5c00c666f551cf26aa53819d592"
SRC_URI[archive.sha256sum] = "7eea3e66e392aca3f9dad6238198753c28e1c32fa4903cbb7739607a2504e5e0"

XORG_PN = "libXtst"
