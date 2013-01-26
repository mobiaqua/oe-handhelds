require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Texas Instruments OMAP framebuffer driver"

SUMMARY = "X.Org X server -- TI OMAP integrated graphics chipsets driver"

DESCRIPTION = "Open-source X.org graphics driver for TI OMAP graphics \
Currently relies on a closed-source submodule for EXA acceleration on \
the following chipsets: \
  + OMAP3430 \
  + OMAP3630 \
  + OMAP4430 \
  + OMAP4460 \
  + OMAP5430 \
  + OMAP5432 \
\
NOTE: this driver is work in progress..  you probably don't want to try \
and use it yet.  The API/ABI between driver and kernel, and driver and \
acceleration submodules is not stable yet.  This driver requires the \
omapdrm kernel driver w/ GEM support. \
"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10ce5de3b111315ea652a5f74ec0c602"
DEPENDS += "virtual/libx11 libdrm xf86driproto"

SRCREV = "4a3b091c4615b8ce6bdfb7ffb3a531f737a8ee95"
PR = "${INC_PR}.3"
PV = "0.4.2+gitr${SRCPV}"

SRC_URI = "git://gitorious.org/ubuntu-omap/xf86-video-omap.git;protocol=git \
           file://configure.patch \
           file://0000-UBUNTU-Add-Headers-for-dev-package.patch \
           file://0001-dri2-fix-some-leaks.patch \
           file://0002-WIP-dri2video-v5.patch \
           file://0003-WIP-non-multiplanar-dri2video.patch \
           file://0004-dri2-fix-various-leaks.patch \
           file://0005-dri2-fix-clipping-with-dri2video.patch \
           file://0006-Fix-WIP-dri2video-v5.patch \
           file://dri2-fix-triple-buffer.patch \
"

S = "${WORKDIR}/git"

CFLAGS += " -I${STAGING_INCDIR}/xorg "

# Use overlay 2 on omap3 to enable other apps to use overlay 1 (e.g. dmai or omapfbplay)
do_compile_prepend_armv7a () {
        sed -i -e s:fb1:fb2:g ${S}/src/omap_xv.c
}
