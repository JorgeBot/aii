package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.Interface;
import com.example.aii.exception.AiiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InterfaceDTO {

    private String interfaceName;
    private Integer timeout;
    private String requestParams;
    private String responseParams;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    public Interface toInterface() {
        return new Convert().doForward(this);
    }

    private static class Convert extends Converter<InterfaceDTO, Interface> {

        private static final Pattern methodPattern = Pattern.compile("^(GET|HEAD|POST|PUT|PATCH|DELETE|OPTIONS|TRACE)");
        private static final Pattern URLPattern = Pattern.compile("\\s(.*)$");
        private static final Pattern headerPattern = Pattern.compile("^(.+):\\s*(.*)$");
        private static final Pattern protocolPattern = Pattern.compile("\\s(\\w+)");
        private static final Pattern hostnamePattern = Pattern.compile("//([a-zA-Z0-9\\.]*)");
        private static final Pattern portPattern = Pattern.compile(":(\\d+)/");
        private static final Pattern pathPattern = Pattern.compile("[^://](/[^?]*)\\??");
        private static final Pattern parameterPattern = Pattern.compile("([^?&=]+)=([^?&=]*)");
        private static final Pattern postFormPattern = Pattern.compile("([^=&]+)=([^=&]*)");

        @Override
        protected Interface doForward(@Nonnull InterfaceDTO interfaceDTO) {
            Interface res = new Interface();
            BeanUtils.copyProperties(interfaceDTO, res);
            String requestParams = interfaceDTO.getRequestParams();
            String responseParams = interfaceDTO.getResponseParams();
            // 拆解requestParams
            if (!StringUtils.isEmpty(requestParams)) {
                List<String> paramLs = Arrays.stream(requestParams.split("\\n\\n"))
                        .filter(f -> !StringUtils.isEmpty(f))   // 去除多余的换行符
                        .collect(Collectors.toList());
                String[] main = paramLs.get(0).split("\\n");
                // general
                String general = main[0];
                // request headers
                String[] headers = Arrays.copyOfRange(main, 1, main.length);
                // request parameters
                String requestParameter = paramLs.size() == 2 ? paramLs.get(1) : null;
                // general转换
                generalFormat(res, general);
                // headers转换
                res.setRequestHeaders(headersFormat(headers));
                // request parameters转换
                if (requestParameter != null) {
                    // 跳过GET HEAD OPTIONS
                    if (!(res.getMethod().equals(HttpMethod.GET)
                            || res.getMethod().equals(HttpMethod.HEAD)
                            || res.getMethod().equals(HttpMethod.OPTIONS))) {
                        res.setRequestData(parametersFormat(res.getRequestHeaders(), requestParameter));
                    }
                }
            }
            // 拆解responseParams
            if (!StringUtils.isEmpty(responseParams)) {
                List<String> paramLs = Arrays.stream(responseParams.split("\\n\\n"))
                        .filter(f -> !StringUtils.isEmpty(f))   // 去除多余的换行符
                        .collect(Collectors.toList());
                String[] headers = paramLs.get(0).split("\\n");
                String responseBody =  paramLs.size() == 2 ? paramLs.get(1) : null;

                if (headers.length != 0) {
                    res.setResponseHeaders(headersFormat(headers));
                }
                if (!StringUtils.isEmpty(responseBody)) {
                    res.setResponseBody(parametersFormat(res.getResponseHeaders(), responseBody));
                }
            }
            return res;
        }

        @Override
        protected InterfaceDTO doBackward(@Nonnull Interface anInterface) {
            return null;
        }

        private void generalFormat(Interface in, String general) {
            Matcher methodMatch = methodPattern.matcher(general);
            if (methodMatch.find()) {
                String method = methodMatch.group(1);
                in.setMethod(HttpMethod.valueOf(method));
            } else {
                throw new AiiException("格式异常，没有匹配到请求方法");
            }
            Matcher URLMatch = URLPattern.matcher(general);
            if (URLMatch.find()) {
                in.setUrl(URLMatch.group(1));
            } else {
                throw new AiiException("格式化异常，没有匹配到URL");
            }
            Matcher protocolMatch = protocolPattern.matcher(general);
            if (protocolMatch.find()) {
                in.setProtocol(protocolMatch.group(1));
            } else {
                throw new AiiException("格式化异常，没有匹配到协议");
            }
            Matcher hostnameMatch = hostnamePattern.matcher(general);
            if (hostnameMatch.find()) {
                in.setHostname(hostnameMatch.group(1));
            } else {
                throw new AiiException("格式化异常，没有匹配到HOST");
            }
            Matcher portMatch = portPattern.matcher(general);
            if (portMatch.find()) {
                in.setPort(Integer.parseInt(portMatch.group(1)));
            } else {
                in.setPort(80);
            }
            Matcher pathMatch = pathPattern.matcher(general);
            if (pathMatch.find()) {
                in.setPath(pathMatch.group(1));
            } else {
                in.setPath("/");
            }
            Matcher parameterMatch = parameterPattern.matcher(general);
            LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
            while (parameterMatch.find()) {
                parameters.put(parameterMatch.group(1), parameterMatch.group(2));
            }
            in.setParameters(parameters);
        }

        private LinkedHashMap<String, String> headersFormat(String[] headerParams) {
            LinkedHashMap<String, String> headers = new LinkedHashMap<>();
            for (String headerParam : headerParams) {
                Matcher headerMatch = headerPattern.matcher(headerParam);
                if (headerMatch.find()) {
                    headers.put(headerMatch.group(1), headerMatch.group(2));
                }
            }
            return headers;
        }

        private Object parametersFormat(LinkedHashMap<String, String> headers, String requestParameters) {
            LinkedMultiValueMap<String, String> multiMp = new LinkedMultiValueMap<>();
            multiMp.setAll(headers);
            MediaType contentType = new HttpHeaders(multiMp).getContentType();
            String baseType = "x-www-form-urlencoded";
            Charset charset = StandardCharsets.UTF_8;
            if (contentType != null) {
                baseType = contentType.getSubtype();
                charset = contentType.getCharset() == null ? charset : contentType.getCharset();
            }
            try {
                requestParameters = URLDecoder.decode(requestParameters, charset.toString());
            } catch (UnsupportedEncodingException e) {
                throw new AiiException("格式化异常，参数无法进行转码");
            }

            switch (baseType) {
                case "x-www-form-urlencoded": {
                    return formString2LinkedHashMap(requestParameters);
                }
                case "json": {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node;
                    try {
                        node = mapper.readTree(requestParameters);
                    } catch (JsonProcessingException e) {
                        throw new AiiException("格式化异常，无法转化请求参数");
                    }
                    if (node.getNodeType().equals(JsonNodeType.ARRAY)) {    // array
                        return mapper.convertValue(node, List.class);
                    } else if (node.getNodeType().equals(JsonNodeType.OBJECT)) {    // json
                        return mapper.convertValue(node, LinkedHashMap.class);
                    }
                }
            }
            return null;
        }

        private LinkedHashMap<String, Object> formString2LinkedHashMap(String requestParameters) {
            LinkedHashMap<String, Object> postForm = new LinkedHashMap<>();
            Matcher postFormMatch = postFormPattern.matcher(requestParameters);
            String key;
            String value;
            while (postFormMatch.find()) {
                key = postFormMatch.group(1);
                value = postFormMatch.group(2);
                if (key.matches(".*\\[\\d+\\]")) {
                    key = key.split("\\[")[0];
                    List<String> valueLs = (List<String>) postForm.putIfAbsent(key, Lists.newArrayList(value));
                    if (valueLs != null) valueLs.add(value);
                } else {
                    postForm.put(key, value);
                }
            }
            return postForm;
        }
    }
}
