#!/usr/bin/env bash


mvn package -pl /api/
mvn package -pl /tokenizer/
mvn package -pl /consumer/
