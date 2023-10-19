@echo off
setlocal enabledelayedexpansion

:: Run the tests
echo Running tests...
java -cp bin;lib\org.junit4-4.3.1.jar;lib\json-simple-1.1.jar org.junit.runner.JUnitCore BoomerangTest

:: Capture the exit code to check for test failures
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% EQU 0 (
    echo All tests passed!
) else (
    echo Some tests failed!
)

:: End the script
exit /b %EXIT_CODE%