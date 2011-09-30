DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
HOMEPAGE = "http://wiki.openmoko.org/wiki/Opkg"
PRIORITY = "optional"
LICENSE = "GPLv2"
RDEPENDS_${PN} = "python"
RDEPENDS_${PN}_virtclass-native = "findutils-native"
SRCREV = "4747"
PV = "0.1.8+svnr${SRCPV}"
PR = "r7"

#MobiAqua: added remove_f_from_ar.patch, fix-for-cutoff-filenames.patch
SRC_URI = "svn://svn.openmoko.org/trunk/src/host/;module=opkg-utils;proto=http \
           file://index-ignore-filenotfound.patch \
           file://arfile_header_split.patch \
           file://mtime-int.patch \
           file://remove_f_from_ar.patch \
           file://fix-for-cutoff-filenames.patch"

S = "${WORKDIR}/opkg-utils"

# Avoid circular dependencies from package_ipk.bbclass
#MobiAqua: added coreutils-native for md5sum
PACKAGES_virtclass-native = "coreutils-native"

do_install() {
        oe_runmake PREFIX=${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
TARGET_CC_ARCH += "${LDFLAGS}"
