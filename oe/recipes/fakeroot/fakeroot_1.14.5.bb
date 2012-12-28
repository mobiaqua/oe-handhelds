require fakeroot.inc
PE = "1"
PR = "${INC_PR}.0"

#MobiAqua: keep using this version

SRC_URI =+ "\
  https://launchpad.net/ubuntu/+archive/primary/+files/${PN}_${PV}.orig.tar.bz2 \
  file://quiet-getopt-check.patch \
"

SRC_URI[md5sum] = "659a1f3a36554abfc2a3eaad2fdc0604"
SRC_URI[sha256] = "b035c834944bf9482027f48c388de8492e96609825265ac03f05408d0b3aae68"
