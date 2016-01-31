DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0 libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r0"
PV = "1.9.0.8.1.1"
PR_append = "+gitr-${SRCREV}"

SRCREV = "4df1d8556cf3b4a6d5f2bc156e8730714c294c28"

SRC_URI = "https://gitorious.org/ubuntu-omap/pvr-omap4.git;protocol=git \
	   file://99-pvr.conf \
	   file://LICENSE.txt \
	   file://video_raw_update.patch \
	   "

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "omap4-sgx-modules libdrm wayland"
PROVIDES += "virtual/egl"

DEFAULT_PREFERENCE = "10"

S = "${WORKDIR}/git"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${bindir}
	cp -pR ${S}${bindir}/* ${D}${bindir}/

	install -d ${D}${includedir}
	cp -pR ${S}${includedir}/* ${D}${includedir}/

	install -d ${D}${libdir}
	cp -pR ${S}${libdir}/* ${D}${libdir}/
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
