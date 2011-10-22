DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "https://launchpadlibrarian.net/81314678/pvr-omap4_1.7.9.0.1.5.orig.tar.gz \
	   file://99-pvr.conf \
	   "

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "2"

SRC_URI[md5sum] = "9a8539417c7eaa2918f0510569ccb0b9"
SRC_URI[sha256sum] = "b5f361ae19352316baf8e5ac051cc40e9831278370a29591e4944edc7b599710"

S = "${WORKDIR}/pvr-omap4-1.7.9.0.1.5"

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

	install -d ${D}${datadir}/X11/xorg.conf.d
	cp ${WORKDIR}/99-pvr.conf ${D}${datadir}/X11/xorg.conf.d/
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir}"
