require xorg-lib-common.inc
DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."
RDEPENDS_${PN}-dev = ""
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "84c66908cf003ad8c272b0eecbdbaee3"
SRC_URI[archive.sha256sum] = "7f811191ba70a34a9994d165ea11a239e52c527f039b6e7f5011588f075fe1a6"

ALLOW_EMPTY = "1"

BBCLASSEXTEND = "native nativesdk"
