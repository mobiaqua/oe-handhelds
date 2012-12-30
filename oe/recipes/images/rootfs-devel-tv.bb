
require rootfs-base.inc
require rootfs-release-tv.bb
require rootfs-tools.inc

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev mplayer libav-dev freetype-dev fontconfig-dev \
		alsa-dev libmpg123-dev omap4-sgx-libs omap4-sgx-modules xserver-xorg-extension-glx xserver-xorg-extension-dri2 \
		xserver-xorg xterm openbox openbox-theme-clearlooks xf86-video-fbdev xserver-nodm-init xhost xf86-input-evdev libgl \
		libglu xorg-minimal-fonts xserver-xorg-extension-dri mesa-dri libx11-dev"

RDEPENDS += ""
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-devel-tv"
