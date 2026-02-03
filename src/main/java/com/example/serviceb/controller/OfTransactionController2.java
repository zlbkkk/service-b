package com.example.serviceb.controller;

import com.example.serviceb.service.OfTransactionService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
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
     * @param params 请求参数，支持：
     *   - pageNo: 页码，默认1
     *   - pageSize: 每页大小，默认10，最大100
     *   - transactionType: 交易类型 (LOAN/REPAYMENT/TRANSFER/ALL)
     *   - status: 交易状态 (PENDING/PROCESSING/APPROVED/REJECTED/COMPLETED/CANCELLED)
     *   - startDate: 开始日期
     *   - endDate: 结束日期
     *   - minAmount: 最小金额
     *   - maxAmount: 最大金额
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
     * 查询交易统计信息
     * 对应前端: ofTransactionController.js -> getTransactionStatistics()
     * 
     * @param params 请求参数，支持日期范围、交易类型等过滤条件
     * @return 统计结果，包含各状态数量、金额汇总等
     */
    @PostMapping("/statistics")
    public Map<String, Object> getStatistics(@RequestBody Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        logger.info("[OfTransactionController2.getStatistics] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService2.getStatistics(params);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
            logger.info("[OfTransactionController2.getStatistics] 请求成功, 耗时={}ms", 
                System.currentTimeMillis() - startTime);
        } catch (IllegalArgumentException e) {
            logger.warn("[OfTransactionController2.getStatistics] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[OfTransactionController2.getStatistics] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
    
    /**
     * 批量审批交易
     * 对应前端: ofTransactionController.js -> batchApproveTransaction()
     * 
     * @param params 请求参数：
     *   - transactionIds: 交易ID列表（必填，最多100条）
     *   - action: 审批动作，APPROVE 或 REJECT（必填）
     *   - reason: 审批原因（拒绝时必填）
     * @return 处理结果
     */
    @PostMapping("/batchApprove")
    public Map<String, Object> batchApprove(@RequestBody Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        logger.info("[OfTransactionController2.batchApprove] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 提取参数
            @SuppressWarnings("unchecked")
            List<String> transactionIds = (List<String>) params.get("transactionIds");
            String action = (String) params.get("action");
            String reason = (String) params.get("reason");
            
            // 参数校验
            if (transactionIds == null || transactionIds.isEmpty()) {
                throw new IllegalArgumentException("交易ID列表不能为空");
            }
            if (action == null || action.isEmpty()) {
                throw new IllegalArgumentException("审批动作不能为空");
            }
            if ("REJECT".equals(action) && (reason == null || reason.trim().isEmpty())) {
                throw new IllegalArgumentException("拒绝时必须填写原因");
            }
            
            // 调用服务层
            Map<String, Object> data = ofTransactionService2.batchApprove(transactionIds, action, reason);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
            logger.info("[OfTransactionController2.batchApprove] 请求成功, 耗时={}ms", 
                System.currentTimeMillis() - startTime);
        } catch (IllegalArgumentException e) {
            logger.warn("[OfTransactionController2.batchApprove] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[OfTransactionController2.batchApprove] 系统异常", e);
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
        
        // 清理空字符串参数
        params.entrySet().removeIf(entry -> 
            entry.getValue() instanceof String && ((String) entry.getValue()).trim().isEmpty()
        );
        
        return params;
    }
}
