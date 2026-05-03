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
 * Hallucination Detection Tests for SDD Framework
 * 
 * @spec: SDD-HAL-T001 - Hallucination detection and prevention
 * 
 * This test suite catches AI-invented patterns, non-existent APIs, and inconsistent implementations
 * that commonly occur when AI agents "hallucinate" or make up code patterns that don't exist
 * in the actual specifications or established frameworks.
 * 
 * Key Detection Areas:
 * 1. Non-existent API Patterns - Detects made-up Spring Boot patterns
 * 2. Inconsistent Implementation - Finds code that doesn't match specifications
 * 3. Invalid Framework Usage - Catches incorrect use of Spring annotations
 * 4. Made-up Dependencies - Identifies references to non-existent libraries
 * 5. Specification Drift - Detects implementations that deviate from specs
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Hallucination Detection Tests")
public class HallucinationDetectionTest {
    
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String SRC_PATH = PROJECT_ROOT + "/src/main/java";
    private static final String SPECS_PATH = PROJECT_ROOT + "/sdd/specs";
    private static final String BUILD_FILE = PROJECT_ROOT + "/build.gradle";
    
    private List<String> javaFiles;
    private List<String> specFiles;
    private Set<String> declaredDependencies;
    
    @BeforeEach
    void setUp() throws IOException {
        // @spec: SDD-HAL-T001 - Initialize hallucination detection data
        loadJavaSourceFiles();
        loadSpecificationFiles();
        loadDeclaredDependencies();
    }
    
    /**
     * Test 1: Non-existent API Pattern Detection
     * Detects AI-invented Spring Boot patterns that don't exist
     * 
     * @spec: SDD-HAL-T001 - Non-existent API pattern detection
     */
    @Test
    @DisplayName("Detect AI-invented Spring Boot patterns")
    void detectNonExistentApiPatterns() throws IOException {
        // @spec: SDD-HAL-T001 - Define known hallucination patterns
        Map<String, String> hallucinationPatterns = new HashMap<>();
        
        // Common AI hallucinations in Spring Boot
        hallucinationPatterns.put("@AutoController", "Non-existent annotation (should be @RestController)");
        hallucinationPatterns.put("@DatabaseService", "Non-existent annotation (should be @Service)");
        hallucinationPatterns.put("@APIMapping", "Non-existent annotation (should be @RequestMapping)");
        hallucinationPatterns.put("@JSONResponse", "Non-existent annotation (should be @ResponseBody)");
        hallucinationPatterns.put("@ValidateInput", "Non-existent annotation (should be @Valid)");
        hallucinationPatterns.put("@DatabaseEntity", "Non-existent annotation (should be @Entity)");
        hallucinationPatterns.put("@SpringComponent", "Non-existent annotation (should be @Component)");
        hallucinationPatterns.put("@RestEndpoint", "Non-existent annotation (should be @RequestMapping)");
        hallucinationPatterns.put("@HttpMethod", "Non-existent annotation (should be @GetMapping, @PostMapping, etc.)");
        hallucinationPatterns.put("@DataRepository", "Non-existent annotation (should be @Repository)");
        
        // Invalid method patterns
        hallucinationPatterns.put("ResponseEntity<Object>", "Generic Object response (should use specific DTOs)");
        hallucinationPatterns.put(".findByAll\\(\\)", "Non-existent method (should be findAll())");
        hallucinationPatterns.put(".saveEntity\\(", "Non-existent method (should be save())");
        hallucinationPatterns.put(".deleteEntity\\(", "Non-existent method (should be delete() or deleteById())");
        hallucinationPatterns.put(".updateEntity\\(", "Non-existent method (should be save() for updates)");
        
        List<String> detectedHallucinations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            for (Map.Entry<String, String> pattern : hallucinationPatterns.entrySet()) {
                Pattern regex = Pattern.compile(pattern.getKey());
                Matcher matcher = regex.matcher(content);
                
                if (matcher.find()) {
                    detectedHallucinations.add(String.format(
                        "HALLUCINATION in %s: %s - %s", 
                        fileName, pattern.getKey(), pattern.getValue()));
                }
            }
        }
        
