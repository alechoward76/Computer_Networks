#!/bin/bash
# NOTE: Update these variables to target different files with this script. #
MAIN="Talk"
TEST="TalkTest"
# ======================================================================== #
CP_DELIM=":" # must be changed to ; for Git Bash on Windows
if [[ "$OSTYPE" == "cygwin" || "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    CP_DELIM=";"
fi
# ======================================================================== #
if [[ $1 == "" || $1 == "default" ]]
then
    javac -d target -cp "src" "src/main/java/${MAIN}.java"
elif [[ $1 == "run" ]]
then
    javac -d target -cp "src" "src/main/java/${MAIN}.java" && \
    java -cp "target" "main.java.${MAIN}" ${@:2}
elif [[ $1 == "test" ]]
then
    javac -d target -cp "lib/*${CP_DELIM}src" "src/test/java/${TEST}.java" && \
    java -cp "lib/*${CP_DELIM}target" "test.java.${TEST}" ${@:2}
elif [[ $1 == "fmt" ]]
then
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
elif [[ $1 == "sync" ]]
then
    git fetch origin main
    git pull origin main
    git add -A
    git commit -m "Syncs changes"
    git push origin main
elif [[ $1 == "submit" ]]
then
    git fetch origin main
    git pull origin main
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
    git add -A
    javac -d target -cp "lib/*${CP_DELIM}src" "src/test/java/${TEST}.java" && \
    java -cp "lib/*${CP_DELIM}target" "test.java.${TEST}" all && \
    git commit -m "Submits assignment" && \
    git push origin main
elif [[ $1 == "clean" ]]
then
    rm -r target/*
else
    echo "build: *** No rule to make target '$1'.  Stop."
fi
