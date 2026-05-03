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
 * Traceability Validation Tests for SDD Framework
 * 
 * @spec: SDD-TRACE-T001 - Traceability chain validation
 * 
 * This test suite ensures complete traceability chain from business requirements to code implementation.
 * It validates that every requirement can be traced through specifications, plans, tasks, and finally
 * to the actual code implementation with proper @spec tags.
 * 
 * Key Validation Areas:
 * 1. Complete Traceability Chain - Requirements → Specs → Plans → Tasks → Code
 * 2. Bidirectional Traceability - Code → Tasks → Plans → Specs → Requirements
 * 3. Orphaned Code Detection - Code without proper traceability tags
 * 4. Missing Implementation Detection - Requirements without corresponding code
 * 5. Traceability Consistency - Consistent tagging across all artifacts
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Traceability Validation Tests")
public class TraceabilityValidationTest {
    
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String SRC_PATH = PROJECT_ROOT + "/src/main/java";
    private static final String SPECS_PATH = PROJECT_ROOT + "/sdd/specs";
    private static final String PLANS_PATH = PROJECT_ROOT + "/sdd/plans";
    private static final String TASKS_PATH = PROJECT_ROOT + "/sdd/tasks";
    
    private List<String> javaFiles;
    private List<String> specFiles;
    private List<String> planFiles;
    private List<String> taskFiles;
    private Map<String, TraceabilityChain> traceabilityChains;
    
    @BeforeEach
    void setUp() throws IOException {
        // @spec: SDD-TRACE-T001 - Initialize traceability validation data
        loadAllFiles();
        buildTraceabilityChains();
    }
    
    /**
     * Test 1: Complete Traceability Chain Validation
     * Ensures every requirement has a complete traceability chain to implementation
     * 
     * @spec: SDD-TRACE-T001 - Complete traceability chain validation
     */
    @Test
    @DisplayName("Every requirement must have complete traceability to implementation")
    void validateCompleteTraceabilityChain() throws IOException {
        List<String> incompleteChains = new ArrayList<>();
        
        for (Map.Entry<String, TraceabilityChain> entry : traceabilityChains.entrySet()) {
            String specId = entry.getKey();
            TraceabilityChain chain = entry.getValue();
            
            // @spec: SDD-TRACE-T001 - Check for complete chain
            if (!chain.hasCompleteChain()) {
                List<String> missingElements = chain.getMissingElements();
                incompleteChains.add(String.format(
                    "Incomplete traceability chain for %s: Missing %s", 
                    specId, String.join(", ", missingElements)));
            }
        }
        
        // @spec: SDD-TRACE-T001 - Assert all chains are complete
        assertTrue(incompleteChains.isEmpty(), 
            "Found incomplete traceability chains:\n" + String.join("\n", incompleteChains));
    }
    
    /**
     * Test 2: Bidirectional Traceability Validation
     * Ensures traceability works both forward (requirements to code) and backward (code to requirements)
     * 
     * @spec: SDD-TRACE-T002 - Bidirectional traceability validation
     */
    @Test
    @DisplayName("Traceability must work bidirectionally")
    void validateBidirectionalTraceability() throws IOException {
        List<String> traceabilityIssues = new ArrayList<>();
        
        // @spec: SDD-TRACE-T002 - Extract all spec tags from code
        Map<String, List<String>> codeToSpecTags = extractSpecTagsFromCode();
        
        for (Map.Entry<String, List<String>> entry : codeToSpecTags.entrySet()) {
            String javaFile = entry.getKey();
            List<String> specTags = entry.getValue();
            
            for (String specTag : specTags) {
                String specId = extractSpecIdFromTag(specTag);
                
                // @spec: SDD-TRACE-T002 - Check backward traceability
                if (!traceabilityChains.containsKey(specId)) {
                    traceabilityIssues.add(String.format(
                        "Orphaned spec tag in %s: %s (no corresponding specification found)", 
                        Paths.get(javaFile).getFileName(), specTag));
                } else {
                    TraceabilityChain chain = traceabilityChains.get(specId);
                    if (!chain.containsImplementationFile(javaFile)) {
                        traceabilityIssues.add(String.format(
                            "Traceability mismatch: %s references %s but chain doesn't include this file", 
                            Paths.get(javaFile).getFileName(), specTag));
                    }
                }
            }
        }
        
        // @spec: SDD-TRACE-T002 - Assert bidirectional traceability
        assertTrue(traceabilityIssues.isEmpty(), 
            "Found bidirectional traceability issues:\n" + String.join("\n", traceabilityIssues));
    }
    
