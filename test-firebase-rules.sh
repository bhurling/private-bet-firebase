#!/usr/bin/env bash
set -e

for file in firebase-rules/tests/*.json
do
    targaryen --verbose firebase-rules/rules.json ${file}
done