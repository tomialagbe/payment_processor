#!/usr/bin/env bash

pushd api/
mvn package -pl .

popd

pushd tokenizer/
mvn package -pl .

popd

pushd consumer/
mvn package -pl .

popd

