package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.Interface;
import com.example.aii.exception.AiiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
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

        private static final Pattern methodPattern = Pattern.compile("^(GET|POST|PUT|PATCH|DELETE)");
        private static final Pattern URLPattern = Pattern.compile("\\s(.*)$");
        private static final Pattern headerPattern = Pattern.compile("^(.+):\\s*(.*)$");
        private static final Pattern protocolPattern = Pattern.compile("\\s(\\w+)");
        private static final Pattern hostnamePattern = Pattern.compile("//([a-zA-Z0-9\\.]*)");
        private static final Pattern portPattern = Pattern.compile(":(\\d+)/");
        private static final Pattern pathPattern = Pattern.compile("[^://](/[^?]*)\\??");
        private static final Pattern parameterPattern = Pattern.compile("([^?&=]+)=([^?&=]*)");
        private static final Pattern postFormPattern = Pattern.compile("(\\w+)=(\\w+)?");

        @Override
        protected Interface doForward(@Nonnull InterfaceDTO interfaceDTO) {
            Interface res = new Interface();
            BeanUtils.copyProperties(interfaceDTO, res);
            String requestParams = interfaceDTO.getRequestParams();
            String[] paramArray = requestParams.split("\\n\\n");
            List<String> paramLs = Arrays.stream(paramArray).filter(f -> !StringUtils.isEmpty(f)).collect(Collectors.toList());
            String[] main = paramLs.get(0).split("\\n");
            // general
            generalFormat(res, main[0]);
            // header
            requestHeadersFormat(res, Arrays.copyOfRange(main, 1, main.length));
            // request parameters
            requestParametersFormat(res, paramLs.get(1));
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
                in.setMethod(Interface.METHOD.valueOf(method));
            } else {
                throw new AiiException("格式异常，没有匹配到请求方法");
            }
            Matcher URLMatch = URLPattern.matcher(general);
            if (URLMatch.find()) {
                in.setUrl(URLMatch.group());
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
            Map<String, String> parameters = new HashMap<>();
            while (parameterMatch.find()) {
                parameters.put(parameterMatch.group(1), parameterMatch.group(2));
            }
            in.setParameters(parameters);
        }

        private void requestHeadersFormat(Interface in, String[] headerParams) {
            Map<String, String> headers = new HashMap<>();
            for (String headerParam : headerParams) {
                Matcher headerMatch = headerPattern.matcher(headerParam);
                if (headerMatch.find()) {
                    headers.put(headerMatch.group(1), headerMatch.group(2));
                }
            }
            in.setHeaders(headers);
        }

        private void requestParametersFormat(Interface in, String requestParameters) {
            if (in.getMethod() != Interface.METHOD.GET && requestParameters.matches("^(\\w+)=.*")) {   // post form
                Map<String, String> postForm = new LinkedHashMap<>();
                Matcher postFormMatch = postFormPattern.matcher(requestParameters);
                while (postFormMatch.find()) {
                    postForm.put(postFormMatch.group(1), postFormMatch.group(2));
                }
                in.setBody(postForm);
                in.setDataType(Interface.DATA_TYPE.FORM_DATA);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node;
                try {
                    node = mapper.readTree(requestParameters);
                } catch (JsonProcessingException e) {
                    throw new AiiException("格式化异常，无法转化请求参数");
                }
                if (node.getNodeType().equals(JsonNodeType.ARRAY)) {
                    in.setDataType(Interface.DATA_TYPE.ARRAY);
                    in.setBody(mapper.convertValue(node, List.class));
                } else if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
                    in.setDataType(Interface.DATA_TYPE.JSON_OBJECT);
                    in.setBody(mapper.convertValue(node, LinkedHashMap.class));
                }
            }
        }

    }
}
