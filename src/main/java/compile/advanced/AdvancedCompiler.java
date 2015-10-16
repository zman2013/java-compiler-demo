package compile.advanced;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class AdvancedCompiler {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException, MalformedURLException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //准备FileManager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //生成JavaFileObject
        JavaFileObject file = constructTestor();
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);
        //收集编译代码时遇到的错误提示信息，可选
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
        //生成编译任务
        Iterable options = Arrays.asList("-d", ".");
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, options, null, files);

        Boolean result = task.call();
        //如果存在，输出诊断信息
        collector.getDiagnostics().forEach(diagnostic -> {
            System.out.println("Line Number ->" + diagnostic.getLineNumber());
            System.out.println("Message -> " + diagnostic.getMessage(Locale.CHINESE));
            System.out.println("Source -> " + diagnostic.getCode());
            System.out.println("\n");
        });

        if (result == true) {
            System.out.println("Successed");

            String className = "compile.CalculatorTest";
            URLClassLoader classLoader = new URLClassLoader(new URL[] { new File("./").toURI().toURL() });
            Class<?> clazz = classLoader.loadClass(className);

            Object instance = clazz.newInstance();
            Method method = clazz.getMethod("testMultiply", null);
            method.invoke(instance, null);
        }
    }

    private static JavaFileObject constructTestor() {

        StringBuilder contents = new StringBuilder();
        /* @formatter:off */
        contents
            .append("package compile;")
            .append("public class CalculatorTest{\n")
            .append("    public void testMultiply(){\n")
            .append("        System.out.println(2*4);\n")
            .append("    }\n")
            .append("    public static void main(String[] args){\n")
            .append("        new CalculatorTest().testMultiply();\n")
            .append("    }\n")
            .append("}\n");
        /* @formatter:on */
        StringObject so = null;
        try {
            so = new StringObject(StandardLocation.SOURCE_PATH.getName() + "/compile/CalculatorTest.java",
                    contents.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return so;
    }
}
