#!/bin/sh

. _env.sh

make -j 4 && {
	scp mplayer root@panda:
}
