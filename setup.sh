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

	if [ -e /bin/uname ]; then
		os=`/bin/uname -s`
	elif [ -e /usr/bin/uname ]; then
		os=`/usr/bin/uname -s`
	else
		os=`uname -s`
	fi
        
	if [ "$1" = "tv" ]; then
		export DISTRO=mobiaqua-tv
		export MACHINE=pandaboard
		image=rootfs-devel-tv
	elif [ "$1" = "car" ]; then
		export DISTRO=mobiaqua-car
		export MACHINE=igep0030
		image=rootfs-devel-car
	elif [ "$1" = "pda-sa1110" ]; then
		export DISTRO=mobiaqua-pda-sa1110
		export MACHINE=pda-sa1110
		image=rootfs-pda-sa1110
	elif [ "$1" = "pda-pxa25x" ]; then
		export DISTRO=mobiaqua-pda-pxa25x
		export MACHINE=pda-pxa25x
		image=rootfs-pda-pxa25x
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
	export MA_FSTAB_FILE="$HOME/.mobiaqua/oe/${DISTRO}_fstab"
	export MA_ROOTFS_POSTPROCESS=${MA_ROOTFS_POSTPROCESS:="echo"}
	export BB_ENV_EXTRAWHITE="MA_TARGET_IP MA_TARGET_MAC MA_DNS_IP MA_NFS_IP MA_NFS_PATH MA_ROOT_PASSWORD MA_DROPBEAR_KEY_FILE \
				MA_FSTAB_FILE MA_ROOTFS_POSTPROCESS"

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
	if [ -f ${MA_FSTAB_FILE} ]; then
		echo " -  target fstab file found"
	else
		echo " -  target fstab file NOT found"
	fi
	if [ "${MA_ROOTFS_POSTPROCESS}" != "" ]; then
		echo " -  rootfs postprocess commands are defined"
	else
		echo " -  rootfs postprocess commands are NOT defined"
	fi
	mkdir -p ${OE_BASE}/build-${DISTRO}/conf

	BBF="\${OE_BASE}/oe/recipes/*/*.bb"

	DL_DIR=${DL_DIR:="$HOME/sources"}

	rm -f ${OE_BASE}/oe/bin/deftar
	rm -f ${OE_BASE}/oe/bin/tar
	rm -f ${OE_BASE}/oe/bin/sed
	rm -f ${OE_BASE}/oe/bin/readlink
	case $os in
	Darwin)
		if [ -e /opt/local/bin/gnutar ]; then
			ln -s /opt/local/bin/gnutar ${OE_BASE}/oe/bin/tar
		elif [ -e /sw/bin/gtar ]; then
			ln -s /sw/bin/gtar ${OE_BASE}/oe/bin/tar
		fi
		if [ -e /usr/bin/tar ]; then
			ln -s /usr/bin/tar ${OE_BASE}/oe/bin/deftar
		fi
		if [ -e /opt/local/bin/gsed ]; then
			ln -s /opt/local/bin/gsed ${OE_BASE}/oe/bin/sed
		elif [ -e /sw/bin/gsed ]; then
			ln -s /sw/bin/gsed ${OE_BASE}/oe/bin/sed
		fi
		if [ -e /opt/local/bin/greadlink ]; then
			ln -s /opt/local/bin/greadlink ${OE_BASE}/oe/bin/readlink
		elif [ -e /sw/sbin/greadlink ]; then
			ln -s /sw/sbin/greadlink ${OE_BASE}/oe/bin/readlink
		fi
		;;
	Linux)
		if [ -e /bin/tar ]; then
			ln -s /bin/tar ${OE_BASE}/oe/bin/deftar
		fi
	esac

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

[ "$ERROR" != "1" ] && [ $EUID -eq 0 ] && error "Script running with superuser privileges! Aborting."

[ "$ERROR" != "1" ] && [ -z "$BASH_VERSION" ] && error "Script NOT running in 'bash' shell"

[ "x$1" != "xtv" ] && [ "x$1" != "xcar" ] && [ "x$1" != "xpda-sa1110" ] && [ "x$1" != "xpda-pxa25x" ] && error "Not supported target!"

[ "$ERROR" != "1" ] && setup $1
