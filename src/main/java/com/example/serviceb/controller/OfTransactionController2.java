package com.example.serviceb.controller;

import com.example.serviceb.service.OfTransactionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(OfTransactionController2.class);

    @Autowired
    private OfTransactionService2 ofTransactionService2;

    /**
     * 分页查询融资交易列表
     * 对应前端: ofTransactionController.js -> ofTransactionPage()
     * 
     * @param params 请求参数
     * @return 响应结果
     */
    @PostMapping("/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        logger.info("[OfTransactionController2.page] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 请求参数预处理
            Map<String, Object> processedParams = preprocessRequest(params);
            
            // 调用服务层
            Map<String, Object> data = ofTransactionService2.page(processedParams);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
            logger.info("[OfTransactionController2.page] 请求成功, 耗时={}ms", System.currentTimeMillis() - startTime);
        } catch (IllegalArgumentException e) {
            logger.warn("[OfTransactionController2.page] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[OfTransactionController2.page] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
    
    /**
     * 请求参数预处理
     */
    private Map<String, Object> preprocessRequest(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        
        // 设置默认值
        if (!params.containsKey("pageNo")) {
            params.put("pageNo", 1);
        }
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10);
        }
        
        return params;
    }
}
