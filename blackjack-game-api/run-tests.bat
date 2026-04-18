@echo off
REM Blackjack Testing Script for Windows
REM Usage: run-tests.bat [unit|integration|e2e|all|coverage]

setlocal enabledelayedexpansion

if "%1"=="" goto :all
if "%1"=="unit" goto :unit
if "%1"=="integration" goto :integration
if "%1"=="e2e" goto :e2e
if "%1"=="all" goto :all
if "%1"=="coverage" goto :coverage
goto :usage

:unit
echo ================================================
echo Running Unit Tests
echo ================================================
call mvn test -Dgroups=unit
if !errorlevel! neq 0 (
    echo [ERROR] Unit tests failed!
    exit /b !errorlevel!
)
goto :success

:integration
echo ================================================
echo Running Integration Tests
echo ================================================
echo [WARNING] Make sure Docker is running
call mvn verify -Dgroups=integration
if !errorlevel! neq 0 (
    echo [ERROR] Integration tests failed!
    exit /b !errorlevel!
)
goto :success

:e2e
echo ================================================
echo Running E2E Tests
echo ================================================
call mvn verify -Dtest=*E2ETest
if !errorlevel! neq 0 (
    echo [ERROR] E2E tests failed!
    exit /b !errorlevel!
)
goto :success

:all
echo ================================================
echo Running All Tests
echo ================================================
call mvn clean verify
if !errorlevel! neq 0 (
    echo [ERROR] Some tests failed!
    exit /b !errorlevel!
)
goto :success

:coverage
echo ================================================
echo Generating Coverage Report
echo ================================================
call mvn clean verify jacoco:report
if !errorlevel! neq 0 (
    echo [ERROR] Coverage generation failed!
    exit /b !errorlevel!
)
echo.
echo Report generated at: target\site\jacoco\index.html
goto :success

:usage
echo Usage: run-tests.bat {unit^|integration^|e2e^|all^|coverage}
echo.
echo Options:
echo   unit         - Run unit tests only
echo   integration  - Run integration tests only (requires Docker)
echo   e2e          - Run end-to-end tests only
echo   all          - Run all tests
echo   coverage     - Generate coverage report
echo.
echo Examples:
echo   run-tests.bat unit
echo   run-tests.bat coverage
exit /b 1

:success
echo.
echo ================================================
echo Tests completed successfully!
echo ================================================
exit /b 0