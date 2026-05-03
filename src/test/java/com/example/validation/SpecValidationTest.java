package com.example.validation;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Spec Validation Tests for SDD Framework
 * 
 * @spec: SDD-VAL-T001 - Spec traceability validation
 * 
 * This test suite validates that all @spec traceability tags reference real specifications
 * and that implementations match spec requirements. It serves as the validation backbone
 * for the SDD framework's hallucination prevention claims.
 * 
 * Key Validation Areas:
 * 1. Spec Traceability - All @spec tags reference existing specifications
 * 2. Implementation Completeness - All spec requirements have corresponding code
 * 3. Consistency Validation - Code behavior matches specification requirements
 * 4. Template Compliance - All specifications follow required templates
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Spec Validation Tests")
public class SpecValidationTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserController userController;
    
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String SRC_PATH = PROJECT_ROOT + "/src";
    private static final String SPECS_PATH = PROJECT_ROOT + "/sdd/specs";
    private static final String PLANS_PATH = PROJECT_ROOT + "/sdd/plans";
    private static final String TASKS_PATH = PROJECT_ROOT + "/sdd/tasks";
    
    private List<String> specFiles;
    private List<String> javaFiles;
    private Map<String, String> specTagsToFiles;
    
    @BeforeEach
    void setUp() throws IOException {
        // @spec: SDD-VAL-T001 - Initialize validation test data
        loadSpecificationFiles();
        loadJavaSourceFiles();
        extractSpecTraceabilityTags();
    }
    
    /**
     * Test 1: Spec Traceability Validation
     * Validates that all @spec tags in code reference real specifications
     * 
     * @spec: SDD-VAL-T001 - Spec tag validation
     */
    @Test
    @DisplayName("All @spec tags must reference existing specifications")
    void validateAllSpecTagsReferenceExistingSpecifications() {
        // @spec: SDD-VAL-T001 - Extract all spec tags from source code
        Map<String, List<String>> specTagsPerFile = extractSpecTagsFromSourceCode();
        
        List<String> invalidTags = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : specTagsPerFile.entrySet()) {
            String fileName = entry.getKey();
            List<String> specTags = entry.getValue();
            
            for (String specTag : specTags) {
                // @spec: SDD-VAL-T001 - Validate spec tag format
                if (!isValidSpecTagFormat(specTag)) {
                    invalidTags.add(String.format("Invalid format in %s: %s", fileName, specTag));
                    continue;
                }
                
                // @spec: SDD-VAL-T001 - Check if specification exists
                String specId = extractSpecIdFromTag(specTag);
                if (!specificationExists(specId)) {
                    invalidTags.add(String.format("Missing spec in %s: %s (Spec ID: %s)", fileName, specTag, specId));
                }
            }
        }
        
        // @spec: SDD-VAL-T001 - Assert no invalid tags found
        if (!invalidTags.isEmpty()) {
            fail("Found invalid @spec tags that don't reference existing specifications:\n" + 
                 String.join("\n", invalidTags));
        }
        
        assertTrue(specTagsPerFile.size() > 0, "Should find @spec tags in source code");
    }
    
    /**
     * Test 2: Implementation Completeness Validation
     * Ensures all specification requirements have corresponding implementations
     * 
     * @spec: SDD-VAL-T002 - Implementation completeness check
     */
    @Test
    @DisplayName("All specification requirements must have corresponding implementations")
    void validateAllSpecificationRequirementsAreImplemented() throws IOException {
        // @spec: SDD-VAL-T002 - Load specification requirements
        Map<String, List<String>> specRequirements = extractRequirementsFromSpecs();
        Map<String, List<String>> implementedFeatures = extractImplementedFeaturesFromCode();
        
        List<String> missingImplementations = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : specRequirements.entrySet()) {
            String specId = entry.getKey();
            List<String> requirements = entry.getValue();
            List<String> implementations = implementedFeatures.getOrDefault(specId, new ArrayList<>());
            
            for (String requirement : requirements) {
                // @spec: SDD-VAL-T002 - Check if requirement is implemented
                boolean isImplemented = implementations.stream()
                    .anyMatch(impl -> impl.toLowerCase().contains(requirement.toLowerCase()));
                
                if (!isImplemented) {
                    missingImplementations.add(String.format("Spec %s: Missing implementation for '%s'", specId, requirement));
                }
            }
        }
        
        // @spec: SDD-VAL-T002 - Assert all requirements are implemented
        if (!missingImplementations.isEmpty()) {
            fail("Found specification requirements without implementations:\n" + 
                 String.join("\n", missingImplementations));
        }
    }
    
    /**
     * Test 3: API Endpoint Specification Compliance
     * Validates that all API endpoints match their specifications
     * 
     * @spec: SDD-VAL-T003 - API specification compliance
     */
    @Test
    @DisplayName("All API endpoints must comply with their specifications")
    void validateApiEndpointsMatchSpecifications() {
        // @spec: SDD-VAL-T003 - Validate UserController endpoints
        assertNotNull(userController, "UserController should be available");
        
        // @spec: SDD-VAL-T003 - Check required endpoints exist
        List<String> requiredEndpoints = Arrays.asList(
            "createUser", "getUserById", "getAllUsers", 
            "updateUser", "deleteUser", "searchUsers", "getUserStats"
        );
        
        Class<?> controllerClass = userController.getClass();
        List<String> actualMethods = Arrays.stream(controllerClass.getDeclaredMethods())
            .map(method -> method.getName())
            .collect(Collectors.toList());
        
        List<String> missingEndpoints = requiredEndpoints.stream()
            .filter(endpoint -> !actualMethods.contains(endpoint))
            .collect(Collectors.toList());
        
        // @spec: SDD-VAL-T003 - Assert all required endpoints are implemented
        assertTrue(missingEndpoints.isEmpty(), 
            "Missing required API endpoints: " + String.join(", ", missingEndpoints));
    }
    
    /**
     * Test 4: Hallucination Detection - Non-existent API Patterns
     * Detects AI-invented patterns that don't exist in specifications
     * 
     * @spec: SDD-VAL-T004 - Hallucination detection
     */
    @Test
    @DisplayName("Detect AI-invented patterns not in specifications")
    void detectHallucinatedApiPatterns() throws IOException {
        // @spec: SDD-VAL-T004 - Define known hallucination patterns
        List<String> commonHallucinationPatterns = Arrays.asList(
            "@Autowired.*Repository.*repository", // Direct repository injection in controllers
            "@RequestMapping.*value.*=.*\".*\"", // Old-style request mapping
            "ResponseEntity<String>", // String responses instead of proper DTOs
            "@PathVariable.*String.*id", // String IDs instead of proper types
            "System\\.out\\.println", // Console logging instead of proper logging
            "@Component.*Service", // Service annotated as Component
            "new.*Exception\\(\\)", // Empty exception constructors
            "@RequestParam.*required.*=.*false" // Optional parameters without defaults
        );
        
        List<String> hallucinationViolations = new ArrayList<>();
        
        // @spec: SDD-VAL-T004 - Scan source files for hallucination patterns
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            
            for (String pattern : commonHallucinationPatterns) {
                Pattern regex = Pattern.compile(pattern);
                Matcher matcher = regex.matcher(content);
                
                if (matcher.find()) {
                    hallucinationViolations.add(String.format(
                        "Potential hallucination in %s: Found pattern '%s'", 
                        javaFile, pattern));
                }
            }
        }
        
        // @spec: SDD-VAL-T004 - Assert no hallucination patterns found
        assertTrue(hallucinationViolations.isEmpty(), 
            "Found potential AI hallucination patterns:\n" + 
            String.join("\n", hallucinationViolations));
    }
    
    /**
     * Test 5: Template Compliance Validation
     * Ensures all specifications follow required templates
     * 
     * @spec: SDD-VAL-T005 - Template compliance check
     */
    @Test
    @DisplayName("All specifications must follow required templates")
    void validateSpecificationTemplateCompliance() throws IOException {
        // @spec: SDD-VAL-T005 - Define required template sections
        List<String> requiredSections = Arrays.asList(
            "# Specification:", "## Metadata", "## Overview", 
            "## Functional Requirements", "## Success Criteria"
        );
        
        List<String> nonCompliantSpecs = new ArrayList<>();
        
        for (String specFile : specFiles) {
            String content = Files.readString(Paths.get(specFile));
            
            for (String section : requiredSections) {
                if (!content.contains(section)) {
                    nonCompliantSpecs.add(String.format(
                        "Spec %s missing required section: %s", 
                        specFile, section));
                }
            }
        }
        
        // @spec: SDD-VAL-T005 - Assert all specs follow template
        assertTrue(nonCompliantSpecs.isEmpty(), 
            "Found non-compliant specifications:\n" + 
            String.join("\n", nonCompliantSpecs));
    }
    
    // Helper Methods
    
    /**
     * Loads all specification files from the specs directory
     * @spec: SDD-VAL-T001 - Spec file loading
     */
    private void loadSpecificationFiles() throws IOException {
        Path specsDir = Paths.get(SPECS_PATH);
        if (!Files.exists(specsDir)) {
            specFiles = new ArrayList<>();
            return;
        }
        
        try (Stream<Path> paths = Files.walk(specsDir)) {
            specFiles = paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".md"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Loads all Java source files from the src directory
     * @spec: SDD-VAL-T001 - Source file loading
     */
    private void loadJavaSourceFiles() throws IOException {
        Path srcDir = Paths.get(SRC_PATH);
        
        try (Stream<Path> paths = Files.walk(srcDir)) {
            javaFiles = paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Extracts spec traceability tags from source files
     * @spec: SDD-VAL-T001 - Traceability extraction
     */
    private void extractSpecTraceabilityTags() throws IOException {
        specTagsToFiles = new HashMap<>();
        Pattern specPattern = Pattern.compile("@spec:\\s*([A-Z0-9-]+)");
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            Matcher matcher = specPattern.matcher(content);
            
            while (matcher.find()) {
                String specTag = matcher.group(1);
                specTagsToFiles.put(specTag, javaFile);
            }
        }
    }
    
    /**
     * Extracts spec tags from source code files
     * @spec: SDD-VAL-T001 - Spec tag extraction
     */
    private Map<String, List<String>> extractSpecTagsFromSourceCode() {
        Map<String, List<String>> result = new HashMap<>();
        Pattern specPattern = Pattern.compile("@spec:\\s*([A-Z0-9-]+(?:-[A-Z0-9]+)*)");
        
        for (String javaFile : javaFiles) {
            try {
                String content = Files.readString(Paths.get(javaFile));
                List<String> tags = new ArrayList<>();
                Matcher matcher = specPattern.matcher(content);
                
                while (matcher.find()) {
                    tags.add(matcher.group(1));
                }
                
                if (!tags.isEmpty()) {
                    result.put(javaFile, tags);
                }
            } catch (IOException e) {
                fail("Failed to read file: " + javaFile);
            }
        }
        
        return result;
    }
    
    /**
     * Validates spec tag format
     * @spec: SDD-VAL-T001 - Tag format validation
     */
    private boolean isValidSpecTagFormat(String specTag) {
        // Valid format: US1-T003 or SDD-VAL-T001
        return specTag.matches("[A-Z0-9]+-[A-Z0-9]+(-[A-Z0-9]+)*");
    }
    
    /**
     * Extracts specification ID from tag
     * @spec: SDD-VAL-T001 - Spec ID extraction
     */
    private String extractSpecIdFromTag(String specTag) {
        // Extract spec ID (e.g., "US1" from "US1-T003")
        return specTag.split("-")[0];
    }
    
    /**
     * Checks if specification exists
     * @spec: SDD-VAL-T001 - Spec existence check
     */
    private boolean specificationExists(String specId) {
        return specFiles.stream()
            .anyMatch(file -> file.contains(specId) || 
                     file.contains(specId.toLowerCase()));
    }
    
    /**
     * Extracts requirements from specification files
     * @spec: SDD-VAL-T002 - Requirements extraction
     */
    private Map<String, List<String>> extractRequirementsFromSpecs() throws IOException {
        Map<String, List<String>> requirements = new HashMap<>();
        
        for (String specFile : specFiles) {
            String content = Files.readString(Paths.get(specFile));
            String specId = extractSpecIdFromFileName(specFile);
            
            List<String> specRequirements = new ArrayList<>();
            
            // Extract acceptance criteria and functional requirements
            Pattern criteriaPattern = Pattern.compile("- \\[ \\] (.+)");
            Matcher matcher = criteriaPattern.matcher(content);
            
            while (matcher.find()) {
                specRequirements.add(matcher.group(1).trim());
            }
            
            requirements.put(specId, specRequirements);
        }
        
        return requirements;
    }
    
    /**
     * Extracts implemented features from code
     * @spec: SDD-VAL-T002 - Implementation extraction
     */
    private Map<String, List<String>> extractImplementedFeaturesFromCode() {
        Map<String, List<String>> implementations = new HashMap<>();
        
        for (String javaFile : javaFiles) {
            try {
                String content = Files.readString(Paths.get(javaFile));
                
                // Extract method names and class features
                Pattern methodPattern = Pattern.compile("public\\s+\\w+\\s+(\\w+)\\s*\\(");
                Matcher matcher = methodPattern.matcher(content);
                
                List<String> features = new ArrayList<>();
                while (matcher.find()) {
                    features.add(matcher.group(1));
                }
                
                // Group by spec ID if found in file
                String specId = extractSpecIdFromContent(content);
                if (specId != null) {
                    implementations.computeIfAbsent(specId, k -> new ArrayList<>()).addAll(features);
                }
                
            } catch (IOException e) {
                fail("Failed to read file: " + javaFile);
            }
        }
        
        return implementations;
    }
    
    /**
     * Extracts spec ID from file name
     * @spec: SDD-VAL-T002 - Spec ID from filename
     */
    private String extractSpecIdFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
    
    /**
     * Extracts spec ID from file content
     * @spec: SDD-VAL-T002 - Spec ID from content
     */
    private String extractSpecIdFromContent(String content) {
        Pattern pattern = Pattern.compile("@spec:\\s*([A-Z0-9]+)");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }
}