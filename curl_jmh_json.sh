#!/usr/bin/env bash

set -e

curl -v -H "Content-Type: application/json" --compressed -X POST -d "@test-kit/src/main/resources/$1.json" http://localhost:8080/api/jmh/json/