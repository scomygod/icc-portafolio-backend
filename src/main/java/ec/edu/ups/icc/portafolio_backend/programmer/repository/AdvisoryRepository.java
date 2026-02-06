package ec.edu.ups.icc.portafolio_backend.programmer.repository;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {
    List<Advisory> findByProfileId(Long profileId);
    List<Advisory> findByRequesterEmail(String email);
    List<Advisory> findByStatus(AdvisoryStatus status);

    @Query("select a from Advisory a where a.scheduledAt between ?1 and ?2")
    List<Advisory> findScheduledBetween(LocalDateTime from, LocalDateTime to);
}