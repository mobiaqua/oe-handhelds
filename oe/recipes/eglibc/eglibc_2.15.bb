require eglibc.inc

DEFAULT_PREFERENCE = "10"

DEPENDS += "gperf-native kconfig-frontends-native"

SRCREV = "16526"

EGLIBC_BRANCH="eglibc-2_15"

SRC_URI = "svn://www.eglibc.org/svn/branches;module=${EGLIBC_BRANCH};proto=http \
           file://check-unknown-symbols.diff \
           file://fix-LC_COLLATE-rules.diff \
           file://preprocessor-collate-uli-sucks.diff \
           file://preprocessor-collate.diff \
           file://locale-print-LANGUAGE.diff \
           file://LC_IDENTIFICATION-optional-fields.diff \
           file://LC_COLLATE-keywords-ordering.diff \
           file://local-atomic.diff \
           file://local-eabi-wchar.diff \
           file://local-ioperm.diff \
           file://local-lowlevellock.diff \
           file://local-sigaction.diff \
           file://unsubmitted-ldconfig-cache-abi.diff \
           file://unsubmitted-ldso-abi-check.diff \
           file://cvs-syscall-mcount.diff \
           file://cvs-makecontext.diff \
           file://unsubmitted-soname-hack.diff \
           file://fedora-nss_dns-gethostbyname4-disable.diff \
           file://local-alias-UTF-8.diff \
           file://local-ldd.diff \
           file://local-nis-shadow.diff \
           file://local-asserth-decls.diff \
           file://local-bindresvport_blacklist.diff \
           file://local-allocalim-header.diff \
           file://local-fhs-linux-paths.diff \
           file://local-fhs-nscd.diff \
           file://local-ipv6-lookup.diff;striplevel=0 \
           file://local-ld-multiarch.diff \
           file://local-ldso-disable-hwcap.diff \
           file://local-ldconfig.diff \
           file://local-ldconfig-fsync.diff \
           file://local-linuxthreads-defines.diff \
           file://local-linuxthreads-fd.diff \
           file://local-linuxthreads-gscope.diff \
           file://local-linuxthreads-lowlevellock.diff \
           file://local-linuxthreads-fatalprepare.diff \
           file://local-linuxthreads-ptw.diff \
           file://local-linuxthreads-semaphore_h.diff \
           file://local-linuxthreads-signals.diff \
           file://local-linuxthreads-stacksize.diff \
           file://local-linuxthreads-tst-sighandler.diff \
           file://local-linuxthreads-unwind.diff \
           file://local-linuxthreads-weak.diff \
           file://local-localedef-fix-trampoline.diff \
           file://local-mktemp.diff \
           file://local-no-pagesize.diff \
           file://local-nss-upgrade.diff \
           file://local-o_cloexec.diff \
           file://local-rtld.diff \
           file://local-stubs_h.diff \
           file://local-stdio-lock.diff \
           file://local-tcsetaddr.diff \
           file://local-disable-test-tgmath2.diff \
           file://local-tst-mktime2.diff \
           file://submitted-nis-netgrp.diff \
           file://submitted-clock-settime.diff \
           file://submitted-longdouble.diff \
           file://local-disable-nscd-host-caching.diff \
           file://local-missing-linux_types.h.diff \
           file://local-nss-overflow.diff \
           file://submitted-popen.diff \
           file://local-linuxthreads-thread_self.diff \
           file://submitted-getaddrinfo-lo.diff \
           file://local-getaddrinfo-interface.diff \
           file://submitted-autotools.diff \
           file://submitted-accept4-hidden.diff \
           file://submitted-missing-etc-hosts.diff \
           file://submitted-bits-fcntl_h-at.diff \
           file://local-no-SOCK_NONBLOCK.diff \
           file://submitted-nptl-invalid-td.patch \
           file://cvs-gai-rfc3484.diff \
           file://local-dlfptr.diff \
           file://submitted-string2-strcmp.diff \
           file://submitted-ldsodefs_rtld_debug.diff \
           file://local-linuxthreads-deps.diff \
           file://local-ldconfig-multiarch.diff \
           file://submitted-fwrite-wur.diff \
           file://local-tst-writev.diff \
           file://submitted-resolv-assert.diff \
           file://submitted-resolv-init.diff \
           file://submitted-sys-uio-vector.diff \
           file://local-linuxthreads-XPG7.diff \
           file://cvs-fmtmsg-lock.diff \
           file://cvs-sort-relocations.diff \
#           file://cvs-revert-fseek-on-fclose.diff \
           file://submitted-resolv-first-query-failure.diff \
           file://cvs-htons-wconversion.diff \
           file://cvs-sscanf-alloca.diff \
           file://cvs-fpe-underflow.diff \
           file://cvs-wordsize-64-fixes.diff \
           file://local-linaro-cortex-strings.diff \
           file://local-rpc-export.diff \
           file://local-rpc-no-strict-aliasing.diff \
           file://local-leak-revert-crash.diff \
           file://local-nscd-NO_MAPPING.diff \
           file://local-altlocaledir.diff \
           file://no-sprintf-pre-truncate.diff \
           file://disable-ld_audit.diff \
           file://delete-header-pot.diff \
           file://lddebug-scopes.diff \
           file://local-CVE-2011-4609.patch \
           file://cvs-CVE-2012-0864.patch \
           file://revert-c5a0802a.diff \
           file://CVE-2012-3480.patch \
           file://CVE-2012-3406.patch \
#           file://eglibc-svn-arm-lowlevellock-include-tls.patch \
#           file://IO-acquire-lock-fix.patch \
#           file://mips-rld-map-check.patch \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
#           file://glibc.fix_sqrt2.patch \
##           file://multilib_readlib.patch \
           file://use-sysroot-cxx-headers.patch \
