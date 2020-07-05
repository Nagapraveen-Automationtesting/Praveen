package com.flipkart.config;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class BaseClass {

    public JSONObject jso;
    public AppiumDriverLocalService server;

    @BeforeSuite
    public void beforeSuite(){

//        if(!server.isRunning()) {
//            if (!checkIfAppiumServerIsRunnig(4723)) {
//
//                server = getAppiumServer();
//                server.start();
//                server.clearOutPutStreams();
////                logg.info("Appium server started.");
//
//            }
//        }



    }

//    public void beforeClass(){}

//    public void beforeTest(){}

    @BeforeMethod
    public void beforeMethod() throws IOException {
        System.out.println("Before Method");
        BrowserOpen bo = new BrowserOpen();
        bo.OpenBrowser("mobile", "chrome", "https://www.flipkart.com/");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("After Method");
    }

    @AfterSuite
    public void afterSuite(){
        if(server!=null){server.stop();}

    }

    public void getJSONPropertyValue() throws Exception {
        InputStream is = null;
        try {
            String json = "testData.json";
            is =  new FileInputStream(System.getProperty("user.dir")+"\\TestData\\testData.json");
            JSONTokener jtake = new JSONTokener(is);
            jso = new JSONObject(jtake);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService getAppiumServer(){
//		HashMap<String, String> environment = new HashMap<String, String>();
//		environment.put("PATH", "C:\\Program Files\\Java\\jdk1.8.0_231:C:\\Program Files\\Java\\jdk1.8.0_231\\bin:C:\\Users\\user\\AppData\\Local\\Android\\Sdk\\tools");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                        .withAppiumJS(new File("C:\\Users\\user\\node_modules\\appium\\build\\lib\\main.js"))
                        .usingPort(4723)
                        .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//				.withEnvironment(environment)
                        .withLogFile(new File("ServerLogs/server.log"))
        );

    }

    public boolean checkIfAppiumServerIsRunnig(int port){
        boolean IsAppiumServerIsRunning =false;
        ServerSocket socket;
        try{
            socket = new ServerSocket(port);
            socket.close();
        }catch(Exception e){
            System.out.println("1");
            IsAppiumServerIsRunning = true;
        }finally{
            socket =null;
        }
        return IsAppiumServerIsRunning;
    }


}
