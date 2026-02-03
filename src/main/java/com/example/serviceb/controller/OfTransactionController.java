package com.example.serviceb.controller;

import com.example.serviceb.service.OfTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Controller
 * 
 * 对应前端项目 beehive-order-finance-frontend 的 API 调用：
 * - src/api/orderApi/controller/ofTransactionController.js
 * 
 * API 基础路径: /order-scfPc-web/ofTransaction
 * 
 * 调用链：
 * 前端 Vue -> 本 Controller -> OfTransactionService -> service-a Provider (Dubbo)
 */
@RestController
@RequestMapping("/order-scfPc-web/ofTransaction")
public class OfTransactionController {

    @Autowired
    private OfTransactionService ofTransactionService;

    /**
     * 分页查询融资交易列表
     * 对应前端: ofTransactionController.js -> ofTransactionPage()
     * 
     * @param params 查询参数
     * @return 分页数据
     */
    @PostMapping("/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService.page(params);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    /**
     * 获取融资汇总信息
     * 对应前端: ofTransactionController.js -> getTransactionSumInfo()
     * 
     * @return 汇总信息
     */
    @PostMapping("/getTransactionSumInfo")
    public Map<String, Object> getTransactionSumInfo() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService.getTransactionSumInfo();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    /**
     * 获取交易详情
     * 对应前端: ofTransactionController.js -> getTransactionDetail()
     * 
     * @param transactionNo 交易编号
     * @return 交易详情
     */
    @PostMapping("/getTransactionDetail")
    public Map<String, Object> getTransactionDetail(@RequestParam String transactionNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService.getTransactionDetail(transactionNo);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    /**
     * 获取供应商上一次回款账户信息
     * 对应前端: ofTransactionController.js -> getSpyLastReturnAccount()
     * 
     * @return 账户信息
     */
    @PostMapping("/getSpyLastReturnAccount")
    public Map<String, Object> getSpyLastReturnAccount() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofTransactionService.getSpyLastReturnAccount();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    /**
     * 更新融资信息
     * 对应前端: ofTransactionController.js -> updateTransactionInfo()
     * 
     * @param transactionInfo 融资信息
     * @return 操作结果
     */
    @PostMapping("/updateTransactionInfo")
    public Map<String, Object> updateTransactionInfo(@RequestBody Map<String, Object> transactionInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = ofTransactionService.updateTransactionInfo(transactionInfo);
            result.put("code", success ? 200 : 400);
            result.put("message", success ? "success" : "failed");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }
        return result;
    }

    /**
     * 批量删除交易
     * 对应前端: ofTransactionController.js -> batchDel()
     * 
     * @param transactionNos 交易编号列表
     * @return 操作结果
     */
    @PostMapping("/batchDel")
    public Map<String, Object> batchDel(@RequestParam List<String> transactionNos) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = ofTransactionService.batchDel(transactionNos);
            result.put("code", success ? 200 : 400);
            result.put("message", success ? "success" : "failed");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", false);
        }
        return result;
    }
}
