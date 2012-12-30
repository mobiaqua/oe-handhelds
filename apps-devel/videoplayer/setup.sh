#!/bin/sh

echo
echo "Prepare videoplayer files..."
echo
cd ${OE_BASE}/build-${DISTRO} && source env.source
cp _compile.sh _debug.sh _env.sh src/
chmod +x src/_compile.sh src/_debug.sh
echo
echo "--- Setup done ---"
echo
