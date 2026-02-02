package com.example.serviceb.service;

import com.example.servicea.provider.BalanceOccupyProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 余额占用服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 BalanceOccupyProvider
 * 测试场景：验证 Mapper 层变更的跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@Service
public class BalanceOccupyService {

    /**
     * 通过 Dubbo RPC 注入 BalanceOccupyProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 BalanceOccupyProvider
     */
    @DubboReference(version = "1.0.0", timeout = 10000, retries = 2)
    private BalanceOccupyProvider balanceOccupyProvider;

    /**
     * 根据ID查询占用记录
     * 
     * @param id 记录ID
     * @return 占用记录
     */
    public Map<String, Object> getOccupyRecord(Long id) {
        // 【Dubbo RPC 调用】调用 service-a 的 Provider
        return balanceOccupyProvider.getOccupyRecordById(id);
    }

    /**
     * 【调用新增方法】分页查询占用明细
     * 
     * 调用 service-a 新增的 BalanceOccupyProvider.queryOccupationDetailPage 方法
     * 该方法内部调用了 OfBalanceOccupyRecordMapper.occupationDetailPage
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public Map<String, Object> queryOccupationDetailPage(
            Long userId, String status, int pageNum, int pageSize) {
        // 【Dubbo RPC 调用】调用 service-a 新增的分页查询方法
        return balanceOccupyProvider.queryOccupationDetailPage(userId, status, pageNum, pageSize);
    }

    /**
     * 根据订单ID查询占用记录
     * 
     * @param orderId 订单ID
     * @return 占用记录列表
     */
    public List<Map<String, Object>> getOccupyRecordsByOrder(Long orderId) {
        // 【Dubbo RPC 调用】调用 service-a 的 Provider
        return balanceOccupyProvider.getOccupyRecordsByOrderId(orderId);
    }

    /**
     * 【调用新增方法】批量查询占用详情
     * 
     * @param ids 记录ID列表
     * @return 占用详情列表
     */
    public List<Map<String, Object>> batchGetOccupyRecords(List<Long> ids) {
        // 【Dubbo RPC 调用】调用 service-a 的批量查询方法
        return balanceOccupyProvider.batchGetOccupyRecords(ids);
    }
}
