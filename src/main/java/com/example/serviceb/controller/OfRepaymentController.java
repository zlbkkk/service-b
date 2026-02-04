package com.example.serviceb.controller;

import com.example.serviceb.service.OfRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 还款管理 Controller
 * 
 * 对应前端: beehive-order-finance-frontend
 * 前端文件: src/api/orderApi/controller/ofRepaymentController.js
 */
@RestController
@RequestMapping("/order-scfPc-web/ofRepayment")
public class OfRepaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(OfRepaymentController.class);

    @Autowired
    private OfRepaymentService ofRepaymentService;

    /**
     * 还款管理分页查询
     * 对应前端: ofRepaymentController.js -> paymentManagementPage()
     * 
     * @param params 请求参数
     * @return 响应结果
     */
    @PostMapping("/paymentManagementPage")
    public Map<String, Object> paymentManagementPage(@RequestBody Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        logger.info("[OfRepaymentController.paymentManagementPage] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofRepaymentService.paymentManagementPage(params);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
            logger.info("[OfRepaymentController.paymentManagementPage] 请求成功, 耗时={}ms", 
                System.currentTimeMillis() - startTime);
        } catch (IllegalArgumentException e) {
            logger.warn("[OfRepaymentController.paymentManagementPage] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[OfRepaymentController.paymentManagementPage] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
}
