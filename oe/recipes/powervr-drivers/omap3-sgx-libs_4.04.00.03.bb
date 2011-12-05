BINLOCATION_omap3  = "${S}/gfx_rel_es3.x"
BINLOCATION_ti816x = "${S}/gfx_rel_es6.x"

ES2LOCATION = "${S}/gfx_rel_es2.x"
ES3LOCATION = "${S}/gfx_rel_es3.x"
ES5LOCATION = "${S}/gfx_rel_es5.x"
ES6LOCATION = "${S}/gfx_rel_es6.x"

DEFAULT_PREFERENCE = "-1"

require libgles-omap3.inc

#MobiAqua: added DEPENDS, and use this version
DEPENDS = "omap3-sgx-modules"

# download required binary distribution from:
# http://software-dl.ti.com/dsps/dsps_public_sw/sdo_sb/targetcontent/gfxsdk/latest/index_FDS.html
# see libgles-omap3.inc for detailed installation instructions

SGXPV = "4_04_00_03"
IMGPV = "1.6.16.4117"
BINFILE := "Graphics_SDK_setuplinux_${SGXPV}.bin"

SRC_URI = "http://software-dl.ti.com/dsps/dsps_public_sw/sdo_sb/targetcontent/gfxsdk/${SGXPV}/exports/Graphics_SDK_setuplinux_${SGXPV}.bin \
                   file://cputype \
                   file://rc.pvr \
                   file://99-bufferclass.rules  \
"

SRC_URI[md5sum] = "4d96cd17de7da89a145fcdd30c930b76"
SRC_URI[sha256sum] = "a542aeef3e3a9b3357323f4fd8341eb30363179d7c7afc91f1b067d84f019766"

S = "${WORKDIR}/Graphics_SDK_${SGXPV}"

