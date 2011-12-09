#!/bin/sh

. _env.sh

ssh root@panda "killall gdbserver; gdbserver :1234 ./mplayer" &

sleep 2
arm-linux-gnueabi-ddd --gdb --debugger arm-linux-gnueabi-gdb ./mplayer
