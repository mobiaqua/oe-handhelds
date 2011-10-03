DESCRIPTION = "The git revision control system used by the Linux kernel developers"
SECTION = "console/utils"
LICENSE = "GPLv2"

#MobiAqua: temporary mirror as main site is down
SRC_URI = "http://kakola.googlecode.com/files/git-1.7.3.5.tar.bz2;name=src"
#SRC_URI = "http://www.kernel.org/pub/software/scm/git/git-${PV}.tar.bz2;name=src"

S = "${WORKDIR}/git-${PV}"

inherit autotools

#MobiAqua: no gui
EXTRA_OEMAKE = "NO_TCLTK=1"

do_install () {
	oe_runmake install DESTDIR="${D}" bindir=${bindir} \
		template_dir=${datadir}/git-core/templates \
		GIT_PYTHON_DIR=${datadir}/git-core/python
}

FILES_${PN} += "${datadir}/git-core"
