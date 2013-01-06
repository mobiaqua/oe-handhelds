require xorg-lib-common.inc
DESCRIPTION = "X11 Session management library"
DEPENDS += "libice xproto xtrans util-linux-ng"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "766de9d1e1ecf8bf74cebe2111d8e2bd"
SRC_URI[archive.sha256sum] = "93c11d569c64f40723b93b44af1efb474a0cfe92573b0c8c330343cabb897f1d"

BBCLASSEXTEND = "native"

XORG_PN = "libSM"
