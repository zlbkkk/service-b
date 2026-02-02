package com.example.serviceb.controller;

import com.example.serviceb.service.FinanceChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 融资过滤器链控制器
 * 
 * 提供融资校验相关的 HTTP 接口
 * 通过 FinanceChainService 调用 service-a 的 FinanceChainProvider
 * 
 * 用于测试配置类变更的跨项目多级追踪：
 * HTTP 请求 → FinanceChainController → FinanceChainService → Dubbo RPC 
 *           → FinanceChainProvider → CeConfirmCheckApplyChainHandler → FinanceChain → Filter
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/finance-chain")
public class FinanceChainController {

    @Autowired
    private FinanceChainService financeChainService;

    /**
     * 执行双确真校验
     * 
     * POST /api/finance-chain/double-confirm
     * 
     * 涉及的 service-a 组件：
     * - CeConfirmCheckApplyChainHandler（新增处理器类）
     * - FinanceChain（过滤器新增）
     * - CeConfirmFinanceThrowExFilter（新增过滤器类）
     * - ApplyFinancingContext（字段新增）
     * 
     * @param request 请求参数
     * @return 校验结果
     */
    @PostMapping("/double-confirm")
    @SuppressWarnings("unchecked")
    public Map<String, Object> executeDoubleConfirmCheck(@RequestBody Map<String, Object> request) {
        Long transactionId = ((Number) request.get("transactionId")).longValue();
        Long orderId = ((Number) request.get("orderId")).longValue();
        Long userId = ((Number) request.get("userId")).longValue();
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        Long ceGroupCompanyId = request.get("ceGroupCompanyId") != null 
                ? ((Number) request.get("ceGroupCompanyId")).longValue() : null;
        Map<String, Object> ceGroupCompanyDO = (Map<String, Object>) request.get("ceGroupCompanyDO");

        return financeChainService.executeDoubleConfirmCheck(
                transactionId, orderId, userId, amount, ceGroupCompanyId, ceGroupCompanyDO);
    }

    /**
     * 执行单次确真校验
     * 
     * POST /api/finance-chain/single-confirm
     * 
     * @param transactionId 交易ID
     * @param amount 金额
     * @param ceGroupCompanyId 集团公司ID
     * @return 校验结果
     */
    @PostMapping("/single-confirm")
    public Map<String, Object> executeSingleConfirmCheck(
            @RequestParam Long transactionId,
            @RequestParam BigDecimal amount,
            @RequestParam Long ceGroupCompanyId) {
        return financeChainService.executeSingleConfirmCheck(transactionId, amount, ceGroupCompanyId);
    }

    /**
     * 获取确真模式建议
     * 
     * GET /api/finance-chain/confirm-mode-suggestion
     * 
     * @param amount 金额
     * @param hasGroupCompany 是否有集团公司
     * @return 建议的确真模式
     */
    @GetMapping("/confirm-mode-suggestion")
    public Map<String, Object> getConfirmModeSuggestion(
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "false") boolean hasGroupCompany) {
        return financeChainService.getConfirmModeSuggestion(amount, hasGroupCompany);
    }
}
