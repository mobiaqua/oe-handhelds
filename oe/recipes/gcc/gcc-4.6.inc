DEFAULT_PREFERENCE = "-999"
ARM_INSTRUCTION_SET = "arm"

require gcc-common.inc
LICENSE = "GPLv3"

DEPENDS = "mpfr gmp libmpc libelf"
NATIVEDEPS = "mpfr-native gmp-native libmpc-native"

INC_PR = "r1"

SRCREV = "195121"
PV = "4.6"

BINV = "4.6.3"
BRANCH = "gcc-4_6-branch"
PR_append = "+svnr${SRCPV}"

SRC_URI = "svn://gcc.gnu.org/svn/gcc/branches;module=${BRANCH};proto=http \
           file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
           file://cache-amnesia.patch \
           file://gcc-flags-for-build.patch \
           file://304-index_macro.patch  \
           file://305-libmudflap-susv3-legacy.patch  \
           file://800-arm-bigendian.patch  \
           file://904-flatten-switch-stmt-00.patch \
           file://arm-nolibfloat.patch \
           file://arm-softfloat.patch \
           file://GLIBC_DYNAMIC_LINKER.patch \
           file://arm-hard-float-loader.patch \
           file://gcc-armv4-pass-fix-v4bx-to-ld.patch \
           file://zecke-xgcc-cpp.patch \
           file://gcc-poison-system-directories.patch \
           file://gcc-poison-dir-extend.patch \
           file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
           file://64bithack.patch \
           file://optional_libstdc.patch \
           file://disable_relax_pic_calls_flag.patch \
           file://COLLECT_GCC_OPTIONS.patch \
           file://fix-for-ice-50099.patch \
           file://gcc-with-linker-hash-style.patch \
           file://pr46934.patch \
           file://pr32219.patch \
           file://pr47551.patch \
           file://gcc-arm-set-cost.patch \
           file://GPLUSPLUS_INCLUDE_DIR_with_sysroot.patch \
           file://tex_fixes.patch \
"

# Language Overrides
FORTRAN = ""
JAVA = ""

S = "${WORKDIR}/${BRANCH}"

EXTRA_OECONF_BASE = " --enable-lto --enable-libssp --disable-bootstrap --disable-libgomp --disable-libmudflap"
EXTRA_OECONF_INITIAL = "--disable-libmudflap --disable-libgomp --disable-libssp --enable-decimal-float=no --disable-libquadmath"
EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap --disable-libgomp --disable-libssp --disable-libquadmath"
