LICENSE = "TI"

PV = "1.00"
PR = "r0"

SRC_URI = "http://launchpadlibrarian.net/125118254/ti-firmware-ipu-dce_1.6+121207+151913+git17438c0.tar.gz \
           file://remote_proc_dce.sh \
"

SRC_URI[md5sum] = "d0309bfe18d70f95a48e15f5cb6c1c3a"
SRC_URI[sha256sum] = "36e1523e9c6b3cd9db60ec35e1307053f72e357b1033795e016bad6e1bdf2ac3"

S = "${WORKDIR}"
DCE_DIR = "ducati-dce-ee4f5fd145de23fb00ee61a4f98dc4f8ae7d812e"

INITSCRIPT_NAME = "remote_proc_dce.sh"

do_install() {
	install -d ${D}${base_libdir}/firmware

	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.debug ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.map ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.license.txt ${D}${base_libdir}/firmware/
	ln -s ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/ducati-m3-core0.xem3

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/remote_proc_dce.sh ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S40${INITSCRIPT_NAME}
	done
}

FILES_${PN} += "${sysconfdir} ${base_libdir}/firmware/"

INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"
