package com.example.serviceb.controller;

import com.example.servicea.dto.OrderDTO;
import com.example.servicea.dto.OrderExportDTO;
import com.example.servicea.vo.OrderDetailVO;
import com.example.serviceb.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单查询控制器
 * 
 * 暴露 HTTP 接口，内部通过 Dubbo RPC 调用 service-a 的 OrderQueryProvider
 * 
 * 完整调用链：
 * HTTP API → Controller → Service → Dubbo RPC → Provider (service-a)
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/orders")
public class OrderQueryController {

    @Autowired
    private OrderQueryService orderQueryService;

    /**
     * 获取订单详情
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.queryOrderDetail()
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public OrderDetailVO getOrderDetail(@PathVariable Long orderId) {
        return orderQueryService.getOrderDetail(orderId);
    }

    /**
     * 获取用户订单列表
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.queryOrdersByUserId()
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/user/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderQueryService.getUserOrders(userId);
    }

    /**
     * 统计用户订单数量
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.countOrdersByUserId()
     * 
     * @param userId 用户ID
     * @return 订单数量
     */
    @GetMapping("/user/{userId}/count")
    public Integer countUserOrders(@PathVariable Long userId) {
        return orderQueryService.countUserOrders(userId);
    }

    /**
     * 【新增接口】根据订单号查询订单详情
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.queryOrderByNumber()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param orderNumber 订单号
     * @return 订单详情
     */
    @GetMapping("/number/{orderNumber}")
    public OrderDetailVO getOrderByNumber(@PathVariable String orderNumber) {
        return orderQueryService.getOrderByNumber(orderNumber);
    }

    /**
     * 【新增接口】批量查询订单详情
     * 
     * 调用链：HTTP POST → OrderQueryService → Dubbo RPC → OrderQueryProvider.batchQueryOrderDetails()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param orderIds 订单ID列表
     * @return 订单详情列表
     */
    @PostMapping("/batch")
    public List<OrderDetailVO> batchGetOrderDetails(@RequestBody List<Long> orderIds) {
        return orderQueryService.batchGetOrderDetails(orderIds);
    }

    /**
     * 【新增接口】导出用户订单数据
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.exportOrders()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出数据列表
     */
    @GetMapping("/export")
    public List<OrderExportDTO> exportUserOrders(
            @RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return orderQueryService.exportUserOrders(userId, startDate, endDate);
    }

    /**
     * 【新增接口】获取订单摘要
     * 
     * 调用链：HTTP GET → OrderQueryService → Dubbo RPC → OrderQueryProvider.queryOrderDetail()
     * 
     * @param orderId 订单ID
     * @return 订单摘要字符串
     */
    @GetMapping("/{orderId}/summary")
    public String getOrderSummary(@PathVariable Long orderId) {
        return orderQueryService.getOrderSummary(orderId);
    }
}
