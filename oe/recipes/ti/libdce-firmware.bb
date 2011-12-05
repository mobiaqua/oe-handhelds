DEPENDS = "ti-libd2cmap ti-tilermemmgr ti-syslink"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.00"
PR = "r0"

FILESPATHPKG =. "libdce:"

SRC_URI = "http://launchpadlibrarian.net/85355221/tiomap4-syslink-mm-firmware_1.50.21.1+dce2+3.tar.gz;name=dce \
	   http://launchpadlibrarian.net/84868865/tiomap4-syslink-ipc-firmware_2.6.1+git20111110+f289b98f.orig.tar.gz;name=ipc \
	   file://ducati-init.sh"

S = "${WORKDIR}"

SRC_URI[dce.md5sum] = "6b67e0d5e276df19f55b025a60f30e97"
SRC_URI[dce.sha256sum] = "e3cdb33e08237e497d86c1ea0297ab2185f0954217cdffbfdc0d8d1c00ca7b51"
SRC_URI[ipc.md5sum] = "ba352b0de9ff468ccbc07ed4c19f91cc"
SRC_URI[ipc.sha256sum] = "8f01097699b514cf54832c095183f3a2a58dfb35e426fef4753823da34419b42"

INITSCRIPT_NAME = "ducati-init.sh"

do_install() {
	install -d ${D}${base_libdir}/firmware/omap4
	install -m 0644 ${S}/tiomap4-syslink-mm-firmware/license.txt ${D}${base_libdir}/firmware/omap4/
	install -m 0644 ${S}/tiomap4-syslink-mm-firmware/dce_app_m3.xem3 ${D}${base_libdir}/firmware/omap4/
	install -m 0644 ${S}/tiomap4-syslink-ipc-firmware-2.6.1+git20111110+f289b98f/Notify_MPUSYS_reroute_Test_Core0.xem3 ${D}${base_libdir}/firmware/omap4/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/${INITSCRIPT_NAME} ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done
}

FILES_${PN} += "${base_libdir}/firmware/"
