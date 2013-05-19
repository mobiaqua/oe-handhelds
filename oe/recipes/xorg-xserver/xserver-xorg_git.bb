# Assign it here, since the hal->udev transition happens post 1.7 in angstrom
DISTRO_XORG_CONFIG_MANAGER_angstrom = "udev"

require xorg-xserver-common.inc

DESCRIPTION = "the X.Org X server"
DEPENDS += "pixman libpciaccess openssl dri2proto glproto xorg-minimal-fonts font-util-native"
PE = "2"
PR = "${INC_PR}.0"
PV = "1.11.4+git${SRCPV}"

DEFAULT_PREFERENCE = "1"
SRCREV = "e58f26253de4f09b2688500c1ac8d20a520f9603"

SRC_URI = "git://gitorious.org/ubuntu-omap/xserver.git;protocol=git \
           file://0001-add-dri2video.patch \
           file://001_fedora_extramodes.patch \
           file://02_Add-libnettle-as-option-for-sha1.diff \
           file://07-xfree86-fix-build-with-xv-disabled.diff \
           file://15-nouveau.diff \
           file://100_rethrow_signals.patch \
           file://105_nvidia_fglrx_autodetect.patch \
           file://111_armel-drv-fallbacks.patch \
           file://122_xext_fix_card32_overflow_in_xauth.patch \
           file://157_check_null_modes.patch \
           file://162_null_crtc_in_rotation.patch \
           file://165_man_xorg_conf_no_device_ident.patch \
           file://166_nullptr_xinerama_keyrepeat.patch \
           file://167_nullptr_xisbread.patch \
           file://168_glibc_trace_to_stderr.patch \
           file://172_cwgetbackingpicture_nullptr_check.patch \
           file://188_default_primary_to_first_busid.patch \
           file://190_cache-xkbcomp_output_for_fast_start_up.patch \
           file://191-Xorg-add-an-extra-module-path.patch \
           file://198_nohwaccess.patch \
           file://200_randr-null.patch \
           file://208_switch_on_release.diff \
           file://209_add_legacy_bgnone_option.patch \
           file://224_return_BadWindow_not_BadMatch.diff \
           file://225_non-root_config_paths.patch \
           file://226_fall_back_to_autoconfiguration.patch \
           file://227_null_ptr_midispcur.patch \
           file://228_log-format-fix.patch \
           file://229_randr_first_check_pScrPriv_before_using_the_pointer.patch \
           file://230_randr_catch_two_more_potential_unset_rrScrPriv_uses.patch \
           file://233-xf86events-valgrind.patch \
           file://235-composite-tracking.diff \
           file://238-xrandr-fix-panning.patch \
#           file://500_pointer_barrier_thresholds.diff \
           file://505_query_pointer_touchscreen.patch \
           file://506_touchscreen_pointer_emulation_checks.patch \
           file://507_touchscreen_fixes.patch \
           file://516-dix-dont-emulate-scroll-events-for-non-existing-axes.patch \
           file://237-dix-set-the-device-transformation-matrix.patch \
           file://1001-xfree86-modes-Let-the-driver-handle-the-transform.patch \
           file://1002-xfree86-modes-Make-cursor-position-transform-a-helpe.patch \
           file://crosscompile.patch \
           file://fix_open_max_preprocessor_error.patch \
           file://macro_tweak.patch \
"

S = "${WORKDIR}/git"

do_install_prepend() {
        mkdir -p ${D}/${libdir}/X11/fonts
}

# The NVidia driver requires Xinerama support in the X server. Ion uses it.
PACKAGE_ARCH_ion = "${MACHINE_ARCH}"
XINERAMA = "${@['--disable-xinerama','--enable-xinerama'][bb.data.getVar('MACHINE',d) in ['ion']]}"

EXTRA_OECONF += " ${CONFIG_MANAGER_OPTION} ${XINERAMA} --disable-kdrive --disable-xephyr --disable-xsdl --disable-xfake --disable-xfbdev --disable-dmx"
EXTRA_OECONF += " --enable-dri2 --disable-unit-tests --disable-docs --disable-devel-docs"

export LDFLAGS += " -ldl "
