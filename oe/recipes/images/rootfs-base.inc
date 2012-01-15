
DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

DEPENDS_append=" virtual/kernel"

INSTALL_PKGS = "\
	base-files base-passwd busybox kernel-modules sysvinit sysvinit-pidof netbase modutils-initscripts \
	tinylogin initscripts tinylogin update-alternatives alsa-utils alsa-state alsa-utils-alsamixer \
	glibc-gconv-ibm850 module-init-tools-depmod ifupdown modutils-collateral dropbear nfs-utils-client \
	util-linux-ng \
"

RRECOMMENDS += ""
RDEPENDS += "ntpdate tzdata tzdata-europe nfs-utils-client"

IMAGE_FSTYPES = "tar.gz"
IMAGE_LINGUAS = ""
IMAGE_INSTALL += "${INSTALL_PKGS} "

IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"

inherit image