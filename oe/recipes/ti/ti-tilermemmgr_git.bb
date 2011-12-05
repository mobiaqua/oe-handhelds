#MobiAqua: update to new git revision
LICENSE = "GPLv2"

PV = "2.00"
PR = "${MACHINE_KERNEL_PR}"
PR_append = "+gitr-${SRCREV}"

SRCREV = "a6dda467f9689cb8c4e2c252f53524afd07c2392"
SRC_URI = "git://git.omapzoom.org/platform/hardware/ti/tiler.git;protocol=git;branch=memmgr_2.0"

inherit autotools

export ARCH = "${TARGET_ARCH}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

EXTRA_OECONF = "--enable-tilermgr"

S = "${WORKDIR}/git/"

do_configure_prepend() {
	sed -i -e 's:-Werror::g' configure.ac
}
