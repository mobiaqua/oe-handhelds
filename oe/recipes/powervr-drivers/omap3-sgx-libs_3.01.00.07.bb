DESCRIPTION = "PowerVR SGX libraries"
SECTION = "libs"
HOMEPAGE = "http://software-dl.ti.com/dsps/dsps_public_sw/sdo_sb/targetcontent/gfxsdk"
LICENCE = "TI TSPA"

PR = "r0"

SRC_URI = "http://launchpadlibrarian.net/56949920/opengles-sgx-omap3_${PV}-0ubuntu2.tar.gz \
	file://LICENSE.txt \
	file://rc.pvr \
	file://powervr.ini \
	file://99-bufferclass.rules \
	file://includes \
"

S = "${WORKDIR}/opengles-sgx-omap3-${PV}"

SRC_URI[md5sum] = "4104af2a1263d110be185924398a1c70"
SRC_URI[sha256sum] = "5733f48f78ab3c8186afb3f6629c60c641472cb56fb4da4848b097e1882bdba3"

INITSCRIPT_NAME = "pvr-init.sh"

do_install() {
	# Create init script and runlevel links
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rc.pvr ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
	for i in 2 3 4 5; do
	  install -d ${D}${sysconfdir}/rc${i}.d
	  ln -sf ../init.d/${INITSCRIPT_NAME} ${D}${sysconfdir}/rc${i}.d/S30${INITSCRIPT_NAME}
	done

	install -m 0644 ${WORKDIR}/powervr.ini ${D}${sysconfdir}/powervr.ini

	install -d ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/eglinfo ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/gl2info ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/gles1test1 ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/gles2test1 ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/ovg_unit_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/pdump ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/pvr2d_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/pvrsrvinit ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/services_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/sgx_blit_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/sgx_flip_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/sgx_init_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/sgx_render_flip_test ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/xgles1test1 ${D}${bindir}
	install -m 0755 ${S}/gfx_rel_es5.x/xmultiegltest ${D}${bindir}

	install -d ${D}${datadir}
	install -m 0666 ${S}/gfx_rel_es5.x/glsltest1* ${D}${datadir}

	install -d ${D}${libdir}
	cp -pR ${S}/gfx_rel_es5.x/lib* ${D}${libdir}

	install -d ${D}${includedir}
	cp -pR ${WORKDIR}/includes/* ${D}${includedir}

	install -d ${D}/usr/share/doc/${PN}
	install -m 0666 ${WORKDIR}/LICENSE.txt ${D}/usr/share/doc/${PN}

	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/99-bufferclass.rules ${D}${sysconfdir}/udev/rules.d/

}

PACKAGES = "${PN}"

FILES_${PN} = "${sysconfdir} ${bindir} ${includedir} ${libdir} ${datadir} /usr/share/doc/"

# These are binaries, so we can't guarantee that LDFLAGS match
INSANE_SKIP = True
INHIBIT_PACKAGE_STRIP = "1"
PACKAGE_STRIP = "no"
