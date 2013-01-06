require xorg-lib-common.inc
DESCRIPTION = "A Sample Authorization Protocol for X"
DEPENDS += " xproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "2d241521df40d27034413436d1a1465c"
SRC_URI[archive.sha256sum] = "7153ba503e2362d552612d9dc2e7d7ad3106d5055e310a26ecf28addf471a489"

BBCLASSEXTEND = "native nativesdk"

XORG_PN = "libXau"
