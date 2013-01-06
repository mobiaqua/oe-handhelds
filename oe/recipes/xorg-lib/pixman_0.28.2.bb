require pixman.inc

LICENSE = "MIT MIT-style PD"

PR = "${INC_PR}.1"

SRC_URI = "http://xorg.freedesktop.org/archive/individual/lib/${BPN}-${PV}.tar.gz \
            file://0001-ARM-qemu-related-workarounds-in-cpu-features-detecti.patch \
            file://Generic-C-implementation-of-pixman_blt-with-overlapp.patch \
           "

SRC_URI[md5sum] = "f68916a612921c24e5f94f1eae71d121"
SRC_URI[sha256sum] = "2afac9006adbc3fba28830007d7a9521b118d516342478dfe7818ffe4aeb9b55"

NEON = " --disable-arm-neon "
NEON_armv7a = " "

EXTRA_OECONF = "${NEON} --disable-gtk"

DEPENDS += "zlib libpng"
BBCLASSEXTEND = "native nativesdk"
