package com.example.aii.controller.interfacemanagement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.aii.client.HttpClient;
import com.example.aii.entity.HttpInterface;
import com.example.aii.service.impl.HttpInterfaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequiresPermissions("HTTP接口")
public class InterfaceManagementController {

    @Resource
    private HttpClient httpClient;

    @Resource
    private HttpInterfaceService httpInterfaceService;

    @GetMapping("/httpInterfacePage")
    public IPage<HttpInterface> getHttpInterfacePage(@RequestParam(required = false) String queryLike,
                                                     @RequestParam Long moduleId,
                                                     Page<HttpInterface> page) {
        return httpInterfaceService.findByNameLikeAndModuleId(queryLike, moduleId, page);
    }

    @PostMapping("/httpInterface/run")
    public HttpInterface runInterface(HttpInterface httpInterface) {
        httpClient.run(httpInterface);
        return httpInterface;
    }

    @PostMapping("/httpInterface")
    public void postHttpInterface(HttpInterfaceDTO dto) {
        httpInterfaceService.save(dto.toHttpInterface());
    }

    @PatchMapping("/httpInterface")
    public void patchHttpInterface(HttpInterfaceDTO dto) {
        httpInterfaceService.updateById(dto.toHttpInterface());
    }
}
