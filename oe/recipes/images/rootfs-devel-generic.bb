
require rootfs-base.inc
require rootfs-tools.inc
require rootfs-release-tv.bb

DESCRIPTION = "<description>"

PV = "1.0.0"
PR = "r0"

INSTALL_PKGS += ""

DEPENDS_append=" gdb-cross"

RDEPENDS += ""
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-devel-generic"
