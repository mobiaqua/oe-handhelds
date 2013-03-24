#MobiAqua: changed depends to libdce-firmware. update to new git revision
DEPENDS = "libdce-firmware libdrm"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.6.7.3"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "01cc1a28b069b80a2414095bfe3d0af52893056d"
SRC_URI = "git://gitorious.org/ubuntu-omap/dce.git;protocol=git"

S = "${WORKDIR}/git"
