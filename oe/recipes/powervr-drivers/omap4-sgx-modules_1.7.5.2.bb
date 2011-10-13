DESCRIPTION = "Kernel drivers for the PowerVR SGX chipset found in the omap4 SoCs"
LICENSE = "GPLv2"

#MobiAqua: custom package
DEFAULT_PREFERENCE = "-1"
COMPATIBLE_MACHINE = "pandaboard"

SRC_URI = "http://launchpadlibrarian.net/73449323/pvr-omap4-kernel_1.7.5.2+git20110610+9696932c.orig.tar.gz \
	   file://0001-Avoid-calling-set_max_mpu_wakeup_lat-as-it-s-not-ava.patch;striplevel=2 \
	   file://0002-SGX-Porting-kernel-module-to-2.6.38.patch;striplevel=2 \
	   file://0003-Changing-module-name-back-to-pvrsrvkm.patch;striplevel=2 \
	   file://0004-Display-controller-should-depend-on-the-xorg-support.patch;striplevel=2 \
	   file://0005-Provide-a-proper-build-date-for-the-pvr-module.patch;striplevel=2 \
	   file://0006-Fixing-OuterCache-definition-to-be-compatible-with-t.patch;striplevel=2 \
	   file://0008-The-IMG-DRM-module-can-become-a-plugin-sub-module-of.patch;striplevel=2 \
	   file://0009-IMG-DRM-make-SUPPORT_DRI_DRM_EXTERNAL-depends-on-DRM.patch;striplevel=2 \
	   file://rc.pvr \
          "

SRC_URI[md5sum] = "c314d336e5a7d8985ea7ad41e46f2dea"
SRC_URI[sha256sum] = "819e80c5976a0ab782b162794ea319e50afbec53410c845341c0e1fb697fe5e0"

S = "${WORKDIR}/pvr-omap4-kernel-1.7.5.2+git20110610+9696932c/sgx"

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
