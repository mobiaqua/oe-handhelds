DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/59313448/pvr-omap4_0.24.9c.orig.tar.gz \
	   file://rc.pvr \
	   "

INITSCRIPT_NAME = "pvr-init.sh"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "virtual/libx11 libxau libxdmcp omap4-sgx-modules"
PROVIDES += "virtual/egl"

SRC_URI[md5sum] = "b5cea86a1d3e57eefbd79840b9720629"
SRC_URI[sha256sum] = "6b8e5904579ec3bae1467b5950d915bbad63ef3d0e54972370b9fddf9124852c"

S = "${WORKDIR}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rc.pvr ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done

	install -d ${D}${bindir}
	install -m 0755 ${S}${bindir}/* ${D}${bindir}

	cp -pR ${S}${sysconfdir}/powervr.ini ${D}${sysconfdir}

	install -d ${D}${libdir}
	cp -pR ${S}${libdir}/* ${D}${libdir}/
	rm ${D}${libdir}/libEGL.so.1
	ln -s ${D}${libdir}/libEGL.so.1.1.16.3758 libEGL.so.1
}


INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"

PACKAGES = "${PN}"

FILES_${PN} = "${sysconfdir} ${bindir} ${libdir}"
