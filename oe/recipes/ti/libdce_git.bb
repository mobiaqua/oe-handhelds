#MobiAqua: changed depends to libdce-firmware. update to new git revision
DEPENDS = "libdce-firmware libdrm"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.7.0.2"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "43c3251ad0bcf1b329a9cca69d3716f56dcbf399"
SRC_URI = "git://gitorious.org/ubuntu-omap/dce.git;protocol=git \
           file://update-from-glsdk.patch \
           file://fix_compilation.patch \
          "

S = "${WORKDIR}/git"
