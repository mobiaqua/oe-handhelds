SUMMARY = "The runtime library for the DRI2 extension to the X Window System"
DESCRIPTION = "The runtime library for the DRI2 extension to the X Window System."
HOMEPAGE = "http://dri.freedesktop.org"
SECTION = "x11/base"
LICENSE = "MIT"
DEPENDS = "xproto virtual/libx11"

SRCREV = "a81ed5d3f403f4122000e6eee915fb78fda8d919"
PV = "1.0.3+git${SRCREV}"

SRC_URI = "git://gitorious.org/ubuntu-omap/libdri2.git;protocol=git \
           file://0001-dri2video-support.patch \
           file://0002-fix-typo.patch \
           file://0003-Fix-potential-NPE-if-user-doesn-t-register-display.patch \
           file://0004-Add-multi-planar-support.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
