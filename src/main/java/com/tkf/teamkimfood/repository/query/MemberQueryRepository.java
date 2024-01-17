package com.tkf.teamkimfood.repository.query;

import com.tkf.teamkimfood.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberQueryRepository {
    @PersistenceContext
    EntityManager em;

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

}
