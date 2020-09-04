package com.example.aii.unpack;

import com.example.aii.exception.AiiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.collect.Lists;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPUnpack {

    private static final Pattern headerPattern = Pattern.compile("(.+?):\\s*(.*)");
    private static final Pattern methodPattern = Pattern.compile("^GET|HEAD|POST|PUT|PATCH|DELETE|OPTIONS|TRACE");
    private static final Pattern urlPattern = Pattern.compile("(?<=\\s)/.*(?=\\sHTTP)");
    private static final Pattern protocolPattern = Pattern.compile("(?<=\\s)HTTP|HTTPS");
    private static final Pattern versionPattern = Pattern.compile("(?<=[HTTP|HTTPS]/)1.0|1.1|2.0");
    private static final Pattern postFormPattern = Pattern.compile("([^=&]+)=([^=&]*)");

    public static General handleGeneral(String packet) {
        General general = new General();
        Matcher methodMatch = methodPattern.matcher(packet);
        if (methodMatch.find()) {
            general.setMethod(HttpMethod.valueOf(methodMatch.group()));
        } else {
            throw new AiiException("格式异常，没有匹配到请求方法");
        }
        Matcher urlMatch = urlPattern.matcher(packet);
        if (urlMatch.find()) {
            general.setPath(urlMatch.group());
        } else {
            throw new AiiException("格式异常，没有匹配到URL");
        }
        Matcher protocolMatch = protocolPattern.matcher(packet);
        if (protocolMatch.find()) {
            general.setProtocol(protocolMatch.group());
        }
        Matcher versionMatch = versionPattern.matcher(packet);
        if (versionMatch.find()) {
            general.setVersion(versionMatch.group());
        } else {
            throw new AiiException("格式异常，没有匹配到HTTP版本");
        }
        return general;
    }

    public static LinkedMultiValueMap<String, String> handleHeaders(String packet) {
        LinkedMultiValueMap<String, String> multiHeader = new LinkedMultiValueMap<>();
        Matcher headerMatch = headerPattern.matcher(packet);
        while (headerMatch.find()) {
            String k = headerMatch.group(1);
            String v = headerMatch.group(2);
            if (v.contains(",")) {
                multiHeader.put(k, Arrays.asList(v.split(",")));
            } else {
                multiHeader.set(k, v);
            }
        }
        return multiHeader;
    }

    public static Function<String, Object> handleJSONBody = packet -> {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(packet);
        } catch (JsonProcessingException e) {
            throw new AiiException("格式化异常，无法转化请求参数");
        }
        if (node.getNodeType().equals(JsonNodeType.ARRAY)) {    // array
            return mapper.convertValue(node, List.class);
        } else if (node.getNodeType().equals(JsonNodeType.OBJECT)) {    // json
            return mapper.convertValue(node, LinkedHashMap.class);
        } else {
            throw new AiiException("格式化异常，非正确的请求体类型");
        }
    };

    public static Function<String, Object> handleFormBody = packet -> {
        MultiValueMap<String, String> multiBody = new LinkedMultiValueMap<>();
        Matcher postFormMatch = postFormPattern.matcher(packet);
        String key;
        String value;
        while (postFormMatch.find()) {
            key = postFormMatch.group(1);
            value = postFormMatch.group(2);
            if (key.matches(".*\\[\\d+\\]")) {
                key = key.split("\\[")[0];
            }
            multiBody.add(key, value);
        }
        return multiBody;
    };

    public static class General {
        private HttpMethod method;
        private String path;
        private String protocol;
        private String version;

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
