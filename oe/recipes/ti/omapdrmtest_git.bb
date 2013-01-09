DEPENDS = "libdce libdrm libav libdri2 libgbm virtual/egl"

inherit autotools

PV = "1.0.0"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "e539f5d70feba54ad7a46edbb7c466410349b76c"
SRC_URI = "git://github.com/robclark/omapdrmtest.git;protocol=git \
           file://av-fixes.patch \
"

S = "${WORKDIR}/git"
