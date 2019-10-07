package skd.chalba.dynamicclassloader;

import net.openhft.compiler.CompilerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author sapan.dang
 */
public class ClassFromFile {

    public static Class getClassFromFile(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String className = "chalba."+file.getName().replaceAll(".java","");
        String javaCode = FileUtils.readFileToString(file, "utf-8");


        //add jars to classpath
        File dir = new File("extlib");
        if(dir.exists()) {
            String[] extensions = new String[]{"jar"};
            Logger.info("Loading all the jars from " + dir.getCanonicalPath() + " including those in subdirectories");
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
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
