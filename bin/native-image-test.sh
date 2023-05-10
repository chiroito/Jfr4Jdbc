#!/bin/bash

export CURRENT_DIR=`pwd`
export SCRIPT_DIR=$(cd $(dirname $0); pwd)
export WORK_DIR=$(cd ${SCRIPT_DIR}; cd ../ ; pwd)

cd ${WORK_DIR}

# install jfr4jdbc-driver for native-image test
mvn -B --no-transfer-progress -DskipTests -Dgpg.skip clean install -f jfr4jdbc-driver/pom.xml

# Run native-imaging test
mvn --no-transfer-progress -Pnative -DskipTests -f artifact-test/pom.xml package

cd ${CURRENT_DIR}