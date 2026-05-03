package com.example.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
 * Template Compliance Tests for SDD Framework
 * 
 * @spec: SDD-TMPL-T001 - Template compliance validation
 * 
 * This test suite verifies that all specifications follow the required templates defined
 * in the SDD framework. It ensures consistency across all documentation artifacts and
 * validates that mandatory sections are present with proper formatting.
 * 
 * Key Validation Areas:
 * 1. Specification Template Compliance - All specs follow spec.template.md
 * 2. Plan Template Compliance - All plans follow plan.template.md
 * 3. Task Template Compliance - All tasks follow task.template.md
 * 4. Mandatory Section Validation - Required sections are present
 * 5. Format Consistency - Consistent markdown formatting and structure
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Template Compliance Tests")
public class TemplateComplianceTest {
    
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String SPECS_PATH = PROJECT_ROOT + "/sdd/specs";
    private static final String PLANS_PATH = PROJECT_ROOT + "/sdd/plans";
    private static final String TASKS_PATH = PROJECT_ROOT + "/sdd/tasks";
    private static final String TEMPLATES_PATH = PROJECT_ROOT + "/sdd/framework/templates";
    
    private List<String> specFiles;
    private List<String> planFiles;
    private List<String> taskFiles;
    private String specTemplate;
    private String planTemplate;
    private String taskTemplate;
    
    @BeforeEach
    void setUp() throws IOException {
        // @spec: SDD-TMPL-T001 - Initialize template compliance test data
        loadDocumentFiles();
        loadTemplates();
    }
    
