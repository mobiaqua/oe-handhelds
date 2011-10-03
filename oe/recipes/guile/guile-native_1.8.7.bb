require guile-native.inc

PR = "r1"
#MobiAqua: added patch-libguile-fports.c.diff and patch-srfi-60.c.dif
SRC_URI = "http://ftp.gnu.org/pub/gnu/guile/guile-${PV}.tar.gz \
           file://configure-fix.patch \
           file://cpp-linemarkers.patch \
           file://reloc.patch \
           file://patch-libguile-fports.c.diff;striplevel=0 \
           file://patch-srfi-60.c.diff;striplevel=0 \
          "

#MobiAqua: disable 64 calls and ac_cv_func_rl_get_keymap
EXTRA_OECONF = "--without-64-calls ac_cv_func_rl_get_keymap=no"

SRC_URI[md5sum] = "991b5b3efcbbc3f7507d05bc42f80a5e"
SRC_URI[sha256sum] = "bfee6339d91955a637e7f541d96f5b1d53271b42bb4a37b8867d186a6c66f0b3"
