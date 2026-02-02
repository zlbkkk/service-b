package com.example.serviceb.service;

import com.example.servicea.dto.OrderDTO;
import com.example.servicea.dto.OrderExportDTO;
import com.example.servicea.vo.OrderDetailVO;
import com.example.servicea.provider.OrderQueryProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单查询服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 OrderQueryProvider
 * 测试场景：验证系统能否识别 Dubbo Provider 的跨服务调用
 * 
 * @author system
 * @version 1.0
 */
@Service
public class OrderQueryService {

    /**
     * 通过 Dubbo RPC 注入 OrderQueryProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 Provider
     */
    @DubboReference(version = "1.0.0", timeout = 5000)
    private OrderQueryProvider orderQueryProvider;

    /**
     * 查询订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    public OrderDetailVO getOrderDetail(Long orderId) {
        // 【Dubbo RPC 调用】调用 service-a 的 OrderQueryProvider.queryOrderDetail()
        return orderQueryProvider.queryOrderDetail(orderId);
    }

    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<OrderDTO> getUserOrders(Long userId) {
        // 【Dubbo RPC 调用】调用 service-a 的 OrderQueryProvider.queryOrdersByUserId()
        return orderQueryProvider.queryOrdersByUserId(userId);
    }

    /**
     * 统计用户订单数量
     * 
     * @param userId 用户ID
     * @return 订单数量
     */
    public Integer countUserOrders(Long userId) {
        // 【Dubbo RPC 调用】调用 service-a 的 OrderQueryProvider.countOrdersByUserId()
        return orderQueryProvider.countOrdersByUserId(userId);
    }

    /**
     * 【新增方法】根据订单号查询订单详情
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param orderNumber 订单号
     * @return 订单详情
     */
    public OrderDetailVO getOrderByNumber(String orderNumber) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 queryOrderByNumber()
        return orderQueryProvider.queryOrderByNumber(orderNumber);
    }

    /**
     * 【新增方法】批量查询订单详情
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param orderIds 订单ID列表
     * @return 订单详情列表
     */
    public List<OrderDetailVO> batchGetOrderDetails(List<Long> orderIds) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 batchQueryOrderDetails()
        return orderQueryProvider.batchQueryOrderDetails(orderIds);
    }

    /**
     * 【新增方法】导出用户订单数据
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导出数据列表
     */
    public List<OrderExportDTO> exportUserOrders(Long userId, String startDate, String endDate) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 exportOrders()
        return orderQueryProvider.exportOrders(userId, startDate, endDate);
    }

    /**
     * 【新增方法】获取订单摘要信息
     * 
     * 组合多个 Provider 调用，生成订单摘要
     * 
     * @param orderId 订单ID
     * @return 订单摘要字符串
     */
    public String getOrderSummary(Long orderId) {
        // 【Dubbo RPC 调用 1】获取订单详情
        OrderDetailVO detail = orderQueryProvider.queryOrderDetail(orderId);
        
        if (detail == null) {
            return "订单不存在";
        }
        
        // 组合信息生成摘要
        return String.format(
            "订单摘要 - 订单号: %s, 用户: %s, 商品: %s, 金额: ¥%.2f, 状态: %s",
            detail.getOrderNumber(),
            detail.getUserName(),
            detail.getProductName(),
            detail.getTotalAmount(),
            detail.getStatusText()
        );
    }
}
