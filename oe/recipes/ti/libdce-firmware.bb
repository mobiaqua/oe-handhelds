LICENSE = "TI"

PV = "1.00"
PR = "r0"

SRC_URI = "http://launchpadlibrarian.net/125118254/ti-firmware-ipu-dce_1.6+121207+151913+git17438c0.tar.gz \
"

SRC_URI[md5sum] = "d0309bfe18d70f95a48e15f5cb6c1c3a"
SRC_URI[sha256sum] = "36e1523e9c6b3cd9db60ec35e1307053f72e357b1033795e016bad6e1bdf2ac3"

S = "${WORKDIR}"
DCE_DIR = "ducati-dce-ee4f5fd145de23fb00ee61a4f98dc4f8ae7d812e"

do_install() {
	install -d ${D}${base_libdir}/firmware

	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.debug ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.map ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.license.txt ${D}${base_libdir}/firmware/
	ln -s ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/ducati-m3-core0.xem3
}

INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

FILES_${PN} = "${base_libdir}/firmware/"
