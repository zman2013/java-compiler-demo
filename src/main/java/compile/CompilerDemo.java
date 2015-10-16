package compile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CompilerDemo {

    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder fullQuanlifiedFileName = new StringBuilder();
        /* @formatter:off */
        fullQuanlifiedFileName.append("src").append(java.io.File.separator)
            .append("main").append(java.io.File.separator)
            .append("java").append(java.io.File.separator)
            .append("compile").append(java.io.File.separator)
            .append("Target.java");
        /* @formatter:on */

        JavaCompiler compiler = ToolProvider. getSystemJavaCompiler();

        FileOutputStream err = new FileOutputStream("err.txt");

        int compilationResult = compiler.run(null, null, err, fullQuanlifiedFileName.toString());

        if (compilationResult == 0) {
            System.out.println("Done");
        } else {
            System.out.println("Failed");
        }
    }
}
