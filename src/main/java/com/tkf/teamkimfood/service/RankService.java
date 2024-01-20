package com.tkf.teamkimfood.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService {
    //조회수 관련은 RecipeService에서 했습니다.
    //recipe 추천수에따른 조회, 추천수 증가 감소 로직
    //멤버,레시피 사이에 둘을 잇는 엔티티 새로 만들어야함.
    //멤버가 해당 레시피에 대한 추천은 default는 false 프론트에서 추천을 누를시 값이 true 로 변경
    //이미 해당 레시피에 추천을 준 경우 값을 false로 변경
    //레시피, 멤버와는 1:N이 될 것(생성될 엔티티가)
    //멤버는 여러 게시글에 추천을 줄 수 있다. 한 게시글엔 1개의 추천만 줄 수 있다. 레시피는 여러명의 멤버에게 추천을 받을 수 있다.
    //랭킹 엔티티엔 boolean으로 추천 1개만 넣고, 추천수는 Wildcard.count, .where(entity.active.eq(isActive))로 하면 될 것.
}
