DEFAULT_PREFERENCE = "-999"
ARM_INSTRUCTION_SET = "arm"

require gcc-common.inc
LICENSE = "GPLv3"

DEPENDS = "mpfr gmp libmpc libelf"
NATIVEDEPS = "mpfr-native gmp-native libmpc-native"

INC_PR = "r1"

SRCREV = "181430"
PV = "4.6"

BINV = "4.6.3"
BRANCH = "gcc-4_6-branch"
PR_append = "+svnr${SRCPV}"

SRC_URI = "svn://gcc.gnu.org/svn/gcc/branches;module=${BRANCH};proto=http \
	   file://gcc-4.3.1-ARCH_FLAGS_FOR_TARGET.patch \
	   file://100-uclibc-conf.patch \
           file://gcc-uclibc-locale-ctype_touplow_t.patch \
	   file://cache-amnesia.patch \
	   file://gcc-flags-for-build.patch \
           file://103-uclibc-conf-noupstream.patch  \
           file://200-uclibc-locale.patch  \
           file://203-uclibc-locale-no__x.patch; \
           file://204-uclibc-locale-wchar_fix.patch; \
           file://205-uclibc-locale-update.patch; \
           file://301-missing-execinfo_h.patch  \
           file://302-c99-snprintf.patch  \
           file://303-c99-complex-ugly-hack.patch  \
           file://304-index_macro.patch  \
           file://305-libmudflap-susv3-legacy.patch  \
           file://306-libstdc++-namespace.patch  \
           file://740-sh-pr24836.patch  \
           file://800-arm-bigendian.patch  \
           file://904-flatten-switch-stmt-00.patch  \
           file://arm-nolibfloat.patch  \
           file://arm-softfloat.patch \
           file://zecke-xgcc-cpp.patch \
           file://gcc-poison-system-directories.patch \
           file://gcc-poison-dir-extend.patch \
           file://gcc-4.3.3-SYSROOT_CFLAGS_FOR_TARGET.patch \
	   file://64bithack.patch \
	   file://optional_libstdc.patch \
	   file://disable_relax_pic_calls_flag.patch \
	   file://COLLECT_GCC_OPTIONS.patch \
           file://volatile_access_backport.patch \
	   file://powerpc-e5500.patch \
           file://fix-for-ice-50099.patch \
	   file://gcc-with-linker-hash-style.patch \
	   file://pr46934.patch \
	   file://pr32219.patch \
	   file://pr47551.patch \
	   file://gcc-arm-set-cost.patch \
	   file://GPLUSPLUS_INCLUDE_DIR_with_sysroot.patch \
	  "

SRC_URI_append_mips64 = " file://mips64-nomultilib.patch "
SRC_URI_append_mips64el = " file://mips64-nomultilib.patch "

# Language Overrides
FORTRAN = ""
#MobiAqua: disabled fortran
#FORTRAN_linux-gnueabi = ",fortran"
JAVA = ""

S = "${WORKDIR}/${BRANCH}"

EXTRA_OECONF_BASE = " --enable-lto \
		      --enable-libssp \
		      --disable-bootstrap --disable-libgomp \
		      --disable-libmudflap"

#MobiAqua: added  --disable-libquadmath in two lines
EXTRA_OECONF_INITIAL = "--disable-libmudflap --disable-libgomp --disable-libssp --enable-decimal-float=no --disable-libquadmath"
EXTRA_OECONF_INTERMEDIATE = "--disable-libmudflap --disable-libgomp --disable-libssp --disable-libquadmath"

EXTRA_OECONF_append_libc-uclibc = " --disable-decimal-float "

EXTRA_OECONF_append_mips64 = " --with-arch-64=mips64 --with-tune-64=mips64"
EXTRA_OECONF_append_mips64el = " --with-arch-64=mips64 --with-tune-64=mips64"

