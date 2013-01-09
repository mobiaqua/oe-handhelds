DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/125415739/pvr-omap4_${PV}.orig.tar.gz \
	   file://99-pvr.conf \
	   file://LICENSE.txt \
	   file://includes \
	   "

SRC_URI[md5sum] = "fe4f7101e7ecbf524cc4b2e381854bd9"
SRC_URI[sha256sum] = "8822ca9472f099be06b1f6526711c0f949b0496056aad6fa67b04c5f8653c5ba"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules libdrm"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "10"

S = "${WORKDIR}"

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
	rm -rf ${D}${libdir}/debug

	install -d ${D}${datadir}/X11/xorg.conf.d
	cp ${WORKDIR}/99-pvr.conf ${D}${datadir}/X11/xorg.conf.d/

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir} ${includedir} /usr/share/doc/"
