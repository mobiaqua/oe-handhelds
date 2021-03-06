SECTION = "libs"
DEPENDS = "linux-libc-headers virtual/${TARGET_PREFIX}gcc-initial"
PROVIDES = "virtual/${TARGET_PREFIX}libc-initial"

PACKAGES = ""
PACKAGES_DYNAMIC = ""

do_configure () {
	sed -ie 's,{ (exit 1); exit 1; }; },{ (exit 0); }; },g' ${S}/configure
	chmod +x ${S}/configure
        (cd ${S} && gnu-configize) || die "failure in running gnu-configize"
        find ${S} -name "configure" | xargs touch
	${S}/configure --host=${TARGET_SYS} --build=${BUILD_SYS} \
		--prefix=/usr \
		--without-cvs --disable-sanity-checks \
		--with-headers=${STAGING_DIR_TARGET}${includedir} \
		--enable-hacker-mode --enable-addons
}

do_compile () {
	:
}

do_install () {
	oe_runmake cross-compiling=yes install_root=${D} \
	includedir='${includedir}' prefix='${prefix}' \
	install-bootstrap-headers=yes install-headers

	oe_runmake csu/subdir_lib
	mkdir -p ${D}${libdir}/
	install -m 644 csu/crt[1in].o ${D}${libdir}
	${CC} -nostdlib -nostartfiles -shared -x c /dev/null -o ${D}${libdir}/libc.so

	# Two headers -- stubs.h and features.h -- aren't installed by install-headers,
	# so do them by hand.  We can tolerate an empty stubs.h for the moment.
	# See e.g. http://gcc.gnu.org/ml/gcc/2002-01/msg00900.html
	mkdir -p ${D}${includedir}/gnu/
	touch ${D}${includedir}/gnu/stubs.h
	cp ${S}/include/features.h ${D}${includedir}/features.h

	if [ -e ${B}/bits/stdio_lim.h ]; then
		cp ${B}/bits/stdio_lim.h  ${D}${includedir}/bits/
	fi
}
