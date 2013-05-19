DEPENDS = "libdrm libgbm virtual/egl"

inherit autotools

PV = "0.0.1"
PR = "r0"
PR_append = "+gitr-${SRCREV}"

SRCREV = "5a03757c0aa52eff44fb0d8ac0529f8cc18b1da0"
SRC_URI = "git://gitorious.org/ubuntu-omap/kmscube.git;protocol=git \
"

S = "${WORKDIR}/git"
