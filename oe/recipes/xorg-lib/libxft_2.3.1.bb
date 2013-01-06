require xorg-lib-common.inc
DESCRIPTION = "FreeType-based font drawing library for X"
DEPENDS += "libxrender freetype fontconfig"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "78d64dece560c9e8699199f3faa521c0"
SRC_URI[archive.sha256sum] = "7fce32b92dcb7b2869bed567af2abc7bbad0d5d6fcf471b8a3e137964a31bbbd"

FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la \
                ${libdir}/*.a ${libdir}/pkgconfig \
                ${datadir}/aclocal ${bindir} ${sbindir}"

python do_package() {
        if bb.data.getVar('DEBIAN_NAMES', d, 1):
            bb.data.setVar('PKG_${PN}', 'libxft2', d)
        bb.build.exec_func('package_do_package', d)
}

XORG_PN = "libXft"
