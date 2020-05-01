require linux-libc-headers.inc

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS += "unifdef-native"
PR = "r0"

COMPATIBLE_TARGET_SYS = "."
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v3.x/linux-${PV}.tar.xz;name=kernel \
          "

SRC_URI[kernel.md5sum] = "38dde260103fb3368088cac9ad8a39e9"
SRC_URI[kernel.sha256sum] = "ad96d797571496c969aa71bf5d08e9d2a8c84458090d29a120f1b2981185a99e"

S = "${WORKDIR}/linux-${PV}"

do_configure() {
	cd ${S}
	oe_runmake allnoconfig ARCH=$ARCH
}

do_compile () {
}

do_install() {
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix} ARCH=$ARCH
}
