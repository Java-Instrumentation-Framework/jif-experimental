/*
 * Main.java
 *
 * Created on May 24, 2007, 1:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.compiler;

public class Main {
    public static void main
            ( final java.lang.String[] args )
            throws java.lang.Throwable {
        java.lang.System.out.printf( "%.02f%n", System.nanoTime() / 1E9 );
        java.lang.System.out.println( eval( "new java.lang.Boolean( true )" ));
        java.lang.System.out.printf( "%.02f%n", System.nanoTime() / 1E9 );
        java.lang.System.out.println( eval( "2 + 3" ));
        java.lang.System.out.printf( "%.02f%n", System.nanoTime() / 1E9 ); }
    
    
    public static boolean writeSource
            ( final java.lang.String sourcePath,
            final java.lang.String sourceCode )
            throws java.io.FileNotFoundException {
        java.io.PrintWriter writer =
                new java.io.PrintWriter( sourcePath );
        writer.println( sourceCode );
        writer.close(); return true; }
    
//    public static boolean compile
//            ( final java.lang.String sourcePath )
//            throws java.io.IOException {
//        final javax.tools.JavaCompiler compiler =
//                javax.tools.ToolProvider.getSystemJavaCompiler();
//        final javax.tools.JavaFileManager manager =
//                compiler.getStandardFileManager(null, null, null);
//        final javax.tools.JavaFileObject source =
//                manager.getFileForInput( sourcePath ); /* java.io.IOException */
//        final javax.tools.JavaCompiler.CompilationTask task =
//                compiler.run( null, source );
//        return task.getResult(); }
    
    public static final java.lang.Class loadExpression
            ( final java.lang.String path, final java.lang.String className )
            throws
            java.net.MalformedURLException,
            java.lang.ClassNotFoundException { /* java.net.MalformedURLException */
        final java.net.URLClassLoader myLoader =
                new java.net.URLClassLoader
                ( new java.net.URL[]{ new java.io.File( path ).toURI().toURL() });
        /* java.lang.ClassNotFoundException */
        return java.lang.Class.forName( className, true, myLoader ); }
    
    public static java.lang.Object evalExpression
            ( final java.lang.Class test )
            throws
            java.lang.NoSuchMethodException,
            java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        final java.lang.Class[] parameterType = null;
        /* java.lang.NoSuchMethodException */
        final java.lang.reflect.Method method =
                test.getMethod( "expression", parameterType );
        final java.lang.Object[] argument = null; Object instance = null;
    /* java.lang.IllegalAccessException,
    java.lang.reflect.InvocationTargetException */
        return method.invoke( instance, argument ); }
    
    public static java.lang.Object eval
            ( final java.lang.String expression )
            throws
            java.io.FileNotFoundException,
            java.io.IOException,
            java.net.MalformedURLException,
            java.lang.ClassNotFoundException,
            java.lang.NoSuchMethodException,
            java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        final java.lang.Object result;
        final java.lang.String path = "c:/";
        final java.lang.String className = "ExpressionWrapper";
        final java.lang.String sourcePath = path + className + ".java";
        writeSource( /* to */ sourcePath,
                "public class " + className + "\n" +
                "{ public static java.lang.Object expression()\n" +
                "  { return " + expression + "; }}\n" );
        if( false) { //compile( sourcePath )) {
            final java.lang.Class class_ =
                    loadExpression( /* from */ path, className );
            result = evalExpression( class_ ); } else { result = null; }
        
        return result; }}