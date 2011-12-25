SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP4 kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"
COMPATIBLE_MACHINE = "pandaboard"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "5"

DEPENDS = "coreutils-native"

inherit kernel

FILESPATHPKG =. "linux-omap4_3.0.0:"

SRCREV = "Ubuntu-3.0.0-1206.11"

COMPATIBLE_HOST = "arm.*-linux"

export ARCH = "arm"
export OS = "Linux"

SRC_URI = "git://kernel.ubuntu.com/ubuntu/ubuntu-oneiric.git;protocol=git;branch=ti-omap4 \
           file://fix-for-new-binutils.patch \
           file://fix_nonlinux_compile.patch \
           file://fix-missing-include.patch \
           file://v4l2.patch;striplevel=2 \
           file://better_fix_v4l2.patch \
           file://fix_hdmi_audio.patch \
           file://fix-overlay.patch \
           file://fixes-for-cache.patch \
           file://make_fb0_alpha.patch \
           file://change_order_overlays.patch \
           file://defconfig"

SRC_URI[md5sum] = "3f52da7b22960aec5f86a605df8c745a"
SRC_URI[sha256sum] = "333e8df467e09aa47d23b76843a698d7dd44cd62b9161ea785347bd44541c8f8"

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
