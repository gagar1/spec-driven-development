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
 * Constitutional Compliance Tests for SDD Framework
 * 
 * @spec: SDD-CONST-T001 - Constitutional compliance validation
 * 
 * This test suite verifies that code follows the architectural laws defined in constitution.md.
 * It serves as an automated enforcement mechanism for the SDD framework's architectural constraints.
 * 
 * Key Validation Areas:
 * 1. Clean Architecture Compliance - Dependency inversion, single responsibility
 * 2. API Design Standards - RESTful principles, naming conventions
 * 3. Data Layer Rules - Repository patterns, JPA compliance
 * 4. Security Requirements - Input validation, authentication patterns
 * 5. Code Quality Gates - Naming conventions, method complexity
 * 
 * @author SDD Framework Validation Team
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SDD Framework - Constitutional Compliance Tests")
public class ConstitutionalComplianceTest {
    
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String SRC_PATH = PROJECT_ROOT + "/src/main/java";
    private static final String CONSTITUTION_PATH = PROJECT_ROOT + "/sdd/framework/constitution.md";
    
    private List<String> javaFiles;
    private String constitutionContent;
    
    @BeforeEach
    void setUp() throws IOException {
        // @spec: SDD-CONST-T001 - Initialize compliance test data
        loadJavaSourceFiles();
        loadConstitutionContent();
    }
    
