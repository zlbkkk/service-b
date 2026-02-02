package com.example.serviceb.service;

import com.example.servicea.provider.TransactionProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 TransactionProvider
 * 测试场景：验证跨项目调用分析系统能否识别各种变动类型的影响
 * 
 * 变动类型测试覆盖：
 * 1. 方法新增 - icbcLoanApplySuccessProcess, icbcLoanApplyRejectProcess
 * 2. 关键逻辑删除 - calculateTransactionAmount
 * 3. 方法重命名 - querySingleDetailResult
 * 4. 查询逻辑优化 - queryTransactionListOrderByCreateTimeDesc
 * 5. 业务场景新增 - processDoubleCheckWorkflow
 * 6. 方法签名变更 - startProcess
 * 
 * @author system
 * @version 1.0
 */
@Service
public class TransactionService {

    /**
     * 通过 Dubbo RPC 注入 TransactionProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 TransactionProvider
     */
    @DubboReference(version = "1.0.0", timeout = 10000, retries = 2)
    private TransactionProvider transactionProvider;

    /**
     * 处理工行贷款申请成功
     * 
     * 调用 service-a 新增的 icbcLoanApplySuccessProcess 方法
     * 
     * @param transactionId 交易ID
     * @param loanAmount 贷款金额
     * @return 处理结果
     */
    public Map<String, Object> handleIcbcLoanSuccess(Long transactionId, BigDecimal loanAmount) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法
        return transactionProvider.icbcLoanApplySuccessProcess(transactionId, loanAmount);
    }

    /**
     * 处理工行贷款申请拒绝
     * 
     * 调用 service-a 新增的 icbcLoanApplyRejectProcess 方法
     * 
     * @param transactionId 交易ID
     * @param rejectReason 拒绝原因
     * @return 处理结果
     */
    public Map<String, Object> handleIcbcLoanReject(Long transactionId, String rejectReason) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法
        return transactionProvider.icbcLoanApplyRejectProcess(transactionId, rejectReason);
    }

    /**
     * 同步中台工作流节点
     * 
     * 调用 service-a 新增的 queryAndUpdateMiddleFlowNodeAndHander 方法
     * 
     * @param transactionId 交易ID
     * @param flowNode 工作流节点
     * @return 同步结果
     */
    public Map<String, Object> syncMiddleFlowNode(Long transactionId, String flowNode) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法
        return transactionProvider.queryAndUpdateMiddleFlowNodeAndHander(transactionId, flowNode);
    }

    /**
     * 计算交易金额
     * 
     * ⚠️ 重要：调用了 service-a 中被修改的方法
     * service-a 的 calculateTransactionAmount 方法移除了除以10000的逻辑
     * 这是一个破坏性变更，可能导致金额计算错误
     * 
     * @param amount 原始金额
     * @return 计算后的金额
     */
    public BigDecimal calculateAmount(BigDecimal amount) {
        // 【Dubbo RPC 调用】调用 service-a 修改后的方法
        // ⚠️ 关键变更：返回值不再除以10000
        return transactionProvider.calculateTransactionAmount(amount);
    }

    /**
     * 查询交易详情
     * 
     * ⚠️ 重要：调用了 service-a 中重命名的方法
     * 旧方法名: queryDetailResult
     * 新方法名: querySingleDetailResult
     * 
     * @param transactionId 交易ID
     * @return 交易详情
     */
    public Map<String, Object> getTransactionDetail(Long transactionId) {
        // 【Dubbo RPC 调用】调用 service-a 重命名后的方法
        return transactionProvider.querySingleDetailResult(transactionId);
    }

    /**
     * 查询用户交易列表
     * 
     * 调用 service-a 优化后的查询方法
     * 变更说明：返回结果按 createTime 倒序排列
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 交易列表
     */
    public List<Map<String, Object>> getUserTransactions(Long userId, int pageNum, int pageSize) {
        // 【Dubbo RPC 调用】调用 service-a 优化后的查询方法
        return transactionProvider.queryTransactionListOrderByCreateTimeDesc(userId, pageNum, pageSize);
    }

    /**
     * 处理双确真工作流
     * 
     * 调用 service-a 新增的业务场景方法
     * 支持三种场景：DOUBLE_CHECK_APPROVE, DOUBLE_CHECK_REJECT, SUPPLIER_RESUBMIT
     * 
     * @param transactionId 交易ID
     * @param workflowType 工作流类型
     * @param params 额外参数
     * @return 处理结果
     */
    public Map<String, Object> handleDoubleCheckWorkflow(
            Long transactionId, String workflowType, Map<String, Object> params) {
        // 【Dubbo RPC 调用】调用 service-a 新增的业务场景方法
        return transactionProvider.processDoubleCheckWorkflow(transactionId, workflowType, params);
    }

    /**
     * 启动工作流
     * 
     * ⚠️ 重要：调用了 service-a 中签名变更的方法
     * 旧签名: startProcess(Long processId, String processType)
     * 新签名: startProcess(Long processId, String processType, Long transactionId)
     * 
     * @param processId 流程ID
     * @param processType 流程类型
     * @param transactionId 交易ID（新增参数）
     * @return 启动结果
     */
    public Map<String, Object> startWorkflow(Long processId, String processType, Long transactionId) {
        // 【Dubbo RPC 调用】调用 service-a 签名变更后的方法
        // ⚠️ 这是破坏性变更：必须传入 transactionId 参数
        return transactionProvider.startProcess(processId, processType, transactionId);
    }

    /**
     * 【复合业务】完整的贷款审批流程
     * 
     * 组合多个 Provider 调用，测试跨项目调用链追踪
     * 
     * @param transactionId 交易ID
     * @param loanAmount 贷款金额
     * @param approved 是否批准
     * @return 审批流程结果
     */
    public String processLoanApproval(Long transactionId, BigDecimal loanAmount, boolean approved) {
        // 【Dubbo RPC 调用 1】启动工作流
        Map<String, Object> startResult = transactionProvider.startProcess(
            transactionId, "LOAN_APPROVAL", transactionId);
        
        if (approved) {
            // 【Dubbo RPC 调用 2】贷款申请成功处理
            Map<String, Object> successResult = transactionProvider.icbcLoanApplySuccessProcess(
                transactionId, loanAmount);
            
            // 【Dubbo RPC 调用 3】同步中台工作流
            transactionProvider.queryAndUpdateMiddleFlowNodeAndHander(transactionId, "APPROVED");
            
            return String.format("贷款审批通过 - 交易ID: %d, 金额: %s", 
                transactionId, loanAmount);
        } else {
            // 【Dubbo RPC 调用 2】贷款申请拒绝处理
            Map<String, Object> rejectResult = transactionProvider.icbcLoanApplyRejectProcess(
                transactionId, "信用评分不足");
            
            // 【Dubbo RPC 调用 3】同步中台工作流
            transactionProvider.queryAndUpdateMiddleFlowNodeAndHander(transactionId, "REJECTED");
            
            return String.format("贷款审批拒绝 - 交易ID: %d, 原因: %s", 
                transactionId, "信用评分不足");
        }
    }
}
