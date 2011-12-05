#MobiAqua: added depends on libdce-firmware. update to new git revision
DEPENDS = "ti-libd2cmap ti-tilermemmgr ti-syslink libdce-firmware"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.5.3.1"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "69de5bce9d80e910fdd526bc509637ab7642a98c"
SRC_URI = "git://gitorious.org/gstreamer-omap/libdce.git;protocol=git"

S = "${WORKDIR}/git"
