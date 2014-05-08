#!/bin/bash

NDK_DIRECTORY="/home/saint/Develop/android-ndk-r9"
PROJECT_DIRECTORY="/home/saint/Develop/studiospace/Flakor/jni"

# Run build:
pushd ${PROJECT_DIRECTORY}
${NDK_DIRECTORY}ndk-build

# Clean temporary files:
# rm -rf ${PROJECT_DIRECTORY}obj
# find . -name gdbserver -print0 | xargs -0 rm -rf
# find . -name gdb.setup -print0 | xargs -0 rm -rf

popd
