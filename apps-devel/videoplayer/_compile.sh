#!/bin/sh

. _env.sh

make && {
	scp videoplayer root@panda:
}
