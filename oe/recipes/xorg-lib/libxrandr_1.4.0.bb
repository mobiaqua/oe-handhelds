require xorg-lib-common.inc
DESCRIPTION = "X11 Resize and Rotate extension library"
LICENSE = "BSD-X"
DEPENDS += "randrproto libxrender libxext"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "0c843636124cc1494e3d87df16957672"
SRC_URI[archive.sha256sum] = "033ad0ac2f012afb05268660f6d78705c85f84689f92fa7b47ce12959b15f5c3"

BBCLASSEXTEND = "nativesdk"

XORG_PN = "libXrandr"
