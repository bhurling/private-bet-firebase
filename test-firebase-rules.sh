#!/usr/bin/env bash

for file in firebase-rules/tests/*.json
do
    targaryen --verbose firebase-rules/rules.json ${file}
done