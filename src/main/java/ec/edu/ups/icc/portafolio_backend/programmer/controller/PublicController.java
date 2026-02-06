package ec.edu.ups.icc.portafolio_backend.programmer.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProgrammerProfileResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.service.AdvisoryService;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ProgrammerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublicController {

    private final ProgrammerService programmerService;
    private final AdvisoryService advisoryService;

    public PublicController(ProgrammerService programmerService, AdvisoryService advisoryService) {
        this.programmerService = programmerService;
        this.advisoryService = advisoryService;
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
}