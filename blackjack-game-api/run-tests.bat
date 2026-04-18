@echo off
echo ================================================
echo Running Blackjack Tests
echo ================================================

echo Running unit tests...
call mvn test -Dtest="!*IT"

if %errorlevel% neq 0 (
    echo [ERROR] Unit tests failed!
    exit /b %errorlevel%
)

echo.
echo ================================================
echo All tests completed successfully!
echo ================================================