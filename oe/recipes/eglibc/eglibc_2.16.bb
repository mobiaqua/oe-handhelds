require eglibc.inc

DEPENDS += "gperf-native kconfig-frontends-native"

SRC_URI = "http://downloads.yoctoproject.org/releases/eglibc/eglibc-${PV}-svnr21224.tar.bz2;name=tarball \
           file://eglibc-svn-arm-lowlevellock-include-tls.patch \
           file://IO-acquire-lock-fix.patch \
           file://mips-rld-map-check.patch \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           file://glibc.fix_sqrt2.patch \
#           file://multilib_readlib.patch \
           file://use-sysroot-cxx-headers.patch \
           file://GLRO_dl_debug_mask.patch \
           file://initgroups_keys.patch \
           file://eglibc_fix_findidx_parameters.patch \
           file://fileops-without-wchar-io.patch \
           file://add_resource_h_to_wait_h.patch \
           file://0001-Avoid-use-of-libgcc_s-and-libgcc_eh-when-building-gl.patch \
           file://0001-Add-ARM-specific-static-stubs.c.patch \
           file://0001-eglibc-menuconfig-support.patch \
           file://0002-eglibc-menuconfig-hex-string-options.patch \
           file://0003-eglibc-menuconfig-build-instructions.patch \
           file://0001-Add-name_to_handle_at-open_by_handle-etc.-to-PowerPC.patch \
           file://0001-R_ARM_TLS_DTPOFF32.patch \
           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0001-glibc-fsf-v1-eaf6f205.patch;name=patch1 \
           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0002-Synchronize-with-linux-elf.h.patch;name=patch2 \
           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0003-Adding-AArch64-support-to-elf-elf.h.patch;name=patch3 \
           file://tzselect-sh.patch \
           file://tzselect-awk.patch \
           file://0001-eglibc-run-libm-err-tab.pl-with-specific-dirs-in-S.patch \
           file://fix-compile-fs-noncase.patch \
           file://fix-xlocale.patch \
           file://fix-rpc.patch \
           file://disable-iconvdata.patch \
          "

SRC_URI[tarball.md5sum] = "88894fa6e10e58e85fbd8134b8e486a8"
SRC_URI[tarball.sha256sum] = "460a45f422da6eb1fd909baab6a64b5ae4c8ba18ea05a1491ed1024c8b98eeaa"

SRC_URI[patch1.md5sum] = "5e52bf8fd9ac390b665d86a57ab7dba7"
SRC_URI[patch1.sha256sum] = "b7eea76e72675a6ed3066952a9e08389c99838d74a58b736d527c82c34e754eb"

SRC_URI[patch2.md5sum] = "e1ae1c416c01e2c991c7ca7e169c577b"
SRC_URI[patch2.sha256sum] = "6093bb80a187081090cb14412f466c08fcaf39ccd62b751e3d871a8c5af03b0d"

SRC_URI[patch3.md5sum] = "6d1d84e14f7abfe9ee3237d0ec6fe9ca"
SRC_URI[patch3.sha256sum] = "03e79ace9eade0d57a3684cb0dc6b415ea52e4f152bfb380684b08445f125410"

S = "${WORKDIR}/eglibc-${PV}/libc"
B = "${WORKDIR}/build-${TARGET_SYS}"

PACKAGES_DYNAMIC = "libc6*"
RPROVIDES_${PN}-dev = "libc6-dev virtual-libc-dev"
PROVIDES_${PN}-dbg = "glibc-dbg"

# the -isystem in bitbake.conf screws up eglibc sysroot
BUILD_CPPFLAGS = "-I${STAGING_INCDIR_NATIVE}"
TARGET_CPPFLAGS = "-I${STAGING_DIR_TARGET}${layout_includedir}"

GLIBC_ADDONS ?= "ports,nptl,libidn"

GLIBC_BROKEN_LOCALES = " _ER _ET so_ET yn_ER sid_ET tr_TR mn_MN gez_ET gez_ER bn_BD te_IN es_CR.ISO-8859-1"

#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
                --without-cvs --disable-profile --disable-debug --without-gd \
                --enable-clocale=gnu \
                --enable-add-ons=${GLIBC_ADDONS} \
                --with-headers=${STAGING_INCDIR} \
                --without-selinux \
                --enable-obsolete-rpc \
                --with-kconfig=${STAGING_BINDIR_NATIVE} \
                ${GLIBC_EXTRA_OECONF}"

do_configure () {
# override this function to avoid the autoconf/automake/aclocal/autoheader
# calls for now
# don't pass CPPFLAGS into configure, since it upsets the kernel-headers
# version check and doesn't really help with anything
        if [ -z "`which rpcgen`" ]; then
                echo "rpcgen not found.  Install glibc-devel."
                exit 1
        fi
        (cd ${S} && gnu-configize) || die "failure in running gnu-configize"
        find ${S} -name "configure" | xargs touch
        CPPFLAGS="" oe_runconf
}

rpcsvc = "bootparam_prot.x nlm_prot.x rstat.x \
	  yppasswd.x klm_prot.x rex.x sm_inter.x mount.x \
	  rusers.x spray.x nfs_prot.x rquota.x key_prot.x"

do_compile () {
	# -Wl,-rpath-link <staging>/lib in LDFLAGS can cause breakage if another glibc is in staging
	unset LDFLAGS
	oe_runmake libdir='${libdir}' slibdir='${base_libdir}' \
        localedir='${libdir}/locale'
	(
		cd ${S}/sunrpc/rpcsvc
		for r in ${rpcsvc}; do
			h=`echo $r|sed -e's,\.x$,.h,'`
			rpcgen -h $r -o $h || oewarn "unable to generate header for $r"
		done
	)
}

require eglibc-package.inc
