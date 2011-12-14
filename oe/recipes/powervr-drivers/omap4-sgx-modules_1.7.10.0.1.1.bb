DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "2"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "http://launchpadlibrarian.net/86859448/pvr-omap4-dkms_${PV}.orig.tar.gz \
	   file://rc.pvr \
          "

SRC_URI[md5sum] = "503e49a6f43fa9c4e491ca89c362a50e"
SRC_URI[sha256sum] = "0fd0b62e5acf0eabaac4aab46f1f817a0737bc3927c5f5c5be0f8cdf8a0b8859"

S = "${WORKDIR}/pvr-omap4-dkms-${PV}/sgx"

inherit module

MACHINE_KERNEL_PR_append = "a"

MAKE_TARGETS = "-C eurasiacon/build/linux2/omap4430_linux BUILD=release W=1"

INITSCRIPT_NAME = "pvr-init.sh"

do_install() {
	mkdir -p ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/gpu/pvr
	cp eurasiacon/binary2_omap4430_linux_release/target/kbuild/*.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/gpu/pvr

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rc.pvr ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done
}

PACKAGE_STRIP = "no"

FILES_${PN} += "${sysconfdir}"
