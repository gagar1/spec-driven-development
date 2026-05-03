package com.example.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Comprehensive Validation Test Suite for SDD Framework
 * 
 * @spec: SDD-SUITE-T001 - Comprehensive validation test orchestration
 * 
 * This test suite orchestrates all validation tests for the SDD framework to prevent hallucination
 * and ensure spec compliance. It serves as the single entry point for running all validation tests
 * that form the backbone of the SDD framework's hallucination prevention claims.
 * 
 * Test Categories Included:
 * 1. SpecValidationTest - Validates @spec traceability tags and implementation completeness
 * 2. ConstitutionalComplianceTest - Ensures adherence to architectural laws
 * 3. HallucinationDetectionTest - Detects AI-invented patterns and inconsistencies
 * 4. TraceabilityValidationTest - Validates complete traceability chains
 * 5. TemplateComplianceTest - Ensures all documents follow required templates
 * 
 * CI/CD Integration:
 * This suite is designed to run in CI/CD pipelines to catch hallucinations before they reach production.
 * All tests are automated and require no manual intervention, making them suitable for continuous validation.
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@Suite
@SelectClasses({
    SpecValidationTest.class,
    ConstitutionalComplianceTest.class,
    HallucinationDetectionTest.class,
    TraceabilityValidationTest.class,
    TemplateComplianceTest.class
})
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Complete Validation Suite")
public class ValidationTestSuite {
    
    /**
     * This test suite automatically runs all validation tests.
     * Individual test classes can also be run separately for focused validation.
     * 
     * @spec: SDD-SUITE-T001 - Validation orchestration
     */
    
    // The @Suite annotation automatically discovers and runs all specified test classes
    // No additional test methods are needed in this orchestration class
}