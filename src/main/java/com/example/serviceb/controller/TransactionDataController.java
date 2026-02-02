package com.example.serviceb.controller;

import com.example.serviceb.service.TransactionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易数据控制器
 * 
 * 提供交易数据相关的 HTTP 接口
 * 通过 TransactionDataService 调用 service-a 的 TransactionDataProvider
 * 
 * 用于测试 Mapper 层高风险变更的跨项目多级追踪：
 * HTTP 请求 → TransactionDataController → TransactionDataService → Dubbo RPC → TransactionDataProvider → Mapper
 * 
 * 测试场景：
 * 1. Mapper 方法新增 - setNull4SingleCeConfirm, setNull4DoubleCeConfirm
 * 2. 高风险数据更新操作
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/transaction-data")
public class TransactionDataController {

    @Autowired
    private TransactionDataService transactionDataService;

    /**
     * 根据ID查询交易记录
     * 
     * GET /api/transaction-data/{id}
     * 
     * @param id 交易ID
     * @return 交易记录
     */
    @GetMapping("/{id}")
    public Map<String, Object> getTransaction(@PathVariable Long id) {
        return transactionDataService.getTransaction(id);
    }

    /**
     * 更新交易状态
     * 
     * PUT /api/transaction-data/{id}/status
     * 
     * @param id 交易ID
     * @param status 新状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        boolean success = transactionDataService.updateStatus(id, status);
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("transactionId", id);
        result.put("newStatus", status);
        return result;
    }

    /**
     * 【新增接口】重置单次确真字段
     * 
     * POST /api/transaction-data/{transactionId}/reset-single-confirm
     * 
     * ⚠️ 高风险操作
     * 调用链：
     * HTTP → TransactionDataController → TransactionDataService 
     *      → Dubbo RPC → TransactionDataProvider 
     *      → OfTransactionMapper.setNull4SingleCeConfirm (新增方法)
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    @PostMapping("/{transactionId}/reset-single-confirm")
    public Map<String, Object> resetSingleConfirm(@PathVariable Long transactionId) {
        return transactionDataService.resetSingleConfirm(transactionId);
    }

    /**
     * 【新增接口】重置双确真字段
     * 
     * POST /api/transaction-data/{transactionId}/reset-double-confirm
     * 
     * ⚠️ 高风险操作
     * 调用链：
     * HTTP → TransactionDataController → TransactionDataService 
     *      → Dubbo RPC → TransactionDataProvider 
     *      → OfTransactionMapper.setNull4DoubleCeConfirm (新增方法)
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    @PostMapping("/{transactionId}/reset-double-confirm")
    public Map<String, Object> resetDoubleConfirm(@PathVariable Long transactionId) {
        return transactionDataService.resetDoubleConfirm(transactionId);
    }

    /**
     * 根据用户ID和状态查询交易列表
     * 
     * GET /api/transaction-data/user/{userId}
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 交易列表
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getTransactionsByUserAndStatus(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "PENDING") String status) {
        return transactionDataService.getTransactionsByUserAndStatus(userId, status);
    }

    /**
     * 【新增接口】批量更新确真状态
     * 
     * POST /api/transaction-data/batch-confirm
     * 
     * @param request 请求参数（包含 transactionIds 和 confirmStatus）
     * @return 操作结果
     */
    @PostMapping("/batch-confirm")
    public Map<String, Object> batchUpdateConfirmStatus(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Long> transactionIds = (List<Long>) request.get("transactionIds");
        String confirmStatus = (String) request.get("confirmStatus");
        return transactionDataService.batchUpdateConfirmStatus(transactionIds, confirmStatus);
    }

    /**
     * 【新增接口】查询待确真的交易列表
     * 
     * GET /api/transaction-data/pending-confirm
     * 
     * @param companyId 公司ID
     * @param confirmType 确真类型
     * @return 待确真交易列表
     */
    @GetMapping("/pending-confirm")
    public List<Map<String, Object>> getPendingConfirmTransactions(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "SINGLE") String confirmType) {
        return transactionDataService.getPendingConfirmTransactions(companyId, confirmType);
    }
}
