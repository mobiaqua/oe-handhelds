require xorg-lib-common.inc
DESCRIPTION = "X11 Distributed Multihead extension library"
DEPENDS += "libxext dmxproto"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "782ced3a9e754dfeb53a8a006a75eb1a"
SRC_URI[archive.sha256sum] = "a7870b648a8768d65432af76dd11581ff69f3955118540d5967eb1eef43838ba"
