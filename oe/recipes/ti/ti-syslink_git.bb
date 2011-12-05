#MobiAqua: update to new git revision
DEPENDS = "ti-tilermemmgr"

LICENSE = "BSD"

PV = "2.0"
PR = "${MACHINE_KERNEL_PR}"
PR_append = "+m2+gitr-${SRCREV}"

SRCREV = "b1b8045b89402a0fe4f4ca133d400ee12a40d55a"
SRC_URI = "git://git.omapzoom.org/platform/hardware/ti/syslink.git;protocol=git;branch=syslink-2.0"

inherit autotools

export ARCH = "${TARGET_ARCH}"
export CROSS_COMPILE = "${TARGET_PREFIX}"
export TILER_INC_PATH= "${STAGING_INCDIR}/timemmgr"
#export KRNLSRC = "${STAGING_KERNELDIR}"

S = "${WORKDIR}/git/syslink"