    /**
     * Test 1: Specification Template Compliance
     * Validates that all specifications follow the required template structure
     * 
     * @spec: SDD-TMPL-T001 - Specification template compliance
     */
    @Test
    @DisplayName("All specifications must follow the required template")
    void validateSpecificationTemplateCompliance() throws IOException {
        List<String> templateViolations = new ArrayList<>();
        
        // @spec: SDD-TMPL-T001 - Define required specification sections
        List<String> requiredSections = Arrays.asList(
            "# Specification:",
            "## Metadata",
            "## Overview",
            "### Purpose",
            "### Scope",
            "### Success Criteria",
            "## Functional Requirements",
            "### User Stories",
            "## Non-Functional Requirements",
            "## API Specifications",
            "## Data Models",
            "## Testing Strategy"
        );
        
        for (String specFile : specFiles) {
            String content = Files.readString(Paths.get(specFile));
            String fileName = Paths.get(specFile).getFileName().toString();
            
            // @spec: SDD-TMPL-T001 - Check for required sections
            for (String section : requiredSections) {
                if (!content.contains(section)) {
                    templateViolations.add(String.format(
                        "Specification %s missing required section: %s", 
                        fileName, section));
                }
            }
            
            // @spec: SDD-TMPL-T001 - Validate metadata format
            if (!hasValidMetadataFormat(content)) {
                templateViolations.add(String.format(
                    "Specification %s has invalid metadata format", fileName));
            }
            
            // @spec: SDD-TMPL-T001 - Validate acceptance criteria format
            if (!hasValidAcceptanceCriteriaFormat(content)) {
                templateViolations.add(String.format(
                    "Specification %s has invalid acceptance criteria format", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T001 - Assert specification template compliance
        assertTrue(templateViolations.isEmpty(), 
            "Found specification template violations:\n" + String.join("\n", templateViolations));
    }
    
    /**
     * Test 2: Plan Template Compliance
     * Validates that all technical plans follow the required template structure
     * 
     * @spec: SDD-TMPL-T002 - Plan template compliance
     */
    @Test
    @DisplayName("All technical plans must follow the required template")
    void validatePlanTemplateCompliance() throws IOException {
        List<String> templateViolations = new ArrayList<>();
        
        // @spec: SDD-TMPL-T002 - Define required plan sections
        List<String> requiredSections = Arrays.asList(
            "# Technical Plan:",
            "## Metadata",
            "## Overview",
            "## Architecture Design",
            "## Implementation Approach",
            "## Technology Stack",
            "## Risk Assessment",
            "## Task Breakdown",
            "## Constitutional Compliance",
            "## Success Criteria"
        );
        
        for (String planFile : planFiles) {
            String content = Files.readString(Paths.get(planFile));
            String fileName = Paths.get(planFile).getFileName().toString();
            
            // @spec: SDD-TMPL-T002 - Check for required sections
            for (String section : requiredSections) {
                if (!content.contains(section)) {
                    templateViolations.add(String.format(
                        "Plan %s missing required section: %s", 
                        fileName, section));
                }
            }
            
            // @spec: SDD-TMPL-T002 - Validate architecture diagrams
            if (!hasArchitectureDiagrams(content)) {
                templateViolations.add(String.format(
                    "Plan %s missing architecture diagrams", fileName));
            }
            
            // @spec: SDD-TMPL-T002 - Validate constitutional compliance section
            if (!hasConstitutionalComplianceValidation(content)) {
                templateViolations.add(String.format(
                    "Plan %s missing constitutional compliance validation", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T002 - Assert plan template compliance
        assertTrue(templateViolations.isEmpty(), 
            "Found plan template violations:\n" + String.join("\n", templateViolations));
    }
    
    /**
     * Test 3: Task Template Compliance
     * Validates that all implementation tasks follow the required template structure
     * 
     * @spec: SDD-TMPL-T003 - Task template compliance
     */
    @Test
    @DisplayName("All implementation tasks must follow the required template")
    void validateTaskTemplateCompliance() throws IOException {
        List<String> templateViolations = new ArrayList<>();
        
        // @spec: SDD-TMPL-T003 - Define required task sections
        List<String> requiredSections = Arrays.asList(
            "# Implementation Task:",
            "## Metadata",
            "## Overview",
            "## Implementation Steps",
            "## Acceptance Criteria",
            "## Testing Requirements",
            "## Risk Mitigation",
            "## Progress Tracking"
        );
        
        for (String taskFile : taskFiles) {
            String content = Files.readString(Paths.get(taskFile));
            String fileName = Paths.get(taskFile).getFileName().toString();
            
            // @spec: SDD-TMPL-T003 - Check for required sections
            for (String section : requiredSections) {
                if (!content.contains(section)) {
                    templateViolations.add(String.format(
                        "Task %s missing required section: %s", 
                        fileName, section));
                }
            }
            
            // @spec: SDD-TMPL-T003 - Validate implementation steps format
            if (!hasValidImplementationSteps(content)) {
                templateViolations.add(String.format(
                    "Task %s has invalid implementation steps format", fileName));
            }
            
            // @spec: SDD-TMPL-T003 - Validate progress tracking format
            if (!hasValidProgressTracking(content)) {
                templateViolations.add(String.format(
                    "Task %s has invalid progress tracking format", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T003 - Assert task template compliance
        assertTrue(templateViolations.isEmpty(), 
            "Found task template violations:\n" + String.join("\n", templateViolations));
    }
    
    /**
     * Test 4: Mandatory Section Content Validation
     * Ensures that mandatory sections contain meaningful content
     * 
     * @spec: SDD-TMPL-T004 - Mandatory section content validation
     */
    @Test
    @DisplayName("Mandatory sections must contain meaningful content")
    void validateMandatorySectionContent() throws IOException {
        List<String> contentViolations = new ArrayList<>();
        
        // @spec: SDD-TMPL-T004 - Validate specification content
        for (String specFile : specFiles) {
            String content = Files.readString(Paths.get(specFile));
            String fileName = Paths.get(specFile).getFileName().toString();
            
            if (hasEmptyMandatorySections(content, "specification")) {
                contentViolations.add(String.format(
                    "Specification %s has empty mandatory sections", fileName));
            }
            
            if (!hasValidUserStories(content)) {
                contentViolations.add(String.format(
                    "Specification %s lacks proper user stories", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T004 - Validate plan content
        for (String planFile : planFiles) {
            String content = Files.readString(Paths.get(planFile));
            String fileName = Paths.get(planFile).getFileName().toString();
            
            if (hasEmptyMandatorySections(content, "plan")) {
                contentViolations.add(String.format(
                    "Plan %s has empty mandatory sections", fileName));
            }
            
            if (!hasValidRiskAssessment(content)) {
                contentViolations.add(String.format(
                    "Plan %s lacks proper risk assessment", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T004 - Validate task content
        for (String taskFile : taskFiles) {
            String content = Files.readString(Paths.get(taskFile));
            String fileName = Paths.get(taskFile).getFileName().toString();
            
            if (hasEmptyMandatorySections(content, "task")) {
                contentViolations.add(String.format(
                    "Task %s has empty mandatory sections", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T004 - Assert meaningful content
        assertTrue(contentViolations.isEmpty(), 
            "Found mandatory sections with insufficient content:\n" + String.join("\n", contentViolations));
    }
    
    /**
     * Test 5: Format Consistency Validation
     * Ensures consistent markdown formatting across all documents
     * 
     * @spec: SDD-TMPL-T005 - Format consistency validation
     */
    @Test
    @DisplayName("All documents must follow consistent formatting")
    void validateFormatConsistency() throws IOException {
        List<String> formatViolations = new ArrayList<>();
        
        List<String> allDocuments = new ArrayList<>();
        allDocuments.addAll(specFiles);
        allDocuments.addAll(planFiles);
        allDocuments.addAll(taskFiles);
        
        for (String documentFile : allDocuments) {
            String content = Files.readString(Paths.get(documentFile));
            String fileName = Paths.get(documentFile).getFileName().toString();
            
            // @spec: SDD-TMPL-T005 - Check heading format consistency
            if (!hasConsistentHeadingFormat(content)) {
                formatViolations.add(String.format(
                    "Document %s has inconsistent heading format", fileName));
            }
            
            // @spec: SDD-TMPL-T005 - Check list format consistency
            if (!hasConsistentListFormat(content)) {
                formatViolations.add(String.format(
                    "Document %s has inconsistent list format", fileName));
            }
            
            // @spec: SDD-TMPL-T005 - Check metadata format consistency
            if (!hasConsistentMetadataFormat(content)) {
                formatViolations.add(String.format(
                    "Document %s has inconsistent metadata format", fileName));
            }
            
            // @spec: SDD-TMPL-T005 - Check code block format consistency
            if (!hasConsistentCodeBlockFormat(content)) {
                formatViolations.add(String.format(
                    "Document %s has inconsistent code block format", fileName));
            }
        }
        
        // @spec: SDD-TMPL-T005 - Assert format consistency
        assertTrue(formatViolations.isEmpty(), 
            "Found format consistency violations:\n" + String.join("\n", formatViolations));
    }
    
    /**
     * Test 6: Cross-Reference Validation
     * Ensures that document cross-references are valid and consistent
     * 
     * @spec: SDD-TMPL-T006 - Cross-reference validation
     */
    @Test
    @DisplayName("All document cross-references must be valid")
    void validateCrossReferences() throws IOException {
        List<String> crossRefViolations = new ArrayList<>();
        
        List<String> allDocuments = new ArrayList<>();
        allDocuments.addAll(specFiles);
        allDocuments.addAll(planFiles);
        allDocuments.addAll(taskFiles);
        
        // @spec: SDD-TMPL-T006 - Build reference map
        Map<String, String> documentMap = buildDocumentMap(allDocuments);
        
        for (String documentFile : allDocuments) {
            String content = Files.readString(Paths.get(documentFile));
            String fileName = Paths.get(documentFile).getFileName().toString();
            
            // @spec: SDD-TMPL-T006 - Extract and validate references
            List<String> references = extractDocumentReferences(content);
            
            for (String reference : references) {
                if (!documentMap.containsKey(reference)) {
                    crossRefViolations.add(String.format(
                        "Document %s contains invalid reference: %s", 
                        fileName, reference));
                }
            }
        }
        
        // @spec: SDD-TMPL-T006 - Assert valid cross-references
        assertTrue(crossRefViolations.isEmpty(), 
            "Found invalid cross-references:\n" + String.join("\n", crossRefViolations));
    }
    
    // Helper Methods
    
    /**
     * Loads all document files
     * @spec: SDD-TMPL-T001 - Document file loading
     */
    private void loadDocumentFiles() throws IOException {
        specFiles = loadFilesFromDirectory(SPECS_PATH);
        planFiles = loadFilesFromDirectory(PLANS_PATH);
        taskFiles = loadFilesFromDirectory(TASKS_PATH);
    }
    
    /**
     * Loads template files
     * @spec: SDD-TMPL-T001 - Template loading
     */
    private void loadTemplates() throws IOException {
        Path specTemplatePath = Paths.get(TEMPLATES_PATH + "/spec.template.md");
        Path planTemplatePath = Paths.get(TEMPLATES_PATH + "/plan.template.md");
        Path taskTemplatePath = Paths.get(TEMPLATES_PATH + "/task.template.md");
        
        specTemplate = Files.exists(specTemplatePath) ? Files.readString(specTemplatePath) : "";
        planTemplate = Files.exists(planTemplatePath) ? Files.readString(planTemplatePath) : "";
        taskTemplate = Files.exists(taskTemplatePath) ? Files.readString(taskTemplatePath) : "";
    }
    
    /**
     * Loads files from directory
     * @spec: SDD-TMPL-T001 - File loading helper
     */
    private List<String> loadFilesFromDirectory(String directory) throws IOException {
        Path dir = Paths.get(directory);
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }
        
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".md"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }
    
    // Validation Helper Methods
    
    private boolean hasValidMetadataFormat(String content) {
        // Check for required metadata fields
        return content.contains("- **Spec ID**:") &&
               content.contains("- **Title**:") &&
               content.contains("- **Author**:") &&
               content.contains("- **Date**:") &&
               content.contains("- **Status**:");
    }
    
    private boolean hasValidAcceptanceCriteriaFormat(String content) {
        // Check for proper acceptance criteria format
        Pattern pattern = Pattern.compile("- \\[ \\] .+");
        return pattern.matcher(content).find();
    }
    
    private boolean hasArchitectureDiagrams(String content) {
        // Check for ASCII diagrams or diagram references
        return content.contains("```") && 
               (content.contains("┌") || content.contains("│") || 
                content.contains("diagram") || content.contains("architecture"));
    }
    
    private boolean hasConstitutionalComplianceValidation(String content) {
        return content.contains("Constitutional Compliance") &&
               (content.contains("constitution.md") || content.contains("architectural laws"));
    }
    
    private boolean hasValidImplementationSteps(String content) {
        // Check for numbered or bulleted implementation steps
        return content.contains("## Implementation Steps") &&
               (content.contains("1.") || content.contains("- "));
    }
    
    private boolean hasValidProgressTracking(String content) {
        // Check for progress tracking elements
        return content.contains("## Progress Tracking") &&
               (content.contains("- [ ]") || content.contains("- [x]"));
    }
    
    private boolean hasEmptyMandatorySections(String content, String documentType) {
        String[] sections = content.split("##");
        
        for (String section : sections) {
            String trimmed = section.trim();
            if (trimmed.startsWith("Overview") || 
                trimmed.startsWith("Purpose") || 
                trimmed.startsWith("Scope")) {
                
                // Check if section has meaningful content (more than just the heading)
                String contentOnly = trimmed.substring(trimmed.indexOf('\n') + 1).trim();
                if (contentOnly.length() < 50) { // Arbitrary threshold for meaningful content
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasValidUserStories(String content) {
        return content.contains("**As a**") &&
               content.contains("**I want**") &&
               content.contains("**So that**");
    }
    
    private boolean hasValidRiskAssessment(String content) {
        return content.contains("## Risk Assessment") &&
               (content.contains("Risk:") || content.contains("Mitigation:") ||
                content.contains("risk") || content.contains("mitigation"));
    }
    
    private boolean hasConsistentHeadingFormat(String content) {
        // Check that headings follow markdown format
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            if (line.trim().startsWith("#")) {
                // Check proper heading format (# followed by space)
                if (!line.matches("#+\\s+.+")) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean hasConsistentListFormat(String content) {
        // Check for consistent list formatting
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("-") || trimmed.startsWith("*")) {
                // Check proper list format (bullet followed by space)
                if (!trimmed.matches("[-*]\\s+.+")) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean hasConsistentMetadataFormat(String content) {
        // Check for consistent metadata formatting
        if (!content.contains("## Metadata")) {
            return true; // No metadata section is acceptable
        }
        
        // Check that metadata follows "- **Key**: Value" format
        Pattern pattern = Pattern.compile("- \\*\\*[^*]+\\*\\*:");
        return pattern.matcher(content).find();
    }
    
    private boolean hasConsistentCodeBlockFormat(String content) {
        // Check for consistent code block formatting
        Pattern pattern = Pattern.compile("```[a-zA-Z]*\\n[\\s\\S]*?\\n```");
        Matcher matcher = pattern.matcher(content);
        
        // If there are code blocks, they should be properly formatted
        return !content.contains("```") || matcher.find();
    }
    
    private Map<String, String> buildDocumentMap(List<String> documents) {
        Map<String, String> documentMap = new HashMap<>();
        
        for (String document : documents) {
            String fileName = Paths.get(document).getFileName().toString();
            String baseName = fileName.replaceFirst("\\.[^.]+$", ""); // Remove extension
            documentMap.put(baseName, document);
            
            // Also map spec IDs
            Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.find()) {
                documentMap.put(matcher.group(1), document);
            }
        }
        
        return documentMap;
    }
    
    private List<String> extractDocumentReferences(String content) {
        List<String> references = new ArrayList<>();
        
        // Extract markdown links
        Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");
        Matcher matcher = linkPattern.matcher(content);
        
        while (matcher.find()) {
            String link = matcher.group(2);
            if (link.endsWith(".md")) {
                String baseName = Paths.get(link).getFileName().toString()
                    .replaceFirst("\\.[^.]+$", "");
                references.add(baseName);
            }
        }
        
        // Extract spec ID references
        Pattern specPattern = Pattern.compile("([A-Z0-9]+)-[A-Z0-9]+");
        matcher = specPattern.matcher(content);
        
        while (matcher.find()) {
            references.add(matcher.group(1));
        }
        
        return references;
    }
}