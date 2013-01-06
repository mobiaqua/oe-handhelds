require xorg-lib-common.inc
DESCRIPTION = "X11 Pixmap library"
LICENSE = "X-BSD"
DEPENDS += "libxext libsm libxt"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "7ae7eff7a14d411e84a67bd166bcec1a"
SRC_URI[archive.sha256sum] = "a6db7e234750e7d60330017972e31d8e1f29f0a8c1391e4ac82f6102d919a735"

PACKAGES =+ "sxpm cxpm"

FILES_cxpm = "${bindir}/cxpm"
FILES_sxpm = "${bindir}/sxpm"

XORG_PN = "libXpm"
