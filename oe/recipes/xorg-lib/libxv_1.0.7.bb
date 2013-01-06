require xorg-lib-common.inc
DESCRIPTION = "X11 Video extension library"
LICENSE = "MIT-style"
DEPENDS += "libxext videoproto"
PR = "${INC_PR}.1"

SRC_URI[archive.md5sum] = "5e1ac203ccd3ce3e89755ed1fbe75b0b"
SRC_URI[archive.sha256sum] = "5d664aeb641f8c867331a0c6b4574a5e7e420f00bf5fcefd874e8d003ea59010"

XORG_PN = "libXv"
