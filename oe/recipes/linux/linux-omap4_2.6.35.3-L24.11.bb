SECTION = "kernel"
DESCRIPTION = "Linux kernel for OMAP4 kernel"
LICENSE = "GPLv2"
KERNEL_IMAGETYPE = "uImage"
COMPATIBLE_MACHINE = "pandaboard"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_pandaboard = "1"

DEPENDS = "coreutils-native"

inherit kernel

FILESPATHPKG =. "linux-omap4-2.6.35.3-L24.1:linux-2.6.35:"

SRCREV = "ti-ubuntu-2.6.35-980.1release14"

COMPATIBLE_HOST = "arm.*-linux"

export ARCH = "arm"
export OS = "Linux"

SRC_URI = "git://dev.omapzoom.org/pub/scm/integration/kernel-ubuntu.git;protocol=git;branch=ti-ubuntu-L24.11 \
           file://0001-tiler-avoid-lock-ups-due-to-unmapped-DMM-entries.patch \
           file://0001-UBUNTU-Config-Fix-FTBS-caused-by-new-binutils.patch \
           file://0004-ARM-Expose-some-CPU-control-registers-via-sysfs.patch \
           file://0005-ARM-Add-option-to-allow-userspace-PLE-access.patch \
           file://0006-ARM-Add-option-to-allow-userspace-access-to-performa.patch \
           file://0007-OMAP4-do-not-force-select-options-which-are-not-requ.patch \
           file://0001-ARM-6329-1-wire-up-sys_accept4-on-ARM.patch \
           file://0002-cgroupfs-create-sys-fs-cgroup-to-mount-cgroupfs-on.patch \
           file://enable_data_prefetch.patch \
           file://fix_nonlinux_compile.patch \
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
