require xorg-lib-common.inc
DESCRIPTION = "X11 miscellaneous utility library"
DEPENDS += "libxt libxext"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "a4efff8de85bd45dd3da124285d10c00"
SRC_URI[archive.sha256sum] = "709081c550cc3a866d7c760a3f97384a1fe16e27fc38fe8169b8db9f33aa7edd"

PACKAGES =+ "libxmuu libxmuu-dev"

FILES_libxmuu = "${libdir}/libXmuu.so.*"
FILES_libxmuu-dev = "${libdir}/libXmuu.so"

LEAD_SONAME = "libXmu"

XORG_PN = "libXmu"
