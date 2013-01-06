require xorg-lib-common.inc
DESCRIPTION = "X11 Inter-Client Exchange library"
DEPENDS += "xproto xtrans"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "471b5ca9f5562ac0d6eac7a0bf650738"
SRC_URI[archive.sha256sum] = "24a991284d02ff0c789bc8d11ad2e4dffe144cb70f24e28f9ce3e8b1ee08b71e"

BBCLASSEXTEND = "native"

XORG_PN = "libICE"
