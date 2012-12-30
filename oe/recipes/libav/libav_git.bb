DESCRIPTION = "Libav is a complete, cross-platform solution to record, convert and stream audio and video."
HOMEPAGE = "http://libav.org/"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPLv2+"

# Provides ffmpeg compat, see http://libav.org/about.html
PROVIDES = "ffmpeg"

ARM_INSTRUCTION_SET = "arm"

inherit autotools pkgconfig

LEAD_SONAME = "libavcodec.so"

FFMPEG_LIBS = "libavcodec libavformat libavutil"

SYSROOT_PREPROCESS_FUNCS = " \
  ffmpeg_stage_cleanup \
  ffmpeg_create_compat_links"

ffmpeg_create_compat_links() {
        rm -rf ${SYSROOT_DESTDIR}${STAGING_INCDIR}/ffmpeg
        mkdir -m 0755 ${SYSROOT_DESTDIR}${STAGING_INCDIR}/ffmpeg
        cd ${SYSROOT_DESTDIR}${STAGING_INCDIR}/ffmpeg

        for lib in ${FFMPEG_LIBS}; do
                ln -s ../$lib/*.h '.' || true
        done
}

ffmpeg_stage_cleanup() {
        rm -rf ${SYSROOT_DESTDIR}${STAGING_LIBDIR}/vhook \
        ${SYSROOT_DESTDIR}${STAGING_DATADIR}
}

PACKAGES += "${PN}-vhook-dbg ${PN}-vhook"

FILES_${PN} = "${bindir}"
FILES_${PN}-dev = "${includedir}/${PN}"

FILES_${PN}-vhook = "${libdir}/vhook"
FILES_${PN}-vhook-dbg += "${libdir}/vhook/.debug"

PACKAGES += "ffmpeg-x264-presets \
             libavcodec  libavcodec-dev  libavcodec-dbg \
             libavdevice libavdevice-dev libavdevice-dbg \
             libavformat libavformat-dev libavformat-dbg \
             libavutil   libavutil-dev   libavutil-dbg \
             libpostproc libpostproc-dev libpostproc-dbg \
             libswscale  libswscale-dev  libswscale-dbg \
             libavfilter libavfilter-dev libavfilter-dbg \
            "

FILES_ffmpeg-x264-presets = "${datadir}/*.ffpreset"

FILES_${PN}-dev = "${includedir}"
FILES_libavcodec = "${libdir}/libavcodec*.so.*"
FILES_libavcodec-dev = "${libdir}/libavcodec*.so ${libdir}/pkgconfig/libavcodec.pc ${libdir}/libavcodec*.a"
FILES_libavcodec-dbg += "${libdir}/.debug/libavcodec*"

FILES_libavdevice = "${libdir}/libavdevice*.so.*"
FILES_libavdevice-dev = "${libdir}/libavdevice*.so ${libdir}/pkgconfig/libavdevice.pc ${libdir}/libavdevice*.a"
FILES_libavdevice-dbg += "${libdir}/.debug/libavdevice*"

FILES_libavformat = "${libdir}/libavformat*.so.*"
FILES_libavformat-dev = "${libdir}/libavformat*.so ${libdir}/pkgconfig/libavformat.pc ${libdir}/libavformat*.a"
FILES_libavformat-dbg += "${libdir}/.debug/libavformat*"

FILES_libavutil = "${libdir}/libavutil*.so.*"
FILES_libavutil-dev = "${libdir}/libavutil*.so ${libdir}/pkgconfig/libavutil.pc ${libdir}/libavutil*.a"
FILES_libavutil-dbg += "${libdir}/.debug/libavutil*"

FILES_libpostproc = "${libdir}/libpostproc*.so.*"
FILES_libpostproc-dev = "${libdir}/libpostproc*.so  ${libdir}/pkgconfig/libpostproc.pc ${libdir}/libpostproc*.a ${includedir}/postproc"
FILES_libpostproc-dbg += "${libdir}/.debug/libpostproc*"

FILES_libswscale = "${libdir}/libswscale*.so.*"
FILES_libswscale-dev = "${libdir}/libswscale*.so ${libdir}/pkgconfig/libswscale.pc ${libdir}/libswscale*.a"
FILES_libswscale-dbg += "${libdir}/.debug/libswscale*"

FILES_libavfilter = "${libdir}/libavfilter*.so.*"
FILES_libavfilter-dev = "${libdir}/libavfilter*.so ${libdir}/pkgconfig/libavfilter.pc ${libdir}/libavfilter*.a"
FILES_libavfilter-dbg += "${libdir}/.debug/libavfilter*"

DEPENDS_i586 += "yasm-native"
DEPENDS_i686 += "yasm-native"

SRCREV = "5e6ee38bd3cef0dd05f1dd7977c71f3479eb6d01"

PV = "0.9+${PR}+gitr${SRCPV}"
PR = "r1"

SRC_URI = "git://git.libav.org/libav.git;protocol=git"

S = "${WORKDIR}/git"
B = "${S}/build.${HOST_SYS}.${TARGET_SYS}"

FULL_OPTIMIZATION_armv7a = "-fexpensive-optimizations  -fno-tree-vectorize -fomit-frame-pointer -O4 -ffast-math"
BUILD_OPTIMIZATION = "${FULL_OPTIMIZATION}"

EXTRA_FFCONF_armv7a = "--cpu=cortex-a9"
EXTRA_FFCONF ?= ""

EXTRA_OECONF = " \
        --enable-shared \
        --enable-pthreads \
        --enable-gpl \
        \
        --cross-prefix=${TARGET_PREFIX} \
        --prefix=${prefix} \
        \
        --disable-avserver \
        --disable-avplay \
        --disable-avconv \
        --disable-avprobe \
        --disable-swscale \
        --disable-x11grab \
        --disable-encoders \
        --disable-indevs \
        --disable-protocols \
        --enable-protocol='file' \
        --disable-filters \
        --disable-demuxers \
        --disable-hwaccels \
        --disable-bzlib \
        --disable-avfilter \
        --disable-avresample \
        --disable-avdevice \
        --arch=${TARGET_ARCH} \
        --target-os="linux" \
        --enable-cross-compile \
        --extra-cflags="${TARGET_CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}" \
        --extra-ldflags="${TARGET_LDFLAGS}" \
        --sysroot="${STAGING_DIR_TARGET}" \
        --enable-hardcoded-tables \
        ${EXTRA_FFCONF} \
"
do_configure_prepend() {
        # We don't have TARGET_PREFIX-pkgconfig
        sed -i '/pkg_config_default="${cross_prefix}${pkg_config_default}"/d' ${S}/configure
}

do_configure() {
        mkdir -p ${B}
        cd ${B}
        #MobiAqua: on mac os x with llvm compiler use clang instead
        if gcc --version | grep llvm-gcc > /dev/null ; then
                ${S}/configure ${EXTRA_OECONF} --host-cc=clang
        else
                ${S}/configure ${EXTRA_OECONF}
        fi
        sed -i -e s:Os:O4:g ${B}/config.h
}
