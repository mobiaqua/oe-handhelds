# Copyright Matthias Hentges <devel@hentges.net> (c) 2006
# License: MIT (see COPYING.MIT)

DESCRIPTION = "Preconfigured mplayer preferences"

PV = "0.0.1"
PR = "r6"

SRC_URI = "file://mplayer.conf file://codecs.conf file://menu.conf file://input.conf"

# Yes, really /usr/etc!!!
#MobiAqua: added codecs.conf
do_install() {
	install -d "${D}/usr${sysconfdir}/mplayer"
	install -m 0644 ${WORKDIR}/mplayer.conf "${D}/usr${sysconfdir}/mplayer"
	install -m 0644 ${WORKDIR}/codecs.conf "${D}/usr${sysconfdir}/mplayer"
	install -m 0644 ${WORKDIR}/menu.conf "${D}/usr${sysconfdir}/mplayer"
	install -m 0644 ${WORKDIR}/input.conf "${D}/usr${sysconfdir}/mplayer"
}

FILES_${PN} = "/usr${sysconfdir}/mplayer"
PACKAGE_ARCH = "all"

