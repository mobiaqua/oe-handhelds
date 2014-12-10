DESCRIPTION = "Linux Kernel for PDA based platforms"
SECTION = "kernel"
LICENSE = "GPL"

#DEFAULT_PREFERENCE = "-1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-pda-3.18:"

inherit kernel

PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v3.x/linux-${PV}.tar.xz \
           file://fix_nonlinux_compile.patch \
           file://0002-input-driver-for-microcontroller-keys-on-the-iPaq-h3.patch \
           file://defconfig \
          "

SRC_URI[md5sum] = "9e854df51ca3fef8bfe566dbd7b89241"
SRC_URI[sha256sum] = "becc413cc9e6d7f5cc52a3ce66d65c3725bc1d1cc1001f4ce6c32b69eb188cbd"

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
