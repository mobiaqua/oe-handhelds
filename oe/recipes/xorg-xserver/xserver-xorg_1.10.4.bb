# Assign it here, since the hal->udev transition happens post 1.7 in angstrom
DISTRO_XORG_CONFIG_MANAGER_angstrom = "udev"

require xorg-xserver-common.inc

DESCRIPTION = "the X.Org X server"
DEPENDS += "pixman libpciaccess openssl dri2proto glproto xorg-minimal-fonts font-util-native"
PE = "2"
PR = "${INC_PR}.0"

DEFAULT_PREFERENCE = "1"
DEFAULT_PREFERENCE_shr = "1"

SRC_URI += " \
            file://hack-assume-pixman-supports-overlapped-blt.patch \
           "
SRC_URI[archive.md5sum] = "94f23d30a77d63e27dd209a57fccfebf"
SRC_URI[archive.sha256sum] = "fafc16b97b9a61b62dfaa74e8d336baa0cea752ce9ed8103c4d212baa8031ca5"

do_install_prepend() {
        mkdir -p ${D}/${libdir}/X11/fonts
}

# The NVidia driver requires Xinerama support in the X server. Ion uses it.
PACKAGE_ARCH_ion = "${MACHINE_ARCH}"
XINERAMA = "${@['--disable-xinerama','--enable-xinerama'][bb.data.getVar('MACHINE',d) in ['ion']]}"

EXTRA_OECONF += " ${CONFIG_MANAGER_OPTION} ${XINERAMA} --disable-kdrive --disable-xephyr --disable-xsdl --disable-xfake --disable-xfbdev --disable-dmx"
EXTRA_OECONF += " --enable-dri2 --disable-unit-tests --disable-docs --disable-devel-docs"

export LDFLAGS += " -ldl "
