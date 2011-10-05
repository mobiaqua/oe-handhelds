#!/bin/sh

error() {
	echo
	echo "* ERROR * " $1
	echo
	[ "x$0" = "x./setup.sh" ] && exit 1
	ERROR=1
}

setup() {
	export OE_BASE=`pwd -P`

	if [ "$1" = "tv" ]; then
		DISTRO=mobiaqua-tv
		MACHINE=pandaboard
		image=rootfs-image-tv
	elif [ "$1" = "car" ]; then
		DISTRO=mobiaqua-car
		MACHINE=igep0030
		image=rootfs-image-car
	else
		DISTRO=mobiaqua
		MACHINE=${MACHINE:=h2200}
		image=rootfs-image
	fi

	DL_DIR=${DL_DIR:="$HOME/sources"}

	mkdir -p  ${OE_BASE}/build-${DISTRO}/conf

	BBF="\${OE_BASE}/oe/recipes/*/*.bb"

	if [ ! -f ${OE_BASE}/build-${DISTRO}/conf/local.conf ] || [ ! -f ${OE_BASE}/build-${DISTRO}/env.source ] || [ "$1" = "--force" ]; then
		PATH_TO_TOOLS="build-${DISTRO}/tmp/sysroots/`uname -m`-`uname -s | awk '{print tolower($0)}'`/usr"
		echo "DL_DIR = \"${DL_DIR}\"
OE_BASE = \"${OE_BASE}\"
BBFILES = \"${BBF}\"
MACHINE = \"${MACHINE}\"
TARGET_OS = \"linux-gnueabi\"
DISTRO = \"${DISTRO}\"
INHERIT = \"rm_work\"
IMAGE_KEEPROOTFS = \"1\"
CACHE = \"${OE_BASE}/build-${DISTRO}/cache/oe-cache.\${USER}\"
ASSUME_PROVIDED += \" git-native desktop-file-utils-native linux-libc-headers-native glib-2.0-native intltool-native \"
PARALLEL_MAKE = \"-j 2\"
#BB_NUMBER_THREADS = \"2\"" > ${OE_BASE}/build-${DISTRO}/conf/local.conf

	    echo "OE_BASE=\"${OE_BASE}\"
export BBPATH=\"\${OE_BASE}/oe/:\${OE_BASE}/bb/:\${OE_BASE}/build-${DISTRO}/\"
if [ ! \`echo \${PATH} | grep \${OE_BASE}/bb/bin\` ]; then
	export PATH=\${OE_BASE}/bb/bin:\${PATH}
fi
export LD_LIBRARY_PATH=
export PYTHONPATH=${OE_BASE}/bb/lib
export LANG=C" > ${OE_BASE}/build-${DISTRO}/env.source

	echo "
source ${OE_BASE}/build-${DISTRO}/env.source
if [ ! \`echo \${PATH} | grep arm/bin\` ]; then
	export PATH=${OE_BASE}/${PATH_TO_TOOLS}/arm/bin:${OE_BASE}/${PATH_TO_TOOLS}/bin:\${PATH}
fi
" > ${OE_BASE}/build-${DISTRO}/crosstools-setup

	echo "--- Created ${OE_BASE}/build-${DISTRO}/conf/local.conf, ${OE_BASE}/build-${DISTRO}/env.source and ${OE_BASE}/build-${DISTRO}/crosstools-setup ---"

	fi

	echo
	echo "--- MobiAqua OE configuration finished ---"
	echo
	echo "--- Usage example: bitbake $image ---"
	echo
}

bitbake() {
	cd ${OE_BASE}/build-${DISTRO} && source env.source && ${OE_BASE}/bb/bin/bitbake $@
}

ERROR=

[ "x$0" = "x./setup.sh" ] && error "Script must run via sourcing like '. setup.sh'"

[ "$ERROR" != "1" ] && [ $EUID -eq 0 ] && error "Script running with superuser privileges ! Aborting."

[ "$ERROR" != 1 ] && [ -z "$BASH_VERSION" ] && error "Script NOT running in 'bash' shell"

[ "$ERROR" != 1 ] && setup $1
