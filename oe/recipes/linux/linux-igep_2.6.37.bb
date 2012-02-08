DESCRIPTION = "2.6 Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-igep-2.6.37:linux-2.6.37:"

COMPATIBLE_MACHINE = "igep0030"

inherit kernel

PR = "r1"
KV = "${PV}-5"

SRC_URI = "http://downloads.igep.es/sources/linux-omap-${KV}.tar.gz \
	   file://fix_nonlinux_compile.patch \
	   file://defconfig \
	  "

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	yes '' | oe_runmake oldconfig
}

S = "${WORKDIR}/linux-omap-${KV}"

