SUMMARY = "Userspace interface to the kernel DRM services"
DESCRIPTION = "The runtime library for accessing the kernel DRM services.  DRM \
stands for \"Direct Rendering Manager\", which is the kernel portion of the \
\"Direct Rendering Infrastructure\" (DRI).  DRI is required for many hardware \
accelerated OpenGL drivers."
HOMEPAGE = "http://dri.freedesktop.org"
SECTION = "x11/base"
LICENSE = "MIT"
PROVIDES = "drm"
DEPENDS = "libpthread-stubs udev"

DEFAULT_PREFERENCE_pandaboard = "10"

SRCREV = "54e4b3ef52ddc541334d12080419700dee95e6c4"
PV = "2.4.39+git${SRCREV}"

FILESPATHPKG =. "libdrm-2.4.39:"

SRC_URI = "git://gitorious.org/ubuntu-omap/libdrm.git;protocol=git \
           file://01_default_perms.diff \
           file://02_build_libkms_against_in_tree_drm.diff \
           file://03_build_against_librt.diff \
           file://04_libdrm-2.4.37-nouveau-1.diff \
           file://0101_omap-release-lock-also-on-error-paths.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-tests ${PN}-drivers ${PN}-kms ${@base_contains('MACHINE_FEATURES', 'x86', '${PN}-intel', '',d)}"
FILES_${PN}-tests = "${bindir}/dr* ${bindir}/mode*"
FILES_${PN}-drivers = "${libdir}/libdrm_*.so.*"
FILES_${PN}-intel = "${libdir}/libdrm_intel.so.*"
FILES_${PN}-kms = "${libdir}/libkms*.so.*"

LEAD_SONAME = "libdrm.so"

EXTRA_OECONF += "--disable-cairo-tests \
                 --enable-omap-experimental-api \
                 --enable-udev \
                 --enable-libkms \
                "
EXTRA_OECONF_append = " ${@base_contains('MACHINE_FEATURES', 'x86', '', '--disable-intel --disable-radeon',d)}"
