require xorg-lib-common.inc
DESCRIPTION = "X11 font rasterisation library"
LICENSE = "BSD-X"
DEPENDS += "freetype fontcacheproto xtrans fontsproto libfontenc"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "6851da5dae0a6cf5f7c9b9e2b05dd3b4"
SRC_URI[archive.sha256sum] = "bbf96fb80b6b95cdb1dc968085082a6e668193a54cd9d6e2af669909c0cb7170"

# disable docs
EXTRA_OECONF += " --disable-devel-docs "

BBCLASSEXTEND = "native"

XORG_PN = "libXfont"
