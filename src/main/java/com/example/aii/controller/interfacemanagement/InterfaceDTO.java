package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.Interface;
import com.example.aii.exception.AiiException;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static class Convert extends Converter<InterfaceDTO, Interface> {
        @Override
        protected Interface doForward(@Nonnull InterfaceDTO interfaceDTO) {
            Interface res = new Interface();
            BeanUtils.copyProperties(interfaceDTO, res);
            String requestParams = interfaceDTO.getRequestParams();
            requestParams.matches("@@@");
            return null;
        }

        @Override
        protected InterfaceDTO doBackward(@Nonnull Interface anInterface) {
            return null;
        }
    }
    private static final Pattern methodPattern = Pattern.compile("\\s(.*)$");
    private static final Pattern URLPattern = Pattern.compile("\\s(.*)$");
    private static final Pattern headerPattern = Pattern.compile("^(.+):\\s*(.*)$");
    private static final Pattern protocolPattern = Pattern.compile("\\s(\\w+)");
    private static final Pattern hostnamePattern = Pattern.compile("//(.*):");
    private static final Pattern portPattern = Pattern.compile(":(\\d+)/");
    private static final Pattern pathPattern = Pattern.compile("[^://](/.*)(\\?.*)?");
    private static final Pattern parameterPattern = Pattern.compile("([^?&=]+)=([^?&=]*)");

//    public static void main(String[] args) {
//        String s = "POST http://127.0.0.1:18081/gateway/datadesign/demand/findDemandPage\n" +
//                "Content-Type: application/x-www-form-urlencoded\n" +
//                "Authorization: Bearer 9fa8e81e-d9e2-4527-99c8-4ac6d543bf50\n" +
//                "\n" +
//                "rows=10&page=1";
//        Map<String, String> headers = new HashMap<>();
//        Map<String, String> parameters = new HashMap<>();
//        String[] ss = s.split("\\n\\n");
//        String[] main = ss[0].split("\\n");
//        // general
//        String general = main[0];
//        Matcher methodMatch = methodPattern.matcher(general);
//        if (methodMatch.find()) {
//            String method = methodMatch.group();
//        } else {
//            throw new AiiException("格式异常，没有匹配到请求方法");
//        }
//        Matcher URLMatch = URLPattern.matcher(general);
//        if (URLMatch.find()) {
//            String URL = URLMatch.group();
//        } else {
//            throw new AiiException("格式化异常，没有匹配到URL");
//        }
//        Matcher protocolMatch = protocolPattern.matcher(general);
//        if (protocolMatch.find()) {
//            String protocol = protocolMatch.group();
//        } else {
//            throw new AiiException("格式化异常，没有匹配到协议");
//        }
//        Matcher hostnameMatch = hostnamePattern.matcher(general);
//        if (hostnameMatch.find()) {
//            String hostname = hostnameMatch.group();
//        } else {
//            throw new AiiException("格式化异常，没有匹配到HOST");
//        }
//        Matcher portMatch = portPattern.matcher(general);
//        if (portMatch.find()) {
//            Integer port = Integer.parseInt(portMatch.group(1));
//        } else {
//            Integer port = 80;
//        }
//        Matcher pathMatch = pathPattern.matcher(general);
//        if (pathMatch.find()) {
//            String path = pathMatch.group(1);
//        } else {
//            throw new AiiException("格式化异常，没有匹配到路径");
//        }
//        Matcher parameterMatch = parameterPattern.matcher(general);
//        while (parameterMatch.find()) {
//            parameters.put(parameterMatch.group(1), parameterMatch.group(2));
//        }
//
//        // header
//        for (int i = 1; i < main.length; i++) {
//            Matcher headerMatch = headerPattern.matcher(main[i]);
//            if (headerMatch.find()) {
//                headers.put(headerMatch.group(1), headerMatch.group(2));
//            }
//        }
//        System.out.println("@@@");
//    }
public static void main(String[] args) {
    Matcher matcher = pathPattern.matcher("POST http://127.0.0.1:18081/gateway/datadesign/demand/findDemandPage?page=10&size=");
    if (matcher.find()) {

        System.out.println(matcher.group(1));
    }

}
}
