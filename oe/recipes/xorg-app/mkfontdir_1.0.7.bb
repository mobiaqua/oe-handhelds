require xorg-app-common.inc
DESCRIPTION = "a program to create an index of X font files in a directory"
RDEPENDS_${PN} += "mkfontscale"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "18c429148c96c2079edda922a2b67632"
SRC_URI[archive.sha256sum] = "56d52a482df130484e51fd066d1b6eda7c2c02ddbc91fe6e2be1b9c4e7306530"

BBCLASSEXTEND = "native"
