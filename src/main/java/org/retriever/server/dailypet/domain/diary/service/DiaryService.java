package org.retriever.server.dailypet.domain.diary.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.diary.dto.response.DiaryView;
import org.retriever.server.dailypet.domain.diary.dto.response.GetGroupDiaryResponse;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.diary.repository.DiaryRepository;
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

    public GetGroupDiaryResponse getGroupDiary(Long familyId) {
        List<Diary> diaryList = diaryRepository.findAllByFamily_FamilyId(familyId);

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
        return GetGroupDiaryResponse.from(diaryResponse);
    }
}
