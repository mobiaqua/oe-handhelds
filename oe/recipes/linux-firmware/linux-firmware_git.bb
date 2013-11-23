DESCRIPTION = "Firmware files for use with Linux kernel"
SECTION = "kernel"

SRCREV = "7d0c7a8cfd78388d90cc784a185b19dcbdbce824"

PV = "0.0+git${SRCREV}"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git;protocol=git"

S = "${WORKDIR}/git"

do_compile() {
	:
}

do_install() {
	install -d  ${D}/lib/firmware/
	# Libertas sd8686
	install -m 0666 libertas/sd8686_v9.bin ${D}/lib/firmware/sd8686.bin
	install -m 0666 libertas/sd8686_v9_helper.bin ${D}/lib/firmware/sd8686_helper.bin
	install -m 0666 LICENCE.libertas ${D}/lib/firmware/
}

PACKAGES = "${PN}-sd8686"

FILES_${PN}-sd8686 = "/lib/firmware/sd8686* /lib/firmware/LICENCE.libertas"
PROVIDES += "${PN}-sd8686"

RPROVIDES_${PN}-sd8686 = "${PN}-sd8686"

PACKAGE_ARCH = "all"
