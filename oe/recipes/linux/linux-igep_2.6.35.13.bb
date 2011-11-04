DESCRIPTION = "2.6 Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "coreutils-native"

FILESPATHPKG =. "linux-igep-2.6.35.13:linux-2.6.35:"

COMPATIBLE_MACHINE = "igep0030"

inherit kernel

PR = "r1"
KV = "${PV}-5"

SRC_URI = "http://downloads.igep.es/sources/linux-omap-${KV}.tar.gz \
#	   file://0002-cgroupfs-create-sys-fs-cgroup-to-mount-cgroupfs-on.patch \
#	   file://0004-ARM-Expose-some-CPU-control-registers-via-sysfs.patch \
#	   file://0005-ARM-Add-option-to-allow-userspace-PLE-access.patch \
#	   file://0006-ARM-Add-option-to-allow-userspace-access-to-performa.patch \
	   file://fix_nonlinux_compile.patch \
	   file://defconfig \
	  "

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	yes '' | oe_runmake oldconfig
}

S = "${WORKDIR}/linux-omap-${KV}"

SRC_URI[md5sum] = "b53f7d0967f0463c0fa172a462cedb8e"
SRC_URI[sha256sum] = "f8e630985ab9f1a59cb0bda4d2265ee290b0d7fa191f27f70d8fbf03ccffc15f"
