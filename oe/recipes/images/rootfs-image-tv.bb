
require rootfs-image.bb

DESCRIPTION = "<description>"

PV = "0.0.1"
PR = "r0"

INSTALL_PKGS += "x-load-pandaboard u-boot-pandaboard libdce-dev ti-syslink-dev libav-dev mplayer mplayer-common \
		 xserver-xorg-extension-glx xserver-xorg-extension-dri2 xserver-xorg xterm openbox \
		 openbox-theme-clearlooks xf86-video-fbdev xserver-nodm-init xhost \
		 xf86-input-evdev libgl libglu xorg-minimal-fonts xserver-xorg-extension-dri mesa-dri \
		"
#not used currently:
# omap4-sgx-libs omap4-sgx-modules"
RRECOMMENDS += ""

IMAGE_FSTYPES = "tar.gz"
IMAGE_BASENAME = "rootfs-tv"
