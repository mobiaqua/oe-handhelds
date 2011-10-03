PR = "${INC_PR}.1"
require gcc-${PV}.inc
require gcc-configure-target.inc
require gcc-package-target.inc

# Gcc 4.3.3 installs crt* in a '4.3.1' dir....
FILES_${PN} += "\
        ${gcclibdir}/${TARGET_SYS}/*/*.o \
	${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lto1 \
	${libexecdir}/gcc/${TARGET_SYS}/${BINV}/lto-wrapper \
"

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"

#MobiAqua: added below lines:
export CPP="gcc -E"
export BUILD_LDFLAGS="-L${STAGING_LIBDIR_NATIVE}"

DEPENDS = "mpfr gmp libelf"
RDEPENDS = "libmpc-static"

SRC_URI += "\
	file://workaround-missing-auto-build.patch \
	file://auto-build.h \
	file://workaround-wrong-native-ldflags.patch \
"
