DESCRIPTION = "libftdi is a library to talk to FTDI chips.\
FT232BM/245BM, FT2232C/D and FT232/245R using libusb,\
including the popular bitbang mode."
HOMEPAGE = "http://www.intra2net.com/en/developer/libftdi/"
LICENSE = "LGPL GPLv2+linking exception"
SECTION = "libs"

DEPENDS = "virtual/libusb1"
DEPENDS_virtclass-native = "virtual/libusb1-native"
SRC_URI = "http://www.intra2net.com/en/developer/libftdi/download/libftdi1-${PV}.tar.bz2 \
           file://autotools.patch \
          "

SRC_URI[md5sum] = "e02dd77ee10b82b5edfbb8c199185bbf"
SRC_URI[sha256sum] = "7290fd38208d8c00d25ed13ce172aaa0403b37148080e4a33a99837022d2b321"

inherit autotools binconfig pkgconfig

BBCLASSEXTEND = "native"

