package ec.edu.ups.icc.portafolio_backend.programmer.entity;

import jakarta.persistence.*;

@Entity
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private ProgrammerProfile profile;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public ProgrammerProfile getProfile() { return profile; }
    public void setProfile(ProgrammerProfile profile) { this.profile = profile; }
}