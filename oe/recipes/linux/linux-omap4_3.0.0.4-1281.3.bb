SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP4 kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"
COMPATIBLE_MACHINE = "pandaboard"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "-1"

DEPENDS = "coreutils-native"

inherit kernel

FILESPATHPKG =. "linux-omap4_3.0.0:"

SRCREV = "ti-ubuntu-3.0-1281.3"

COMPATIBLE_HOST = "arm.*-linux"

export ARCH = "arm"
export OS = "Linux"

SRC_URI = "git://dev.omapzoom.org/pub/scm/integration/kernel-ubuntu.git;protocol=git \
           file://0001-UBUNTU-Config-Fix-FTBS-caused-by-new-binutils.patch \
           file://fix_nonlinux_compile.patch \
           file://fix-cross-size-tool.patch \
           file://fix-missing-include.patch \
           file://defconfig"

S = "${WORKDIR}/git"

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	yes '' | oe_runmake oldconfig
}

do_install_append() {
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH
}

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"
