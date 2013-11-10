
require rootfs-base.inc
require rootfs-release-tv.bb
require rootfs-tools.inc

X11_PKGS = "xserver-xorg-extension-glx xserver-xorg-extension-dri2 \
		xserver-xorg openbox openbox-theme-clearlooks xf86-video-omap xserver-nodm-init xhost xf86-input-evdev libgl \
		libglu xorg-minimal-fonts xserver-xorg-extension-dri mesa-dri libx11"

INSTALL_PKGS += "u-boot-pandaboard libdce libdce-firmware libavcodec libavformat libavutil omapdrmtest kmscube \
		omap4-sgx-libs omap4-sgx-modules wayland openssh-sftp-server"

#INSTALL_PKGS += "${X11_PKGS} "

DEPENDS_append=" gdb-cross"
RDEPENDS += ""
RRECOMMENDS += ""

IMAGE_BASENAME = "rootfs-devel-tv"
