package ec.edu.ups.icc.portafolio_backend.programmer.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProgrammerProfileResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.service.AdvisoryService;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ProgrammerService;
import org.springframework.web.bind.annotation.*;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ProjectService;
import ec.edu.ups.icc.portafolio_backend.programmer.service.AvailabilityService;

import java.util.List;

@RestController
public class PublicController {
    private final ProgrammerService programmerService;
    private final AdvisoryService advisoryService;
    private final ProjectService projectService;
    private final AvailabilityService availabilityService;

    public PublicController(ProgrammerService programmerService, AdvisoryService advisoryService,
                            ProjectService projectService, AvailabilityService availabilityService) {
        this.programmerService = programmerService;
        this.advisoryService = advisoryService;
        this.projectService = projectService;
        this.availabilityService = availabilityService;
    }

    @GetMapping("/api/programmers")
    public List<ProgrammerProfileResponse> listProgrammers() {
        return programmerService.listAll();
    }

    @GetMapping("/api/programmers/{id}")
    public ProgrammerProfileResponse getProgrammer(@PathVariable Long id) {
        return programmerService.getByProfileId(id);
    }

    @PostMapping("/api/advisories")
    public AdvisoryResponse createAdvisory(@RequestBody AdvisoryRequest request) {
        return advisoryService.create(request);
    }

    @GetMapping("/api/advisories")
    public List<AdvisoryResponse> listByRequester(@RequestParam String email) {
        return advisoryService.listByRequester(email);
    }

    @GetMapping("/api/programmers/{id}/projects")
    public List<ProjectResponse> listProjects(@PathVariable Long id) {
        return projectService.listByProfile(id);
    }

    @GetMapping("/api/programmers/{id}/availability")
    public List<AvailabilityResponse> listAvailability(@PathVariable Long id) {
        return availabilityService.list(id);
    }
}