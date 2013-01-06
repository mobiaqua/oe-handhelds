require xorg-app-common.inc
DESCRIPTION = "X Resize and Rotate extension command."
LICENSE = "BSD-X"
DEPENDS += "libxrandr libxrender"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[archive.md5sum] = "9735173a84dca9b05e06fd4686196b07"
SRC_URI[archive.sha256sum] = "1059ff7a9ad0df8e00a765ffa4e08a505304c02663112da370ac7082030b980e"
