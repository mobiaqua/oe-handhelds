DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/91202777/pvr-omap4_${PV}.orig.tar.gz \
	   file://99-pvr.conf \
	   file://LICENSE.txt \
	   file://includes \
	   "

SRC_URI[md5sum] = "8538799461794654b03d43fc5dfbaa36"
SRC_URI[sha256sum] = "b0a15ef5123a953eff26f5cd82aed553b4139fc420a31add8924789d6d1d0341"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules libdrm"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "2"


S = "${WORKDIR}/pvr-omap4-${PV}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${includedir}
	cp -pR ${S}${includedir}/* ${D}${includedir}/
	cp -pR ${WORKDIR}/includes/* ${D}${includedir}/

	install -d ${D}${libdir}
#	rm -r ${S}${libdir}/debug/
#	rm -r ${S}${libdir}/xorg/
#	rm ${S}${libdir}/libpvrPVR2D_DRIWSEGL*
	cp -pR ${S}${libdir}/* ${D}${libdir}/

	install -d ${D}${datadir}/X11/xorg.conf.d
	cp ${WORKDIR}/99-pvr.conf ${D}${datadir}/X11/xorg.conf.d/
	cp -pR ${S}${datadir}/sgx-lib ${D}${datadir}/

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir} ${includedir} /usr/share/doc/"