        // @spec: SDD-HAL-T001 - Assert no hallucinated patterns found
        assertTrue(detectedHallucinations.isEmpty(), 
            "Detected AI hallucination patterns:\n" + String.join("\n", detectedHallucinations));
    }
    
    /**
     * Test 2: Invalid Framework Usage Detection
     * Catches incorrect use of Spring annotations and patterns
     * 
     * @spec: SDD-HAL-T002 - Invalid framework usage detection
     */
    @Test
    @DisplayName("Detect incorrect Spring Framework usage")
    void detectInvalidFrameworkUsage() throws IOException {
        List<String> frameworkViolations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-HAL-T002 - Check for invalid annotation combinations
            if (hasInvalidAnnotationCombinations(content)) {
                frameworkViolations.add(String.format(
                    "Invalid annotation combination in %s", fileName));
            }
            
            // @spec: SDD-HAL-T002 - Check for incorrect HTTP method usage
            if (hasIncorrectHttpMethodUsage(content)) {
                frameworkViolations.add(String.format(
                    "Incorrect HTTP method usage in %s", fileName));
            }
            
            // @spec: SDD-HAL-T002 - Check for invalid repository methods
            if (hasInvalidRepositoryMethods(content)) {
                frameworkViolations.add(String.format(
                    "Invalid repository method patterns in %s", fileName));
            }
            
            // @spec: SDD-HAL-T002 - Check for incorrect dependency injection
            if (hasIncorrectDependencyInjection(content)) {
                frameworkViolations.add(String.format(
                    "Incorrect dependency injection patterns in %s", fileName));
            }
        }
        
        // @spec: SDD-HAL-T002 - Assert no framework violations found
        assertTrue(frameworkViolations.isEmpty(), 
            "Detected invalid Spring Framework usage:\n" + String.join("\n", frameworkViolations));
    }
    
    /**
     * Test 3: Made-up Dependencies Detection
     * Identifies references to non-existent libraries or imports
     * 
     * @spec: SDD-HAL-T003 - Made-up dependencies detection
     */
    @Test
    @DisplayName("Detect references to non-existent dependencies")
    void detectMadeUpDependencies() throws IOException {
        List<String> invalidDependencies = new ArrayList<>();
        
        // @spec: SDD-HAL-T003 - Define known invalid imports
        Set<String> commonHallucinatedImports = Set.of(
            "org.springframework.web.bind.annotation.AutoController",
            "org.springframework.data.jpa.repository.DatabaseRepository",
            "org.springframework.boot.autoconfigure.DatabaseService",
            "org.springframework.web.bind.annotation.JSONMapping",
            "org.springframework.validation.ValidateInput",
            "org.springframework.stereotype.DatabaseComponent",
            "org.springframework.web.bind.annotation.RestEndpoint",
            "org.springframework.data.domain.PageableResponse",
            "org.springframework.http.ResponseEntity.Builder",
            "org.springframework.boot.context.DatabaseConfiguration"
        );
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-HAL-T003 - Check for hallucinated imports
            for (String invalidImport : commonHallucinatedImports) {
                if (content.contains(invalidImport)) {
                    invalidDependencies.add(String.format(
                        "Invalid import in %s: %s", fileName, invalidImport));
                }
            }
            
            // @spec: SDD-HAL-T003 - Check for undefined custom annotations
            List<String> customAnnotations = extractCustomAnnotations(content);
            for (String annotation : customAnnotations) {
                if (!isValidCustomAnnotation(annotation)) {
                    invalidDependencies.add(String.format(
                        "Undefined custom annotation in %s: %s", fileName, annotation));
                }
            }
        }
        
        // @spec: SDD-HAL-T003 - Assert no invalid dependencies found
        assertTrue(invalidDependencies.isEmpty(), 
            "Detected references to non-existent dependencies:\n" + String.join("\n", invalidDependencies));
    }
    
    /**
     * Test 4: Specification Drift Detection
     * Detects implementations that deviate from specifications
     * 
     * @spec: SDD-HAL-T004 - Specification drift detection
     */
    @Test
    @DisplayName("Detect implementation drift from specifications")
    void detectSpecificationDrift() throws IOException {
        List<String> specificationDrifts = new ArrayList<>();
        
        // @spec: SDD-HAL-T004 - Load specification requirements
        Map<String, Set<String>> specRequirements = extractSpecificationRequirements();
        Map<String, Set<String>> implementedFeatures = extractImplementedFeatures();
        
        for (Map.Entry<String, Set<String>> entry : specRequirements.entrySet()) {
            String specId = entry.getKey();
            Set<String> requiredFeatures = entry.getValue();
            Set<String> implemented = implementedFeatures.getOrDefault(specId, new HashSet<>());
            
            // @spec: SDD-HAL-T004 - Check for extra features not in spec
            Set<String> extraFeatures = new HashSet<>(implemented);
            extraFeatures.removeAll(requiredFeatures);
            
            for (String extraFeature : extraFeatures) {
                if (isSignificantFeature(extraFeature)) {
                    specificationDrifts.add(String.format(
                        "Specification drift in %s: Implemented '%s' not in specification", 
                        specId, extraFeature));
                }
            }
            
            // @spec: SDD-HAL-T004 - Check for missing required features
            Set<String> missingFeatures = new HashSet<>(requiredFeatures);
            missingFeatures.removeAll(implemented);
            
            for (String missingFeature : missingFeatures) {
                specificationDrifts.add(String.format(
                    "Specification drift in %s: Missing required feature '%s'", 
                    specId, missingFeature));
            }
        }
        
        // @spec: SDD-HAL-T004 - Assert no specification drift found
        assertTrue(specificationDrifts.isEmpty(), 
            "Detected specification drift:\n" + String.join("\n", specificationDrifts));
    }
    
    /**
     * Test 5: Inconsistent Implementation Detection
     * Finds code patterns that are inconsistent within the codebase
     * 
     * @spec: SDD-HAL-T005 - Inconsistent implementation detection
     */
    @Test
    @DisplayName("Detect inconsistent implementation patterns")
    void detectInconsistentImplementations() throws IOException {
        List<String> inconsistencies = new ArrayList<>();
        
        // @spec: SDD-HAL-T005 - Collect implementation patterns
        Map<String, Set<String>> patternsByType = new HashMap<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-HAL-T005 - Collect error handling patterns
            Set<String> errorPatterns = extractErrorHandlingPatterns(content);
            patternsByType.computeIfAbsent("ErrorHandling", k -> new HashSet<>()).addAll(errorPatterns);
            
            // @spec: SDD-HAL-T005 - Collect validation patterns
            Set<String> validationPatterns = extractValidationPatterns(content);
            patternsByType.computeIfAbsent("Validation", k -> new HashSet<>()).addAll(validationPatterns);
            
            // @spec: SDD-HAL-T005 - Collect response patterns
            Set<String> responsePatterns = extractResponsePatterns(content);
            patternsByType.computeIfAbsent("Response", k -> new HashSet<>()).addAll(responsePatterns);
        }
        
        // @spec: SDD-HAL-T005 - Check for inconsistent patterns
        for (Map.Entry<String, Set<String>> entry : patternsByType.entrySet()) {
            String patternType = entry.getKey();
            Set<String> patterns = entry.getValue();
            
            if (patterns.size() > 2) { // Allow some variation, but not too much
                inconsistencies.add(String.format(
                    "Inconsistent %s patterns found: %s", 
                    patternType, String.join(", ", patterns)));
            }
        }
        
        // @spec: SDD-HAL-T005 - Assert consistent implementations
        assertTrue(inconsistencies.isEmpty(), 
            "Detected inconsistent implementation patterns:\n" + String.join("\n", inconsistencies));
    }
    
    // Helper Methods
    
    /**
     * Loads all Java source files
     * @spec: SDD-HAL-T001 - Source file loading
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
     * Loads all specification files
     * @spec: SDD-HAL-T001 - Spec file loading
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
     * Loads declared dependencies from build file
     * @spec: SDD-HAL-T001 - Dependency loading
     */
    private void loadDeclaredDependencies() throws IOException {
        declaredDependencies = new HashSet<>();
        
        if (Files.exists(Paths.get(BUILD_FILE))) {
            String buildContent = Files.readString(Paths.get(BUILD_FILE));
            Pattern pattern = Pattern.compile("implementation\\s+'([^']+)'");
            Matcher matcher = pattern.matcher(buildContent);
            
            while (matcher.find()) {
                declaredDependencies.add(matcher.group(1));
            }
        }
    }
    
    // Validation Helper Methods
    
    private boolean hasInvalidAnnotationCombinations(String content) {
        // Check for conflicting annotations
        return (content.contains("@RestController") && content.contains("@Controller")) ||
               (content.contains("@Service") && content.contains("@Component")) ||
               (content.contains("@Repository") && content.contains("@Component"));
    }
    
    private boolean hasIncorrectHttpMethodUsage(String content) {
        // Check for semantic mismatches
        return (content.contains("@GetMapping") && content.contains("create")) ||
               (content.contains("@GetMapping") && content.contains("delete")) ||
               (content.contains("@PostMapping") && content.contains("get") && !content.contains("getAll"));
    }
    
    private boolean hasInvalidRepositoryMethods(String content) {
        // Check for non-existent JPA repository methods
        Pattern pattern = Pattern.compile("\\.(findByAll|saveEntity|deleteEntity|updateEntity)\\(");
        return pattern.matcher(content).find();
    }
    
    private boolean hasIncorrectDependencyInjection(String content) {
        // Check for field injection instead of constructor injection
        return content.contains("@Autowired") && 
               content.contains("private") && 
               !content.contains("@RequiredArgsConstructor") &&
               !content.contains("public.*\\(.*\\)");
    }
    
    private List<String> extractCustomAnnotations(String content) {
        List<String> annotations = new ArrayList<>();
        Pattern pattern = Pattern.compile("@([A-Z][a-zA-Z]+)");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String annotation = matcher.group(1);
            if (!isStandardSpringAnnotation(annotation)) {
                annotations.add(annotation);
            }
        }
        
        return annotations;
    }
    
    private boolean isStandardSpringAnnotation(String annotation) {
        Set<String> standardAnnotations = Set.of(
            "RestController", "Controller", "Service", "Repository", "Component",
            "Autowired", "RequestMapping", "GetMapping", "PostMapping", "PutMapping", "DeleteMapping",
            "RequestBody", "RequestParam", "PathVariable", "Valid", "Entity", "Table", "Id",
            "GeneratedValue", "Column", "Transactional", "Override", "Test", "DisplayName"
        );
        return standardAnnotations.contains(annotation);
    }
    
    private boolean isValidCustomAnnotation(String annotation) {
        // Check if custom annotation is defined in the project
        // For this demo, we'll assume no custom annotations are valid
        return false;
    }
    
    private Map<String, Set<String>> extractSpecificationRequirements() throws IOException {
        Map<String, Set<String>> requirements = new HashMap<>();
        
        for (String specFile : specFiles) {
            String content = Files.readString(Paths.get(specFile));
            String specId = extractSpecIdFromFileName(specFile);
            
            Set<String> specRequirements = new HashSet<>();
            
            // Extract API endpoints from specs
            Pattern endpointPattern = Pattern.compile("(POST|GET|PUT|DELETE)\\s+(/[^\\s]+)");
            Matcher matcher = endpointPattern.matcher(content);
            
            while (matcher.find()) {
                specRequirements.add(matcher.group(1) + " " + matcher.group(2));
            }
            
            requirements.put(specId, specRequirements);
        }
        
        return requirements;
    }
    
    private Map<String, Set<String>> extractImplementedFeatures() throws IOException {
        Map<String, Set<String>> features = new HashMap<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String specId = extractSpecIdFromContent(content);
            
            if (specId != null) {
                Set<String> implementedFeatures = new HashSet<>();
                
                // Extract HTTP mappings
                Pattern mappingPattern = Pattern.compile("@(Get|Post|Put|Delete)Mapping\\(\"([^\"]+)\"");
                Matcher matcher = mappingPattern.matcher(content);
                
                while (matcher.find()) {
                    implementedFeatures.add(matcher.group(1).toUpperCase() + " " + matcher.group(2));
                }
                
                features.put(specId, implementedFeatures);
            }
        }
        
        return features;
    }
    
    private boolean isSignificantFeature(String feature) {
        // Filter out minor implementation details
        return !feature.contains("toString") && 
               !feature.contains("equals") && 
               !feature.contains("hashCode") &&
               !feature.contains("getter") &&
               !feature.contains("setter");
    }
    
    private Set<String> extractErrorHandlingPatterns(String content) {
        Set<String> patterns = new HashSet<>();
        
        if (content.contains("@ExceptionHandler")) patterns.add("ExceptionHandler");
        if (content.contains("try-catch")) patterns.add("TryCatch");
        if (content.contains("throws")) patterns.add("ThrowsDeclaration");
        
        return patterns;
    }
    
    private Set<String> extractValidationPatterns(String content) {
        Set<String> patterns = new HashSet<>();
        
        if (content.contains("@Valid")) patterns.add("ValidAnnotation");
        if (content.contains("@NotNull")) patterns.add("NotNullAnnotation");
        if (content.contains("validate")) patterns.add("ManualValidation");
        
        return patterns;
    }
    
    private Set<String> extractResponsePatterns(String content) {
        Set<String> patterns = new HashSet<>();
        
        if (content.contains("ResponseEntity")) patterns.add("ResponseEntity");
        if (content.contains("@ResponseBody")) patterns.add("ResponseBody");
        if (content.contains("return new")) patterns.add("DirectReturn");
        
        return patterns;
    }
    
    private String extractSpecIdFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("([A-Z0-9]+)-");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
    
    private String extractSpecIdFromContent(String content) {
        Pattern pattern = Pattern.compile("@spec:\\s*([A-Z0-9]+)");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }
}