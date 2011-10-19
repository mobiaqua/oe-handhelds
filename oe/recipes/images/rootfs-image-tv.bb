
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev mplayer mplayer-common"
RRECOMMENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-tv"
