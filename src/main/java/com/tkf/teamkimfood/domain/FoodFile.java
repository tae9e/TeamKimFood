package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "foodfile")
public class FoodFile {

    @Id @Column(name = "foodfile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String fileName;
    private String fileType;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "foodFile", cascade = CascadeType.ALL)
    private List<FoodImg> foodImgs = new ArrayList<>();

    @Builder
    public FoodFile(String fileName, String fileType, LocalDateTime createdAt) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.createdAt = createdAt;
    }

    //연관관게
    public void SetMember(Member member){
        this.member = member;
        member.getFoodFiles().add(this);
    }

}
