DESCRIPTION = "2.6 Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "coreutils-native"

COMPATIBLE_MACHINE = "igep0030"

inherit kernel

PR = "r1"
KV = "${PV}-3"

SRC_URI = "http://downloads.igep.es/sources/linux-omap-${KV}.tar.gz"

do_configure() {
	rm -f ${S}/.config || true

        oe_runmake igep00x0_defconfig
}
           
S = "${WORKDIR}/linux-omap-${KV}"
