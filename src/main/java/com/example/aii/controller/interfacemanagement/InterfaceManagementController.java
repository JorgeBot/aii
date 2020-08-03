package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.Interface;
import com.example.aii.service.InterfaceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class InterfaceManagementController {

    @Resource
    private InterfaceService interfaceService;

    @PostMapping("/interface")
    public void postInterface(InterfaceDTO interfaceDTO) {
        Interface anInterface = interfaceDTO.toInterface();
        interfaceService.save(anInterface);
    }
}
