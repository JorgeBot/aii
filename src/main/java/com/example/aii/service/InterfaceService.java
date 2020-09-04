package com.example.aii.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Interface;
import com.example.aii.exception.AiiException;
import com.example.aii.mapper.InterfaceMapper;
import com.example.aii.util.CommonUtils;
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
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
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
        MediaType contentType = new HttpHeaders(anInterface.getRequestHeaders()).getContentType();
        RequestEntity<Object> re;
        // 请求体
        URI uri = URI.create(anInterface.getRequestHeaders().getFirst("Host") + anInterface.getPath());
        if (contentType == null || contentType.getSubtype().equals("x-www-form-urlencoded")) {
            re = new RequestEntity<>(anInterface.getRequestData(), anInterface.getRequestHeaders(), anInterface.getMethod(), uri);

        } else {
            ObjectMapper mapper = new ObjectMapper();
            re = new RequestEntity<>(mapper.writeValueAsString(anInterface.getRequestData()), anInterface.getRequestHeaders(), anInterface.getMethod(), uri);
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

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress("localhost", 80), 1000);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write("GET /interface/run/19 HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: application/json, text/plain, */*\r\n" +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36\r\n" +
                        "Origin: http://localhost:8080\r\n" +
                        "Sec-Fetch-Site: same-site\r\n" +
                        "Sec-Fetch-Mode: cors\r\n" +
                        "Sec-Fetch-Dest: empty\r\n" +
                        "Referer: http://localhost:8080/\r\n" +
                        "Accept-Encoding: gzip, deflate, br\r\n" +
                        "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                        "Cookie: sidebarStatus=0; JSESSIONID=8C2F507F1552DE907E086CE956B2C1B2\r\n\r\n");
                bw.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {//由于服务器端没有关闭socket,会一直阻塞在这里
                    System.out.println(line);
                }
                br.close();
                bw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
