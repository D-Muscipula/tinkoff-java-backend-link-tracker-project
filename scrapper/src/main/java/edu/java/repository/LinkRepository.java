package edu.java.repository;

import edu.java.dto.Link;
import java.net.URI;
import java.util.Optional;

public interface LinkRepository {
    void add(Link link);

    void removeById(Long id);

    void removeByURL(URI url);


    Optional<Link> findById(Long id);

    Optional<Link> findByURL(URI url);
}
