
DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

DEPENDS_append=" virtual/kernel"

INSTALL_PKGS = "\
	base-files base-passwd busybox kernel-modules sysvinit sysvinit-pidof libstdc++ libgcc \
	netbase modutils-initscripts tinylogin initscripts tinylogin update-alternatives alsa-utils alsa-state \
	alsa-utils-alsamixer alsa-utils-amixer alsa-utils-speakertest alsa-utils-alsactl \
	e2fsprogs e2fsprogs-mke2fs util-linux-ng dosfstools tar gzip bzip2 module-init-tools-depmod \
	ifupdown modutils-collateral mtd-utils dropbear bzip2 mtd-utils unzip mtools \
	binutils gcc make patch m4 gdb python perl gdbserver automake autoconf \
	bison coreutils file gawk libtool pkgconfig sed expat fakeroot gettext ncurses openssl readline grep \
	strace ltrace git subversion screen dropbear findutils mc time usbutils procps \
	bzip2-dev curl-dev db-dev libelf-dev libstdc++-dev openssl-dev ncurses-dev zlib-dev \
	libusb-dev libsqlite-dev gcc-symlinks g++-symlinks cpp-symlinks binutils-symlinks \
	perl-module-config-heavy perl-module-threads perl-module-thread-queue fakeroot-dev perl-module-attributes \
	libsdl-x11-dev xserver-xorg-extension-glx xserver-xorg-extension-dri2 xserver-xorg xterm openbox \
	openbox-theme-clearlooks xf86-video-fbdev xserver-nodm-init xhost \
	xf86-input-evdev libgl libglu xorg-minimal-fonts xserver-xorg-extension-dri ttf-bitstream-vera ttf-dejavu-sans ttf-freefonts \
"

RRECOMMENDS += ""
RDEPENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-base"
IMAGE_LINGUAS = ""
IMAGE_INSTALL += "${INSTALL_PKGS} "

inherit image
