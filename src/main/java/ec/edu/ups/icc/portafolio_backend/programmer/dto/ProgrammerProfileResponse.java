package ec.edu.ups.icc.portafolio_backend.programmer.dto;

import java.util.List;

public record ProgrammerProfileResponse(
    Long id,
    Long userId,
    String name,
    String email,
    String specialty,
    String description,
    String photoUrl,
    List<ContactRequest> contacts,
    List<SocialRequest> socials
) {}