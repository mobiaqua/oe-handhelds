require xorg-app-common.inc
DESCRIPTION = "X display information utility"
LICENSE = "MIT"
DEPENDS += "libxtst libxext libxxf86vm libxxf86dga libxxf86misc libxi libxrender libxinerama libdmx libxau"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI += "file://disable-xkb.patch"
SRC_URI[archive.md5sum] = "1ef08f4c8d0e669c2edd49e4a1bf650d"
SRC_URI[archive.sha256sum] = "23ee4944a32b5701b4379cb420729eb7a4dde54de2b5b006d4747855efd6d73f"

EXTRA_OECONF = "--disable-xkb"
