package com.tkf.teamkimfood.repository;

import com.tkf.teamkimfood.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
