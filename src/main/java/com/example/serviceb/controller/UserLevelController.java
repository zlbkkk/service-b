package com.example.serviceb.controller;

import com.example.serviceb.service.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户等级控制器
 * 
 * 暴露 HTTP 接口，内部通过 Dubbo RPC 调用 service-a 的 UserInfoProvider
 * 
 * 完整调用链：
 * HTTP API → Controller → Service → Dubbo RPC → Provider (service-a)
 * 
 * ⚠️ 重点测试：UserInfoProvider.getUserLevel() 返回值结构变更的影响
 * 
 * @author system
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    /**
     * 获取用户基本信息
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.getUserInfo()
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public Map<String, Object> getUserInfo(@PathVariable Long userId) {
        return userLevelService.getUserInfo(userId);
    }

    /**
     * 检查用户是否存在
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.checkUserExists()
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    @GetMapping("/{userId}/exists")
    public Boolean checkUserExists(@PathVariable Long userId) {
        return userLevelService.checkUserExists(userId);
    }

    /**
     * 【修改接口】获取用户等级信息
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.getUserLevel()
     * 
     * ⚠️ 重要：这个接口调用了 service-a 中被修改的 Provider 方法
     * service-a 的 getUserLevel 方法返回值增加了新字段：
     * - discount (会员折扣)
     * - freeShipping (是否包邮)
     * - levelUpgradeDate (升级日期)
     * 
     * @param userId 用户ID
     * @return 用户等级信息（包含新增字段）
     */
    @GetMapping("/{userId}/level")
    public Map<String, Object> getUserLevel(@PathVariable Long userId) {
        return userLevelService.getUserLevel(userId);
    }

    /**
     * 【新增接口】获取用户折扣信息
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.getUserLevel()
     * ⚠️ 使用 service-a 返回的新字段 discount 和 freeShipping
     * 
     * @param userId 用户ID
     * @return 折扣信息
     */
    @GetMapping("/{userId}/discount")
    public Map<String, Object> getUserDiscount(@PathVariable Long userId) {
        return userLevelService.getUserDiscount(userId);
    }

    /**
     * 【新增接口】更新用户信息
     * 
     * 调用链：HTTP PUT → UserLevelService → Dubbo RPC → UserInfoProvider.updateUserInfo()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param updateData 更新数据
     * @return 更新结果
     */
    @PutMapping("/{userId}")
    public Map<String, Object> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updateData) {
        return userLevelService.updateUserInfo(userId, updateData);
    }

    /**
     * 【新增接口】获取用户积分明细
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.getUserPointsHistory()
     * ⚠️ 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 积分明细
     */
    @GetMapping("/{userId}/points/history")
    public Map<String, Object> getUserPointsHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userLevelService.getUserPointsHistory(userId, pageNum, pageSize);
    }

    /**
     * 【新增接口】获取用户完整信息
     * 
     * 调用链：HTTP GET → UserLevelService → 多个 Dubbo RPC 调用
     * 
     * 涉及的 Dubbo 调用：
     * 1. UserInfoProvider.getUserInfo()
     * 2. UserInfoProvider.getUserLevel() ⚠️ 使用修改后的返回值结构
     * 3. UserInfoProvider.getUserPointsHistory()
     * 
     * @param userId 用户ID
     * @return 用户完整信息
     */
    @GetMapping("/{userId}/full")
    public Map<String, Object> getUserFullInfo(@PathVariable Long userId) {
        return userLevelService.getUserFullInfo(userId);
    }

    /**
     * 【新增接口】计算订单折扣价格
     * 
     * 调用链：HTTP GET → UserLevelService → Dubbo RPC → UserInfoProvider.getUserLevel()
     * ⚠️ 使用 service-a 返回的新字段 discount 和 freeShipping 进行计算
     * 
     * @param userId 用户ID
     * @param originalPrice 原价
     * @return 折扣后价格信息
     */
    @GetMapping("/{userId}/calculate-discount")
    public Map<String, Object> calculateDiscountPrice(
            @PathVariable Long userId,
            @RequestParam Double originalPrice) {
        return userLevelService.calculateDiscountPrice(userId, originalPrice);
    }
}
