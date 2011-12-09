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
		export DISTRO=mobiaqua-tv
		export MACHINE=pandaboard
		image=rootfs-image-tv
	elif [ "$1" = "car" ]; then
		export DISTRO=mobiaqua-car
		export MACHINE=igep0030
		image=rootfs-image-car
	else
		export DISTRO=mobiaqua
		export MACHINE=${MACHINE:=h2200}
		image=rootfs-image
	fi

	if [ -e ${HOME}/.mobiaqua/oe/${DISTRO}_defaults ]; then
		. ${HOME}/.mobiaqua/oe/${DISTRO}_defaults
		echo "Reading custom settings from file '${HOME}/.mobiaqua/oe/${DISTRO}_defaults'"
	else
		echo "No custom settings file: '${HOME}/.mobiaqua/oe/${DISTRO}_defaults'"
		echo "Using defaults instead."
	fi

	MA_DL_DIR=${MA_DL_DIR:="$HOME/sources"}
	export MA_TARGET_IP=${MA_TARGET_IP:="192.168.1.10"}
	export MA_TARGET_MAC=${MA_TARGET_MAC:=""}
	export MA_DNS_IP=${MA_DNS_IP:="192.168.1.1"}
	export MA_NFS_IP=${MA_NFS_IP:="192.168.1.1"}
	export MA_NFS_PATH=${MA_NFS_PATH:="/nfsroot"}
	export MA_ROOT_PASSWORD=${MA_ROOT_PASSWORD:=""}
	export MA_DROPBEAR_KEY_FILE="$HOME/.mobiaqua/oe/${DISTRO}_dropbear_rsa_host_key"

	export BB_ENV_EXTRAWHITE="MA_TARGET_IP MA_TARGET_MAC MA_DNS_IP MA_NFS_IP MA_NFS_PATH MA_ROOT_PASSWORD MA_DROPBEAR_KEY_FILE"

	echo "--- Settings:"
	echo " -  sources:    ${MA_DL_DIR}"
	echo " -  target ip:  ${MA_TARGET_IP}"
	echo " -  target mac: ${MA_TARGET_MAC}"
	echo " -  dns ip:     ${MA_DNS_IP}"
	echo " -  nfs ip:     ${MA_NFS_IP}"
	echo " -  nfs path:   ${MA_NFS_PATH}"
	if [ "$MA_ROOT_PASSWORD" != "" ]; then
		echo " -  root password is defined"
	else
		echo " -  root password is NOT defined"
	fi
	if [ -f ${MA_DROPBEAR_KEY_FILE} ]; then
		echo " -  target dropbear host key file found"
	else
		echo " -  target dropbear host key file NOT found"
	fi
	mkdir -p ${OE_BASE}/build-${DISTRO}/conf

	BBF="\${OE_BASE}/oe/recipes/*/*.bb"

	DL_DIR=${DL_DIR:="$HOME/sources"}

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
BB_NUMBER_THREADS = \"2\"
" > ${OE_BASE}/build-${DISTRO}/conf/local.conf



		echo "OE_BASE=\"${OE_BASE}\"
export BBPATH=\"\${OE_BASE}/oe/:\${OE_BASE}/bb/:\${OE_BASE}/build-${DISTRO}/\"
if [ ! \`echo \${PATH} | grep \${OE_BASE}/bb/bin\` ]; then
	export PATH=\${OE_BASE}/bb/bin:\${PATH}
fi
unset LD_LIBRARY_PATH
export LD_LIBRARY_PATH=
export PYTHONPATH=${OE_BASE}/bb/lib
export LANG=C
unset TERMINFO
unset GCONF_SCHEMA_INSTALL_SOURCE
" > ${OE_BASE}/build-${DISTRO}/env.source



		echo "source ${OE_BASE}/build-${DISTRO}/env.source
if [ ! \`echo \${PATH} | grep arm/bin\` ]; then
	export PATH=${OE_BASE}/${PATH_TO_TOOLS}/arm/bin:${OE_BASE}/${PATH_TO_TOOLS}/bin:\${PATH}
fi
" > ${OE_BASE}/build-${DISTRO}/crosstools-setup



		echo "--- Created:"
		echo " -  ${OE_BASE}/build-${DISTRO}/conf/local.conf,"
		echo " -  ${OE_BASE}/build-${DISTRO}/env.source,"
		echo " -  ${OE_BASE}/build-${DISTRO}/crosstools-setup ---"
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
