package com.example.serviceb.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Service
 * 通过 Dubbo 调用 service-a 的 OfTransactionProvider2
 */
@Service
public class OfTransactionService2 {

    /**
     * 分页查询融资交易列表
     * 对应前端: /ofTransaction/page
     */
    public Map<String, Object> page(Map<String, Object> params) {
        // 实际调用: ofTransactionProvider2.page(params);
        Map<String, Object> result = new HashMap<>();
        result.put("records", List.of());
        result.put("total", 0);
        return result;
    }
}
