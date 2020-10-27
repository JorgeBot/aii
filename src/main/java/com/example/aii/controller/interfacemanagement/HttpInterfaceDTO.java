package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.HttpInterface;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;

public class HttpInterfaceDTO {
    private Long Id;
    private String interfaceName;
    private String host;
    private Integer port;
    private String request;
    private String response;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public HttpInterface toHttpInterface() {
        return new Convert().doForward(this);
    }

    public static class Convert extends Converter<HttpInterfaceDTO, HttpInterface> {
        @Override
        protected HttpInterface doForward(@Nonnull HttpInterfaceDTO httpInterfaceDTO) {
            HttpInterface httpInterface = new HttpInterface();
            BeanUtils.copyProperties(httpInterfaceDTO, httpInterface);
            return httpInterface;
        }

        @Override
        protected HttpInterfaceDTO doBackward(@Nonnull HttpInterface httpInterface) {
            return null;
        }
    }
}
