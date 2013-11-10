
require rootfs-base.inc

DESCRIPTION = "<description>"

PV = "1.0.0"
PR = "r0"

INSTALL_PKGS += ""

RDEPENDS += "kernel-image"
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-pda-pxa250"
IMAGE_INSTALL += "${INSTALL_PKGS} "
