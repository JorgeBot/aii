package com.example.aii;

import com.example.aii.mapper.InterfaceMapper;
import com.example.aii.service.InterfaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class AiiApplicationTests {

    @Resource
    InterfaceMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    @Resource
    InterfaceService interfaceService;

    @Test
    void contextLoads() throws IOException {
        ByteSource bytes = ByteSource.Util.bytes("93f3eb2c556e4fa4945fc0ebb5d1b8f8");
        char[] cr = {'1', '2', '3', '4', '5', '6'};
        SimpleHash simpleHash = new SimpleHash("SHA-256", cr, bytes, 2);
    }

    @Test
    void m1() throws JsonProcessingException {
        ResponseEntity<String> run = interfaceService.run(17L);
    }
}
