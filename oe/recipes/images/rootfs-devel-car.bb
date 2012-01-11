
require rootfs-base.inc
require rootfs-release-car.bb
require rootfs-devel.inc

#not use currently:
#INSTALL_PKGS += "u-boot-igep omap3-sgx-libs"
#not buildable on mac os x for now
#INSTALL_PKGS += "libgles-omap3 ti-codecs-omap3530"

RDEPENDS += "
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-devel-car"
