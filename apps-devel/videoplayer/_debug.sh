#!/bin/sh

. _env.sh

ssh root@panda "killall gdbserver 2>/dev/null; gdbserver :1234 ./videoplayer" &

sleep 2
arm-linux-gnueabi-ddd --gdb --debugger arm-linux-gnueabi-gdb ./videoplayer

ssh root@panda "killall gdbserver 2>/dev/null; killall videoplayer"
