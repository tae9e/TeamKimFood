package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.dto.dashboards.DailyStatsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    @PersistenceContext
    private EntityManager em;

    public List<DailyStatsDto> getDailyUserActivityStats() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DailyStatsDto> query = cb.createQuery(DailyStatsDto.class);
        Root<Member> member = query.from(Member.class);

        // 날짜 별로 데이터를 그룹화
        Expression<String> groupByDate = cb.function(
                "DATE_FORMAT", String.class, member.get("createdDate"), cb.literal("%Y-%m-%d")
        );
        query.multiselect(groupByDate, cb.count(member)).groupBy(groupByDate);

        return em.createQuery(query).getResultList();
    }
}
