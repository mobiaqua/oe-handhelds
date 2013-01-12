require binutils.inc
PR = "${INC_PR}.3"
LICENSE = "GPLv3"

SRC_URI = "${GNU_MIRROR}/binutils/binutils-${PV}.tar.bz2 \
     file://110-arm-eabi-conf.patch \
     file://libtool-update.patch \
     file://libtool-update-new.patch \
     file://binutils-2.19.1-ld-sysroot.patch \
     file://libiberty_path_fix.patch \
     "
SRC_URI[md5sum] = "bde820eac53fa3a8d8696667418557ad"
SRC_URI[sha256sum] = "cdecfa69f02aa7b05fbcdf678e33137151f361313b2f3e48aba925f64eabf654"

# 2.21.1a has a mismatched dir name within the tarball
S = "${WORKDIR}/binutils-2.21.1"

FILES_${PN}-symlinks += "${bindir}/elfedit"
# When we enable gold this might need to be make conditional
FILES_${PN}-symlinks += "${bindir}/ld.bfd"
