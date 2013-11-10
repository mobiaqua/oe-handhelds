
require rootfs-base.inc

DESCRIPTION = "<description>"

PV = "1.0.0"
PR = "r0"

INSTALL_PKGS += ""

RDEPENDS += "kernel-image"
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-pda-sa1110"
IMAGE_INSTALL += "${INSTALL_PKGS} "
