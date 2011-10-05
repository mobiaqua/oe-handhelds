DESCRIPTION = "SGX540 (PowerVR) OpenGL ES 1.1, 2.0, OpenVG libraries for OMAP4."
LICENSE = "proprietary-binary"

PR = "r1"

SRC_URI = "http://launchpadlibrarian.net/59313448/pvr-omap4_0.24.9c.orig.tar.gz"

COMPATIBLE_MACHINE = "pandaboard"
DEPENDS = "virtual/libx11 libxau libxdmcp"
PROVIDES += "virtual/egl"

SRC_URI[md5sum] = "ff8c1f2b8e4cb42f4ced6a613b081ada"
SRC_URI[sha256sum] = "cdb0bd3964e107733d632aa8224e0537b05c1ffac34befc036423458c8d75255"

S = "${WORKDIR}/Graphics_SDK_${SGXPV}"

