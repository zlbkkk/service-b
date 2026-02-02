package com.example.serviceb.service;

import com.example.servicea.provider.UserInfoProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户等级服务
 * 
 * 通过 Dubbo RPC 调用 service-a 的 UserInfoProvider
 * 测试场景：验证系统能否识别 Dubbo Provider 返回值结构变更的影响
 * 
 * @author system
 * @version 1.0
 */
@Service
public class UserLevelService {

    /**
     * 通过 Dubbo RPC 注入 UserInfoProvider
     * 【关键调用点】service-b 通过 Dubbo 调用 service-a 的 Provider
     */
    @DubboReference(version = "1.0.0", timeout = 3000)
    private UserInfoProvider userInfoProvider;

    /**
     * 获取用户基本信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    public Map<String, Object> getUserInfo(Long userId) {
        // 【Dubbo RPC 调用】调用 service-a 的 UserInfoProvider.getUserInfo()
        return userInfoProvider.getUserInfo(userId);
    }

    /**
     * 检查用户是否存在
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    public Boolean checkUserExists(Long userId) {
        // 【Dubbo RPC 调用】调用 service-a 的 UserInfoProvider.checkUserExists()
        return userInfoProvider.checkUserExists(userId);
    }

    /**
     * 【修改方法】获取用户等级信息
     * 
     * ⚠️ 重要：这个方法调用了 service-a 中被修改的 Provider 方法
     * service-a 的 getUserLevel 方法返回值增加了新字段：
     * - discount (会员折扣)
     * - freeShipping (是否包邮)
     * - levelUpgradeDate (升级日期)
     * 
     * @param userId 用户ID
     * @return 用户等级信息（包含新增字段）
     */
    public Map<String, Object> getUserLevel(Long userId) {
        // 【Dubbo RPC 调用】调用 service-a 修改后的方法
        // ⚠️ 返回值结构变更：增加了 discount, freeShipping, levelUpgradeDate 字段
        return userInfoProvider.getUserLevel(userId);
    }

    /**
     * 【新增方法】获取用户折扣信息
     * 
     * 使用 service-a 返回的新字段 discount
     * 
     * @param userId 用户ID
     * @return 折扣信息
     */
    public Map<String, Object> getUserDiscount(Long userId) {
        // 【Dubbo RPC 调用】获取用户等级信息
        Map<String, Object> levelInfo = userInfoProvider.getUserLevel(userId);
        
        Map<String, Object> discountInfo = new HashMap<>();
        discountInfo.put("userId", userId);
        discountInfo.put("level", levelInfo.get("level"));
        discountInfo.put("levelName", levelInfo.get("levelName"));
        
        // ⚠️ 使用新增的字段
        discountInfo.put("discount", levelInfo.get("discount"));
        discountInfo.put("freeShipping", levelInfo.get("freeShipping"));
        
        return discountInfo;
    }

    /**
     * 【新增方法】更新用户信息
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param updateData 更新数据
     * @return 更新结果
     */
    public Map<String, Object> updateUserInfo(Long userId, Map<String, Object> updateData) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 updateUserInfo()
        return userInfoProvider.updateUserInfo(userId, updateData);
    }

    /**
     * 【新增方法】获取用户积分明细
     * 
     * 调用 service-a 新增的 Provider 方法
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 积分明细
     */
    public Map<String, Object> getUserPointsHistory(Long userId, Integer pageNum, Integer pageSize) {
        // 【Dubbo RPC 调用】调用 service-a 新增的方法 getUserPointsHistory()
        return userInfoProvider.getUserPointsHistory(userId, pageNum, pageSize);
    }

    /**
     * 【新增方法】获取用户完整信息
     * 
     * 组合多个 Provider 调用，生成用户完整信息
     * 
     * @param userId 用户ID
     * @return 用户完整信息
     */
    public Map<String, Object> getUserFullInfo(Long userId) {
        Map<String, Object> fullInfo = new HashMap<>();
        
        // 【Dubbo RPC 调用 1】获取基本信息
        Map<String, Object> basicInfo = userInfoProvider.getUserInfo(userId);
        fullInfo.putAll(basicInfo);
        
        // 【Dubbo RPC 调用 2】获取等级信息（包含新增字段）
        Map<String, Object> levelInfo = userInfoProvider.getUserLevel(userId);
        fullInfo.put("levelInfo", levelInfo);
        
        // ⚠️ 提取新增的字段
        fullInfo.put("discount", levelInfo.get("discount"));
        fullInfo.put("freeShipping", levelInfo.get("freeShipping"));
        fullInfo.put("levelUpgradeDate", levelInfo.get("levelUpgradeDate"));
        
        // 【Dubbo RPC 调用 3】获取积分明细
        Map<String, Object> pointsHistory = userInfoProvider.getUserPointsHistory(userId, 1, 10);
        fullInfo.put("pointsHistory", pointsHistory);
        
        return fullInfo;
    }

    /**
     * 【新增方法】计算订单折扣价格
     * 
     * 使用 service-a 返回的 discount 字段计算折扣价格
     * 
     * @param userId 用户ID
     * @param originalPrice 原价
     * @return 折扣后价格
     */
    public Map<String, Object> calculateDiscountPrice(Long userId, Double originalPrice) {
        // 【Dubbo RPC 调用】获取用户等级信息
        Map<String, Object> levelInfo = userInfoProvider.getUserLevel(userId);
        
        // ⚠️ 使用新增的 discount 字段
        Double discount = (Double) levelInfo.get("discount");
        Boolean freeShipping = (Boolean) levelInfo.get("freeShipping");
        
        Double discountPrice = originalPrice * discount;
        Double shippingFee = freeShipping ? 0.0 : 10.0;
        Double finalPrice = discountPrice + shippingFee;
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("originalPrice", originalPrice);
        result.put("discount", discount);
        result.put("discountPrice", discountPrice);
        result.put("freeShipping", freeShipping);
        result.put("shippingFee", shippingFee);
        result.put("finalPrice", finalPrice);
        
        return result;
    }
}
