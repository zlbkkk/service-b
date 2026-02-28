package com.example.serviceb.client;

import com.example.common.service.OfTransactionService;
import org.springframework.stereotype.Component;

/**
 * 融资交易 Dubbo 客户端
 * 通过 Dubbo 调用 service-a 的 OfTransactionProvider
 */
@Component
public class OfTransactionClient implements OfTransactionService {

    // 实际项目中这里会使用 @DubboReference 注解注入远程服务
    // @DubboReference(version = "1.0.0", timeout = 5000)
    // private OfTransactionService ofTransactionService;
    
    /**
     * 模拟 Dubbo 调用：查询主发票总数
     * 
     * @param companyId 企业ID
     * @return 主发票总数
     */
    @Override
    public Integer queryMainInvoiceTotal(String companyId) {
        // 实际项目中会通过 Dubbo RPC 调用 service-a 的方法
        // return ofTransactionService.queryMainInvoiceTotal(companyId);
        
        // 这里模拟 Dubbo 调用返回结果
        System.out.println("[Dubbo调用] service-b -> service-a: queryMainInvoiceTotal(" + companyId + ")");
        
        // 模拟返回数据
        if ("COMP001".equals(companyId)) return 156;
        if ("COMP002".equals(companyId)) return 89;
        if ("COMP003".equals(companyId)) return 234;
        return 0;
    }
    
    /**
     * 模拟 Dubbo 调用：查询收入发票总数
     * 
     * @param companyId 企业ID
     * @return 收入发票总数
     */
    @Override
    public Integer queryIncomeInvoiceTotal(String companyId) {
        // 实际项目中会通过 Dubbo RPC 调用 service-a 的方法
        // return ofTransactionService.queryIncomeInvoiceTotal(companyId);
        
        // 这里模拟 Dubbo 调用返回结果
        System.out.println("[Dubbo调用] service-b -> service-a: queryIncomeInvoiceTotal(" + companyId + ")");
        
        // 模拟返回数据
        if ("COMP001".equals(companyId)) return 203;
        if ("COMP002".equals(companyId)) return 145;
        if ("COMP003".equals(companyId)) return 312;
        return 0;
    }
}
