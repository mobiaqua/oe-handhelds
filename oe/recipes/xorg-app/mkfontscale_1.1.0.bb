require xorg-app-common.inc
DESCRIPTION = "a program to create an index of scalable font files for X"
DEPENDS += " zlib libfontenc freetype "
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "414fcb053418fb1418e3a39f4a37e0f7"
SRC_URI[archive.sha256sum] = "ce55f862679b8ec127d7f7315ac04a8d64a0d90a0309a70dc56c1ba3f9806994"

BBCLASSEXTEND = "native"
