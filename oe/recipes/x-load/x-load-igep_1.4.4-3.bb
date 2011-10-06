DESCRIPTION = "x-loader for IGEP based platforms"
SECTION = "bootloader"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "signgp-native"
PROVIDES += "x-load"
COMPATIBLE_MACHINE = "igep0030"
PARALLEL_MAKE=""
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "http://downloads.igep.es/sources/x-loader-${PV}.tar.gz \
	   file://armv7-a.patch;patch=1 \
	  "

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake distclean
	oe_runmake igep0030-sdcard_config
	oe_runmake
}

do_install () {
	install -d ${D}/boot
	install ${S}/x-load.bin.ift ${D}/boot/${MLO_IMAGE}
	ln -sf ${MLO_IMAGE} ${D}/boot/${MLO_SYMLINK_NOMACHINE}
}

FILES_${PN} = "/boot"

do_deploy () {
	signGP ${S}/x-load.bin
	install -d ${DEPLOY_DIR_IMAGE}
	install ${S}/x-load.bin.ift ${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${MLO_SYMLINK}
	ln -sf ${MLO_IMAGE} ${MLO_SYMLINK}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${MLO_SYMLINK}
}
do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_install
