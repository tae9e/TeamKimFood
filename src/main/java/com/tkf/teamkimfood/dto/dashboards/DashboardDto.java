package com.tkf.teamkimfood.dto.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

    private Long totalUsers;
    private Long activeUsers;
    private Long totalRecipes;
    private String popularRecipe;

}
