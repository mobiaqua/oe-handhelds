DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "2"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "https://launchpadlibrarian.net/83641501/pvr-omap4-dkms_1.7.9.2.1.1.orig.tar.gz \
	   file://rc.pvr \
          "

SRC_URI[md5sum] = "7beef86f873a6ed5e9c21d6a47b0e414"
SRC_URI[sha256sum] = "91612c8a648194d88be849490fd667d882a11a26556165209d883b7a8588ca97"

S = "${WORKDIR}/pvr-omap4-dkms-1.7.9.2.1.1/sgx"

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
