package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AvailabilitySlot;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Modality;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AvailabilityRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ProgrammerProfileRepository profileRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository, ProgrammerProfileRepository profileRepository) {
        this.availabilityRepository = availabilityRepository;
        this.profileRepository = profileRepository;
    }

    public AvailabilityResponse add(Long profileId, AvailabilityRequest r) {
        var profile = profileRepository.findById(profileId).orElseThrow();
        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setProfile(profile);
        slot.setDay(DayOfWeek.valueOf(r.day()));
        slot.setStartTime(LocalTime.parse(r.startTime()));
        slot.setEndTime(LocalTime.parse(r.endTime()));
        slot.setModality(Modality.valueOf(r.modality()));
        availabilityRepository.save(slot);
        return toResponse(slot);
    }

    public List<AvailabilityResponse> list(Long profileId) {
        return availabilityRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        availabilityRepository.deleteById(id);
    }

    private AvailabilityResponse toResponse(AvailabilitySlot slot) {
        return new AvailabilityResponse(
            slot.getId(),
            slot.getDay().name(),
            slot.getStartTime().toString(),
            slot.getEndTime().toString(),
            slot.getModality().name()
        );
    }
}