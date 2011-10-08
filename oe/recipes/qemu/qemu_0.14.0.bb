LICENSE = "GPL"
DEPENDS = "zlib ncurses"
PR = "r3"
SRC_URI = "\
    http://download.savannah.gnu.org/releases/qemu/qemu-${PV}.tar.gz \
    file://leftover.patch \
    file://91-oh-sdl-cursor.patch \
    file://fix_baum_c_compilation.patch \
    file://fallback.to.safe.mmap_min_addr.patch \
    file://spice-qxl-locking-fix-for-qemu-kvm.patch \
    file://Detect-and-use-GCC-atomic-builtins-for-locking.patch \
    file://larger_default_ram_size.patch \
    "
SRC_URI[md5sum] = "f9d145d5c09de9f0984ffe9bd1229970"
SRC_URI[sha256sum] = "ba21e84d7853217830e167dae9999cdbff481189c6a0bb600ac7fb7201453108"

BBCLASSEXTEND="native"

#MobiAqua: custom target list, removed --enable-kvm
EXTRA_OECONF = "--target-list=arm-softmmu,i386-softmmu,x86_64-softmmu --disable-werror --disable-vnc-tls"

S = "${WORKDIR}/qemu-${PV}"

EXTRA_OECONF += " --disable-curl --disable-sdl --disable-strip \
                "

EXTRA_OECONF_append_virtclass-native = " --extra-cflags="-I${STAGING_INCDIR_NATIVE}""

inherit autotools

do_configure() {
	${S}/configure --prefix=${prefix} ${EXTRA_OECONF}
}
