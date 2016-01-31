#MobiAqua: changed depends to libdce-firmware. update to new git revision
DEPENDS = "libdce-firmware libdrm"

LICENSE = "TI"

inherit autotools lib_package

PV = "1.7.0.2"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "33bde2904debedbf33819a445da7c9baa1149a97"
SRC_URI = "git://git.ti.com/glsdk/dce.git;protocol=git \
           file://update-from-glsdk.patch \
           file://disable-wayland-x11.patch \
          "

S = "${WORKDIR}/git"
