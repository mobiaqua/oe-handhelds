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

SRC_URI[md5sum] = "169cfc32b32b5cae99212fe8e4347215"
SRC_URI[sha256sum] = "f00f72e385b522b8f05c9b1ed371abad438362620e6eb8164e2a99b79bb3f6d3"

S = "${WORKDIR}/mpg123-${PV}"

#MobiAqua: do not run parallel
PARALLEL_MAKE = ""
