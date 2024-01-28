package com.tkf.teamkimfood.repository.rank;

import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.Rank;
import com.tkf.teamkimfood.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findByMemberAndRecipe(Member member, Recipe recipe);
}
