DESCRIPTION = "U-boot for IGEP based platforms"
SECTION = "bootloader"
LICENSE = "GPL"

PROVIDES += "u-boot"
DEPENDS = "mtd-utils"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

PR = "r1"

COMPATIBLE_MACHINE = "igep0030"

SRC_URI = "http://downloads.igep.es/sources/${PN}-${PV}.tar.gz \
	file://fw_env.config "

PACKAGE_ARCH = "${MACHINE_ARCH}"

PARALLEL_MAKE = ""

do_compile () {
	unset LDFLAGS CFLAGS CPPFLAGS
	oe_runmake ${MACHINE}_config
	oe_runmake CROSS_COMPILE=${TARGET_PREFIX}
	oe_runmake CROSS_COMPILE=${TARGET_PREFIX} env tools
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/tools/env/fw_printenv ${D}${bindir}
	ln -sf fw_printenv ${D}${bindir}/fw_setenv
	install -m 0744 ${S}/tools/mkimage ${D}${bindir}

	install -d ${D}${sysconfdir}
	install -m 0666 ${WORKDIR}/fw_env.config ${D}${sysconfdir}
}

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0666 ${S}/u-boot.bin ${DEPLOY_DIR_IMAGE}/u-boot-${PV}.${MACHINE}.bin
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/u-boot-${PV}.${MACHINE}.bin

	rm -f u-boot-${MACHINE}.bin
	ln -sf u-boot-${PV}.${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin
}
do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile

PACKAGES = "u-boot-env u-boot-env-dbg"

FILES_u-boot-env = "${bindir}/fw_* ${sysconfdir} ${bindir}/mkimage"
FILES_u-boot-env-dbg = "${bindir}/.debug"
