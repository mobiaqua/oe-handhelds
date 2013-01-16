LICENSE = "TI"

PV = "1.00"
PR = "r0"

FILESPATHPKG =. "libdce:"

SRC_URI = "http://launchpadlibrarian.net/125118254/ti-firmware-ipu-dce_1.6+121207+151913+git17438c0.tar.gz;name=dce \
           http://launchpadlibrarian.net/125118246/ti-firmware-dsp-rdaemon_1.6+121207+154857+git17438c0.tar.gz;name=dsp \
"

SRC_URI[dce.md5sum] = "d0309bfe18d70f95a48e15f5cb6c1c3a"
SRC_URI[dce.sha256sum] = "36e1523e9c6b3cd9db60ec35e1307053f72e357b1033795e016bad6e1bdf2ac3"
SRC_URI[dsp.md5sum] = "9782e9e3ee0da1b3ec46ba0f05cc15d1"
SRC_URI[dsp.sha256sum] = "ce9133a4e9519b5a3cbbcad8688b0c0818a0be13a1759bd02daa9dc915c32dfc"

S = "${WORKDIR}"
DCE_DIR = "ducati-dce-ee4f5fd145de23fb00ee61a4f98dc4f8ae7d812e"
DSP_DIR = "tesla-rdaemon-bfaac0fa5aa95f148d001adbab5fb5397fb9fbe7"

do_install() {
	install -d ${D}${base_libdir}/firmware

	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.debug ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.xem3.map ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DCE_DIR}/ti-firmware-ipu-dce.license.txt ${D}${base_libdir}/firmware/
	ln -s ti-firmware-ipu-dce.xem3 ${D}${base_libdir}/firmware/ducati-m3-core0.xem3

	install -m 0644 ${S}/${DSP_DIR}/ti-firmware-dsp-rdaemon.xe64T ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DSP_DIR}/ti-firmware-dsp-rdaemon.xe64T.debug ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DSP_DIR}/ti-firmware-dsp-rdaemon.xe64T.map ${D}${base_libdir}/firmware/
	install -m 0644 ${S}/${DSP_DIR}/ti-firmware-dsp-rdaemon.license.txt ${D}${base_libdir}/firmware/
	ln -s ti-firmware-dsp-rdaemon.xe64T ${D}${base_libdir}/firmware/tesla-dsp.xe64T
}

INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"

FILES_${PN} = "${base_libdir}/firmware/"
