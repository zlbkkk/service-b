package com.example.serviceb.controller;

import com.example.serviceb.service.OfProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目管理 Controller
 * 
 * 对应前端项目 beehive-order-finance-frontend 的 API 调用：
 * - src/api/orderApi/controller/ofProjectController.js
 * 
 * API 基础路径: /order-scfPc-web/ofProject
 * 
 * 调用链：
 * 前端 Vue -> 本 Controller -> OfProjectService -> service-a Provider (Dubbo)
 */
@RestController
@RequestMapping("/order-scfPc-web/ofProject")
public class OfProjectController {

    @Autowired
    private OfProjectService ofProjectService;

    /**
     * 分页查询项目列表
     * 对应前端: ofProjectController.js -> listPage()
     * 
     * @param params 查询参数
     * @return 分页数据
     */
    @PostMapping("/listPage")
    public Map<String, Object> listPage(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofProjectService.listPage(params);
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
     * 新增项目
     * 对应前端: ofProjectController.js -> addProject()
     * 
     * @param project 项目数据
     * @return 操作结果
     */
    @PostMapping("/addProject")
    public Map<String, Object> addProject(@RequestBody Map<String, Object> project) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = ofProjectService.addProject(project);
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
     * 更新项目
     * 对应前端: ofProjectController.js -> updateProject()
     * 
     * @param project 项目数据
     * @return 操作结果
     */
    @PostMapping("/updateProject")
    public Map<String, Object> updateProject(@RequestBody Map<String, Object> project) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = ofProjectService.updateProject(project);
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
     * 根据买方查询项目列表
     * 对应前端: ofProjectController.js -> listProjectByBuyer()
     * 
     * @param buyerId 买方ID
     * @return 项目列表
     */
    @PostMapping("/listProjectByBuyer")
    public Map<String, Object> listProjectByBuyer(@RequestParam String buyerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> data = ofProjectService.listProjectByBuyer(buyerId);
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
     * 获取在线项目信息
     * 对应前端: ofProjectController.js -> getOnlineProject()
     * 
     * @param projectId 项目ID
     * @return 项目信息
     */
    @GetMapping("/getOnlineProject")
    public Map<String, Object> getOnlineProject(@RequestParam String projectId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = ofProjectService.getOnlineProject(projectId);
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
     * 同步贷后材料至资金方
     * 对应前端: ofProjectController.js -> syncPostLoan()
     * 
     * @param ofProjectId 项目ID
     * @return 同步结果
     */
    @GetMapping("/syncPostLoan")
    public Map<String, Object> syncPostLoan(@RequestParam String ofProjectId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = ofProjectService.syncPostLoan(ofProjectId);
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
