package com.example.aii;

import com.example.aii.entity.Interface;
import com.example.aii.entity.Role;
import com.example.aii.mapper.InterfaceMapper;
import com.example.aii.service.RoleService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class AiiApplicationTests {

    @Resource
    InterfaceMapper mapper;

    @Test
    void contextLoads() throws IOException {
        ByteSource bytes = ByteSource.Util.bytes("93f3eb2c556e4fa4945fc0ebb5d1b8f8");
        char[] cr = {'1', '2', '3', '4', '5', '6'};
        SimpleHash simpleHash = new SimpleHash("SHA-256", cr, bytes, 2);
    }

    @Test
    void m1() {
        Interface anInterface = mapper.selectById(1);
        System.out.println(anInterface);
    }
}
