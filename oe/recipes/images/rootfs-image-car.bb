
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-igep u-boot-igep"
RRECOMMENDS = ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-car"
