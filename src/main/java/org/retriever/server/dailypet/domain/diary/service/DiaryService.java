package org.retriever.server.dailypet.domain.diary.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.diary.dto.request.CreateDiaryRequest;
import org.retriever.server.dailypet.domain.diary.dto.response.DiaryView;
import org.retriever.server.dailypet.domain.diary.dto.response.GetGroupDiaryResponse;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.diary.repository.DiaryRepository;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final SecurityUtil securityUtil;
    private final FamilyRepository familyRepository;

    public GetGroupDiaryResponse getGroupDiaries(Long familyId) {
        List<Diary> diaryList = diaryRepository.findAllByFamily_FamilyId(familyId);

        return GetGroupDiaryResponse.from(getDiaryResponse(diaryList));
    }

    private List<DiaryView> getDiaryResponse(List<Diary> diaryList) {

        TreeMap<LocalDate, List<Diary>> diariesPerPublishDate = diaryList.stream()
                .collect(Collectors.groupingBy(
                        Diary::getPublishDate,
                        TreeMap::new,
                        Collectors.toList())
                );

        List<DiaryView> diaryResponse = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Diary>> diaryEntry : diariesPerPublishDate.entrySet()) {
            diaryResponse.add(DiaryView.createDataView(diaryEntry.getKey()));
            for (Diary diary : diaryEntry.getValue()) {
                diaryResponse.add(DiaryView.createContentView(diary));
            }
        }
        return diaryResponse;
    }


    public void createDiary(Long familyId, CreateDiaryRequest request) {
        Member author = securityUtil.getMemberByUserDetails();
        // TODO util로 id로 객체 조회하는 메서드들 따로 빼기 (매번 orElseThrow해야 함)
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);
        Diary newDiary = Diary.of(author, family, request);
        author.createDiary(newDiary);
        family.linkDiary(newDiary);
        diaryRepository.save(newDiary);
    }
}
