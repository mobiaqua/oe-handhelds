DEPENDS = "ti-libd2cmap ti-tilermemmgr ti-syslink"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.0.0"
PR = "r4"
PR_append = "+gitr${SRCREV}"

SRCREV = "2115a0b6b6fccc27d6ecc274e37e57f2275f99c6"
SRC_URI = "git://github.com/robclark/libdce.git;protocol=git \
	   file://ducati-init.sh"

S = "${WORKDIR}/git"

#MobiAqua: added startup ducati firmware
INITSCRIPT_NAME = "ducati-init.sh"

do_install_append() {
	install -d ${D}${base_libdir}/firmware/omap4
	install -m 0644 firmware/* ${D}${base_libdir}/firmware/omap4/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done
}

FILES_${PN} += "${base_libdir}/firmware/"
