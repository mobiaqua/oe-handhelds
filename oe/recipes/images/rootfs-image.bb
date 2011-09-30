
DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

DEPENDS_append=" virtual/kernel "

INSTALL_PKGS = "\
	base-files base-passwd busybox kernel-modules sysvinit sysvinit-pidof \
	netbase modutils-initscripts tinylogin initscripts tinylogin update-alternatives \
	e2fsprogs e2fsprogs-mke2fs util-linux-ng dosfstools module-init-tools-depmod ifupdown \
	modutils-collateral mtd-utils \
"

RRECOMMENDS = ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-base"
IMAGE_LINGUAS = ""
IMAGE_INSTALL = "${INSTALL_PKGS} "

inherit image
