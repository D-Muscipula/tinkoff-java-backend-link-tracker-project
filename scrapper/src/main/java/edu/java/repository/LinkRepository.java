package edu.java.repository;

import edu.java.dto.Link;
import java.util.Optional;

public interface LinkRepository {
    void add(Link link);

    void remove(Link link);

    Optional<Link> findById(Long id);

    Optional<Link> findByURL(Long id);
}
