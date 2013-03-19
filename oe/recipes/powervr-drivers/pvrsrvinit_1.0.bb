DESCRIPTION = "A tool to make init call to pvr service"
LICENSE = "GPLv2"
SECTION = "base"
PRIORITY = "required"
PR = "r1"

SRC_URI = "file://pvrsrvinit.c"
S = "${WORKDIR}/pvrsrvinit-${PV}"

do_configure() {
	install -m 0644 ${WORKDIR}/pvrsrvinit.c ${S}/
}

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} -lsrv_init -s -o ${S}/pvrsrvinit ${S}/pvrsrvinit.c
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${S}/pvrsrvinit ${D}${bindir}/pvrsrvinit
}
