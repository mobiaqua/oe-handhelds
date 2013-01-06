require xorg-lib-common.inc
DESCRIPTION = "X11 toolkit intrinsics library"
DEPENDS += "libsm virtual/libx11 kbproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "a6f137ae100e74ebe3b71eb4a38c40b3"
SRC_URI[archive.sha256sum] = "8db593c3fc5ffc4e9cd854ba50af1eac9b90d66521ba17802b8f1e0d2d7f05bd"

EXTRA_OECONF += "--disable-install-makestrs --disable-xkb"

do_compile() {
        (
                unset CC LD CXX CCLD
                oe_runmake -C util 'XT_CFLAGS=' 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE -I${STAGING_INCDIR_NATIVE} ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makestrs
        ) || exit 1
        oe_runmake
}

BBCLASSEXTEND = "native"

XORG_PN = "libXt"
