DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/83641534/pvr-omap4_1.7.9.2.1.4.orig.tar.gz \
	   file://LICENSE.txt \
	   "

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "2"

SRC_URI[md5sum] = "7e4312f2af2340e21570f8ea02619f1c"
SRC_URI[sha256sum] = "90cca3f647721b1cb753460271b84828b1d9f41cdead384fb854fa5f57bdf758"

S = "${WORKDIR}/pvr-omap4-1.7.9.2.1.4"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${S}${bindir}/* ${D}${bindir}

	install -d ${D}${includedir}
	cp -pR ${S}${includedir}/* ${D}${includedir}/

	install -d ${D}${libdir}
	cp -pR ${S}${libdir}/* ${D}${libdir}/

	install -d ${D}${datadir}
	cp -pR ${S}${datadir}/sgx-lib ${D}${datadir}/

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir} ${includedir} /usr/share/doc/"
