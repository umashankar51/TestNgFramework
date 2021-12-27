package utils;

import java.io.*;
import java.nio.file.FileSystems;

public class CommonUtils {

    public static String getEndpoint(String resourceName) {
        String generatedEndpoint = ConfigUtils.getEnvVariable("BASE_URL")
                                + ConfigUtils.getEnvVariable(resourceName);
        return generatedEndpoint;
    }

    public static String getFilePath(String fileName) {
        String filePath =  System.getProperty("user.dir")+"//target/test-classes/images/"+fileName;
        System.out.println("filePath: "+filePath);
        if(!System.getProperty("os.name").contains("ux")) {
            filePath = filePath.replaceFirst("/", "");
            filePath = filePath.replaceAll("/","\\\\");
        }
        return filePath;
    }

    public static String readTemplate(String fileName) {
        try {
            String fileContent = "";
            InputStream in = Thread.currentThread()
                            .getContextClassLoader()
                            .getResource("templates/"+fileName)
                            .openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = br.readLine()) != null){
                fileContent = fileContent.concat(line+"\n");
            }
            br.close();
            in.close();
            return fileContent;
            //return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
