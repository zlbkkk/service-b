package com.example.serviceb.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目管理 Service
 * 通过 Dubbo 调用 service-a 的 OfProjectProvider
 */
@Service
public class OfProjectService {

    /**
     * 分页查询项目列表
     * 对应前端: /ofProject/listPage
     */
    public Map<String, Object> listPage(Map<String, Object> params) {
        // 实际调用: ofProjectProvider.listPage(params);
        Map<String, Object> result = new HashMap<>();
        result.put("records", List.of());
        result.put("total", 0);
        return result;
    }
}
