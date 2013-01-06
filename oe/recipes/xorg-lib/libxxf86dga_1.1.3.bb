require xorg-lib-common.inc
DESCRIPTION = "X11 Direct Graphics Access extension library"
DEPENDS += "libxext xf86dgaproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI += "file://libxxf86dga-1.1.3_fix_for_x32.patch"

SRC_URI[archive.md5sum] = "b7f38465c46e7145782d37dbb9da8c09"
SRC_URI[archive.sha256sum] = "551fa374dbef0f977de1f35d005fa9ffe92b7a87e82dbe62d6a4640f5b0b4994"

XORG_PN = "libXxf86dga"
