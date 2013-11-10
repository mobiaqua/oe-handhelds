DESCRIPTION = "Package for building OMAP4 DCE Firmware"
LICENSE = "proprietary-binary"
PR = "r0"

EXCLUDE_FROM_WORLD = "1"

# http://downloads.ti.com/dsps/dsps_public_sw/glsdk/6_00_00_07/index_FDS.html
SRC_URI = "file://${DL_DIR}/ti-glsdk_omap5-uevm_6_00_00_07_linux-installer.bin \
           file://fix_dce.patch \
"

S = "${WORKDIR}/ti-glsdk_omap5-uevm_6_00_00_07"

# Logic to unpack installjammer file
require ti-eula-unpack.inc
