package skd.test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sapan.dang
 */
public class experiements {



    public static void main(String[] args) throws Exception {


        printClassPath();

        System.out.println("===============================");
        System.out.println("adding custom jars");
        System.out.println("===============================");

        loadLibrary(new File("/home/sapan/Documents//GitLoadProject/templateLoad/loadtestscripts/loadgenerator/java_gen/load1/libs/sqlite-jdbc-3.27.2.1.jar"));
        addSoftwareLibrary(new File("/home/sapan/Documents//GitLoadProject/templateLoad/loadtestscripts/loadgenerator/java_gen/load1/libs/sqlite-jdbc-3.27.2.1.jar"));


        printClassPath();


    }

    private static void addSoftwareLibrary(File file) throws Exception {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
    }


    public static void printClassPath(){
        String jcp=System.getProperty("java.class.path");// $NON-NLS-1$
        String[] bits = jcp.split(File.pathSeparator);
        System.out.println("ClassPath");
        for(String bit : bits){
            System.out.println(bit);
        }

    }


    public  static void printClassPath2()
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            System.out.println(url.getFile());
        }
    }


    public static synchronized void loadLibrary(java.io.File jar) throws InvocationTargetException, MalformedURLException, IllegalAccessException, NoSuchMethodException {
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
        } catch (Exception e){
            throw e;
        }
    }

    @Test
    public void packageName() throws IOException {
        System.out.println("Started");
        String javaCode = FileUtils.readFileToString(new File("Task1.java"), "utf-8");
        String data = javaCode.substring(javaCode.indexOf("package"),javaCode.indexOf(";"));
        System.out.println(data);

    }

    @Test
    public void loadJar3() throws Exception
    {
        String pathToJar = "";
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className);

        }
    }

}
