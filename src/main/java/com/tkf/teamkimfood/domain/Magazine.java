package com.tkf.teamkimfood.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "magazines")
public class Magazine {

    @Id @Column(name = "magazines_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String imgName;
    private LocalDateTime writeDate;
    private LocalDateTime correctionDate;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL)
    private List<FoodImg> foodImgs = new ArrayList<>();

    //메거진 데이터 삽입시 사용해주세요
    @Builder
    public Magazine(String title, String content, String imgName, LocalDateTime writeDate, LocalDateTime correctionDate) {
        this.title = title;
        this.content = content;
        this.imgName = imgName;
        this.writeDate = writeDate;
        this.correctionDate = correctionDate;
    }

    //연관관계 메서드
    public void setMember(Member member){
        this.member = member;
        member.getMagazines().add(this);
    }
}
