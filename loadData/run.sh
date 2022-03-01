#!/bin/bash
propertyFile=$1
vertexNum=$2
edgeRatio=$3
dataPath=$4
sampleNum=$5
mvn exec:java -Dexec.mainClass="com.benchmark.load.application" -Dexec.args="$propertyFile $vertexNum $edgeRatio $dataPath $sampleNum"

