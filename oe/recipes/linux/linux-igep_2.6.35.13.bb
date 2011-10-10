DESCRIPTION = "2.6 Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "coreutils-native"

FILESPATHPKG =. "linux-igep-2.6.35.13:"

COMPATIBLE_MACHINE = "igep0030"

inherit kernel

PR = "r1"
KV = "${PV}-3"

SRC_URI = "http://downloads.igep.es/sources/linux-omap-${KV}.tar.gz \
	   file://0001-ARM-6329-1-wire-up-sys_accept4-on-ARM.patch \
	   file://0002-ARM-Add-prompt-for-CONFIG_ALIGNMENT_TRAP.patch \
	   file://0002-cgroupfs-create-sys-fs-cgroup-to-mount-cgroupfs-on.patch \
	   file://0003-ARM-Print-warning-on-alignment-trap-in-kernel-mode.patch \
	   file://0004-ARM-Expose-some-CPU-control-registers-via-sysfs.patch \
	   file://0005-ARM-Add-option-to-allow-userspace-PLE-access.patch \
	   file://0006-ARM-Add-option-to-allow-userspace-access-to-performa.patch \
	   file://fix_nonlinux_compile.patch \
	   file://linux-2.6.36-tidspbridge.patch \
	  "

do_configure() {
	rm -f ${S}/.config || true

        oe_runmake igep00x0_defconfig
}
           
S = "${WORKDIR}/linux-omap-${KV}"

SRC_URI[md5sum] = "18bddabb9cb46d1c26199872e91f0f2c"
SRC_URI[sha256sum] = "5a5453cd4abcfd2f7b995ddb9fefde221d0fbff229f52505d071dc135984c267"
