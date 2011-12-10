#!/bin/sh

. _env.sh

ssh root@panda "killall gdbserver 2>/dev/null; gdbserver :1234 ./mplayer" &

sleep 2
arm-linux-gnueabi-ddd --gdb --debugger arm-linux-gnueabi-gdb ./mplayer

ssh root@panda "killall gdbserver 2>/dev/null; killall mplayer"
