DEPENDS = "libdce-firmware libdrm"

LICENSE = "BSD"

inherit autotools lib_package

PV = "1.0"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "f65ff5c36e470e1d306380cb3ee7c3102e78bd38"
SRC_URI = "git://github.com/mobiaqua/libdce.git;protocol=git"

S = "${WORKDIR}/git"