    /**
     * Test 3: Orphaned Code Detection
     * Identifies code that lacks proper traceability tags
     * 
     * @spec: SDD-TRACE-T003 - Orphaned code detection
     */
    @Test
    @DisplayName("All production code must have traceability tags")
    void detectOrphanedCode() throws IOException {
        List<String> orphanedCode = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-TRACE-T003 - Skip test files and configuration files
            if (isTestFile(fileName) || isConfigurationFile(fileName)) {
                continue;
            }
            
            // @spec: SDD-TRACE-T003 - Check for spec tags
            if (!hasSpecTraceabilityTags(content)) {
                orphanedCode.add(String.format(
                    "Orphaned code in %s: No @spec traceability tags found", fileName));
            } else {
                // @spec: SDD-TRACE-T003 - Check for adequate coverage
                List<String> publicMethods = extractPublicMethods(content);
                List<String> taggedMethods = extractTaggedMethods(content);
                
                double coverageRatio = taggedMethods.size() / (double) Math.max(1, publicMethods.size());
                if (coverageRatio < 0.8) { // Require 80% traceability coverage
                    orphanedCode.add(String.format(
                        "Low traceability coverage in %s: %.1f%% (%d/%d methods tagged)", 
                        fileName, coverageRatio * 100, taggedMethods.size(), publicMethods.size()));
                }
            }
        }
        
