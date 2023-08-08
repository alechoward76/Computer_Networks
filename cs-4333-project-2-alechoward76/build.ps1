param(
    $rule = "default"
)
<# NOTE: Update these variables to target different files with this script. #>
$MAIN = "main.java.Main"
# ======================================================================== #
$CP_DELIM = ";"
if ( $IsMacOS -or $IsLinux ) {
    $CP_DELIM = ":" # changes to : for Mac or Linux
}
<# ======================================================================== #>
if ( $rule -eq "" -or $rule -eq "default" ) {
    javac -d target -cp "lib/*$($CP_DELIM)src" src/main/java/*.java
    javac -d target -cp "lib/*$($CP_DELIM)src" src/test/java/*.java
} elseif ( $rule -eq "run" ) {
    javac -d target -cp "lib/*$($CP_DELIM)src" "src/$($MAIN.replace('.', '/')).java" && `
    java -cp "lib/*$($CP_DELIM)target" $MAIN $args
} elseif ( $rule -eq "test" ) {
    javac -d target -cp "lib/*$($CP_DELIM)src" src/test/java/*.java && `
    java -cp "lib/*$($CP_DELIM)target" test.java.TUGrader $args
} elseif ( $rule -eq "fmt" ) {
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
} elseif ( $rule -eq "sync" ) {
    git add -A
    git commit -m "Syncs changes"
    git fetch origin main
    git pull origin main
    git push origin main
} elseif ( $rule -eq "submit" ) {
    java -jar lib/google-java-format.jar --replace --skip-javadoc-formatting src/main/java/*.java src/test/java/*.java
    git add -A
    javac -d target -cp "lib/*$($CP_DELIM)src" src/test/java/*.java && `
    java -cp "lib/*$($CP_DELIM)target" test.java.TUGrader && `
    git commit -m "Submits assignment" && `
    git fetch origin main && `
    git pull origin main && `
    git push origin main
} elseif ( $rule -eq "init" ) {
    rm -r config
    rm .grader.conf
    Write-Output "all" | Out-File .grader.conf
    rm src/test/java/FailingTest.java
    rm src/test/java/PassingTest.java
    rm src/test/java/TUGraderTest.java
    (Get-Content build.sh) -Replace 'MAIN="test.java.TUGrader"', "MAIN=`"$($args[0])`"" | Set-Content build.sh
    (Get-Content build.ps1) -Replace '^\$MAIN = "test.java.TUGrader"', "`$MAIN = `"$($args[0])`"" | Set-Content build.ps1
} elseif ( $rule -eq "clean" ) {
    rm -r target/*
} else {
    Write-Output "build: *** No rule to make target '$rule'.  Stop."
}
