require xorg-lib-common.inc
DESCRIPTION = "X Display Manager Control Protocol library"
DEPENDS += "xproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "b94af6cef211cf3ee256f7e81f70fcd9"
SRC_URI[archive.sha256sum] = "9ace6d4230f9dce4ed090692f82f613253ada8f887b23b3d8ff3dd4e3a7c118e"

BBCLASSEXTEND = "native nativesdk"

XORG_PN = "libXdmcp"
