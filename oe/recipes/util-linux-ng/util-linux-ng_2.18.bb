require util-linux-ng.inc

PR = "${INC_PR}.0"

SRC_URI += "file://uclibc-compile.patch \
	    file://util-linux-ng-replace-siginterrupt.patch \
	    file://fdiskbsdlabel.h-nios2.patch \
	    file://uclibc-nolargefile.patch \
           "

# fallocate is glibc 2.10, fallocate64 is glibc 2.11
# we need to disable it for older versions
EXTRA_OECONF += "ac_cv_func_fallocate=no"
EXTRA_OECONF_append_virtclass-native += "--disable-fallocate --disable-use-tty-group"

#MobiAqua: temporary mirror as main site is down
#SRC_URI[archive.md5sum] = "2f5f71e6af969d041d73ab778c141a77"
#SRC_URI[archive.sha256sum] = "b8c5c07c763888aa712b4585393346667a00793127c54cef0470cfa456b031cc"
SRC_URI[archive.md5sum] = "aff22adfff1bf39cd3f13371b95a3d3a"
SRC_URI[archive.sha256sum] = "b4b47ff2e92a3acfbcae6d91d53af1e7e4050b014d61ad28c3b703c65a7d6532"
