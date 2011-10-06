DESCRIPTION = "A program that you can use to select particular records in a \
file and perform operations upon them."
SECTION = "interpreters"
LICENSE = "GPL"
RDEPENDS_gawk += "gawk-common"
RDEPENDS_pgawk += "gawk-common"
PR = "r4"

SRC_URI = "${GNU_MIRROR}/gawk/gawk-${PV}.tar.gz"

inherit autotools update-alternatives

#MobiAqua: use newer version as compile fine target
do_configure_prepend () {
        grep -E '^AC_DEFUN' m4/*.m4|grep -E '\(\[?(AM|AC)_'|xargs rm -f
}

PACKAGES += "gawk-common pgawk"

FILES_${PN} = "${bindir}/gawk* ${bindir}/igawk"
FILES_gawk-common += "${datadir}/awk/* ${libexecdir}/awk/*"
FILES_pgawk = "${bindir}/pgawk*"
FILES_${PN}-dbg += "${libexecdir}/awk/.debug"

ALTERNATIVE_NAME = "awk"
ALTERNATIVE_PATH = "gawk"
ALTERNATIVE_LINK = "${bindir}/awk"
ALTERNATIVE_PRIORITY = "100"

SRC_URI[md5sum] = "4760325489479cac17fe0114b8f62f30"
SRC_URI[sha256sum] = "463dcb9d0ca398b1d4f5a332f6cd9cec56441265fca616f2ea1b44d459e9f0f8"

BBCLASSEXTEND = "native"

#MobiAqua: added autoconf-native, gettext-native
RDEPENDS_${PN}_virtclass-native = "autoconf-native gettext-native"
