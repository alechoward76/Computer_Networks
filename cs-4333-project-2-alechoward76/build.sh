#!/bin/bash
# NOTE: Update these variables to target different files with this script. #
MAIN="main.java.Main"
# ======================================================================== #
CP_DELIM=":"
if [[ "$OSTYPE" == "cygwin" || "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    CP_DELIM=";" # changes to ; for Windows
fi
# ======================================================================== #
if [[ $1 == "" || $1 == "default" ]]
then
    javac -d target -cp "lib/*${CP_DELIM}src" src/main/java/*.java
    javac -d target -cp "lib/*${CP_DELIM}src" src/test/java/*.java
elif [[ $1 == "run" ]]
then
    javac -d target -cp "lib/*${CP_DELIM}src" "src/${MAIN//.//}.java" && \
    java -cp "lib/*${CP_DELIM}target" $MAIN "${@:2}"
elif [[ $1 == "test" ]]
then
    javac -d target -cp "lib/*${CP_DELIM}src" src/test/java/*.java && \
    java -cp "lib/*${CP_DELIM}target" test.java.TUGrader "${@:2}"
elif [[ $1 == "fmt" ]]
then
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
elif [[ $1 == "sync" ]]
then
    git add -A
    git commit -m "Syncs changes"
    git fetch origin main
    git pull origin main
    git push origin main
elif [[ $1 == "submit" ]]
then
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
    git add -A
    javac -d target -cp "lib/*${CP_DELIM}src" src/test/java/*.java && \
    java -cp "lib/*${CP_DELIM}target" test.java.TUGrader && \
    git commit -m "Submits assignment" && \
    git fetch origin main && \
    git pull origin main && \
    git push origin main
elif [[ $1 == "init" ]]
then
    rm -r config
    rm .grader.conf
    echo "all" > .grader.conf
    rm src/test/java/FailingTest.java
    rm src/test/java/PassingTest.java
    rm src/test/java/TUGraderTest.java
    sed -i ".sh" "s/MAIN=\"test\.java\.TUGrader\"/MAIN=\"$2\"/" "build.sh"
    rm build.sh.sh
    sed -i ".ps1" "s/\$MAIN = \"test\.java\.TUGrader\"/\$MAIN = \"$2\"/" "build.ps1"
    rm build.ps1.ps1
elif [[ $1 == "clean" ]]
then
    rm -r target/*
else
    echo "build: *** No rule to make target '$1'.  Stop."
fi
