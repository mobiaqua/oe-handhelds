require xorg-lib-common.inc
DESCRIPTION = "X cursor management library"
LICENSE = "BSD-X"
DEPENDS += "libxrender libxfixes"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "52efa81b7f26c8eda13510a2fba98eea"
SRC_URI[archive.sha256sum] = "f78827de4a1b7ce8cceca24a9ab9d1b1d2f6a61362f505166ffc19b07c0bad8f"

BBCLASSEXTEND = "native"

XORG_PN = "libXcursor"
