
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-igep-mlo omap3-sgx-libs"
#not use currently: "u-boot-igep"
#not buildable on mac os x for now
#INSTALL_PKGS += "libgles-omap3 ti-codecs-omap3530"

RDEPENDS += "kernel-module-mailbox kernel-module-bridgedriver"
RRECOMMENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-car"
