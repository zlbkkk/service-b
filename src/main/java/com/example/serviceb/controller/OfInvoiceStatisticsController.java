package com.example.serviceb.controller;

import com.example.serviceb.client.OfTransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 融资交易发票统计 Controller
 * 
 * 测试场景：Service 层新增方法
 * 对应测试方案：3. Service 层新增方法
 * 
 * 真实案例：
 * 提交: 523bc2ae - feat(S26-169): 订单融资--提供两个总数查询接口
 * 文件: OfTransactionServiceImpl.java
 * 变动: 新增 queryMainInvoiceTotal() 和 queryIncomeInvoiceTotal() 方法
 * 
 * 影响范围：
 * - 直接影响: Controller调用该方法的接口
 * - 间接影响: 前端调用该接口的页面和组件
 * - 跨项目影响: 其他项目通过Dubbo调用该服务
 */
@RestController
@RequestMapping("/order-scfPc-web/ofInvoice")
public class OfInvoiceStatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(OfInvoiceStatisticsController.class);

    @Autowired
    private OfTransactionClient ofTransactionClient;

    /**
     * 查询发票统计信息
     * 
     * 通过 Dubbo 调用 service-a 的 OfTransactionService
     * 调用新增的 queryMainInvoiceTotal() 和 queryIncomeInvoiceTotal() 方法
     * 
     * 对应前端: beehive-order-finance-frontend
     * 前端路由: /finance/invoice/statistics
     * 前端文件: src/views/finance/InvoiceStatistics.vue
     * API文件: src/api/orderApi/controller/ofInvoiceController.js
     * 
     * @param params 请求参数，包含 companyId
     * @return 发票统计结果
     */
    @PostMapping("/statistics")
    public Map<String, Object> getInvoiceStatistics(@RequestBody Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        logger.info("[OfInvoiceStatisticsController.getInvoiceStatistics] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 参数校验
            String companyId = (String) params.get("companyId");
            if (companyId == null || companyId.trim().isEmpty()) {
                throw new IllegalArgumentException("企业ID不能为空");
            }
            
            // 跨项目调用: 通过 Dubbo 调用 service-a 的新增方法
            logger.info("[Dubbo调用] 开始调用 service-a 的发票统计方法, companyId={}", companyId);
            
            // 调用新增的 queryMainInvoiceTotal() 方法
            Integer mainInvoiceTotal = ofTransactionClient.queryMainInvoiceTotal(companyId);
            logger.info("[Dubbo调用] queryMainInvoiceTotal 返回结果: {}", mainInvoiceTotal);
            
            // 调用新增的 queryIncomeInvoiceTotal() 方法
            Integer incomeInvoiceTotal = ofTransactionClient.queryIncomeInvoiceTotal(companyId);
            logger.info("[Dubbo调用] queryIncomeInvoiceTotal 返回结果: {}", incomeInvoiceTotal);
            
            // 组装返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("companyId", companyId);
            data.put("mainInvoiceTotal", mainInvoiceTotal);
            data.put("incomeInvoiceTotal", incomeInvoiceTotal);
            data.put("totalInvoices", mainInvoiceTotal + incomeInvoiceTotal);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
            logger.info("[OfInvoiceStatisticsController.getInvoiceStatistics] 请求成功, 耗时={}ms", 
                System.currentTimeMillis() - startTime);
        } catch (IllegalArgumentException e) {
            logger.warn("[OfInvoiceStatisticsController.getInvoiceStatistics] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[OfInvoiceStatisticsController.getInvoiceStatistics] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
    
    /**
     * 查询主发票总数
     * 
     * 单独提供主发票查询接口
     * 通过 Dubbo 调用 service-a 的 queryMainInvoiceTotal() 方法
     * 
     * @param params 请求参数，包含 companyId
     * @return 主发票总数
     */
    @PostMapping("/mainInvoiceTotal")
    public Map<String, Object> getMainInvoiceTotal(@RequestBody Map<String, Object> params) {
        logger.info("[OfInvoiceStatisticsController.getMainInvoiceTotal] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            String companyId = (String) params.get("companyId");
            if (companyId == null || companyId.trim().isEmpty()) {
                throw new IllegalArgumentException("企业ID不能为空");
            }
            
            // Dubbo 调用 service-a 的新增方法
            Integer total = ofTransactionClient.queryMainInvoiceTotal(companyId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("companyId", companyId);
            data.put("mainInvoiceTotal", total);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
        } catch (IllegalArgumentException e) {
            logger.warn("[getMainInvoiceTotal] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[getMainInvoiceTotal] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
    
    /**
     * 查询收入发票总数
     * 
     * 单独提供收入发票查询接口
     * 通过 Dubbo 调用 service-a 的 queryIncomeInvoiceTotal() 方法
     * 
     * @param params 请求参数，包含 companyId
     * @return 收入发票总数
     */
    @PostMapping("/incomeInvoiceTotal")
    public Map<String, Object> getIncomeInvoiceTotal(@RequestBody Map<String, Object> params) {
        logger.info("[OfInvoiceStatisticsController.getIncomeInvoiceTotal] 接收到请求, params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        try {
            String companyId = (String) params.get("companyId");
            if (companyId == null || companyId.trim().isEmpty()) {
                throw new IllegalArgumentException("企业ID不能为空");
            }
            
            // Dubbo 调用 service-a 的新增方法
            Integer total = ofTransactionClient.queryIncomeInvoiceTotal(companyId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("companyId", companyId);
            data.put("incomeInvoiceTotal", total);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
            
        } catch (IllegalArgumentException e) {
            logger.warn("[getIncomeInvoiceTotal] 参数错误: {}", e.getMessage());
            result.put("code", 400);
            result.put("message", "参数错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("[getIncomeInvoiceTotal] 系统异常", e);
            result.put("code", 500);
            result.put("message", "系统繁忙，请稍后重试");
        }
        return result;
    }
}
