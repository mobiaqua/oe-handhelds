ARM_INSTRUCTION_SET = "arm"

require gcc-common.inc
LICENSE = "GPLv3"

#MobiAqua: custom 4.6.1
DEPENDS = "mpfr gmp libmpc libelf"
NATIVEDEPS = "mpfr-native gmp-native libmpc-native"

INC_PR = "r0"

SRC_URI = "${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
	   file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://cache-amnesia.patch \
	   file://gcc-flags-for-build.patch \
	   file://304-index_macro.patch \
	   file://305-libmudflap-susv3-legacy.patch \
	   file://904-flatten-switch-stmt-00.patch  \
	   file://arm-nolibfloat.patch \
	   file://arm-softfloat.patch \
	   file://zecke-xgcc-cpp.patch \
	   file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://64bithack.patch \
	   file://GPLUSPLUS_INCLUDE_DIR_with_sysroot.patch \
	   file://COLLECT_GCC_OPTIONS.patch \
	   file://volatile_access_backport.patch \
	   file://fix-for-ice-50099.patch \
	   file://ICE_fix_for_arm.patch \
	  "

# Language Overrides
FORTRAN = ""
JAVA = ""

EXTRA_OECONF_BASE = " --enable-lto \
		      --enable-libssp \
		      --disable-bootstrap --disable-libgomp \
		      --disable-libmudflap --enable-cheaders=c_global"

EXTRA_OECONF_INITIAL = "--disable-libmudflap --disable-libgomp --disable-libssp --enable-decimal-float=no --disable-libquadmath"
EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap --disable-libgomp --disable-libssp --disable-libquadmath"

SRC_URI[md5sum] = "c57a9170c677bf795bdc04ed796ca491"
SRC_URI[sha256sum] = "8eebf51c908151d1f1a3756c8899c5e71572e8469a547ad72a1ef16a08a31b59"

