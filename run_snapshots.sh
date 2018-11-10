#!/bin/bash

set -o pipefail
set -e

./node_modules/.bin/percy exec -- mvn test
