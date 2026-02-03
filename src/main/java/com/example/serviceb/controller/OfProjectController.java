package com.example.serviceb.controller;

import com.example.serviceb.service.OfProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目管理 Controller
 * 
 * 对应前端: beehive-order-finance-frontend
 * 前端文件: src/api/orderApi/controller/ofProjectController.js
 */
@RestController
@RequestMapping("/order-scfPc-web/ofProject")
public class OfProjectController {

    @Autowired
    private OfProjectService ofProjectService;

    /**
     * 分页查询项目列表
     * 对应前端: ofProjectController.js -> listPage()
     */
    @PostMapping("/listPage")
    public Map<String, Object> listPage(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofProjectService.listPage(params);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
