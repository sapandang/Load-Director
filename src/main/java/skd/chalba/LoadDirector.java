package skd.chalba;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;
import skd.chalba.dynamicclassloader.ClassFromFile;
import skd.chalba.runner.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * @author sapan.dang
 */
public class LoadDirector {

    static CommandLine cmd;

  public static String currentDirectory = System.getProperty("user.dir");
  public static String extlibDir;

   static HashMap<String, TaskRunner> taskRunnerHashMap = new HashMap<>();

    public static void main(String[] arg) throws Exception {
        Logger.info("LoadDirector Started...");
        Logger.info("Current working directory "+currentDirectory);

        String path = LoadDirector.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        String libPath =  decodedPath.substring(0, path.lastIndexOf("/") + 1);
        Logger.info("executable directory "+libPath);

        //generate extlib dir
        File exlibpath = new File(libPath+"/extlib");
        if(!exlibpath.exists())
        {
            exlibpath.mkdirs();
        }
        extlibDir=exlibpath.getAbsolutePath();

        //loadjars(); //load the jars


        //no arguments:
        if(arg.length == 0)
        {
            // new File(LoadDirector.class.getClassLoader().getResource("help.txt").getFile()) //not working in dist jar
            InputStream helpStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("help.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(helpStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }

        commandParser(arg);
        loadFileAndRun();
        //StartServer(); //TODO : need to change to work as library
        Logger.info("server started.... SERVER MODE is still in alpha stage");

    }

    public static void commandParser(String[] arg){
        try {
            //***Definition Stage***
            // create Options object
            Options options = new Options();

            // add option "-f"
            options.addOption("f", true, "specify java file containing task");
            // add option "-newFile"
            options.addOption("newFile", false, "will generate new template file in the current directory");

            //***Parsing Stage***
            //Create a parser
            CommandLineParser parser = new DefaultParser();

            //parse the options passed as command line arguments
            cmd = parser.parse(options, arg);

            //***Interrogation Stage***
            //hasOptions checks if option is present or not
            if (cmd.hasOption("f")) {
                Logger.info("loading the file " + cmd.getOptionValue("f"));
            }
            if(cmd.hasOption("newFile"))
            {
                Logger.info("Generating new File");
                generateTemplateFile();

            }

        } catch (Exception e) {

            Logger.error(e);
            System.exit(0);
        }

    }

//==============================================================================================
//============================== CMD INVOKERS METHODS ==========================================
//==============================================================================================
    public static void loadFileAndRun() throws Exception
    {

        String taskFile;
        //load the commnad line file
        if(cmd.hasOption("f"))
        {
            taskFile = cmd.getOptionValue("f");
            File file = new File(taskFile);
            Logger.info("file "+file.getAbsolutePath());

            //register the task
            Class taskclass =  ClassFromFile.getClassFromFile(file);
            registerTask(taskclass);
            startTask(taskclass.getName());
        }


    }


    public static void generateTemplateFile() throws IOException {

        System.out.println("Generating template file 'Task1.java' in directory "+currentDirectory);

        //Not working in distjar
        //FileUtils.copyToDirectory(new File(Thread.currentThread().getContextClassLoader().getResource("Task1.java").getPath()), new File(currentDirectory));

        InputStream task1Stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Task1.java");
        byte[] buffer = new byte[task1Stream.available()];
        task1Stream.read(buffer);

        File targetFile = new File(currentDirectory,"Task1.java");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);


        System.out.println("Please rename Task1.java since this file will overridden when called newFile");

    }

//====================================================================
//======================== class LOADER ==============================
//====================================================================

public static void loadjars()
{

    try {

       // ClassFromFile.addSoftwareLibrary(new File("/home/sapan/Documents//GitLoadProject/templateLoad/loadtestscripts/loadgenerator/java_gen/load1/libs/sqlite-jdbc-3.27.2.1.jar"));
        ClassFromFile.loadLibrary(new File("/home/sapan/Documents//GitLoadProject/templateLoad/loadtestscripts/loadgenerator/java_gen/load1/libs/sqlite-jdbc-3.27.2.1.jar"));

        System.out.println("Loading jars");
        String pathToJar = "/home/sapan/Documents//GitLoadProject/templateLoad/loadtestscripts/loadgenerator/java_gen/load1/libs/sqlite-jdbc-3.27.2.1.jar";
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
            System.out.println("Loading class "+className);
        }


    }catch (Exception e)
    {
            e.printStackTrace();
            Logger.error("error loading the jar file");
            Logger.error(e);
    }

}


//=============================================================================
//==================================== SERVER ==================================
//================================================================================

