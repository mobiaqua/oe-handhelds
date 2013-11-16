DESCRIPTION = "Linux Kernel for PDA based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-pda-3.10:"

inherit kernel

PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v3.x/linux-${PV}.tar.bz2 \
	   file://fix_nonlinux_compile.patch \
	   file://defconfig \
	  "

SRC_URI[md5sum] = "4a3ffd2e9ade96909db94c598f9e0dc1"
SRC_URI[sha256sum] = "d87130c9d4851d5714b6f630f74d86de3d1131e9ca0a111ee73759126b924147"

do_configure() {
	install ${WORKDIR}/defconfig ${S}/.config
	yes '' | oe_runmake oldconfig
}

do_compile() {
	HOST_INC=-I${STAGING_INCDIR_NATIVE}
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
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

S = "${WORKDIR}/linux-${PV}"
