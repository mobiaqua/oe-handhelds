
require rootfs-base.inc
require rootfs-release-tv.bb
require rootfs-devel.inc

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev libav-dev mplayer mplayer-common \
		live555-dev live555-static freetype-dev fontconfig-dev alsa-dev libmpg123-dev \
		omap4-sgx-libs omap4-sgx-modules"

RDEPENDS += ""
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-devel-tv"
