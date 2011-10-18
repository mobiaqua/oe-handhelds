DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "1"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "http://launchpadlibrarian.net/64787500/pvr-omap4-kernel_1.7~git0f0b25f.orig.tar.gz \
	   file://rc.pvr \
          "

SRC_URI[md5sum] = "3e6f674451ad9cfa535b2fd67d0b4137"
SRC_URI[sha256sum] = "f0302437c159224cb591d41f29c44404941e97dc106cdfe588357b449545b18a"

S = "${WORKDIR}/pvr-omap4-kernel-1.7~git0f0b25f/sgx"

inherit module

MACHINE_KERNEL_PR_append = "a"

MAKE_TARGETS = "-C eurasiacon/build/linux/omap4430_linux/kbuild BUILD=release SUPPORT_XORG=0"

INITSCRIPT_NAME = "pvr-init.sh"

do_install() {
	mkdir -p ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/gpu/pvr
	cp eurasiacon/binary_omap4430_linux_release/*.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/gpu/pvr

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rc.pvr ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc${i}.d
		ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done
}

PACKAGE_STRIP = "no"

FILES_${PN} += "${sysconfdir}"
