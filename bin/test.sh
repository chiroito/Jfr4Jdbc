#!/bin/bash

export CURRENT_DIR=`pwd`
export SCRIPT_DIR=$(cd $(dirname $0); pwd)
export WORK_DIR=$(cd ${SCRIPT_DIR}; cd ../ ; pwd)

cd ${WORK_DIR}

# Run unit test
mvn -B --no-transfer-progress test -f pom.xml

# install jfr4jdbc-driver for artifact test
mvn -B --no-transfer-progress -DskipTests -Dgpg.skip clean install -f jfr4jdbc-driver/pom.xml

# Run artifact test
mvn -B --no-transfer-progress test -f artifact-test/pom.xml

cd ${CURRENT_DIR}