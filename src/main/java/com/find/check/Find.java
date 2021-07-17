package com.find.check;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.*;

public class Find
{
    public void processFolder(File folder, String configFile, String objectsFile) {
        File[] folderEntries = folder.listFiles();

        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                processFolder(entry, configFile, objectsFile);
                continue;
            }

            System.out.println(entry.getPath());

            // строим дерево и сохраняем его
            FindStatement(entry.getPath(), configFile, objectsFile);

        }
    }
    List<String> objects = new ArrayList<>();
    String cur_object = "";
    String cur_package = "";
    List<String> statements = new ArrayList<>();

    public void FindStatement(String inputFile, String configFile, String objectsFile)

    {
        try
        {
            statements = Files.readAllLines(Paths.get(configFile), Charset.forName("Windows-1251"));

            objects = Files.readAllLines(Paths.get(objectsFile), Charset.forName("Windows-1251"));
            for(int i = 0; i<statements.size(); i++)
                statements.set(i,statements.get(i).trim().toLowerCase());
            for(int i = 0; i<objects.size(); i++)
                objects.set(i,objects.get(i).trim().toLowerCase());
            FileReader reader = new FileReader(inputFile);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);
            FindRecurse(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FindRecurse(JsonElement jsonElement)
    {
        if(jsonElement.isJsonObject())
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            java.util.Iterator iterator = jsonObject.keySet().iterator();
            while(iterator.hasNext())
            {
                Object it = iterator.next();
                boolean f = false;
                if(jsonObject.get(it.toString()).isJsonPrimitive() && jsonObject.get(it.toString()).getAsJsonPrimitive().toString().contains("@"))
                {
                    String s=jsonObject.get(it.toString()).getAsJsonPrimitive().toString().substring(2,jsonObject.get(it.toString()).getAsJsonPrimitive().toString().indexOf("."))+".json";
                    System.out.println("link to "+s+". File structure:");
                    try
                    {
                        FileReader reader = new FileReader(s);
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jo = (JsonObject) jsonParser.parse(reader);
                        FindRecurse1(jo);
                        System.out.println("end of file "+s);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!f)
                {
                    FindRecurse(jsonObject.get(it.toString()));
                }
            }
        }
        else if (jsonElement.isJsonArray())
        {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement je : jsonArray)
            {
                FindRecurse(je);
            }
        }
    }



    public void FindRecurse1(JsonElement jsonElement)
    {
        if(jsonElement.isJsonObject())
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            java.util.Iterator iterator = jsonObject.keySet().iterator();
            while(iterator.hasNext())
            {
                Object it = iterator.next();
                switch(it.toString())
                {
                    case "procedure_body":
                    case "function_body" :
                        JsonArray ja = jsonObject.get(it.toString()).getAsJsonArray();
                        System.out.println("  "+ja.get(0).getAsJsonObject().get("text").toString().substring(1,ja.get(0).getAsJsonObject().get("text").toString().length()-1)+" "+ja.get(1).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().substring(1, ja.get(1).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().length()-1));
                        break;
                    case "procedure_name" :
                    case "function_name" :
                    case "synonym_name":
                    case "trigger_name" :
                    case "tableview_name":
                        JsonArray jar = jsonObject.get(it.toString()).getAsJsonArray();
                        System.out.println("  "+it.toString().substring(0,it.toString().indexOf("_"))+ " " +jar.get(0).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().substring(1, jar.get(0).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().length()-1));
                        break;
                    case "create_package_body" :
                        JsonArray jarr = jsonObject.get(it.toString()).getAsJsonArray();
                        String str = jarr.get(5).getAsJsonObject().get("package_name").getAsJsonArray().get(0).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString();
                        System.out.println("  "+"package "+str.substring(1,str.length()-1));
                        break;
                    case "declare_spec":
                        JsonArray dec = jsonObject.get(it.toString()).getAsJsonArray();
                        System.out.println("  "+dec.get(0).getAsJsonObject().get("text").toString().substring(1,dec.get(0).getAsJsonObject().get("text").toString().length()-1)+" "+dec.get(1).getAsJsonObject().get("variable_declaration").getAsJsonArray().get(1).getAsJsonObject().get("identifier").getAsJsonArray().get(0).getAsJsonObject().get("id_expression").getAsJsonArray().get(0).getAsJsonObject().get("regular_id").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().substring(1, dec.get(1).getAsJsonObject().get("type_spec").getAsJsonArray().get(0).getAsJsonObject().get("datatype").getAsJsonArray().get(0).getAsJsonObject().get("native_datatype_element").getAsJsonArray().get(0).getAsJsonObject().get("text").toString().length()-1));

                        break;
                }
                if(jsonObject.get(it.toString()).isJsonPrimitive() && jsonObject.get(it.toString()).getAsJsonPrimitive().toString().contains("@"))
                {
                    String s=jsonObject.get(it.toString()).getAsJsonPrimitive().toString().substring(2,jsonObject.get(it.toString()).getAsJsonPrimitive().toString().indexOf("."))+".json";
                    System.out.println("link to "+s+". File structure:");
                    try
                    {
                        FileReader reader = new FileReader(s);
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jo = (JsonObject) jsonParser.parse(reader);
                        FindRecurse1(jo);
                        System.out.println("end of file "+s);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                    FindRecurse1(jsonObject.get(it.toString()));

            }
        }
        else if (jsonElement.isJsonArray())
        {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement je : jsonArray)
            {
                FindRecurse1(je);
            }
        }
    }
}
