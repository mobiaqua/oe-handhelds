DESCRIPTION = "Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0030 = "1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-igep-2.6.37:linux-2.6.37:"

COMPATIBLE_MACHINE = "igep0030"

inherit kernel

PR = "r1"

SRCREV = "604f384abe8e3533e440eb07e00df313865405da"

SRC_URI = "git://git.isee.biz/pub/scm/linux-omap-2.6.git;protocol=git;branch=linux-2.6.37.y \
           file://defconfig \
          "

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	yes '' | oe_runmake oldconfig
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

S = "${WORKDIR}/git"

