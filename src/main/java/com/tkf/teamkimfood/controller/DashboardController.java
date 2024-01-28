package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.dashboards.DailyStatsDto;
import com.tkf.teamkimfood.dto.dashboards.DashboardDto;
import com.tkf.teamkimfood.dto.dashboards.MemberManagementDto;
import com.tkf.teamkimfood.dto.dashboards.RecipeManagementDto;
import com.tkf.teamkimfood.service.dashboards.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardDto getDashboardStats() {
        return dashboardService.getDashboards();
    }

    @GetMapping("/stats/daily")
    public List<DailyStatsDto> getDailyStats() {
        return dashboardService.getDailyStats();
    }

    @GetMapping("/memberManagement")
    public List<MemberManagementDto> getAllMembers() {
        return dashboardService.getUsersToDashboard();
    }

    @GetMapping("/recipeManagement")
    public List<RecipeManagementDto> getAllRecipes() {
        return dashboardService.getAllRecipeForManage();
    }
}
