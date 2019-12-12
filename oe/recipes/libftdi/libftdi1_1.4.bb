DESCRIPTION = "libftdi is a library to talk to FTDI chips.\
FT232BM/245BM, FT2232C/D and FT232/245R using libusb,\
including the popular bitbang mode."
HOMEPAGE = "http://www.intra2net.com/en/developer/libftdi/"
LICENSE = "LGPL GPLv2+linking exception"
SECTION = "libs"

DEPENDS = "virtual/libusb1"
DEPENDS_virtclass-native = "virtual/libusb1-native"
SRC_URI = "https://www.intra2net.com/en/developer/libftdi/download/libftdi1-${PV}.tar.bz2 \
           file://autotools.patch \
          "
S = "${WORKDIR}/libftdi1-${PV}"

SRC_URI[md5sum] = "0c09fb2bb19a57c839fa6845c6c780a2"
SRC_URI[sha256sum] = "ec36fb49080f834690c24008328a5ef42d3cf584ef4060f3a35aa4681cb31b74"

inherit autotools binconfig pkgconfig

EXTRA_OECONF = "--with-boost=no --without-examples --without-docs"

BBCLASSEXTEND = "native"

