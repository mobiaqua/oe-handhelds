DESCRIPTION = "Linux Kernel for PDA based platforms"
SECTION = "kernel"
LICENSE = "GPL"

DEFAULT_PREFERENCE = "-1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-pda-4.4:"

inherit kernel

PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v4.x/linux-${PV}.tar.xz \
           file://fix_nonlinux_compile.patch \
           file://0002-input-driver-for-microcontroller-keys-on-the-iPaq-h3.patch \
           file://h3600-fix-micro-port-mod.patch \
           file://h3600-fix-mtd-part.patch \
           file://h3600-fix-micro-keys.patch \
           file://fix-blocking-irq0.patch \
           file://defconfig \
          "

SRC_URI[md5sum] = "9a78fa2eb6c68ca5a40ed5af08142599"
SRC_URI[sha256sum] = "401d7c8fef594999a460d10c72c5a94e9c2e1022f16795ec51746b0d165418b2"

S = "${WORKDIR}/linux-${PV}"

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
