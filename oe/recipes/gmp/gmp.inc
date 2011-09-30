SECTION = "libs"
DESCRIPTION = "GNU multiprecision arithmetic library"
HOMEPAGE = "http://www.swox.com/gmp/"
LICENSE = "GPL LGPL"
INC_PR = "r1"

#MobiAqua: added patches from MacPorts: patch-config.guess.i7.diff, patch-gmp-h.in.diff
SRC_URI = "${GNU_MIRROR}/gmp/gmp-${PV}.tar.bz2;name=gmp \
	   file://configure.patch \
	   file://amd64.patch \
	   file://patch-config.guess.i7.diff;striplevel=0 \
	   file://patch-gmp-h.in.diff;striplevel=0"

inherit autotools 

ARM_INSTRUCTION_SET = "arm"


EXTRA_OECONF_append_virtclass-native = " --enable-static"

#MobiAqua: disable BBCLASSEXTEND and NATIVE_INSTALL_WORKS, and added separate native package
#BBCLASSEXTEND = "native nativesdk"
#NATIVE_INSTALL_WORKS = "1"

acpaths = ""

