#!/bin/bash

# Check if test coverage meets minimum requirements
# Usage: ./check-coverage.sh

set -e

# Minimum coverage thresholds
MIN_LINE_COVERAGE=85
MIN_BRANCH_COVERAGE=80

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo ""
echo -e "${BLUE}================================================${NC}"
echo -e "${GREEN}Checking Test Coverage${NC}"
echo -e "${BLUE}================================================${NC}"
echo ""

# Generate coverage report
echo -e "${YELLOW}Generating coverage report...${NC}"
mvn clean verify jacoco:report > /dev/null 2>&1

if [ -f "target/site/jacoco/jacoco.csv" ]; then
    # Extract line coverage
    LINE_COVERAGE=$(awk -F',' '
        NR>1 {
            missed += $8;
            covered += $9
        }
        END {
            if (missed + covered > 0)
                printf "%.1f", (covered / (missed + covered)) * 100
            else
                printf "0"
        }' target/site/jacoco/jacoco.csv)

    # Extract branch coverage
    BRANCH_COVERAGE=$(awk -F',' '
        NR>1 {
            missed += $10;
            covered += $11
        }
        END {
            if (missed + covered > 0)
                printf "%.1f", (covered / (missed + covered)) * 100
            else
                printf "0"
        }' target/site/jacoco/jacoco.csv)

    echo -e "${BLUE}Coverage Results:${NC}"
    echo "=================="
    echo -e "Line Coverage:   ${YELLOW}${LINE_COVERAGE}%${NC}"
    echo -e "Branch Coverage: ${YELLOW}${BRANCH_COVERAGE}%${NC}"
    echo ""

    # Check line coverage
    LINE_OK=true
    if (( $(echo "$LINE_COVERAGE < $MIN_LINE_COVERAGE" | bc -l) )); then
        echo -e "${RED}✗ Line coverage (${LINE_COVERAGE}%) is below minimum (${MIN_LINE_COVERAGE}%)${NC}"
        LINE_OK=false
    else
        echo -e "${GREEN}✓ Line coverage (${LINE_COVERAGE}%) meets minimum (${MIN_LINE_COVERAGE}%)${NC}"
    fi

    # Check branch coverage
    BRANCH_OK=true
    if (( $(echo "$BRANCH_COVERAGE < $MIN_BRANCH_COVERAGE" | bc -l) )); then
        echo -e "${RED}✗ Branch coverage (${BRANCH_COVERAGE}%) is below minimum (${MIN_BRANCH_COVERAGE}%)${NC}"
        BRANCH_OK=false
    else
        echo -e "${GREEN}✓ Branch coverage (${BRANCH_COVERAGE}%) meets minimum (${MIN_BRANCH_COVERAGE}%)${NC}"
    fi

    echo ""

    if [ "$LINE_OK" = true ] && [ "$BRANCH_OK" = true ]; then
        echo -e "${GREEN}✅ Coverage check PASSED!${NC}"
        exit 0
    else
        echo -e "${RED}❌ Coverage check FAILED!${NC}"
        exit 1
    fi
else
    echo -e "${RED}Coverage report not found! Run 'mvn verify' first.${NC}"
    exit 1
fi