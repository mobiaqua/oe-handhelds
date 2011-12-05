
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev libav-dev mplayer mplayer-common \
		"
#not used currently:
# omap4-sgx-libs omap4-sgx-modules"
RRECOMMENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-tv"
