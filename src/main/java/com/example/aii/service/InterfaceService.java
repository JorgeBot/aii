package com.example.aii.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Interface;
import com.example.aii.exception.AiiException;
import com.example.aii.mapper.InterfaceMapper;
import com.example.aii.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.LinkedHashMap;

@Service
public class InterfaceService {

    @Resource
    private InterfaceMapper interfaceMapper;

    @Resource
    private RestTemplate restTemplate;

    public void save(Interface in) {
        interfaceMapper.insert(in);
    }

    public IPage<Interface> findByInterfaceNameLike(String interfaceNameLike, Page<Interface> page) {
        QueryWrapper<Interface> q = new QueryWrapper<>();
        if (interfaceNameLike != null && !interfaceNameLike.equals("")) {
            q.like("interface_name", interfaceNameLike);
        }
        return interfaceMapper.selectPage(page, q);
    }

    public ResponseEntity<String> run(Long interfaceId) throws JsonProcessingException {
        Interface anInterface = interfaceMapper.selectById(interfaceId);
        ResponseEntity<String> response;
        // 请求头初始化
        MultiValueMap<String, String> multiHeaders = new LinkedMultiValueMap<>();
        multiHeaders.setAll(anInterface.getRequestHeaders());
        MediaType contentType = new HttpHeaders(multiHeaders).getContentType();
        RequestEntity<Object> re;
        // 请求体
        if (contentType == null || contentType.getSubtype().equals("x-www-form-urlencoded")) {
            MultiValueMap<String, Object> multiBody = CommonUtil.Map2MultiValueMap((LinkedHashMap<String, Object>) anInterface.getRequestData());
            re = new RequestEntity<>(multiBody, multiHeaders, anInterface.getMethod(), URI.create(anInterface.getUrl()));

        } else {
            ObjectMapper mapper = new ObjectMapper();
            re = new RequestEntity<>(mapper.writeValueAsString(anInterface.getRequestData()), multiHeaders, anInterface.getMethod(), URI.create(anInterface.getUrl()));
        }
        try {
            response = restTemplate.exchange(re, String.class);
        } catch (Exception e) {
            throw new AiiException("请求异常：" + e.getMessage());
        }
        return response;
    }

    public void update(Interface anInterface) {
        interfaceMapper.updateById(anInterface);
    }
}
