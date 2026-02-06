package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.*;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Contact;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.ProgrammerProfile;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.SocialLink;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgrammerService {

    private final ProgrammerProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProgrammerService(ProgrammerProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProgrammerProfileResponse createOrUpdateProfile(Long userId, ProgrammerProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        ProgrammerProfile profile = profileRepository.findByUserId(userId).orElseGet(ProgrammerProfile::new);
        profile.setUser(user);
        profile.setSpecialty(request.specialty());
        profile.setDescription(request.description());
        profile.setPhotoUrl(request.photoUrl());

        profile.getContacts().clear();
        if (request.contacts() != null) {
            for (ContactRequest c : request.contacts()) {
                Contact contact = new Contact();
                contact.setType(c.type());
                contact.setUrl(c.url());
                contact.setProfile(profile);
                profile.getContacts().add(contact);
            }
        }

        profile.getSocials().clear();
        if (request.socials() != null) {
            for (SocialRequest s : request.socials()) {
                SocialLink social = new SocialLink();
                social.setName(s.name());
                social.setUrl(s.url());
                social.setProfile(profile);
                profile.getSocials().add(social);
            }
        }

        profileRepository.save(profile);
        return toResponse(profile);
    }

    public ProgrammerProfileResponse getByUserId(Long userId) {
        return toResponse(profileRepository.findByUserId(userId).orElseThrow());
    }

    public List<ProgrammerProfileResponse> listAll() {
        return profileRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProgrammerProfileResponse getByProfileId(Long id) {
        return toResponse(profileRepository.findById(id).orElseThrow());
    }

    private ProgrammerProfileResponse toResponse(ProgrammerProfile profile) {
        return new ProgrammerProfileResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getUser().getName(),
            profile.getUser().getEmail(),
            profile.getSpecialty(),
            profile.getDescription(),
            profile.getPhotoUrl(),
            profile.getContacts().stream().map(c -> new ContactRequest(c.getType(), c.getUrl())).toList(),
            profile.getSocials().stream().map(s -> new SocialRequest(s.getName(), s.getUrl())).toList()
        );
    }
}