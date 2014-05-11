require binutils.inc
PR = "${INC_PR}.3"
LICENSE = "GPLv3"

SRC_URI = "${GNU_MIRROR}/binutils/binutils-${PV}.tar.bz2 \
     file://libtool-update.patch \
     file://libtool-update-new.patch \
     file://libiberty_path_fix.patch \
     file://binutils-x86_64_i386_biarch.patch \
     file://binutils-poison.patch \
     file://binutils-armv5e.patch \
     file://tex_fixes.patch \
     ${BACKPORT} \
     "

BACKPORT = "\
     file://backport/0001-bfd.patch \
     file://backport/0003-Assemble-all-sources-files-in-each-test.patch \
     file://backport/0005-bfd.patch \
     file://backport/0010-bfd-ChangeLog.patch \
     file://backport/0012-Fix-TLS-LD-to-LE-optimization-for-x32.patch \
     file://backport/0013-gas-ChangeLog.patch \
     file://backport/0014-Fix-opcode-for-64-bit-jecxz.patch \
     file://backport/0017-ld-elf-comm-data.exp-Add-XFAIL-for-arm-targets-refer.patch \
     file://backport/0018-bfd.patch \
     file://backport/0024-bfd.patch \
     file://backport/0026-ld-testsuite.patch \
"

SRC_URI[md5sum] = "33adb18c3048d057ac58d07a3f1adb38"
SRC_URI[sha256sum] = "2ab2e5b03e086d12c6295f831adad46b3e1410a3a234933a2e8fac66cb2e7a19"

# 2.21.1a has a mismatched dir name within the tarball
S = "${WORKDIR}/binutils-${PV}"

FILES_${PN}-symlinks += "${bindir}/elfedit"
# When we enable gold this might need to be make conditional
FILES_${PN}-symlinks += "${bindir}/ld.bfd"
