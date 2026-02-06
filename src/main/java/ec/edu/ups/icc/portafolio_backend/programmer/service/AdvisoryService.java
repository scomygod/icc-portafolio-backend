package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.UpdateAdvisoryStatusRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AdvisoryRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final ProgrammerProfileRepository profileRepository;
    private final NotificationService notificationService;

    public AdvisoryService(AdvisoryRepository advisoryRepository,
                           ProgrammerProfileRepository profileRepository,
                           NotificationService notificationService) {
        this.advisoryRepository = advisoryRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }

    public AdvisoryResponse create(AdvisoryRequest request) {
        var profile = profileRepository.findById(request.programmerProfileId()).orElseThrow();
        Advisory advisory = new Advisory();
        advisory.setProfile(profile);
        advisory.setRequesterName(request.requesterName());
        advisory.setRequesterEmail(request.requesterEmail());
        advisory.setScheduledAt(LocalDateTime.parse(request.scheduledAt()));
        advisory.setComment(request.comment());
        advisoryRepository.save(advisory);

        notificationService.notifyAdvisoryCreated(advisory);

        return toResponse(advisory);
    }

    public List<AdvisoryResponse> listByProfile(Long profileId) {
        return advisoryRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
    }

    public List<AdvisoryResponse> listByRequester(String email) {
        return advisoryRepository.findByRequesterEmail(email).stream().map(this::toResponse).toList();
    }

    public AdvisoryResponse updateStatus(Long id, UpdateAdvisoryStatusRequest request) {
        Advisory advisory = advisoryRepository.findById(id).orElseThrow();
        advisory.setStatus(AdvisoryStatus.valueOf(request.status()));
        advisory.setResponse(request.response());
        advisoryRepository.save(advisory);

        notificationService.notifyAdvisoryUpdated(advisory);

        return toResponse(advisory);
    }

    private AdvisoryResponse toResponse(Advisory a) {
        return new AdvisoryResponse(
            a.getId(),
            a.getProfile().getId(),
            a.getProfile().getUser().getName(),
            a.getRequesterName(),
            a.getRequesterEmail(),
            a.getScheduledAt().toString(),
            a.getComment(),
            a.getStatus().name(),
            a.getResponse()
        );
    }
}