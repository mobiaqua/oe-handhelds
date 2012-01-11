INSTALL_PKGS += "\
	libstdc++ libgcc libgcc-dev alsa-utils-amixer alsa-utils-speakertest alsa-utils-alsactl e2fsprogs \
	e2fsprogs-mke2fs dosfstools tar gzip bzip2 mtd-utils bzip2 mtd-utils unzip mtools \
	binutils gcc make patch m4 gdb python perl gdbserver automake autoconf \
	bison coreutils file gawk libtool pkgconfig sed expat fakeroot gettext ncurses openssl readline grep \
	strace ltrace git screen dropbear findutils mc time usbutils procps \
	bzip2-dev curl-dev db-dev libelf-dev libstdc++-dev openssl-dev ncurses-dev zlib-dev \
	libusb-dev libsqlite-dev gcc-symlinks g++-symlinks cpp-symlinks binutils-symlinks \
	perl-module-config-heavy perl-module-threads perl-module-thread-queue perl-module-attributes \
"

RRECOMMENDS += ""
RDEPENDS += "libstdc++-dev"
