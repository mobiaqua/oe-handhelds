DESCRIPTION = "WSEGL library using /dev/fb0"
LICENSE = "GPL"
SECTION = "libs"
PR = "r1"

inherit lib_package

SRC_URI = "file://pvrlinuxfbdrawable.c file://pvrlinuxfbdrawable.h file://pvrlinuxfbdrawable_p.h file://pvrlinuxfbwsegl.c"
S = "${WORKDIR}/omap-fb-wsegl-${PV}"

do_configure() {
	install -m 0644 ${WORKDIR}/pvrlinuxfb* ${S}/
}

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} -s -fPIC -shared ${S}/pvrlinuxfbdrawable.c ${S}/pvrlinuxfbwsegl.c -o ${S}/pvrlinuxfbwsegl.so
}

do_install() {
	install -d ${D}${libdir}
	install -m 0755 ${S}/pvrlinuxfbwsegl.so ${D}${libdir}/libpvrPVR2D_DRIWSEGL.so
}

FILES_${PN} = " ${libdir}/ "
