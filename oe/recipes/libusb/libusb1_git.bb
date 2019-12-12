DESCRIPTION = "Userspace library to access USB (version 1.0)"
HOMEPAGE = "http://libusb.sf.net"
BUGTRACKER = "http://www.libusb.org/report"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

BBCLASSEXTEND = "native nativesdk"

PR = "r1"

SRCREV = "f20f2be7825e34b5273af17fc0740c60bd352b32"

SRC_URI = "git://github.com/libusb/libusb.git;protocol=git \
          "

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PROVIDES = "virtual/libusb1"

EXTRA_OECONF = "--libdir=${base_libdir} --disable-udev"

do_install_append() {
	install -d ${D}${libdir}
	if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
		mv ${D}${base_libdir}/pkgconfig ${D}${libdir}
	fi
}

FILES_${PN} += "${base_libdir}/*.so.*"

FILES_${PN}-dev += "${base_libdir}/*.so ${base_libdir}/*.la"
