package com.tkf.teamkimfood.dto.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatsDto {

    private String date;
    private Long count;

}
