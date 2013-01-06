require xorg-lib-common.inc
DESCRIPTION = "X11 font encoding library"
LICENSE = "BSD-X"
DEPENDS += "zlib xproto font-util-native"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "a2a861f142c3b4367f14fc14239fc1f7"
SRC_URI[archive.sha256sum] = "de72812f1856bb63bd2226ec8c2e2301931d3c72bd0f08b0d63a0cdf0722017f"

BBCLASSEXTEND = "native"
