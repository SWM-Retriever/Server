package org.retriever.server.dailypet.domain.petcare.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.entity.PetCareAlarm;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;
import org.retriever.server.dailypet.domain.petcare.repository.PetCareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetCareService {

    private final PetRepository petRepository;
    private final PetCareRepository petCareRepository;

    @Transactional
    public void registerPetCare(Long petId, CreatePetCareRequest dto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(PetNotFoundException::new);

        PetCare petCare = PetCare.from(dto);

        List<CustomDayOfWeek> dayOfWeeks = dto.getDayOfWeeks();

        for (CustomDayOfWeek dayOfWeek : dayOfWeeks) {
            PetCareAlarm petCareAlarm = PetCareAlarm.from(dayOfWeek);
            petCare.addPetCareAlarm(petCareAlarm);
        }

        pet.registerPetCare(petCare);

        // CascadeType.PERSIST를 통해 연관된 petCareAlarm 포함해서 저장
        petCareRepository.save(petCare);
    }
}
