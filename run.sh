#!/bin/bash

if [ -d "target" ]; then
  java -jar "$(pwd)"/target/LamportCLocks-1.0-SNAPSHOT.jar "$@"
else
  echo "JAR not found, please run 'mvn package' first"
fi