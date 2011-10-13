DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/73949137/pvr-omap4_1.7.5.2+git20110610+9696932c.2.orig.tar.gz \
	   file://99-pvr.conf \
	   "

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "virtual/libx11 libxau libxdmcp omap4-sgx-modules"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "-1"

SRC_URI[md5sum] = "2ff9b35276da13b0b837628027deab83"
SRC_URI[sha256sum] = "4bb9040703213a0bfcbe3cbd64197915992309b69bb4dc0a5aee0e7d91c8bbce"

S = "${WORKDIR}/pvr-omap4-1.7.5.2+git20110610+9696932c.2"

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
PACKAGE_STRIP = "no"

PACKAGES = "${PN}"

FILES_${PN} = "${bindir} ${libdir} ${datadir}"
