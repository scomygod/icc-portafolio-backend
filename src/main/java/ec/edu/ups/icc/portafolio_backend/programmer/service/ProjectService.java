package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Participation;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Project;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.ProjectSection;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProgrammerProfileRepository profileRepository;

    public ProjectService(ProjectRepository projectRepository, ProgrammerProfileRepository profileRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    public ProjectResponse create(Long profileId, ProjectRequest request) {
        var profile = profileRepository.findById(profileId).orElseThrow();
        Project p = new Project();
        p.setProfile(profile);
        apply(p, request);
        projectRepository.save(p);
        return toResponse(p);
    }

    public ProjectResponse update(Long projectId, ProjectRequest request) {
        var p = projectRepository.findById(projectId).orElseThrow();
        apply(p, request);
        projectRepository.save(p);
        return toResponse(p);
    }

    public void delete(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<ProjectResponse> listByProfile(Long profileId) {
        return projectRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
    }

    private void apply(Project p, ProjectRequest r) {
        p.setName(r.name());
        p.setDescription(r.description());
        p.setParticipation(Participation.valueOf(r.participation()));
        p.setTechnologies(r.technologies());
        p.setRepoUrl(r.repoUrl());
        p.setDemoUrl(r.demoUrl());
        p.setSection(ProjectSection.valueOf(r.section()));
        p.setActive(r.active());
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(
            p.getId(), p.getName(), p.getDescription(),
            p.getParticipation().name(), p.getTechnologies(),
            p.getRepoUrl(), p.getDemoUrl(),
            p.getSection().name(), p.isActive()
        );
    }
}