    public static void StartServer() throws Exception
    {
        Server server = new Server(8080);
        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(HomeServlet.class, "/");

        servletHandler.addServletWithMapping(ApiServlet.class, "/api/");
        servletHandler.addServletWithMapping(ApiServlet.class, "/api/tasklist");
        servletHandler.addServletWithMapping(ApiServlet.class, "/api/start");
        servletHandler.addServletWithMapping(ApiServlet.class, "/api/stop");
        servletHandler.addServletWithMapping(ApiServlet.class, "/api/getprops");
        servletHandler.addServletWithMapping(ApiServlet.class, "/api/saveprops");
        servletHandler.addServletWithMapping(JVMStatsServlet.class, "/api/jvmstats");

        server.start();
        server.join();
    }

    public static class HomeServlet extends HttpServlet
    {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(new java.io.File("server/index.html")));
        }
    }


    public static class JVMStatsServlet extends HttpServlet
    {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(JVMStats.getJVMStats());
        }
    }

    public static class ApiServlet extends  HttpServlet
    {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            Logger.info(" "+request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            //send the taskList
            if(request.getServletPath().toLowerCase().equals("/api/tasklist"))
            {
                response.getWriter().println(getTaskDetails());
                return;
            }


            //start the task
            if(request.getServletPath().toLowerCase().equals("/api/start"))
            {
                String taskName = request.getParameter("task");
                Logger.info("starting task name "+taskName);

                if(taskName!=null)
                {

                    response.getWriter().println( startTask(taskName));
                }else {
                    response.getWriter().println("task not found");
                }

                return;
            }


            //start the task
            if(request.getServletPath().toLowerCase().equals("/api/stop"))
            {
                String taskName = request.getParameter("task");
                Logger.info("stopping task name "+taskName);

                if(taskName!=null)
                {

                    response.getWriter().println(stopTask(taskName));
                }else {
                    response.getWriter().println("task not found");
                }

                return;
            }

            //get the properties
            if(request.getServletPath().toLowerCase().equals("/api/getprops"))
            {
                //response.getWriter().println(""+getConfigProperty());

                return;
            }


            response.getWriter().println("404");
        }

        //================================================
        //=========== post method ========================
        //=================================================



        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            Logger.info(" "+request.getServletPath());

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            //save the properties
            if(request.getServletPath().toLowerCase().equals("/api/saveprops"))
            {
                //get the body: JAVA8 only
                String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Logger.info("props received "+requestBody);
                //saveConfigProperty(requestBody);
               // response.getWriter().println(""+getConfigProperty());
                return;
            }


            response.getWriter().println("404");
        }
    }



    public static String registerTask(Class taskClass, int totalThreads, String taskName, long delayBetweenThreads)
    {

            TaskRunner taskRunner = new TaskRunner(taskClass,totalThreads,taskName,delayBetweenThreads);
            taskRunnerHashMap.put(taskName,taskRunner);
            return "taskRegisterd";

    }

    public static void registerTask(Class taskClass) {

        Logger.info("registering task "+taskClass.getName());
        String taskClassName = taskClass.getName();
        //Get the thread count
        ThreadCount threadCount = (ThreadCount) taskClass.getAnnotation(ThreadCount.class);
        int _threadCount =threadCount.value();
        if(!threadCount.fromCsvWithHeaders().equals(""))
        {
            Logger.info("Setting up thread count from the csv file with headers:  "+threadCount.fromCsvWithHeaders());
            Path path = Paths.get(threadCount.fromCsvWithHeaders());
            try {
                _threadCount = Math.toIntExact(Files.lines(path).count());
                _threadCount-=1; //remove the header
            } catch (IOException e) {
                Logger.error("file not fount "+threadCount.fromCsvWithHeaders());
                e.printStackTrace();
                System.exit(0);
            }
        }
        if(!threadCount.fromCsvWithoutHeaders().equals(""))
        {
            Logger.info("Setting up thread count from the csv file without headers:  "+threadCount.fromCsvWithoutHeaders());
            Path path = Paths.get(threadCount.fromCsvWithoutHeaders());
            try {
                _threadCount = Math.toIntExact(Files.lines(path).count());
            } catch (IOException e) {
                Logger.error("File not found "+threadCount.fromCsvWithoutHeaders());
                e.printStackTrace();
                System.exit(0);
            }
        }



       // TaskName taskName = (TaskName) taskClass.getAnnotation(TaskName.class);
        ThreadSpawnDelay threadSpawnDelay = (ThreadSpawnDelay) taskClass.getAnnotation(ThreadSpawnDelay.class);

        Logger.info("threadCount "+threadCount.value());
       // Logger.info("taskName "+taskName.value());
        Logger.info("threadSpawnDelay "+threadSpawnDelay.value());

        TaskRunner taskRunner = new TaskRunner(taskClass,_threadCount,taskClassName,threadSpawnDelay.value());
        taskRunnerHashMap.put(taskClassName,taskRunner);

    }


    public static String  startTask(String taskName)
    {

        try {

            TaskRunner taskRunner = taskRunnerHashMap.get(taskName);
            if(taskRunner!=null) {
                taskRunner.startTasks();
                Logger.info("starting task "+taskName);
                return "taskStarted";
            }else {
                Logger.info("task not found "+taskName);
                return "task not found";
            }

        }catch (Exception e)
        {
            Logger.error(e);
            e.printStackTrace();
            return "taskStartError "+e.getMessage();
        }

    }

    public static String stopTask(String taskName)
    {
        try {
            TaskRunner taskRunner = taskRunnerHashMap.get(taskName);
            if(taskName!=null) {
                taskRunner.stopTasks();
                return "taskStoped";
            }else
            {
                return "task not found";
            }

        }catch (Exception e)
        {
            Logger.error(e);
            e.printStackTrace();
            return "taskStopError "+e.getMessage();
        }
    }


