#!/bin/sh

# On gentoo, to install inotifywait:
#   emerge -av sys-fs/inotify-tools

# First build everything
make clean html
# Automatically build on changes in source directory on Linux
while inotifywait `find . -iname '*.rest'` ; do make html ; done
