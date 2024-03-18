package edu.java.scrapper.repository;

import edu.java.scrapper.dto.UserLink;
import java.util.List;
import java.util.Optional;

public interface UserLinkRepository {
    void add(UserLink userLink);

    void removeById(Long id);

    Integer removeByUserId(Long userId);

    Integer removeByLinkId(Long userId);

    void removeByUserIdAndLinkId(Long userId, Long linkId);

    Optional<UserLink> findById(Long id);

    List<UserLink> findByUserId(Long userId);

    List<UserLink> findByLinkId(Long linkId);

    List<UserLink> findAll();

    Optional<UserLink> findByUserIdAndLinkId(Long userId, Long linkId);
}
