package com.example.serviceb.controller;

import com.example.serviceb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付控制器
 * 
 * 暴露 HTTP 接口，内部通过 Dubbo RPC 调用 service-a 的 PaymentProvider
 * 
 * 完整调用链：
 * HTTP API → Controller → Service → Dubbo RPC → Provider (service-a)
 * 
 * ⚠️ 重点测试：PaymentProvider.processRefund() 方法签名变更的影响
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 创建支付订单
     * 
     * 调用链：HTTP POST → PaymentService → Dubbo RPC → PaymentProvider.createPayment()
     * 
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式
     * @return 支付结果
     */
    @PostMapping("/create")
    public Map<String, Object> createPayment(
            @RequestParam Long orderId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod) {
        return paymentService.createPayment(orderId, amount, paymentMethod);
    }

    /**
     * 查询支付状态
     * 
     * 调用链：HTTP GET → PaymentService → Dubbo RPC → PaymentProvider.queryPaymentStatus()
     * 
     * @param paymentId 支付ID
     * @return 支付状态
     */
    @GetMapping("/{paymentId}/status")
    public Map<String, Object> queryPaymentStatus(@PathVariable String paymentId) {
        return paymentService.queryPaymentStatus(paymentId);
    }

    /**
     * 【修改接口】处理退款
     * 
     * 调用链：HTTP POST → PaymentService → Dubbo RPC → PaymentProvider.processRefund()
     * 
     * ⚠️ 重要：这个接口调用了 service-a 中被修改的 Provider 方法
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
    @PostMapping("/refund")
    public Map<String, Object> processRefund(
            @RequestParam String paymentId,
            @RequestParam BigDecimal refundAmount,
            @RequestParam String reason,
            @RequestParam String operator) {  // ⚠️ 新增参数
        return paymentService.processRefund(paymentId, refundAmount, reason, operator);
    }

    /**
     * 【新增接口】查询退款状态
     * 
     * 调用链：HTTP GET → PaymentService → Dubbo RPC → PaymentProvider.queryRefundStatus()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param refundId 退款ID
     * @return 退款状态
     */
    @GetMapping("/refund/{refundId}/status")
    public Map<String, Object> queryRefundStatus(@PathVariable String refundId) {
        return paymentService.queryRefundStatus(refundId);
    }

    /**
     * 【新增接口】取消支付
     * 
     * 调用链：HTTP POST → PaymentService → Dubbo RPC → PaymentProvider.cancelPayment()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param paymentId 支付ID
     * @param reason 取消原因
     * @return 取消结果
     */
    @PostMapping("/{paymentId}/cancel")
    public Map<String, Object> cancelPayment(
            @PathVariable String paymentId,
            @RequestParam String reason) {
        return paymentService.cancelPayment(paymentId, reason);
    }

    /**
     * 【新增接口】完整的退款流程
     * 
     * 调用链：HTTP POST → PaymentService → 多个 Dubbo RPC 调用
     * 
     * 涉及的 Dubbo 调用：
     * 1. PaymentProvider.queryPaymentStatus()
     * 2. PaymentProvider.processRefund() ⚠️ 使用修改后的方法签名
     * 3. PaymentProvider.queryRefundStatus()
     * 
     * @param paymentId 支付ID
     * @param refundAmount 退款金额
     * @param reason 退款原因
     * @param operator 操作人
     * @return 退款流程结果
     */
    @PostMapping("/refund/workflow")
    public String processRefundWorkflow(
            @RequestParam String paymentId,
            @RequestParam BigDecimal refundAmount,
            @RequestParam String reason,
            @RequestParam String operator) {
        return paymentService.processRefundWorkflow(paymentId, refundAmount, reason, operator);
    }
}
