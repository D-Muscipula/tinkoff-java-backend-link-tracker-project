package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.jpa.Link;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(URI url);
}
