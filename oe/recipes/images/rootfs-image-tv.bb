
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev libav-dev mplayer mplayer-common \
		live555-dev live555-static freetype-dev fontconfig-dev alsa-dev libmpg123-dev \
		"
#not used currently:
# omap4-sgx-libs omap4-sgx-modules"
RRECOMMENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-tv"
