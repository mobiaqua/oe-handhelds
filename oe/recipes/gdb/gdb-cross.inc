require gdb-common.inc

DEPENDS = "expat-native ncurses-native readline-native"

EXTRA_OECONF += "--without-python"

#EXTRA_OEMAKE += "LDFLAGS='${BUILD_LDFLAGS}'"

GDBPROPREFIX = ""

inherit cross
inherit gettext
