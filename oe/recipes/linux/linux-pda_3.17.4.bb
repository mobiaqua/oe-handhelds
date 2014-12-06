DESCRIPTION = "Linux Kernel for PDA based platforms"
SECTION = "kernel"
LICENSE = "GPL"

#DEFAULT_PREFERENCE = "-1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-pda-3.17.4:"

inherit kernel

PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v3.x/linux-${PV}.tar.xz \
           file://fix_nonlinux_compile.patch \
           file://0001-ARM-sa1100-add-Micro-ASIC-platform-device.patch \
           file://0002-input-driver-for-microcontroller-keys-on-the-iPaq-h3.patch \
           file://defconfig \
          "

SRC_URI[md5sum] = "ce49828adecf8908eb3a9ffc5b860d44"
SRC_URI[sha256sum] = "4a55419c946a45c62a96b0313eff574c38650a3a270b2acd8168ec888a4fd02b"

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
