#!/usr/bin/env sh
# ------------------------------------------------------------------
#  Compile all demos and run one of them (Mac / Linux).
#  Usage (from inside the jdbc folder):   ./run.sh J03_InsertDemo
#  Needs the Connector/J .jar inside ../lib and DB_PASSWORD exported.
# ------------------------------------------------------------------
if [ -z "$1" ]; then
  echo "Usage: ./run.sh J03_InsertDemo"
  exit 1
fi
javac -cp "../lib/*" -d out *.java || exit 1
java -cp "out:../lib/*" "$1"
