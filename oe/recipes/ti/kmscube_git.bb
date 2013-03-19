DEPENDS = "libdrm libgbm virtual/egl"

inherit autotools

PV = "1.0.0"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "d04bbd4120a94560a3f74f7eea64f4fcafcebb15"
SRC_URI = "git://github.com/robclark/kmscube.git;protocol=git \
"

S = "${WORKDIR}/git"
