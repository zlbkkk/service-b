package com.example.serviceb.service;

import com.example.servicea.provider.FinanceChainProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 融资过滤器链服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 FinanceChainProvider
 * 测试场景：验证配置类/过滤器/处理器变更的跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@Service
public class FinanceChainService {

    /**
     * 通过 Dubbo RPC 注入 FinanceChainProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 FinanceChainProvider
     */
    @DubboReference(version = "1.0.0", timeout = 10000, retries = 2)
    private FinanceChainProvider financeChainProvider;

    /**
     * 执行双确真校验
     * 
     * 调用 service-a 新增的双确真校验功能
     * 涉及：CeConfirmCheckApplyChainHandler、FinanceChain、CeConfirmFinanceThrowExFilter
     * 
     * @param transactionId 交易ID
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param amount 金额
     * @param ceGroupCompanyId 集团公司ID
     * @param ceGroupCompanyDO 集团公司信息
     * @return 校验结果
     */
    public Map<String, Object> executeDoubleConfirmCheck(
            Long transactionId,
            Long orderId,
            Long userId,
            BigDecimal amount,
            Long ceGroupCompanyId,
            Map<String, Object> ceGroupCompanyDO) {
        // 【Dubbo RPC 调用】调用 service-a 的双确真校验
        return financeChainProvider.executeDoubleConfirmCheck(
                transactionId, orderId, userId, amount, ceGroupCompanyId, ceGroupCompanyDO);
    }

    /**
     * 执行单次确真校验
     * 
     * @param transactionId 交易ID
     * @param amount 金额
     * @param ceGroupCompanyId 集团公司ID
     * @return 校验结果
     */
    public Map<String, Object> executeSingleConfirmCheck(
            Long transactionId,
            BigDecimal amount,
            Long ceGroupCompanyId) {
        // 【Dubbo RPC 调用】调用 service-a 的单次确真校验
        return financeChainProvider.executeSingleConfirmCheck(transactionId, amount, ceGroupCompanyId);
    }

    /**
     * 获取确真模式建议
     * 
     * @param amount 金额
     * @param hasGroupCompany 是否有集团公司
     * @return 建议的确真模式
     */
    public Map<String, Object> getConfirmModeSuggestion(BigDecimal amount, boolean hasGroupCompany) {
        // 【Dubbo RPC 调用】调用 service-a 获取确真模式建议
        return financeChainProvider.getConfirmModeSuggestion(amount, hasGroupCompany);
    }
}
