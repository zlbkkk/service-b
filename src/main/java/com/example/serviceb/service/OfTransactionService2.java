package com.example.serviceb.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Service
 * 通过 Dubbo 调用 service-a 的 OfTransactionProvider2
 */
@Service
public class OfTransactionService2 {
    
    private static final Logger logger = LoggerFactory.getLogger(OfTransactionService2.class);
    
    /** 默认分页大小 */
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    /** 最大分页大小 */
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * 分页查询融资交易列表
     * 对应前端: /ofTransaction/page
     * 
     * @param params 查询参数，包含 pageNo, pageSize, transactionType, startDate, endDate 等
     * @return 分页结果
     */
    public Map<String, Object> page(Map<String, Object> params) {
        logger.info("[OfTransactionService2] 开始查询融资交易列表, params={}", params);
        
        // 1. 参数校验和预处理
        Map<String, Object> validatedParams = validateAndPreprocessParams(params);
        
        // 2. 构建查询条件
        Map<String, Object> queryCondition = buildQueryCondition(validatedParams);
        
        // 3. 实际调用: ofTransactionProvider2.page(queryCondition);
        // 这里模拟返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("records", List.of());
        result.put("total", 0);
        result.put("pageNo", validatedParams.get("pageNo"));
        result.put("pageSize", validatedParams.get("pageSize"));
        
        logger.info("[OfTransactionService2] 查询完成, total={}", result.get("total"));
        return result;
    }
    
    /**
     * 参数校验和预处理
     */
    private Map<String, Object> validateAndPreprocessParams(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>(params);
        
        // 分页参数校验
        int pageNo = getIntValue(params, "pageNo", 1);
        int pageSize = getIntValue(params, "pageSize", DEFAULT_PAGE_SIZE);
        
        // 页码不能小于1
        if (pageNo < 1) {
            pageNo = 1;
            logger.warn("[参数校验] pageNo 小于1，已修正为1");
        }
        
        // 分页大小限制
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
            logger.warn("[参数校验] pageSize 小于1，已修正为默认值: {}", DEFAULT_PAGE_SIZE);
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
            logger.warn("[参数校验] pageSize 超过最大值，已修正为: {}", MAX_PAGE_SIZE);
        }
        
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    /**
     * 构建查询条件
     */
    private Map<String, Object> buildQueryCondition(Map<String, Object> params) {
        Map<String, Object> condition = new HashMap<>();
        
        // 交易类型
        if (params.containsKey("transactionType")) {
            String transactionType = (String) params.get("transactionType");
            if (transactionType != null && !transactionType.isEmpty() && !"ALL".equals(transactionType)) {
                condition.put("transactionType", transactionType);
            }
        }
        
        // 日期范围
        if (params.containsKey("startDate")) {
            condition.put("startDate", params.get("startDate"));
        }
        if (params.containsKey("endDate")) {
            condition.put("endDate", params.get("endDate"));
        }
        
        // 分页参数
        condition.put("pageNo", params.get("pageNo"));
        condition.put("pageSize", params.get("pageSize"));
        
        return condition;
    }
    
    /**
     * 获取整数值，带默认值
     */
    private int getIntValue(Map<String, Object> params, String key, int defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
