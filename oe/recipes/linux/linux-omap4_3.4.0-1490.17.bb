SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP4 kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"
COMPATIBLE_MACHINE = "pandaboard"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "10"

DEPENDS = "coreutils-native elf-native"

inherit kernel

FILESPATHPKG =. "linux-omap4_3.4.0:"

SRCREV = "ti-ubuntu-3.4.0-1490.17"

COMPATIBLE_HOST = "arm.*-linux"

export ARCH = "arm"
export OS = "Linux"

SRC_URI = "git://dev.omapzoom.org/pub/scm/integration/kernel-ubuntu.git;protocol=git;branch=ti-ubuntu-3.4-stable \
           file://fix_nonlinux_compile.patch \
           file://defconfig"

S = "${WORKDIR}/git"

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	install ${WORKDIR}/defconfig ${S}/.config.old
#	yes '' | oe_runmake oldconfig
}

do_compile() {
	HOST_INC=-I${STAGING_INCDIR_NATIVE}
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	oe_runmake include/linux/version.h ${KERNEL_EXTRA_OEMAKE}
	oe_runmake ${KERNEL_IMAGETYPE} ${KERNEL_EXTRA_OEMAKE} HOST_INC=${HOST_INC}
}

do_compile_kernelmodules() {
	HOST_INC=-I${STAGING_INCDIR_NATIVE}
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake modules ${KERNEL_EXTRA_OEMAKE} HOST_INC=${HOST_INC}
	else
		oenote "no modules to compile"
	fi
}

do_install_append() {
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH
	install -d ${D}${exec_prefix}/include/linux

	# hack:
	mkdir -p ${STAGING_KERNEL_DIR}/include/linux
	rm -f ${STAGING_KERNEL_DIR}/include/linux/omap_drm.h
	rm -f ${STAGING_KERNEL_DIR}/include/linux/omap_drv.h
	if [ -f ${S}/include/linux/omap_drm.h ]; then
		cp -L ${S}/include/linux/omap_drm.h ${STAGING_KERNEL_DIR}/include/linux
		cp -L ${S}/include/linux/omap_drv.h ${STAGING_KERNEL_DIR}/include/linux
	fi
}

staging_helper_append() {
	rm -f ${STAGING_KERNEL_DIR}/include/linux/omap_drm.h
	rm -f ${STAGING_KERNEL_DIR}/include/linux/omap_drv.h
	if [ -f ${S}/include/linux/omap_drm.h ]; then
		cp -L ${S}/include/linux/omap_drm.h ${STAGING_KERNEL_DIR}/include/linux
		cp -L ${S}/include/linux/omap_drv.h ${STAGING_KERNEL_DIR}/include/linux
	fi
}

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"
