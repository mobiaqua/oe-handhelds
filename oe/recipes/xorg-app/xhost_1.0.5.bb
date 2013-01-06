require xorg-app-common.inc
DESCRIPTION = "Server access control program for X"
LICENSE = "MIT"
DEPENDS += "libxmu libxau"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "a0fcd2cb6ddd9f378944cc6f4f83cd7c"
SRC_URI[archive.sha256sum] = "2870d19f3f4867ead5ba4e35bb73d1fa302be29d812c13e4195066c78d1f8850"
