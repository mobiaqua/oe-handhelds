require eglibc_${PV}.bb
require eglibc-initial.inc

DEFAULT_PREFERENCE = "10"

do_configure_prepend () {
        unset CFLAGS
}
