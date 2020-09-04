package com.example.aii.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedHashMap;

@TableName(autoResultMap = true)
public class Interface extends BaseEntity {

    private String interfaceName;
    private Integer timeout;
    private HttpMethod method;
    private String protocol;
    private String version;
    private Integer port;
    private String path;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private LinkedHashMap<String, String> parameters;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private LinkedMultiValueMap<String, String> requestHeaders;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object requestData;
    private String requestParams;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private LinkedMultiValueMap<String, String> responseHeaders;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object responseBody;
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

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LinkedHashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public LinkedMultiValueMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(LinkedMultiValueMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public LinkedMultiValueMap<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(LinkedMultiValueMap<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }
}
