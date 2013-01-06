require xorg-proto-common.inc

SUMMARY = "DRI2: Direct Rendering Infrastructure 2 headers"

DESCRIPTION = "This package provides the wire protocol for the Direct \
Rendering Ifnrastructure 2.  DIR is required for may hardware \
accelerated OpenGL drivers."

SRCREV = "383e42f03b11d42bd940c5781e87ef33e1b11331"
PV = "2.8+git${SRCPV}"
PR = "r0"

FILESPATHPKG =. "dri2proto-2.8:"

SRC_URI = "git://gitorious.org/ubuntu-omap/dri2proto.git;protocol=git \
           file://0001-Remove-extra-DRI2SwapBuffers.patch \
           file://0002-video-support-for-dri2.patch \
"

S = "${WORKDIR}/git"

