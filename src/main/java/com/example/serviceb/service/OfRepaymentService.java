package com.example.serviceb.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 还款管理 Service
 * 通过 Dubbo 调用 service-a 的 OfRepaymentProvider
 */
@Service
public class OfRepaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(OfRepaymentService.class);
    
    /** 默认分页大小 */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 还款管理分页查询
     * 对应前端: /ofRepayment/paymentManagementPage
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    public Map<String, Object> paymentManagementPage(Map<String, Object> params) {
        logger.info("[OfRepaymentService.paymentManagementPage] 开始查询, params={}", params);
        
        // 参数校验
        int pageNo = getIntValue(params, "pageNo", 1);
        int pageSize = getIntValue(params, "pageSize", DEFAULT_PAGE_SIZE);
        
        // 模拟返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("records", List.of());
        result.put("total", 0);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        
        logger.info("[OfRepaymentService.paymentManagementPage] 查询完成, total={}", result.get("total"));
        return result;
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
