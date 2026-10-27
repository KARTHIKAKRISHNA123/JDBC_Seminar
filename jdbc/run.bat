@echo off
REM ------------------------------------------------------------------
REM  Compile all demos and run one of them (Windows).
REM  Usage (from inside the jdbc folder):   run J03_InsertDemo
REM  Needs the Connector/J .jar inside ..\lib and DB_PASSWORD set.
REM ------------------------------------------------------------------
if "%~1"=="" (
    echo Usage: run J03_InsertDemo
    exit /b 1
)
javac -cp "..\lib\*" -d out *.java || exit /b 1
java -cp "out;..\lib\*" %1
