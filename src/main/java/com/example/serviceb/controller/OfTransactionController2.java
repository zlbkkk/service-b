package com.example.serviceb.controller;

import com.example.serviceb.service.OfTransactionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 融资交易 Controller
 * 
 * 对应前端: beehive-order-finance-frontend
 * 前端文件: src/api/orderApi/controller/ofTransactionController.js
 */
@RestController
@RequestMapping("/order-scfPc-web/ofTransaction")
public class OfTransactionController2 {

    @Autowired
    private OfTransactionService2 ofTransactionService2;

    /**
     * 分页查询融资交易列表
     * 对应前端: ofTransactionController.js -> ofTransactionPage()
     */
    @PostMapping("/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService2.page(params);
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
