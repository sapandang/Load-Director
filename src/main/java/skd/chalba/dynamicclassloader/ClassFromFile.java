package skd.chalba.dynamicclassloader;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import net.openhft.compiler.CompilerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.tinylog.Logger;
import skd.chalba.LoadDirector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sapan.dang
 */
public class ClassFromFile {

    public static Class getClassFromFile(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //String className = "chalba."+file.getName().replaceAll(".java","");
        String javaCode = FileUtils.readFileToString(file, "utf-8");


        String className=file.getName().replaceAll(".java","");;
        try{

            CompilationUnit compilationUnit = StaticJavaParser.parse(javaCode);
            Optional<PackageDeclaration> classA = compilationUnit.getPackageDeclaration();

            String packageName  = classA.get().getName().toString();
            className = packageName+"."+file.getName().replaceAll(".java","");

        }catch (Exception e)
        {
            //Lazy to catch exceptions
            Logger.error("File does not contain package name");
            Logger.error(e);
        }


        //add jars to classpath
        File dir = new File("extlib");
        Logger.info("Scanning Path for library "+dir.getAbsolutePath());
        if(dir.exists()) {
            String[] extensions = new String[]{"jar"};
            Logger.info("Loading all the jars from " + dir.getCanonicalPath() + " including those in subdirectories");
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
            for (File filex : files) {
                Logger.info("Loading " + filex.getCanonicalPath());
                boolean stat = CompilerUtils.addClassPath(filex.getCanonicalPath());

            }
        }

        //add jar from executable dir extlib path
        File extlibDir = new File(LoadDirector.extlibDir);
        Logger.info("Scanning Path for library "+extlibDir.getAbsolutePath());
        if(extlibDir.exists()) {
            String[] extensions = new String[]{"jar"};
            Logger.info("Loading all the jars from " + extlibDir.getCanonicalPath() + " including those in subdirectories");
            List<File> files = (List<File>) FileUtils.listFiles(extlibDir, extensions, true);
            for (File filex : files) {
                Logger.info("Loading " + filex.getCanonicalPath());
                boolean stat = CompilerUtils.addClassPath(filex.getCanonicalPath());

            }
        }


       Class taskClass = CompilerUtils.loadFromJava(className, javaCode);
       return taskClass;

    }

    public static void addSoftwareLibrary(File file) throws Exception {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
    }

    public static synchronized void loadLibrary(java.io.File jar)
    {
        try {
            /*We are using reflection here to circumvent encapsulation; addURL is not public*/
            java.net.URLClassLoader loader = (java.net.URLClassLoader)ClassLoader.getSystemClassLoader();
            java.net.URL url = jar.toURI().toURL();
            /*Disallow if already loaded*/
            for (java.net.URL it : java.util.Arrays.asList(loader.getURLs())){
                if (it.equals(url)){
                    return;
                }
            }
            java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{java.net.URL.class});
            method.setAccessible(true); /*promote the method to public access*/
            method.invoke(loader, new Object[]{url});
        } catch (final java.lang.NoSuchMethodException |
                java.lang.IllegalAccessException |
                java.net.MalformedURLException |
                java.lang.reflect.InvocationTargetException e){
             // throw new MyException(e);
        }
    }

}
