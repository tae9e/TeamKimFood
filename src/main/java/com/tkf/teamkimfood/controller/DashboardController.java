package com.tkf.teamkimfood.controller;

import com.tkf.teamkimfood.dto.dashboards.DailyStatsDto;
import com.tkf.teamkimfood.dto.dashboards.DashboardDto;
import com.tkf.teamkimfood.service.dashboards.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin/dashboard/stats")
    public DashboardDto getDashboardStats() {
        return dashboardService.getDashboards();
    }

    @GetMapping("/admin/dashboard/stats/daily")
    public List<DailyStatsDto> getDailyStats() {
        return dashboardService.getDailyStats();
    }
}
