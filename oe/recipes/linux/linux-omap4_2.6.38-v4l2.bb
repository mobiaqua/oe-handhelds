SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP4 kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"
COMPATIBLE_MACHINE = "pandaboard"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "10"

DEPENDS = "coreutils-native"

inherit kernel

FILESPATHPKG =. "linux-omap4_2.6.38-v4l2:"

SRCREV = "33fb7c306ef396ae8fe96b0caf1fd12d7ec0216a"

COMPATIBLE_HOST = "arm.*-linux"

export ARCH = "arm"
export OS = "Linux"

SRC_URI = "git://github.com/robclark/kernel-omap4.git;protocol=git;branch=ti-omap4-drm-syslink-v4l2 \
           file://fix-for-new-binutils.patch \
           file://fix_nonlinux_compile.patch \
           file://smsc95xx.patch \
           file://3.0-dss2-drm.patch \
           file://dss2-fix-hdmi.patch \
           file://dss2-5tapfilter.patch \
           file://v4l2.patch \
           file://fix-dss2.patch \
           file://fix-missing-include.patch \
           file://syslink-tiler.patch \
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
