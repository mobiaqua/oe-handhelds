DESCRIPTION = "x-load bootloader loader"
SECTION = "bootloaders"
PRIORITY = "optional"
LICENSE = "GPLv2+"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "1"

DEPENDS = "signgp-native"
PROVIDES += "x-load"
PARALLEL_MAKE=""
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "1.5.1+${PR}+gitr${SRCREV}"
PR ="r1"

SRCREV = "ce2dc2f1edf152d681c732f378e2bd464d78050f"

SRC_URI = "git://gitorious.org/x-loader/x-loader.git;branch=master;protocol=git \
	   file://no_signGP.patch"

XLOAD_LOAD_ADDRESS = 0x40304350
XLOAD_MACHINE = "omap4430panda_config"

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

S = "${WORKDIR}/git"

MLO_IMAGE ?= "MLO-${MACHINE}-${PV}-${PR}"
MLO_SYMLINK ?= "MLO"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake distclean
	oe_runmake ${XLOAD_MACHINE}
	oe_runmake
}

do_install () {
	signGP ${S}/x-load.bin ${XLOAD_LOAD_ADDRESS} 1

	install -d ${D}/boot
	install -m 0644 ${S}/x-load.bin.ift ${D}/boot/${MLO_IMAGE}
	ln -sf ${MLO_IMAGE} ${D}/boot/${MLO_SYMLINK}
}

FILES_${PN} = "/boot"

do_deploy () {
	signGP ${S}/x-load.bin ${XLOAD_LOAD_ADDRESS} 1
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 ${S}/x-load.bin.ift ${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${MLO_SYMLINK}
	ln -sf ${MLO_IMAGE} ${MLO_SYMLINK}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${MLO_SYMLINK}
}
do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_install
