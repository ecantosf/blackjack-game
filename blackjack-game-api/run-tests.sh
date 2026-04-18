#!/bin/bash

# Blackjack Testing Script
# Usage: ./run-tests.sh [unit|integration|e2e|all|coverage|mutation|architecture]

set -e

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo ""
    echo -e "${BLUE}================================================${NC}"
    echo -e "${GREEN}$1${NC}"
    echo -e "${BLUE}================================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# Run unit tests
run_unit_tests() {
    print_header "Running Unit Tests"
    mvn test -Dgroups=unit
    print_success "Unit tests completed!"
}

# Run integration tests
run_integration_tests() {
    print_header "Running Integration Tests"
    print_warning "Make sure Docker is running"
    mvn verify -Dgroups=integration
    print_success "Integration tests completed!"
}

# Run E2E tests
run_e2e_tests() {
    print_header "Running E2E Tests"
    mvn verify -Dtest=*E2ETest
    print_success "E2E tests completed!"
}

# Run all tests
run_all_tests() {
    print_header "Running All Tests"
    mvn clean verify
    print_success "All tests completed!"
}

# Generate coverage report
run_coverage() {
    print_header "Generating Coverage Report"
    mvn clean verify jacoco:report
    print_success "Coverage report generated!"
    echo "Report available at: target/site/jacoco/index.html"

    # Open report in browser (if supported)
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open target/site/jacoco/index.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open target/site/jacoco/index.html 2>/dev/null || true
    fi
}

# Run mutation tests
run_mutation_tests() {
    print_header "Running Mutation Tests"
    print_warning "This may take several minutes..."
    mvn org.pitest:pitest-maven:mutationCoverage
    print_success "Mutation tests completed!"
    echo "Report available at: target/pit-reports/index.html"

    if [[ "$OSTYPE" == "darwin"* ]]; then
        open target/pit-reports/index.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open target/pit-reports/index.html 2>/dev/null || true
    fi
}

# Run architecture tests
run_architecture_tests() {
    print_header "Running Architecture Tests"
    mvn test -Dtest=*ArchitectureTest
    print_success "Architecture tests completed!"
}

# Show usage
show_usage() {
    echo "Usage: $0 {unit|integration|e2e|all|coverage|mutation|architecture}"
    echo ""
    echo "Options:"
    echo "  unit         - Run unit tests only"
    echo "  integration  - Run integration tests only (requires Docker)"
    echo "  e2e          - Run end-to-end tests only"
    echo "  all          - Run all tests"
    echo "  coverage     - Generate coverage report"
    echo "  mutation     - Run mutation tests"
    echo "  architecture - Run architecture tests"
    echo ""
    echo "Examples:"
    echo "  ./run-tests.sh unit"
    echo "  ./run-tests.sh coverage"
}

# Main script logic
case "$1" in
    unit)
        run_unit_tests
        ;;
    integration)
        run_integration_tests
        ;;
    e2e)
        run_e2e_tests
        ;;
    all)
        run_all_tests
        ;;
    coverage)
        run_coverage
        ;;
    mutation)
        run_mutation_tests
        ;;
    architecture)
        run_architecture_tests
        ;;
    *)
        show_usage
        exit 1
        ;;
esac

exit 0