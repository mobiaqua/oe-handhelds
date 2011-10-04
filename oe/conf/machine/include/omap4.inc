SOC_FAMILY = "omap4"
TARGET_ARCH = "arm"

#MobiAqua: omap4 is a9 not a8
require conf/machine/include/tune-cortexa9.inc
PREFERRED_PROVIDER_virtual/kernel = "linux-omap"
# Increase this everytime you change something in the kernel
MACHINE_KERNEL_PR = "r101"

KERNEL_IMAGETYPE = "uImage"

UBOOT_ENTRYPOINT = "0x80008000"
UBOOT_LOADADDRESS = "0x80008000"

EXTRA_IMAGEDEPENDS += "u-boot x-load"

