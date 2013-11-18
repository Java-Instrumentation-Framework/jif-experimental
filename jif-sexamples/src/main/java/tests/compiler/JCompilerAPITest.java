/*
 * JCompilerAPITest.java
 */
package tests.compiler;

import java.io.*;

import java.util.*;

import javax.tools.*;


public class JCompilerAPITest {
    public static void main(String[] args) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null,
                null);

        Iterable<?extends JavaFileObject> compilationUnits = 
                fileManager.getJavaFileObjectsFromStrings(Arrays.asList("Hello.java"));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null,
                null, compilationUnits);

        Boolean success = task.call();

        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.printf("Code: %s%n" + "Kind: %s%n" + "Position: %s%n" +
                "Start Position: %s%n" + "End Position: %s%n" + "Source: %s%n" + "Message:  %s%n",
                diagnostic.getCode(), diagnostic.getKind(), diagnostic.getPosition(),
                diagnostic.getStartPosition(), diagnostic.getEndPosition(), diagnostic.getSource(),
                diagnostic.getMessage(null));
        }
        fileManager.close();
        System.out.println("Success: " + success);
    }
}
