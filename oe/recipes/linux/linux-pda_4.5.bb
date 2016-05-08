DESCRIPTION = "Linux Kernel for PDA based platforms"
SECTION = "kernel"
LICENSE = "GPL"

#DEFAULT_PREFERENCE = "-1"

DEPENDS = "coreutils-native elf-native"

FILESPATHPKG =. "linux-pda-4.5:"

inherit kernel

PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v4.x/linux-${PV}.tar.xz \
           file://fix_nonlinux_compile.patch \
           file://h3600-fix-micro-port-mod.patch \
           file://h3600-fix-mtd-part.patch \
           file://h3600-fix-micro-keys.patch \
           file://fix-blocking-irq0.patch \
           file://defconfig \
          "

SRC_URI[md5sum] = "a60d48eee08ec0536d5efb17ca819aef"
SRC_URI[sha256sum] = "a40defb401e01b37d6b8c8ad5c1bbab665be6ac6310cdeed59950c96b31a519c"

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
