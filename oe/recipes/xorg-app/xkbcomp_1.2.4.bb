require xorg-app-common.inc
DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."
DEPENDS += " libxkbfile"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "a0fc1ac3fc4fe479ade09674347c5aa0"
SRC_URI[archive.sha256sum] = "91d0c9ab445d21dfe1892dbae5ae5264f39bae68223dd092ffc547c9450b5a2d"

BBCLASSEXTEND = "native"
