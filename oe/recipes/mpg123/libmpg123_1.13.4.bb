DESCRIPTION = "mpg123 is a fast and free console based real time MPEG \
Audio Player for Layer 1, 2 and 3."
LICENSE = "LGPL"
DESCRIPTION = "multimedia"
HOMEPAGE = "http://www.mpg123.de"
RCONFLICTS_${PN} = "mpg321"
RREPLACES_${PN} = "mpg321"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/mpg123/mpg123-${PV}.tar.bz2"

EXTRA_OECONF="--with-cpu=generic_nofpu"
EXTRA_OECONF_arm="--with-cpu=arm_nofpu"
EXTRA_OECONF_arm6="--with-cpu=arm_nofpu"
EXTRA_OECONF_armv7a="--with-cpu=neon"
EXTRA_OECONF += "--with-optimization=4"

inherit autotools

SRC_URI[md5sum] = "073620b3938c4cb9c4f70e8fe3e114b8"
SRC_URI[sha256sum] = "f7ec51069ad0d6ac589d78ee431fbf7a4874f9c7338f01b67964640305e627c3"

S = "${WORKDIR}/mpg123-${PV}"

#MobiAqua: do not run parallel
PARALLEL_MAKE = ""
