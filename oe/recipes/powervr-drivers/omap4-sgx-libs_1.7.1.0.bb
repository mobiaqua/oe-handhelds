DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/67059843/pvr-omap4_1.7~git0f0b25f.3.orig.tar.gz \
	   file://99-pvr.conf \
	   "

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "1"

SRC_URI[md5sum] = "119acf81fd70c4c6b036349e03dd7abd"
SRC_URI[sha256sum] = "2ba8057b2c8d99a9bb4e17bc8de9233c68e178bb891dc0a18aae41d4345a4693"

S = "${WORKDIR}/pvr-omap4-1.7~git0f0b25f.3"

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
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir}"
