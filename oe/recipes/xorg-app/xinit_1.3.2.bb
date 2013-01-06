require xorg-app-common.inc
DESCRIPTION = "X Window System initializer"
RDEPENDS_${PN} += "util-linux-ng"
PE = "1"
PR = "${INC_PR}.1"

#MobiAqua: added --without-launchd
EXTRA_OECONF = "ac_cv_path_MCOOKIE=${bindir}/mcookie --without-launchd"

SRC_URI[archive.md5sum] = "9c0943cbd83e489ad1b05221b97efd44"
SRC_URI[archive.sha256sum] = "a1867fdaa83f68750b12ba4305c3c62f5992d0f52cfeb98e96c27a8e690e0235"

FILES_${PN} += "${libdir}X11/xinit"
