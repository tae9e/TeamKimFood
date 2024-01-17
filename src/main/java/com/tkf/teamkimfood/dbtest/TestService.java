package com.tkf.teamkimfood.dbtest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public Long join(JoinEx joinEx){
        TestEntity test1 = TestEntity.builder()
                .age(joinEx.age1)
                .email(joinEx.email1)
                .name(joinEx.name1)
                .password(joinEx.password1)
                .regTime(LocalDateTime.now())
                .build();
        testRepository.save(test1);
        return test1.getId();
    }

    public List<TestDto> findAll(){
        List<TestEntity> allMembers = testRepository.findAll();
        return allMembers.stream()
                .map(TestDto::new)
                .toList();
    }


}
