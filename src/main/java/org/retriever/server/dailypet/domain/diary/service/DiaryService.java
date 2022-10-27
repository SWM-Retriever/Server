package org.retriever.server.dailypet.domain.diary.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.diary.dto.request.CreateDiaryRequest;
import org.retriever.server.dailypet.domain.diary.dto.request.EditDiaryRequest;
import org.retriever.server.dailypet.domain.diary.dto.response.DiaryView;
import org.retriever.server.dailypet.domain.diary.dto.response.GetGroupDiaryResponse;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.diary.exception.DiaryNotFoundException;
import org.retriever.server.dailypet.domain.diary.repository.DiaryQueryRepository;
import org.retriever.server.dailypet.domain.diary.repository.DiaryRepository;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final SecurityUtil securityUtil;
    private final FamilyRepository familyRepository;
    private final DiaryQueryRepository diaryQueryRepository;

    @Transactional(readOnly = true)
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
            diaryEntry.getValue().sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
            for (Diary diary : diaryEntry.getValue()) {
                diaryResponse.add(DiaryView.createContentView(diary));
            }
        }
        return diaryResponse;
    }

    public DiaryView getRecentDiary(Long familyId) {
        Diary recentDiary = diaryQueryRepository.findRecentDiary(familyId);
        if (recentDiary == null) {
            throw new DiaryNotFoundException();
        }
        return DiaryView.createContentView(recentDiary);
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

    public void editDiary(Long familyId, Long diaryId, EditDiaryRequest request) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(DiaryNotFoundException::new);
        diary.editText(request.getDiaryText());
    }

    public void deleteDiary(Long familyId, Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
