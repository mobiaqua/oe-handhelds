DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "-1"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "http://launchpadlibrarian.net/81314683/pvr-omap4-dkms_1.7.9.0.1.3.orig.tar.gz \
	   file://rc.pvr \
          "

SRC_URI[md5sum] = "0de2b38ef0d9d3695bd86eb28c25754c"
SRC_URI[sha256sum] = "603a1e7bf3c56888bb3707cd91e8c3f134947044648f093316eab633039507df"

S = "${WORKDIR}/pvr-omap4-dkms-1.7.9.0.1.3/sgx"

inherit module

MACHINE_KERNEL_PR_append = "a"

MAKE_TARGETS = "-C eurasiacon/build/linux2/omap4430_linux BUILD=release SUPPORT_V4L2_GFX=0 W=1"

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
