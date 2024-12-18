package com.cat.clashforsingbox.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ClashForSingbox {

    public void outFile() {
        // 输入和输出文件路径
        String clashConfigPath = "clash.yaml"; // Clash 配置文件路径
        String singBoxConfigPath = "singbox-config.json"; // SingBox 配置文件路径

        try {
            // 读取 Clash 配置
            // 使用 SnakeYAML 加载 YAML 文件
            Yaml yaml = new Yaml();

            // 读取 YAML 文件并将其转换为 Java 对象（Map 或 List）
            FileReader reader = new FileReader(new File(clashConfigPath));

            Object yamlObject = yaml.load(reader);

            // 使用 Jackson 将 Java 对象转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(yamlObject);


            ObjectMapper mapper = new ObjectMapper();
            JsonNode clashConfig = mapper.readTree(jsonContent);

            // 转换为 SingBox 配置
            ObjectNode singBoxConfig = mapper.createObjectNode();
            singBoxConfig.put("log", "info");
            singBoxConfig.put("log_file", "singbox.log");
            singBoxConfig.put("dns", mapper.createObjectNode());
            singBoxConfig.put("route", mapper.createObjectNode());

            // 转换代理部分
            ArrayNode clashProxies = (ArrayNode) clashConfig.get("proxies");
            ArrayNode singBoxProxies = mapper.createArrayNode();
            if (clashProxies != null) {
                for (JsonNode proxy : clashProxies) {
                    ObjectNode singBoxProxy = convertProxy(proxy, mapper);
                    if (singBoxProxy != null) {
                        singBoxProxies.add(singBoxProxy);
                    }
                }
            }
            singBoxConfig.set("outbounds", singBoxProxies);

            // 保存 SingBox 配置
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(singBoxConfigPath), singBoxConfig);
            System.out.println("转换完成，输出文件：" + singBoxConfigPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode convertProxy(JsonNode clashProxy, ObjectMapper mapper) {
        String type = clashProxy.get("type").asText();
        ObjectNode singBoxProxy = mapper.createObjectNode();
        switch (type) {
            case "vmess":
                singBoxProxy.put("type", "vmess");
                singBoxProxy.put("tag", clashProxy.get("name").asText());
                singBoxProxy.put("server", clashProxy.get("server").asText());
                singBoxProxy.put("server_port", clashProxy.get("port").asInt());
                singBoxProxy.put("uuid", clashProxy.get("uuid").asText());
                singBoxProxy.put("alter_id", clashProxy.get("alterId").asInt());
                singBoxProxy.put("cipher", clashProxy.get("cipher").asText());
                return singBoxProxy;

            case "trojan":
                singBoxProxy.put("type", "trojan");
                singBoxProxy.put("tag", clashProxy.get("name").asText());
                singBoxProxy.put("server", clashProxy.get("server").asText());
                singBoxProxy.put("server_port", clashProxy.get("port").asInt());
                singBoxProxy.put("password", clashProxy.get("password").asText());
                return singBoxProxy;

            case "shadowsocks":
                singBoxProxy.put("type", "shadowsocks");
                singBoxProxy.put("tag", clashProxy.get("name").asText());
                singBoxProxy.put("server", clashProxy.get("server").asText());
                singBoxProxy.put("server_port", clashProxy.get("port").asInt());
                singBoxProxy.put("password", clashProxy.get("password").asText());
                singBoxProxy.put("method", clashProxy.get("cipher").asText());
                return singBoxProxy;

            case "hysteria2":
                singBoxProxy.put("type", "hysteria2");
                singBoxProxy.put("tag", clashProxy.get("name").asText());
                singBoxProxy.put("server", clashProxy.get("server").asText());
                singBoxProxy.put("server_port", clashProxy.get("port").asInt());
                singBoxProxy.put("password", clashProxy.get("password").asText());

                return singBoxProxy;

            default:
                System.out.println("不支持的代理类型：" + type);
                return null;
        }
    }
}
