require xorg-lib-common.inc
DESCRIPTION = "X11 Rendering Extension client library"
LICENSE = "BSD-X"
DEPENDS += "virtual/libx11 renderproto xproto libxdmcp"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "ee62f4c7f0f16ced4da63308963ccad2"
SRC_URI[archive.sha256sum] = "f9b46b93c9bc15d5745d193835ac9ba2a2b411878fad60c504bbb8f98492bbe6"

BBCLASSEXTEND = "native nativesdk"

XORG_PN = "libXrender"
