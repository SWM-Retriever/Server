package org.retriever.server.dailypet.domain.diary.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.diary.dto.request.CreateDiaryRequest;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.model.IsDeleted;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "is_deleted = 'FALSE'")
@SQLDelete(sql = "UPDATE diary SET is_deleted = 'TRUE' WHERE diary_id = ?")
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    private LocalDate publishDate;

    private String diaryText;

    private String diaryImageUrl;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", nullable = false)
    private Family family;

    public static Diary of(Member member, Family family, CreateDiaryRequest createDiaryRequest) {
        return Diary.builder()
                .diaryText(createDiaryRequest.getDiaryText())
                .diaryImageUrl(createDiaryRequest.getDiaryImageUrl())
                .publishDate(LocalDate.now())
                .isDeleted(IsDeleted.FALSE)
                .author(member)
                .family(family)
                .build();
    }

    public void editText(String newText) {
        this.diaryText = newText;
    }
}
