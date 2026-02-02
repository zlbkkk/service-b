package com.example.serviceb.controller;

import com.example.serviceb.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易管理控制器
 * 
 * 提供交易相关的 HTTP 接口
 * 通过 TransactionService 调用 service-a 的 TransactionProvider
 * 
 * 用于测试跨项目多级追踪 HTTP 接口路径：
 * HTTP 请求 → TransactionController → TransactionService → Dubbo RPC → TransactionProvider
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * 处理工行贷款申请成功
     * 
     * POST /api/transactions/icbc/loan/success
     * 
     * @param transactionId 交易ID
     * @param loanAmount 贷款金额
     * @return 处理结果
     */
    @PostMapping("/icbc/loan/success")
    public Map<String, Object> handleIcbcLoanSuccess(
            @RequestParam Long transactionId,
            @RequestParam BigDecimal loanAmount) {
        return transactionService.handleIcbcLoanSuccess(transactionId, loanAmount);
    }

    /**
     * 处理工行贷款申请拒绝
     * 
     * POST /api/transactions/icbc/loan/reject
     * 
     * @param transactionId 交易ID
     * @param rejectReason 拒绝原因
     * @return 处理结果
     */
    @PostMapping("/icbc/loan/reject")
    public Map<String, Object> handleIcbcLoanReject(
            @RequestParam Long transactionId,
            @RequestParam String rejectReason) {
        return transactionService.handleIcbcLoanReject(transactionId, rejectReason);
    }

    /**
     * 同步中台工作流节点
     * 
     * POST /api/transactions/flow/sync
     * 
     * @param transactionId 交易ID
     * @param flowNode 工作流节点
     * @return 同步结果
     */
    @PostMapping("/flow/sync")
    public Map<String, Object> syncMiddleFlowNode(
            @RequestParam Long transactionId,
            @RequestParam String flowNode) {
        return transactionService.syncMiddleFlowNode(transactionId, flowNode);
    }

    /**
     * 计算交易金额
     * 
     * GET /api/transactions/calculate
     * 
     * @param amount 原始金额
     * @return 计算后的金额
     */
    @GetMapping("/calculate")
    public Map<String, Object> calculateAmount(@RequestParam BigDecimal amount) {
        BigDecimal result = transactionService.calculateAmount(amount);
        Map<String, Object> response = new HashMap<>();
        response.put("originalAmount", amount);
        response.put("calculatedAmount", result);
        return response;
    }

    /**
     * 查询交易详情
     * 
     * GET /api/transactions/{transactionId}/detail
     * 
     * @param transactionId 交易ID
     * @return 交易详情
     */
    @GetMapping("/{transactionId}/detail")
    public Map<String, Object> getTransactionDetail(@PathVariable Long transactionId) {
        return transactionService.getTransactionDetail(transactionId);
    }

    /**
     * 查询用户交易列表
     * 
     * GET /api/transactions/user/{userId}
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 交易列表
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return transactionService.getUserTransactions(userId, pageNum, pageSize);
    }

    /**
     * 处理双确真工作流
     * 
     * POST /api/transactions/workflow/double-check
     * 
     * @param transactionId 交易ID
     * @param workflowType 工作流类型
     * @param params 额外参数
     * @return 处理结果
     */
    @PostMapping("/workflow/double-check")
    public Map<String, Object> handleDoubleCheckWorkflow(
            @RequestParam Long transactionId,
            @RequestParam String workflowType,
            @RequestBody(required = false) Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return transactionService.handleDoubleCheckWorkflow(transactionId, workflowType, params);
    }

    /**
     * 启动工作流
     * 
     * POST /api/transactions/workflow/start
     * 
     * @param processId 流程ID
     * @param processType 流程类型
     * @param transactionId 交易ID
     * @return 启动结果
     */
    @PostMapping("/workflow/start")
    public Map<String, Object> startWorkflow(
            @RequestParam Long processId,
            @RequestParam String processType,
            @RequestParam Long transactionId) {
        return transactionService.startWorkflow(processId, processType, transactionId);
    }

    /**
     * 贷款审批流程
     * 
     * POST /api/transactions/loan/approval
     * 
     * @param transactionId 交易ID
     * @param loanAmount 贷款金额
     * @param approved 是否批准
     * @return 审批结果
     */
    @PostMapping("/loan/approval")
    public Map<String, Object> processLoanApproval(
            @RequestParam Long transactionId,
            @RequestParam BigDecimal loanAmount,
            @RequestParam boolean approved) {
        String result = transactionService.processLoanApproval(transactionId, loanAmount, approved);
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        return response;
    }
}
