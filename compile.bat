@echo off

echo Compiling code...
javac -d bin -sourcepath src -cp lib\json-simple-1.1.jar src\*.java
xcopy resources\ bin\ /E /I /Y
if ERRORLEVEL 1 goto error

echo Compiling tests...
javac -d bin -cp lib\org.junit4-4.3.1.jar;bin test\BoomerangTest.java
if ERRORLEVEL 1 goto error

echo Compilation successful!
exit /b 0

:error
echo Compilation failed!
exit /b 1