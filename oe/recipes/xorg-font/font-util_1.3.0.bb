require xorg-font-common.inc

PACKAGE_ARCH = "${BASE_PACKAGE_ARCH}"

DESCRIPTION = "X font utils."

DEPENDS = "util-macros"
RDEPENDS_${PN} = "mkfontdir mkfontscale encodings"
BBCLASSEXTEND = "native"

PE = "1"
PR = "${INC_PR}.0"

do_configure_prepend() {
        sed -i "s#MAPFILES_PATH=\`pkg-config#MAPFILES_PATH=\`PKG_CONFIG_PATH=\"${STAGING_LIBDIR_NATIVE}/pkg-config\" pkg-config#g" fontutil.m4.in
}

SRC_URI[archive.md5sum] = "ddfc8a89d597651408369d940d03d06b"
SRC_URI[archive.sha256sum] = "dfa9e55625a4e0250f32fabab1fd5c8ffcd2d1ff2720d6fcf0f74bc8a5929195"
