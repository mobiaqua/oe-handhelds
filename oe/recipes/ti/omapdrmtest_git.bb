DEPENDS = "libdce libdrm libav libgbm virtual/egl"

inherit autotools

PV = "1.0.0"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "e539f5d70feba54ad7a46edbb7c466410349b76c"
SRC_URI = "git://github.com/robclark/omapdrmtest.git;protocol=git \
           file://0001-util-do-not-redefine-bool-in-C.patch \
           file://0004-display-kms-align-width-on-128-bytes-to-please-Ducat.patch \
           file://0004-display-kmscube-align-width-on-128-bytes-to-please-Ducat.patch \
           file://av-fixes.patch \
"

S = "${WORKDIR}/git"

FLAGS="-lm -lavcodec"

EXTRA_OECONF = "LDFLAGS='${FLAGS}' --disable-silent-rules --disable-x11"
