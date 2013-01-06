require xorg-lib-common.inc
DESCRIPTION = "X11 keyboard file manipulation library"
LICENSE = "GPL"
DEPENDS += "virtual/libx11 kbproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "19e6533ae64abba0773816a23f2b9507"
SRC_URI[archive.sha256sum] = "8aa94e19c537c43558f30906650cea6e15fa012591445d9f927658c3b32a8f3a"

BBCLASSEXTEND = "native"
