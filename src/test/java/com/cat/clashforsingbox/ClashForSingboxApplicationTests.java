package com.cat.clashforsingbox;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cat.clashforsingbox.utils.SingboxForClash;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

@SpringBootTest
class ClashForSingboxApplicationTests {

	@Test
	void contextLoads() {

        /*try {
            FileInputStream fileInputStream=new FileInputStream("D:\\Documents\\IdeaProjects\\ClashForSingbox\\src\\main\\resources\\clash.yaml");
			Yaml yaml=new Yaml();
			Map<String,Object> map=yaml.load(fileInputStream);
            JSON json= JSONObject.parseObject(JSON.toJSONString(map));
            System.out.println(json.toJSONString());
            FileOutputStream fileOutputStream=new FileOutputStream("clash-config.json");
            fileOutputStream.write(json.toJSONString().getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        SingboxForClash singboxForClash = new SingboxForClash();
        singboxForClash.outFile();

    }

}
