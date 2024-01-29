package com.tkf.teamkimfood.service.dashboards;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.dto.dashboards.DailyStatsDto;
import com.tkf.teamkimfood.dto.dashboards.DashboardDto;
import com.tkf.teamkimfood.dto.dashboards.MemberManagementDto;
import com.tkf.teamkimfood.dto.dashboards.RecipeManagementDto;
import com.tkf.teamkimfood.repository.DashboardRepository;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardService {

    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;
    private final DashboardRepository dashboardRepository;

    public DashboardDto getDashboards() {
        DashboardDto dashboardDto = new DashboardDto();

        dashboardDto.setTotalUsers(getTotalUsers());
//        dashboardDto.setActiveUsers(getActiveUsers());
        dashboardDto.setTotalRecipes(getTotalRecipes());
        return dashboardDto;
    }

    public List<DailyStatsDto> getDailyStats() {
        return dashboardRepository.getDailyUserActivityStats();
    }

    private Long getTotalUsers() {
        return memberRepository.count();
    }
    // 혹시 멤버에다 private boolean active;값 추가하려는데 괜찮을까요
//   Member member = memberRepository.findByEmail(email);
//        if (member != null) {
//        member.setActive(true);
//        memberRepository.save(member);
//    }
//    Member member = memberRepository.findByEmail(email);
//        if (member != null) {
//        member.setActive(false);
//        memberRepository.save(member);
//    }
    //접속유저수
//    private Long getActiveUsers() {
//
//    }
    private Long getTotalRecipes() {
        return recipeRepository.count();
    }

    //멤버관리를위해 전체 멤버 불러오기
    public List<MemberManagementDto> getUsersToDashboard() {
        List<Member> allUsers = memberRepository.findAll();

        return allUsers.stream()
                .map(m -> new MemberManagementDto(m.getId(), m.getName(), m.getEmail(), m.getNickname(), m.getPhoneNumber()))
                .toList();
    }

    //레시피 관리
    public List<RecipeManagementDto> getAllRecipeForManage() {
        return recipeRepository.findAllByMemberNickNameFromRecipe();
    }

}
