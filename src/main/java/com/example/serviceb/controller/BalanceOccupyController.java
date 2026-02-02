package com.example.serviceb.controller;

import com.example.serviceb.service.BalanceOccupyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 余额占用控制器
 * 
 * 提供余额占用相关的 HTTP 接口
 * 通过 BalanceOccupyService 调用 service-a 的 BalanceOccupyProvider
 * 
 * 用于测试 Mapper 层变更的跨项目多级追踪：
 * HTTP 请求 → BalanceOccupyController → BalanceOccupyService → Dubbo RPC → BalanceOccupyProvider → Mapper
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/balance-occupy")
public class BalanceOccupyController {

    @Autowired
    private BalanceOccupyService balanceOccupyService;

    /**
     * 根据ID查询占用记录
     * 
     * GET /api/balance-occupy/{id}
     * 
     * @param id 记录ID
     * @return 占用记录
     */
    @GetMapping("/{id}")
    public Map<String, Object> getOccupyRecord(@PathVariable Long id) {
        return balanceOccupyService.getOccupyRecord(id);
    }

    /**
     * 【新增接口】分页查询占用明细
     * 
     * GET /api/balance-occupy/page
     * 
     * 调用链：
     * HTTP → BalanceOccupyController → BalanceOccupyService 
     *      → Dubbo RPC → BalanceOccupyProvider 
     *      → OfBalanceOccupyRecordMapper.occupationDetailPage (新增方法)
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @GetMapping("/page")
    public Map<String, Object> queryOccupationDetailPage(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "ACTIVE") String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return balanceOccupyService.queryOccupationDetailPage(userId, status, pageNum, pageSize);
    }

    /**
     * 根据订单ID查询占用记录
     * 
     * GET /api/balance-occupy/order/{orderId}
     * 
     * @param orderId 订单ID
     * @return 占用记录列表
     */
    @GetMapping("/order/{orderId}")
    public List<Map<String, Object>> getOccupyRecordsByOrder(@PathVariable Long orderId) {
        return balanceOccupyService.getOccupyRecordsByOrder(orderId);
    }

    /**
     * 【新增接口】批量查询占用详情
     * 
     * POST /api/balance-occupy/batch
     * 
     * @param ids 记录ID列表
     * @return 占用详情列表
     */
    @PostMapping("/batch")
    public List<Map<String, Object>> batchGetOccupyRecords(@RequestBody List<Long> ids) {
        return balanceOccupyService.batchGetOccupyRecords(ids);
    }
}
