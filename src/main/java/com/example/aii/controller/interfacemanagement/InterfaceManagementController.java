package com.example.aii.controller.interfacemanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.entity.Interface;
import com.example.aii.entity.User;
import com.example.aii.service.InterfaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class InterfaceManagementController {

    @Resource
    private InterfaceService interfaceService;

    @GetMapping("/interfacePage")
    public IPage<Interface> getInterfacePage(@RequestParam(required = false) String interfaceNameLike,
                                             @RequestParam(defaultValue = "1") Integer current,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return interfaceService.findByInterfaceNameLike(interfaceNameLike, new Page<>(current, size));

    }

    @GetMapping("/interface/run/{interfaceId}")
    public Map<String, Object> runInterface(@PathVariable("interfaceId") Long interfaceId) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        ResponseEntity<String> re = interfaceService.run(interfaceId);
        StringBuilder sb = new StringBuilder();
        re.getHeaders().forEach((k, v) -> sb.append(k).append(": ").append(v.get(0)).append("\n"));
        sb.append("\n").append(re.getBody());
        res.put("status", re.getStatusCodeValue());
        res.put("responseParams", sb.toString());

        return res;
    }

    @PostMapping("/interface")
    public void postInterface(InterfaceDTO interfaceDTO) {
        Interface anInterface = interfaceDTO.toInterface();
        interfaceService.save(anInterface);
    }

    @PatchMapping("/interface/{id}")
    public void patchInterface(@PathVariable("id") Long id, InterfaceDTO interfaceDTO) {
        Interface anInterface = interfaceDTO.toInterface();
        anInterface.setId(id);
        interfaceService.update(anInterface);
    }

    @PostMapping("/test")
    public User getUser(@RequestBody User user) {
        return user;
    }
}
