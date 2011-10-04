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
SRCREV = "linux-igep-2.6.35.y"

SRC_URI = "git://git.igep.es/pub/scm/linux-omap-2.6.git;protocol=git;branch=linux-2.6.35.y"

do_configure() {
	rm -f ${S}/.config || true

        oe_runmake igep00x0_defconfig
}
           
S = "${WORKDIR}/linux-omap-${KV}"
