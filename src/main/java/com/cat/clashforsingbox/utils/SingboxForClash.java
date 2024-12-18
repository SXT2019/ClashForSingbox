package com.cat.clashforsingbox.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class SingboxForClash {

    public void outFile(){
        // 输入和输出文件路径
        String singBoxConfigPath = "singbox-config.json"; // SingBox 配置文件路径
        String clashConfigPath = "clash-config.yaml"; // 输出 Clash 配置文件路径

        try {
            // 读取 SingBox 配置
            ObjectMapper mapper = new ObjectMapper();
            JsonNode singBoxConfig = mapper.readTree(new File(singBoxConfigPath));
            System.out.println(singBoxConfig);
            // 创建 Clash 配置
            ObjectNode clashConfig = mapper.createObjectNode();
            ArrayNode proxies = mapper.createArrayNode();
            ArrayNode rules = mapper.createArrayNode(); // 如果有规则需要转化，可在此添加

            // 转换代理配置
            ArrayNode singBoxProxies = (ArrayNode) singBoxConfig.get("outbounds");
            if (singBoxProxies != null) {
                for (JsonNode proxy : singBoxProxies) {
                    ObjectNode clashProxy = convertToClashProxy(proxy, mapper);
                    if (clashProxy != null) {
                        proxies.add(clashProxy);
                    }
                }
            }

            // 填充 Clash 配置
            clashConfig.put("proxies", proxies);
            clashConfig.put("proxy-groups", mapper.createArrayNode()); // 可以根据需要填充
            clashConfig.put("rules", rules);

            Map<String,Object> clashConfigMap = mapper.convertValue(clashConfig, Map.class);

            // 设置 YAML 格式选项
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);  // 设置为块级样式（多行显示）
            Yaml yaml = new Yaml(options);
            // 将 Clash 配置转换为 YAML 并写入到文件
            String yamlToString=yaml.dump(clashConfigMap);
            // 使用 dump 方法输出到文件
            FileWriter fw = new FileWriter(clashConfigPath);
            fw.write(yamlToString);
            fw.flush();
            fw.close();
            System.out.println("转换完成，输出文件：" + clashConfigPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObjectNode convertToClashProxy(JsonNode singBoxProxy, ObjectMapper mapper) {
        String type = singBoxProxy.get("type").asText();
        ObjectNode clashProxy = mapper.createObjectNode();

        switch (type) {
            case "vmess":
                clashProxy.put("type", "vmess");
                clashProxy.put("name", singBoxProxy.get("tag").asText());
                clashProxy.put("server", singBoxProxy.get("server").asText());
                clashProxy.put("port", singBoxProxy.get("server_port").asInt());
                clashProxy.put("uuid", singBoxProxy.get("uuid").asText());
                clashProxy.put("alterId", singBoxProxy.get("alter_id").asInt());
                clashProxy.put("cipher", singBoxProxy.get("cipher").asText());
                return clashProxy;

            case "trojan":
                clashProxy.put("type", "trojan");
                clashProxy.put("name", singBoxProxy.get("tag").asText());
                clashProxy.put("server", singBoxProxy.get("server").asText());
                clashProxy.put("port", singBoxProxy.get("server_port").asInt());
                clashProxy.put("password", singBoxProxy.get("password").asText());
                return clashProxy;

            case "shadowsocks":
                clashProxy.put("type", "shadowsocks");
                clashProxy.put("name", singBoxProxy.get("tag").asText());
                clashProxy.put("server", singBoxProxy.get("server").asText());
                clashProxy.put("port", singBoxProxy.get("server_port").asInt());
                clashProxy.put("password", singBoxProxy.get("password").asText());
                clashProxy.put("cipher", singBoxProxy.get("method").asText());
                return clashProxy;

            case "hysteria2":
                clashProxy.put("type", "hysteria2");
                clashProxy.put("name", singBoxProxy.get("tag").asText());
                clashProxy.put("server", singBoxProxy.get("server").asText());
                clashProxy.put("port", singBoxProxy.get("server_port").asInt());
                clashProxy.put("password", singBoxProxy.get("password").asText());
                return clashProxy;

            default:
                System.out.println("不支持的代理类型：" + type);
                return null;
        }
    }
}
