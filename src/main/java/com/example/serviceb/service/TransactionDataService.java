package com.example.serviceb.service;

import com.example.servicea.provider.TransactionDataProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 交易数据服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 TransactionDataProvider
 * 测试场景：验证 Mapper 层高风险变更的跨项目影响识别
 * 
 * 变更类型测试覆盖：
 * 1. Mapper 方法新增 - setNull4SingleCeConfirm, setNull4DoubleCeConfirm
 * 2. 高风险数据更新操作
 * 
 * @author system
 * @version 1.0
 */
@Service
public class TransactionDataService {

    /**
     * 通过 Dubbo RPC 注入 TransactionDataProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 TransactionDataProvider
     */
    @DubboReference(version = "1.0.0", timeout = 10000, retries = 2)
    private TransactionDataProvider transactionDataProvider;

    /**
     * 根据ID查询交易记录
     * 
     * @param id 交易ID
     * @return 交易记录
     */
    public Map<String, Object> getTransaction(Long id) {
        // 【Dubbo RPC 调用】调用 service-a 的 Provider
        return transactionDataProvider.getTransactionById(id);
    }

    /**
     * 更新交易状态
     * 
     * @param id 交易ID
     * @param status 新状态
     * @return 是否成功
     */
    public boolean updateStatus(Long id, String status) {
        // 【Dubbo RPC 调用】调用 service-a 的 Provider
        return transactionDataProvider.updateTransactionStatus(id, status);
    }

    /**
     * 【调用新增方法】重置单次确真字段
     * 
     * ⚠️ 高风险操作：调用 service-a 新增的 TransactionDataProvider.resetSingleCeConfirm 方法
     * 该方法内部调用了 OfTransactionMapper.setNull4SingleCeConfirm
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public Map<String, Object> resetSingleConfirm(Long transactionId) {
        // 【Dubbo RPC 调用】调用 service-a 新增的重置单次确真方法
        return transactionDataProvider.resetSingleCeConfirm(transactionId);
    }

    /**
     * 【调用新增方法】重置双确真字段
     * 
     * ⚠️ 高风险操作：调用 service-a 新增的 TransactionDataProvider.resetDoubleCeConfirm 方法
     * 该方法内部调用了 OfTransactionMapper.setNull4DoubleCeConfirm
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public Map<String, Object> resetDoubleConfirm(Long transactionId) {
        // 【Dubbo RPC 调用】调用 service-a 新增的重置双确真方法
        return transactionDataProvider.resetDoubleCeConfirm(transactionId);
    }

    /**
     * 根据用户ID和状态查询交易列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 交易列表
     */
    public List<Map<String, Object>> getTransactionsByUserAndStatus(Long userId, String status) {
        // 【Dubbo RPC 调用】调用 service-a 的 Provider
        return transactionDataProvider.getTransactionsByUserAndStatus(userId, status);
    }

    /**
     * 【调用新增方法】批量更新确真状态
     * 
     * @param transactionIds 交易ID列表
     * @param confirmStatus 确真状态
     * @return 操作结果
     */
    public Map<String, Object> batchUpdateConfirmStatus(List<Long> transactionIds, String confirmStatus) {
        // 【Dubbo RPC 调用】调用 service-a 的批量更新方法
        return transactionDataProvider.batchUpdateConfirmStatus(transactionIds, confirmStatus);
    }

    /**
     * 【调用新增方法】查询待确真的交易列表
     * 
     * @param companyId 公司ID
     * @param confirmType 确真类型
     * @return 待确真交易列表
     */
    public List<Map<String, Object>> getPendingConfirmTransactions(Long companyId, String confirmType) {
        // 【Dubbo RPC 调用】调用 service-a 的查询方法
        return transactionDataProvider.getPendingConfirmTransactions(companyId, confirmType);
    }
}
