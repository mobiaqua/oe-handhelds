DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/136815447/pvr-omap4_${PV}.orig.tar.gz \
	   file://99-pvr.conf \
	   file://LICENSE.txt \
	   "

SRC_URI[md5sum] = "50e73213b59d8611bf30967d9fb3e7ef"
SRC_URI[sha256sum] = "76c01091bb58f55859e395a2f7dfe5101efbb8b0f313e24fc01765f6048ff723"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules libdrm wayland"
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
	install -d ${D}${bindir}
	cp -pR ${S}${bindir}/* ${D}${bindir}/
#	cp -pR ${S}${libdir}/debug/usr/bin/* ${D}${bindir}/

	install -d ${D}${includedir}
	cp -pR ${S}${includedir}/* ${D}${includedir}/

	install -d ${D}${libdir}
	cp -pR ${S}${libdir}/* ${D}${libdir}/
#	cp -pR ${S}${libdir}/debug/usr/lib/* ${D}${libdir}/
	rm -rf ${D}${libdir}/debug

#	install -d ${D}${datadir}/X11/xorg.conf.d
#	cp ${WORKDIR}/99-pvr.conf ${D}${datadir}/X11/xorg.conf.d/

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir} ${includedir} /usr/share/doc/"
