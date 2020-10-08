package skd.chalba;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.tinylog.Logger;
import skd.chalba.dynamicclassloader.ClassFromFile;
import skd.chalba.elements.PropReader;
import skd.chalba.interfaces.*;
import skd.chalba.runner.*;
import skd.chalba.server.ReporterSocket;
import spark.Request;
import spark.Response;
import spark.Route;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.NativeString.substring;
import static spark.Spark.*;

/**
 * @author sapan.dang
 */
public class LoadDirector {

    static CommandLine cmd;
    public static ArrayList<String> excludeArrayList;

  public static String currentDirectory = System.getProperty("user.dir");
  public static String extlibDir;


  //Vars
    public static boolean isServerModeEnable = false;
    public static int serverPort = 8080;



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

        if(isServerModeEnable)
        {
            loadFileAndStartServer();
        }else {
            loadFileAndRun();

        }

        //StartServer(); //TODO : need to change to work as library
        Logger.info("server started.... SERVER MODE is still in alpha stage");

        //TODO:test code comment while production
       // StartServer();

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

            options.addOption("s", true, "will start server mode");

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

            if(cmd.hasOption("s"))
            {
                isServerModeEnable=true;
                Logger.info("sarting server mode in port "+cmd.getOptionValue("s"));
                serverPort=Integer.parseInt(cmd.getOptionValue("s"));
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

    public static void loadFileAndStartServer() throws Exception
    {


        String taskFile;
        Class taskclass = null;
        //load the commnad line file
        if(cmd.hasOption("f"))
        {
            taskFile = cmd.getOptionValue("f");
            File file = new File(taskFile);
            Logger.info("file "+file.getAbsolutePath());

            //register the task
             taskclass =  ClassFromFile.getClassFromFile(file);
            registerTask(taskclass);
        }

        //setting up the server
        port(serverPort);

        get("/", (req, res) ->
        {
            return "LOAD DIRECTOR SERVER RUNNING" +
                    "call /start?th=12  : number of threads";
        });

        Class finalTaskclass = taskclass;
        get("/start", (req, res) ->
        {
            String message="";
            String threadquery =  req.queryParams("th");

            message+="Starting threads "+threadquery;

            try {

                TaskRunner taskRunner = taskRunnerHashMap.get(finalTaskclass.getName());
                if(taskRunner!=null) {
                    taskRunner.startTasks(Integer.parseInt(threadquery));
                    Logger.info("starting task "+finalTaskclass.getName());
                    return "taskStarted "+message;
                }else {
                    Logger.info("task not found "+finalTaskclass.getName());
                    return "task not found "+message;
                }

            }catch (Exception e)
            {
                Logger.error(e);
                e.printStackTrace();
                return "taskStartError "+e.getMessage();
            }




        });





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
        Logger.info("Starting server..");
        //setUp server
        port(8080);
        setupRoutes();
        setFilters();
        //webSocket("/echo", ReporterSocket.class);
        //init();

        //setUp exclude path
        excludeArrayList = new ArrayList<>();
        excludeArrayList.add("/auth/");
        excludeArrayList.add("/img");
        excludeArrayList.add("/filters");

    }

    public static void setupRoutes(){

        //setup static files
        staticFiles.externalLocation("assets");

    }

    public static void setFilters()
    {

        //setup CROS
        options("/*", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return "OK";
            }
        });

        //after filter
        after((request, response) -> {

            // Website you wish to allow to connect
            //response.header("Access-Control-Allow-Origin", "http://localhost:8081"); //production
            response.header("Access-Control-Allow-Origin", "*");

            // Request methods you wish to allow
            response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");

            // Request headers you wish to allow
            response.header("Access-Control-Allow-Headers", "X-Requested-With,content-type");

            // Set to true if you need the website to include cookies in the requests sent
            // to the API (e.g. in case you use sessions)
            response.header("Access-Control-Allow-Credentials", "true");


        });

    }







    public static String registerTask(Class taskClass, int totalThreads, String taskName, long delayBetweenThreads)
    {

            TaskRunner taskRunner = new TaskRunner(taskClass,totalThreads,taskName,delayBetweenThreads);
            taskRunnerHashMap.put(taskName,taskRunner);
            return "taskRegisterd";

    }

    public static void registerTask(Class taskClass) throws Exception {

        Logger.info("registering task "+taskClass.getName());
        String taskClassName = taskClass.getName();

        //Get the thread count
        ThreadCount threadCount = (ThreadCount) taskClass.getAnnotation(ThreadCount.class);
        int _threadCount =threadCount.value();

        //csv parameter
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

        //csv parameter
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

        //get run duration
        TimeoutCallback timeoutCallback = (TimeoutCallback) taskClass.getAnnotation(TimeoutCallback.class);

       // TaskName taskName = (TaskName) taskClass.getAnnotation(TaskName.class);
        ThreadSpawnDelay threadSpawnDelay = (ThreadSpawnDelay) taskClass.getAnnotation(ThreadSpawnDelay.class);

        //Read from csv file
        LoadProp loadProp = (LoadProp) taskClass.getAnnotation(LoadProp.class);
        //check if prop file value provided or not
        if(loadProp != null) {
            if (!loadProp.value().equalsIgnoreCase("")) {
                Logger.info("Load thread count from property file " + loadProp.value());
                PropReader propReader = new PropReader(loadProp.value());
                //prop file exist so get the thread count value
                if (!threadCount.fromProp().equalsIgnoreCase("")) {
                    //key has been provided so look for the value
                    String threadCountKey = propReader.getProperty(threadCount.fromProp());
                    _threadCount = Integer.parseInt(threadCountKey);
                }
            }
        }


        //BATCH spawning OF THREADS
        ThreadBatchSpawnDelay threadBatchSpawnDelay = (ThreadBatchSpawnDelay)  taskClass.getAnnotation(ThreadBatchSpawnDelay.class);
        if(threadBatchSpawnDelay != null)
        {

        }



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

    public static String  startTask(Class taskClass)
    {

        String taskName = taskClass.getName();
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