//============================================================================================
//========= SUPPORT FUNCTIONS ================================================================
//============================================================================================

    public static JSONArray getTaskDetails()
    {
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<taskRunnerHashMap.size();i++)
        {
            TaskRunner tmpTaskRunner = taskRunnerHashMap.get(taskRunnerHashMap.keySet().toArray()[i]);
            jsonArray.put(tmpTaskRunner.toJsonObject());
        }
        return jsonArray;
    }

  /*  public static JSONArray getConfigProperty(){

        JSONArray resultArrary = new JSONArray();
        Properties properties = Config.getProps();

        Enumeration sx = properties.propertyNames();
        while (sx.hasMoreElements() )
        {
            JSONObject propsJSONObject = new JSONObject();

            String key = sx.nextElement().toString();

            //Logger.info("=> "+key);

            propsJSONObject.put("name",""+key);
            propsJSONObject.put("value",""+properties.getProperty(key));

            resultArrary.put(propsJSONObject);
        }

        return resultArrary;
    }
*/
/*
    public static void saveConfigProperty(String jsonRequest)
    {
        JSONArray requestJson = new JSONArray(jsonRequest);

        for(int i=0;i<requestJson.length();i++)
        {
           //. Logger.info("=>"+requestJson.getJSONObject(i).get("name"));
            Config.saveProperty(requestJson.getJSONObject(i).get("name").toString(),requestJson.getJSONObject(i).get("value").toString());

        }
        Config.reloadconfigFile();
    }*/


}