#           file://GLRO_dl_debug_mask.patch \
#           file://initgroups_keys.patch \
#           file://eglibc_fix_findidx_parameters.patch \
#           file://fileops-without-wchar-io.patch \
#           file://add_resource_h_to_wait_h.patch \
#           file://0001-Avoid-use-of-libgcc_s-and-libgcc_eh-when-building-gl.patch \
           file://0001-Add-ARM-specific-static-stubs.c.patch \
#           file://0001-eglibc-menuconfig-support.patch \
#           file://0002-eglibc-menuconfig-hex-string-options.patch \
#           file://0003-eglibc-menuconfig-build-instructions.patch \
#           file://0001-Add-name_to_handle_at-open_by_handle-etc.-to-PowerPC.patch \
           file://0001-R_ARM_TLS_DTPOFF32.patch \
#           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0001-glibc-fsf-v1-eaf6f205.patch;name=patch1 \
#           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0002-Synchronize-with-linux-elf.h.patch;name=patch2 \
#           http://people.linaro.org/~toolchain/openembedded/patches/eglibc/aarch64-0003-Adding-AArch64-support-to-elf-elf.h.patch;name=patch3 \
           file://tzselect-sh.patch \
           file://tzselect-awk.patch \
#           file://0001-eglibc-run-libm-err-tab.pl-with-specific-dirs-in-S.patch \
           file://fix-compile-fs-noncase.patch \
           file://fix-xlocale.patch \
           file://fix-rpc.patch \
           file://fix-timezone.patch \
           file://disable-iconvdata.patch \
           file://IO-acquire-lock-fix.patch \
          "

S = "${WORKDIR}/eglibc-2_15/libc"
B = "${WORKDIR}/build-${TARGET_SYS}"

do_munge() {
	# Integrate ports into tree
	mv ${WORKDIR}/eglibc-2_15/ports ${S}
	mv ${WORKDIR}/eglibc-2_15/linuxthreads/linuxthreads ${S}
}

addtask munge before do_patch after do_unpack

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
