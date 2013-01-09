DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "2"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "http://launchpadlibrarian.net/121088771/pvr-omap4-dkms_${PV}.orig.tar.gz \
           file://rc.pvr \
           file://0001-core-mk-fix.patch \
          "

SRC_URI[md5sum] = "13152f14f2c62fda215a7ba5d927d016"
SRC_URI[sha256sum] = "3645f41267fc23a023ed05f06728135bfa6db3cabc06b27335ed42dc42ae7d8e"

S = "${WORKDIR}/git-import-orig/sgx"

inherit module

MACHINE_KERNEL_PR_append = "a"

MAKE_TARGETS = "-C eurasiacon/build/linux2/omap4430_linux BUILD=release W=1 V=1 SUPPORT_V4L2_GFX=0"

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