    /**
     * Test 1: Clean Architecture Compliance
     * Validates dependency inversion and single responsibility principles
     * 
     * @spec: SDD-CONST-T001 - Clean architecture validation
     */
    @Test
    @DisplayName("Code must follow Clean Architecture principles")
    void validateCleanArchitectureCompliance() throws IOException {
        List<String> violations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-CONST-T001 - Check dependency inversion
            if (isControllerFile(fileName) && containsDirectRepositoryInjection(content)) {
                violations.add(String.format(
                    "Dependency Inversion Violation in %s: Controller directly injects Repository (should use Service)", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T001 - Check single responsibility
            if (hasMultipleResponsibilities(content, fileName)) {
                violations.add(String.format(
                    "Single Responsibility Violation in %s: Class appears to have multiple responsibilities", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T001 - Check proper dependency injection
            if (containsNewInstanceCreation(content)) {
                violations.add(String.format(
                    "Dependency Injection Violation in %s: Creating instances with 'new' instead of injection", 
                    fileName));
            }
        }
        
        // @spec: SDD-CONST-T001 - Assert no clean architecture violations
        assertTrue(violations.isEmpty(), 
            "Found Clean Architecture violations:\n" + String.join("\n", violations));
    }
    
    /**
     * Test 2: API Design Standards Compliance
     * Validates RESTful principles and naming conventions
     * 
     * @spec: SDD-CONST-T002 - API design standards validation
     */
    @Test
    @DisplayName("API design must follow RESTful standards")
    void validateApiDesignStandards() throws IOException {
        List<String> violations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            if (!isControllerFile(Paths.get(javaFile).getFileName().toString())) {
                continue;
            }
            
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-CONST-T002 - Check proper HTTP method usage
            if (hasImproperHttpMethodUsage(content)) {
                violations.add(String.format(
                    "HTTP Method Violation in %s: Using wrong HTTP methods for operations", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T002 - Check consistent naming conventions
            if (hasInconsistentNaming(content)) {
                violations.add(String.format(
                    "Naming Convention Violation in %s: Inconsistent naming patterns", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T002 - Check proper error handling
            if (lacksProperErrorHandling(content)) {
                violations.add(String.format(
                    "Error Handling Violation in %s: Missing proper error handling", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T002 - Check input validation
            if (lacksInputValidation(content)) {
                violations.add(String.format(
                    "Input Validation Violation in %s: Missing @Valid annotations", 
                    fileName));
            }
        }
        
        // @spec: SDD-CONST-T002 - Assert no API design violations
        assertTrue(violations.isEmpty(), 
            "Found API Design violations:\n" + String.join("\n", violations));
    }
    
    /**
     * Test 3: Data Layer Rules Compliance
     * Validates repository patterns and JPA compliance
     * 
     * @spec: SDD-CONST-T003 - Data layer compliance validation
     */
    @Test
    @DisplayName("Data layer must follow repository patterns and JPA rules")
    void validateDataLayerCompliance() throws IOException {
        List<String> violations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-CONST-T003 - Check repository pattern usage
            if (isServiceFile(fileName) && containsDirectDatabaseAccess(content)) {
                violations.add(String.format(
                    "Repository Pattern Violation in %s: Direct database access instead of repository", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T003 - Check entity integrity
            if (isEntityFile(fileName) && lacksProperJpaAnnotations(content)) {
                violations.add(String.format(
                    "Entity Integrity Violation in %s: Missing proper JPA annotations", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T003 - Check transaction management
            if (isServiceFile(fileName) && lacksTransactionalAnnotations(content)) {
                violations.add(String.format(
                    "Transaction Management Violation in %s: Missing @Transactional annotations", 
                    fileName));
            }
        }
        
        // @spec: SDD-CONST-T003 - Assert no data layer violations
        assertTrue(violations.isEmpty(), 
            "Found Data Layer violations:\n" + String.join("\n", violations));
    }
    
    /**
     * Test 4: Security Requirements Compliance
     * Validates input sanitization and security patterns
     * 
     * @spec: SDD-CONST-T004 - Security compliance validation
     */
    @Test
    @DisplayName("Code must follow security requirements")
    void validateSecurityCompliance() throws IOException {
        List<String> violations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-CONST-T004 - Check input sanitization
            if (isControllerFile(fileName) && lacksInputSanitization(content)) {
                violations.add(String.format(
                    "Input Sanitization Violation in %s: Missing input validation/sanitization", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T004 - Check sensitive data logging
            if (containsSensitiveDataLogging(content)) {
                violations.add(String.format(
                    "Sensitive Data Violation in %s: Potential logging of sensitive data", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T004 - Check hardcoded credentials
            if (containsHardcodedCredentials(content)) {
                violations.add(String.format(
                    "Hardcoded Credentials Violation in %s: Contains hardcoded credentials", 
                    fileName));
            }
        }
        
        // @spec: SDD-CONST-T004 - Assert no security violations
        assertTrue(violations.isEmpty(), 
            "Found Security violations:\n" + String.join("\n", violations));
    }
    
    /**
     * Test 5: Code Quality Gates Compliance
     * Validates naming conventions and code complexity
     * 
     * @spec: SDD-CONST-T005 - Code quality validation
     */
    @Test
    @DisplayName("Code must meet quality standards")
    void validateCodeQualityStandards() throws IOException {
        List<String> violations = new ArrayList<>();
        
        for (String javaFile : javaFiles) {
            String content = Files.readString(Paths.get(javaFile));
            String fileName = Paths.get(javaFile).getFileName().toString();
            
            // @spec: SDD-CONST-T005 - Check naming conventions
            if (hasInvalidNamingConventions(content)) {
                violations.add(String.format(
                    "Naming Convention Violation in %s: Invalid naming patterns", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T005 - Check method length
            List<String> longMethods = findLongMethods(content);
            if (!longMethods.isEmpty()) {
                violations.add(String.format(
                    "Method Length Violation in %s: Methods exceed 20 lines: %s", 
                    fileName, String.join(", ", longMethods)));
            }
            
            // @spec: SDD-CONST-T005 - Check class size
            if (isClassTooLarge(content)) {
                violations.add(String.format(
                    "Class Size Violation in %s: Class exceeds 200 lines", 
                    fileName));
            }
            
            // @spec: SDD-CONST-T005 - Check documentation
            if (lacksProperDocumentation(content)) {
                violations.add(String.format(
                    "Documentation Violation in %s: Missing Javadoc for public methods", 
                    fileName));
            }
        }
        
        // @spec: SDD-CONST-T005 - Assert no quality violations
        assertTrue(violations.isEmpty(), 
            "Found Code Quality violations:\n" + String.join("\n", violations));
    }
    
    // Helper Methods for Validation
    
    /**
     * Loads all Java source files
     * @spec: SDD-CONST-T001 - Source file loading
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
     * Loads constitution content
     * @spec: SDD-CONST-T001 - Constitution loading
     */
    private void loadConstitutionContent() throws IOException {
        Path constitutionFile = Paths.get(CONSTITUTION_PATH);
        if (Files.exists(constitutionFile)) {
            constitutionContent = Files.readString(constitutionFile);
        } else {
            constitutionContent = "";
        }
    }
    
    // File Type Checkers
    
    private boolean isControllerFile(String fileName) {
        return fileName.contains("Controller");
    }
    
    private boolean isServiceFile(String fileName) {
        return fileName.contains("Service");
    }
    
    private boolean isEntityFile(String fileName) {
        return fileName.contains("Entity") || 
               fileName.equals("User.java") || 
               fileName.matches(".*[A-Z][a-z]+\\.java");
    }
    
    // Clean Architecture Validators
    
    private boolean containsDirectRepositoryInjection(String content) {
        return content.contains("@Autowired") && 
               content.contains("Repository") && 
               content.contains("@RestController");
    }
    
    private boolean hasMultipleResponsibilities(String content, String fileName) {
        // Simple heuristic: check for multiple distinct concerns
        int concernCount = 0;
        if (content.contains("@RestController") || content.contains("@RequestMapping")) concernCount++;
        if (content.contains("@Service") || content.contains("@Component")) concernCount++;
        if (content.contains("@Repository")) concernCount++;
        if (content.contains("@Entity")) concernCount++;
        
        return concernCount > 1;
    }
    
    private boolean containsNewInstanceCreation(String content) {
        Pattern pattern = Pattern.compile("new\\s+[A-Z][a-zA-Z]*\\s*\\(");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String match = matcher.group();
            // Exclude DTOs and exceptions which are OK to create with 'new'
            if (!match.contains("Request") && !match.contains("Response") && 
                !match.contains("Exception") && !match.contains("ArrayList") &&
                !match.contains("HashMap") && !match.contains("Date")) {
                return true;
            }
        }
        return false;
    }
    
    // API Design Validators
    
    private boolean hasImproperHttpMethodUsage(String content) {
        // Check for common anti-patterns
        return content.contains("@GetMapping") && content.contains("create") ||
               content.contains("@PostMapping") && content.contains("delete") ||
               content.contains("@GetMapping") && content.contains("update");
    }
    
    private boolean hasInconsistentNaming(String content) {
        // Check for snake_case in method names (should be camelCase)
        Pattern pattern = Pattern.compile("public\\s+\\w+\\s+\\w*_\\w*\\s*\\(");
        return pattern.matcher(content).find();
    }
    
    private boolean lacksProperErrorHandling(String content) {
        // Check if controller methods don't have proper exception handling
        return content.contains("@RestController") && 
               !content.contains("@ExceptionHandler") && 
               !content.contains("try") &&
               !content.contains("throws");
    }
    
    private boolean lacksInputValidation(String content) {
        // Check for request body parameters without @Valid
        return content.contains("@PostMapping") && 
               content.contains("@RequestBody") && 
               !content.contains("@Valid");
    }
    
    // Data Layer Validators
    
    private boolean containsDirectDatabaseAccess(String content) {
        return content.contains("EntityManager") || 
               content.contains("@Query") || 
               content.contains("createQuery");
    }
    
    private boolean lacksProperJpaAnnotations(String content) {
        return content.contains("class") && 
               !content.contains("@Entity") && 
               !content.contains("@Table") &&
               (content.contains("private Long id") || content.contains("private String"));
    }
    
    private boolean lacksTransactionalAnnotations(String content) {
        return content.contains("@Service") && 
               (content.contains("save") || content.contains("delete") || content.contains("update")) &&
               !content.contains("@Transactional");
    }
    
    // Security Validators
    
    private boolean lacksInputSanitization(String content) {
        return content.contains("@RequestParam") && 
               !content.contains("@Valid") && 
               !content.contains("validate");
    }
    
    private boolean containsSensitiveDataLogging(String content) {
        return content.contains("log") && 
               (content.contains("password") || content.contains("token") || 
                content.contains("secret") || content.contains("key"));
    }
    
    private boolean containsHardcodedCredentials(String content) {
        Pattern pattern = Pattern.compile("(password|secret|key|token)\\s*=\\s*\"[^\"]+\"");
        return pattern.matcher(content.toLowerCase()).find();
    }
    
    // Code Quality Validators
    
    private boolean hasInvalidNamingConventions(String content) {
        // Check for invalid class names (should be PascalCase)
        Pattern classPattern = Pattern.compile("class\\s+[a-z][a-zA-Z]*");
        if (classPattern.matcher(content).find()) return true;
        
        // Check for invalid method names (should be camelCase)
        Pattern methodPattern = Pattern.compile("public\\s+\\w+\\s+[A-Z][a-zA-Z]*\\s*\\(");
        return methodPattern.matcher(content).find();
    }
    
    private List<String> findLongMethods(String content) {
        List<String> longMethods = new ArrayList<>();
        String[] lines = content.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.matches(".*public\\s+\\w+\\s+\\w+\\s*\\(.*")) {
                String methodName = extractMethodName(line);
                int methodLength = countMethodLines(lines, i);
                
                if (methodLength > 20) {
                    longMethods.add(methodName + " (" + methodLength + " lines)");
                }
            }
        }
        
        return longMethods;
    }
    
    private boolean isClassTooLarge(String content) {
        return content.split("\n").length > 200;
    }
    
    private boolean lacksProperDocumentation(String content) {
        // Check if public methods lack Javadoc
        Pattern publicMethodPattern = Pattern.compile("public\\s+\\w+\\s+\\w+\\s*\\(");
        Matcher matcher = publicMethodPattern.matcher(content);
        
        while (matcher.find()) {
            int start = matcher.start();
            String beforeMethod = content.substring(Math.max(0, start - 200), start);
            if (!beforeMethod.contains("/**") && !beforeMethod.contains("@spec:")) {
                return true;
            }
        }
        
        return false;
    }
    
    // Utility Methods
    
    private String extractMethodName(String methodSignature) {
        Pattern pattern = Pattern.compile("\\s+(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(methodSignature);
        return matcher.find() ? matcher.group(1) : "unknown";
    }
    
    private int countMethodLines(String[] lines, int startIndex) {
        int braceCount = 0;
        int lineCount = 0;
        boolean inMethod = false;
        
        for (int i = startIndex; i < lines.length; i++) {
            String line = lines[i];
            lineCount++;
            
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    braceCount++;
                    inMethod = true;
                } else if (c == '}') {
                    braceCount--;
                    if (inMethod && braceCount == 0) {
                        return lineCount;
                    }
                }
            }
        }
        
        return lineCount;
    }
}