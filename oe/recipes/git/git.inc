DESCRIPTION = "The git revision control system used by the Linux kernel developers"
SECTION = "console/utils"
LICENSE = "GPLv2"

SRC_URI = "http://pkgs.fedoraproject.org/repo/pkgs/git/git-1.7.3.5.tar.bz2/8a8cd93b8a4dff0a03c0fdc77253af3e/git-1.7.3.5.tar.bz2;name=src"

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
