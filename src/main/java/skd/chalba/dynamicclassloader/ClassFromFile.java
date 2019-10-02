package skd.chalba.dynamicclassloader;

import net.openhft.compiler.CompilerUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author sapan.dang
 */
public class ClassFromFile {

    public static Class getClassFromFile(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String className = "chalba."+file.getName().replaceAll(".java","");
        String javaCode = FileUtils.readFileToString(file, "utf-8");
        Class taskClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
        return taskClass;


    }

}
