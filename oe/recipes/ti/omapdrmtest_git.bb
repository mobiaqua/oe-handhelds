DEPENDS = "libdce libdrm libav libgbm virtual/egl"

inherit autotools

PV = "1.0.6"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "70a270ce08112ec9d5aae3eba33dd55aa77d9ffb"
SRC_URI = "git://gitorious.org/ubuntu-omap/omapdrmtest.git;protocol=git \
           file://0001-util-do-not-redefine-bool-in-C.patch \
           file://0002-omx-cam-add-test-app.patch \
           file://0003-omx-cam-do-not-select-sensor-for-stereo-mode.patch \
           file://0004-display-kms-align-width-on-128-bytes-to-please-Ducat.patch \
           file://0004-display-kmscube-align-width-on-128-bytes-to-please-Ducat.patch \
           file://0005-Hack-disp-kmscube-reduce-u-v-by-10.patch \
           file://0006-fix-kernel-3.8-build.patch \
           file://av-fixes.patch \
"

S = "${WORKDIR}/git"

FLAGS="-lm -lavcodec"

EXTRA_OECONF = "LDFLAGS='${FLAGS}' --disable-silent-rules --disable-x11"