        // @spec: SDD-TRACE-T003 - Assert no orphaned code
        assertTrue(orphanedCode.isEmpty(), 
            "Found orphaned code without proper traceability:\n" + String.join("\n", orphanedCode));
    }
    
    /**
     * Test 4: Missing Implementation Detection
     * Identifies requirements that lack corresponding code implementation
     * 
     * @spec: SDD-TRACE-T004 - Missing implementation detection
     */
    @Test
    @DisplayName("All requirements must have corresponding implementations")
    void detectMissingImplementations() throws IOException {
        List<String> missingImplementations = new ArrayList<>();
        
        for (Map.Entry<String, TraceabilityChain> entry : traceabilityChains.entrySet()) {
            String specId = entry.getKey();
            TraceabilityChain chain = entry.getValue();
            
            // @spec: SDD-TRACE-T004 - Extract requirements from specifications
            List<String> requirements = extractRequirementsFromSpec(chain.getSpecFile());
            
            for (String requirement : requirements) {
                // @spec: SDD-TRACE-T004 - Check if requirement has implementation
                if (!hasCorrespondingImplementation(requirement, chain)) {
                    missingImplementations.add(String.format(
                        "Missing implementation for %s: '%s'", specId, requirement));
                }
            }
        }
        
        // @spec: SDD-TRACE-T004 - Assert all requirements are implemented
        assertTrue(missingImplementations.isEmpty(), 
            "Found requirements without implementations:\n" + String.join("\n", missingImplementations));
    }
    
    /**
     * Test 5: Traceability Consistency Validation
     * Ensures consistent tagging format and conventions across all artifacts
     * 
     * @spec: SDD-TRACE-T005 - Traceability consistency validation
     */
    @Test
    @DisplayName("Traceability tags must follow consistent format")
    void validateTraceabilityConsistency() throws IOException {
        List<String> consistencyIssues = new ArrayList<>();
        
        // @spec: SDD-TRACE-T005 - Define expected tag format
        Pattern validTagPattern = Pattern.compile("@spec:\\s*([A-Z0-9]+-[A-Z0-9]+(?:-[A-Z0-9]+)*)");
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-TRACE-T005 - Extract all potential spec tags
            Pattern allSpecTags = Pattern.compile("@spec:[^\n]*");
            Matcher matcher = allSpecTags.matcher(content);
            
            while (matcher.find()) {
                String tag = matcher.group();
                
                // @spec: SDD-TRACE-T005 - Validate tag format
                if (!validTagPattern.matcher(tag).find()) {
                    consistencyIssues.add(String.format(
                        "Invalid tag format in %s: %s", fileName, tag));
                }
                
                // @spec: SDD-TRACE-T005 - Check tag placement
                if (!isProperlyPlaced(tag, content)) {
                    consistencyIssues.add(String.format(
                        "Improper tag placement in %s: %s", fileName, tag));
                }
            }
        }
        
        // @spec: SDD-TRACE-T005 - Assert consistent traceability
        assertTrue(consistencyIssues.isEmpty(), 
            "Found traceability consistency issues:\n" + String.join("\n", consistencyIssues));
    }
    
    /**
     * Test 6: Cross-Artifact Traceability Validation
     * Ensures traceability references are consistent across specs, plans, and tasks
     * 
     * @spec: SDD-TRACE-T006 - Cross-artifact traceability validation
     */
    @Test
    @DisplayName("Traceability references must be consistent across all artifacts")
    void validateCrossArtifactTraceability() throws IOException {
        List<String> crossArtifactIssues = new ArrayList<>();
        
        for (Map.Entry<String, TraceabilityChain> entry : traceabilityChains.entrySet()) {
            String specId = entry.getKey();
            TraceabilityChain chain = entry.getValue();
            
            // @spec: SDD-TRACE-T006 - Validate spec to plan references
            if (chain.hasPlan()) {
                List<String> specReferences = extractSpecReferencesFromPlan(chain.getPlanFile());
                if (!specReferences.contains(specId)) {
                    crossArtifactIssues.add(String.format(
                        "Plan %s doesn't reference its specification %s", 
                        Paths.get(chain.getPlanFile()).getFileName(), specId));
                }
            }
            
            // @spec: SDD-TRACE-T006 - Validate plan to task references
            for (String taskFile : chain.getTaskFiles()) {
                List<String> planReferences = extractPlanReferencesFromTask(taskFile);
                if (chain.hasPlan() && !planReferences.contains(specId)) {
                    crossArtifactIssues.add(String.format(
                        "Task %s doesn't reference its plan for %s", 
                        Paths.get(taskFile).getFileName(), specId));
                }
            }
            
            // @spec: SDD-TRACE-T006 - Validate task to code references
            for (String implementationFile : chain.getImplementationFiles()) {
                List<String> codeReferences = extractSpecTagsFromFile(implementationFile);
                boolean hasValidReference = codeReferences.stream()
                    .anyMatch(tag -> tag.startsWith(specId));
                
                if (!hasValidReference) {
                    crossArtifactIssues.add(String.format(
                        "Implementation %s doesn't reference its specification %s", 
                        Paths.get(implementationFile).getFileName(), specId));
                }
            }
        }
        
        // @spec: SDD-TRACE-T006 - Assert cross-artifact consistency
        assertTrue(crossArtifactIssues.isEmpty(), 
            "Found cross-artifact traceability issues:\n" + String.join("\n", crossArtifactIssues));
    }
    
    // Helper Classes and Methods
    
    /**
     * Represents a complete traceability chain for a specification
     * @spec: SDD-TRACE-T001 - Traceability chain representation
     */
    private static class TraceabilityChain {
        private String specId;
        private String specFile;
        private String planFile;
        private List<String> taskFiles = new ArrayList<>();
        private List<String> implementationFiles = new ArrayList<>();
        
        public TraceabilityChain(String specId) {
            this.specId = specId;
        }
        
        public boolean hasCompleteChain() {
            return specFile != null && 
                   !implementationFiles.isEmpty() &&
                   (planFile != null || !taskFiles.isEmpty());
        }
        
        public List<String> getMissingElements() {
            List<String> missing = new ArrayList<>();
            if (specFile == null) missing.add("Specification");
            if (planFile == null) missing.add("Plan");
            if (taskFiles.isEmpty()) missing.add("Tasks");
            if (implementationFiles.isEmpty()) missing.add("Implementation");
            return missing;
        }
        
        public boolean containsImplementationFile(String file) {
            return implementationFiles.contains(file);
        }
        
        public boolean hasPlan() {
            return planFile != null;
        }
        
        // Getters and setters
        public String getSpecFile() { return specFile; }
        public void setSpecFile(String specFile) { this.specFile = specFile; }
        
        public String getPlanFile() { return planFile; }
        public void setPlanFile(String planFile) { this.planFile = planFile; }
        
        public List<String> getTaskFiles() { return taskFiles; }
        public void addTaskFile(String taskFile) { this.taskFiles.add(taskFile); }
        
        public List<String> getImplementationFiles() { return implementationFiles; }
        public void addImplementationFile(String implementationFile) { 
            this.implementationFiles.add(implementationFile); 
        }
    }
    
    /**
     * Loads all project files
     * @spec: SDD-TRACE-T001 - File loading
     */
    private void loadAllFiles() throws IOException {
        javaFiles = loadFilesWithExtension(SRC_PATH, ".java");
        specFiles = loadFilesWithExtension(SPECS_PATH, ".md");
        planFiles = loadFilesWithExtension(PLANS_PATH, ".md");
        taskFiles = loadFilesWithExtension(TASKS_PATH, ".md");
    }
    
    /**
     * Loads files with specific extension from directory
     * @spec: SDD-TRACE-T001 - File loading helper
     */
    private List<String> loadFilesWithExtension(String directory, String extension) throws IOException {
        Path dir = Paths.get(directory);
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }
        
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(extension))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Builds traceability chains for all specifications
     * @spec: SDD-TRACE-T001 - Traceability chain building
     */
    private void buildTraceabilityChains() throws IOException {
        traceabilityChains = new HashMap<>();
        
        // @spec: SDD-TRACE-T001 - Process specification files
        for (String specFile : specFiles) {
            String specId = extractSpecIdFromFileName(specFile);
            TraceabilityChain chain = new TraceabilityChain(specId);
            chain.setSpecFile(specFile);
            traceabilityChains.put(specId, chain);
        }
        
        // @spec: SDD-TRACE-T001 - Process plan files
        for (String planFile : planFiles) {
            String specId = extractSpecIdFromFileName(planFile);
            if (traceabilityChains.containsKey(specId)) {
                traceabilityChains.get(specId).setPlanFile(planFile);
            }
        }
        
        // @spec: SDD-TRACE-T001 - Process task files
        for (String taskFile : taskFiles) {
            String specId = extractSpecIdFromFileName(taskFile);
            if (traceabilityChains.containsKey(specId)) {
                traceabilityChains.get(specId).addTaskFile(taskFile);
            }
        }
        
        // @spec: SDD-TRACE-T001 - Process implementation files
        for (String javaFile : javaFiles) {
            List<String> specTags = extractSpecTagsFromFile(javaFile);
            for (String tag : specTags) {
                String specId = extractSpecIdFromTag(tag);
                if (traceabilityChains.containsKey(specId)) {
                    traceabilityChains.get(specId).addImplementationFile(javaFile);
                }
            }
        }
    }
    
    // Extraction Helper Methods
    
    private Map<String, List<String>> extractSpecTagsFromCode() throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        
        for (String javaFile : javaFiles) {
            List<String> tags = extractSpecTagsFromFile(javaFile);
            if (!tags.isEmpty()) {
                result.put(javaFile, tags);
            }
        }
        
        return result;
    }
    
    private List<String> extractSpecTagsFromFile(String filePath) throws IOException {
        String content = Files.readString(Paths.get(filePath));
        List<String> tags = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("@spec:\\s*([A-Z0-9-]+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            tags.add(matcher.group(1));
        }
        
        return tags;
    }
    
    private String extractSpecIdFromTag(String tag) {
        return tag.split("-")[0];
    }
    
    private String extractSpecIdFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
    
    private boolean isTestFile(String fileName) {
        return fileName.contains("Test") || fileName.contains("test");
    }
    
    private boolean isConfigurationFile(String fileName) {
        return fileName.contains("Config") || fileName.contains("Application");
    }
    
    private boolean hasSpecTraceabilityTags(String content) {
        return content.contains("@spec:");
    }
    
    private List<String> extractPublicMethods(String content) {
        List<String> methods = new ArrayList<>();
        Pattern pattern = Pattern.compile("public\\s+\\w+\\s+(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            methods.add(matcher.group(1));
        }
        
        return methods;
    }
    
    private List<String> extractTaggedMethods(String content) {
        List<String> taggedMethods = new ArrayList<>();
        String[] lines = content.split("\n");
        
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].contains("@spec:") && 
                lines[i + 1].matches(".*public\\s+\\w+\\s+\\w+\\s*\\(.*")) {
                Pattern pattern = Pattern.compile("public\\s+\\w+\\s+(\\w+)\\s*\\(");
                Matcher matcher = pattern.matcher(lines[i + 1]);
                if (matcher.find()) {
                    taggedMethods.add(matcher.group(1));
                }
            }
        }
        
        return taggedMethods;
    }
    
    private List<String> extractRequirementsFromSpec(String specFile) throws IOException {
        if (specFile == null) return new ArrayList<>();
        
        String content = Files.readString(Paths.get(specFile));
        List<String> requirements = new ArrayList<>();
        
        // Extract acceptance criteria
        Pattern pattern = Pattern.compile("- \\[ \\] (.+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            requirements.add(matcher.group(1).trim());
        }
        
        return requirements;
    }
    
    private boolean hasCorrespondingImplementation(String requirement, TraceabilityChain chain) throws IOException {
        String requirementLower = requirement.toLowerCase();
        
        for (String implementationFile : chain.getImplementationFiles()) {
            String content = Files.readString(Paths.get(implementationFile)).toLowerCase();
            
            // Simple heuristic: check if key words from requirement appear in implementation
            String[] keywords = requirementLower.split("\\s+");
            int matchCount = 0;
            
            for (String keyword : keywords) {
                if (keyword.length() > 3 && content.contains(keyword)) {
                    matchCount++;
                }
            }
            
            // If more than half the keywords match, consider it implemented
            if (matchCount > keywords.length / 2) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isProperlyPlaced(String tag, String content) {
        // Check if tag is placed before a method, class, or field declaration
        int tagIndex = content.indexOf(tag);
        if (tagIndex == -1) return false;
        
        String afterTag = content.substring(tagIndex + tag.length());
        
        // Should be followed by whitespace and then a declaration
        return afterTag.matches("\\s*(public|private|protected|class|interface|@\\w+).*");
    }
    
    private List<String> extractSpecReferencesFromPlan(String planFile) throws IOException {
        String content = Files.readString(Paths.get(planFile));
        List<String> references = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            references.add(matcher.group(1));
        }
        
        return references;
    }
    
    private List<String> extractPlanReferencesFromTask(String taskFile) throws IOException {
        String content = Files.readString(Paths.get(taskFile));
        List<String> references = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            references.add(matcher.group(1));
        }
        
        return references;
    }
}