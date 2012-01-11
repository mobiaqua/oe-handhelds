DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/89451441/pvr-omap4_${PV}.orig.tar.gz \
	   file://LICENSE.txt \
	   file://includes \
	   "

SRC_URI[md5sum] = "892dfeeb2c2663c9ac92cc200bc2e56b"
SRC_URI[sha256sum] = "51774b2f6503793115a60cd30860573fced695a16a1212ee8b7795f453318cbe"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules"
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
	cp -pR ${S}${libdir}/* ${D}${libdir}/

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir} ${includedir} /usr/share/doc/"
