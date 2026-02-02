package com.example.serviceb.service;

import com.example.servicea.provider.PaymentProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 PaymentProvider
 * 测试场景：验证系统能否识别 Dubbo Provider 方法签名变更的影响
 * 
 * @author system
 * @version 1.0
 */
@Service
public class PaymentService {

    /**
     * 通过 Dubbo RPC 注入 PaymentProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 Provider
     */
    @DubboReference(version = "1.0.0", timeout = 10000, retries = 2)
    private PaymentProvider paymentProvider;

    /**
     * 创建支付订单
     * 
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式
     * @return 支付结果
     */
    public Map<String, Object> createPayment(Long orderId, BigDecimal amount, String paymentMethod) {
        // 【Dubbo RPC 调用】调用 service-a 的 PaymentProvider.createPayment()
        return paymentProvider.createPayment(orderId, amount, paymentMethod);
    }

    /**
     * 查询支付状态
     * 
     * @param paymentId 支付ID
     * @return 支付状态
     */
    public Map<String, Object> queryPaymentStatus(String paymentId) {
        // 【Dubbo RPC 调用】调用 service-a 的 PaymentProvider.queryPaymentStatus()
        return paymentProvider.queryPaymentStatus(paymentId);
    }

    /**
     * 【修改方法】处理退款
     * 
     * ⚠️ 重要：这个方法调用了 service-a 中被修改的 Provider 方法
     * service-a 的 processRefund 方法增加了 operator 参数
     * 
     * 旧签名: processRefund(String paymentId, BigDecimal refundAmount, String reason)
     * 新签名: processRefund(String paymentId, BigDecimal refundAmount, String reason, String operator)
     * 
     * @param paymentId 支付ID
     * @param refundAmount 退款金额
     * @param reason 退款原因
     * @param operator 操作人（新增参数）
     * @return 退款结果
     */
    public Map<String, Object> processRefund(String paymentId, BigDecimal refundAmount, String reason, String operator) {
        // 【Dubbo RPC 调用】调用 service-a 修改后的方法
        // ⚠️ 这是破坏性变更：必须传入 operator 参数
        return paymentProvider.processRefund(paymentId, refundAmount, reason, operator);
    }

    /**
     * 【新增方法】查询退款状态
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param refundId 退款ID
     * @return 退款状态
     */
    public Map<String, Object> queryRefundStatus(String refundId) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 queryRefundStatus()
        return paymentProvider.queryRefundStatus(refundId);
    }

    /**
     * 【新增方法】取消支付
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param paymentId 支付ID
     * @param reason 取消原因
     * @return 取消结果
     */
    public Map<String, Object> cancelPayment(String paymentId, String reason) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 cancelPayment()
        return paymentProvider.cancelPayment(paymentId, reason);
    }

    /**
     * 【新增方法】完整的退款流程
     * 
     * 组合多个 Provider 调用，实现完整的退款流程
     * 
     * @param paymentId 支付ID
     * @param refundAmount 退款金额
     * @param reason 退款原因
     * @param operator 操作人
     * @return 退款流程结果
     */
    public String processRefundWorkflow(String paymentId, BigDecimal refundAmount, String reason, String operator) {
        // 【Dubbo RPC 调用 1】查询支付状态
        Map<String, Object> paymentStatus = paymentProvider.queryPaymentStatus(paymentId);
        
        if (!"SUCCESS".equals(paymentStatus.get("status"))) {
            return "错误：支付未成功，无法退款";
        }
        
        // 【Dubbo RPC 调用 2】处理退款（使用修改后的方法签名）
        Map<String, Object> refundResult = paymentProvider.processRefund(paymentId, refundAmount, reason, operator);
        
        if (Boolean.TRUE.equals(refundResult.get("success"))) {
            String refundId = (String) refundResult.get("refundId");
            
            // 【Dubbo RPC 调用 3】查询退款状态
            Map<String, Object> refundStatus = paymentProvider.queryRefundStatus(refundId);
            
            return String.format(
                "退款流程完成 - 退款ID: %s, 状态: %s",
                refundId,
                refundStatus.get("status")
            );
        } else {
            return "退款申请失败";
        }
    }
}
