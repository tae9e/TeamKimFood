package com.tkf.teamkimfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkf.teamkimfood.domain.Member;
import com.tkf.teamkimfood.domain.status.MemberRole;
import com.tkf.teamkimfood.dto.aboutrecipe.CategoryPreferenceDto;
import com.tkf.teamkimfood.dto.aboutrecipe.RecipeDto;
import com.tkf.teamkimfood.repository.MemberRepository;
import com.tkf.teamkimfood.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    public Member setMember(){
        return Member.builder()
                .id(1L)
                .nickname("테스트")
                .email("test@test.com")
                .password("1q2w3e4r")
                .phoneNumber("010-0000-0000")
                .memberRole(MemberRole.USER)
                .build();
    }


    @Test
    @WithMockUser
    public void testSaveRecipe() throws Exception {

        // 서비스 모의 객체 설정
        Member member = setMember();
        memberRepository.save(member);
//        when(recipeService.saveRecipe(anyLong(), any(RecipeDto.class), any(CategoryPreferenceDto.class), anyList(),anyList(), anyList()))
//                .thenReturn(1L); // 예상 반환 값
        // 테스트할 요청 본문
//
        String jsonRequest = "{"
                + "\"memberId\": \"1\","
                + "\"memberRole\": \"U\","
                + "\"recipeDto\": {"
                + "    \"title\": \"test\","
                + "    \"content\": \"123\""
                + "},"
                + "\"categoryPreferenceDto\": {"
                + "    \"situation\": \"TOGETHER\","
                + "    \"foodStuff\": \"EGG\","
                + "    \"foodNationType\": \"ETCWESTERN\""
                + "},"
                + "\"recipeDetailListDto\": [{"
                + "    \"ingredients\": \"123\","
                + "    \"dosage\": \"123\""
                + "}],"
                + "\"explanations\": [\"213r213r21\"]"  // 쉼표 제거
                + "}";
        String path = "C:/shop/item/";
        String imgName = "image"+"test"+".jpg";
        MockMultipartFile mockFile = new MockMultipartFile(
                path, // form에서 사용되는 field name
                imgName, // 업로드될 파일명
                "image/jpeg", // 파일 타입 (MIME 타입)
                new byte[10] // 파일 컨텐츠 (실제 데이터)
        );
        List<MockMultipartFile> mockFileList = new ArrayList<>();
        mockFileList.add(mockFile);
        String originalFilename = mockFile.getOriginalFilename();
        log.info(originalFilename);
        // POST 요청 모의 실행
//        mockMvc.perform(post("/api/recipes/save")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isOk()); // 상태 코드 검증
        mockFileList.stream().map(mockMultipartFile -> {

                try {
                    return mockMvc.perform(MockMvcRequestBuilders.multipart("/api/recipes/save")
                                    .file("foodImgFileList", mockMultipartFile.getBytes())
                                    .param("recipeRequest", jsonRequest)
                                    .with(csrf())
                                    .contentType(MediaType.MULTIPART_FORM_DATA))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

        });
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/recipes/save")
//                        .file(recipeRequestPart)
//                        .file(filePart)
//                        .with(csrf())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());


        // 서비스 메서드 호출 확인
//        verify(recipeService, times(1)).saveRecipe(anyLong(), any(RecipeDto.class), any(CategoryPreferenceDto.class), anyList(), anyList(), anyList());
    }
}