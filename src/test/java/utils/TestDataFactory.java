package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ItemDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {
    private static JSONObject testData = new JSONObject();
    private static ObjectMapper mapper = new ObjectMapper();

    public static void loadTestData(){
        try {
            String fileContent = "";
            InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("testdata/"+ConfigUtils.getTestVariable("env")+"Data.json")
                    .openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null){
                fileContent = fileContent.concat(line+"\n");
            }
            br.close();
            in.close();
            testData = new JSONObject(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Object getTestData(String dataType, String dataName){
        switch (dataType) {
            case "listItem":
                List<String> dataList = new ArrayList<>();
                JSONArray dataJSONArray = testData.getJSONArray(dataName);
                for(int i=0; i<dataJSONArray.length(); i++){
                    dataList.add(dataJSONArray.getString(i));
                }
                return dataList;
            case "searchItem":
                try{
                    return mapper.readValue(
                            testData.getJSONObject("searchItem")
                                    .getJSONObject(dataName)
                                    .toString(),
                            ItemDTO.class);
                }catch (Exception e){
                    return null;
                }
            default:
                return null;
        }
    }
}
