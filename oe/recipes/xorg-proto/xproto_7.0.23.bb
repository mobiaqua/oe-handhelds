require xorg-proto-common.inc
PE = "1"
PR = "${INC_PR}.0"

SRC_URI += "file://xproto_fix_for_x32.patch"

EXTRA_OECONF_append = " --enable-specs=no"

SRC_URI[archive.md5sum] = "d4d241a4849167e4e694fe73371c328c"
SRC_URI[archive.sha256sum] = "ade04a0949ebe4e3ef34bb2183b1ae8e08f6f9c7571729c9db38212742ac939e"
