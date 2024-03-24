package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.UserLinkRepository;
import edu.java.scrapper.dto.UserLink;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.tables.UsersLinks.USERS_LINKS;


public class JooqUserLinkRepository implements UserLinkRepository {
    private final DSLContext dslContext;

    public JooqUserLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void add(UserLink userLink) {
        dslContext.insertInto(USERS_LINKS, USERS_LINKS.TG_USER, USERS_LINKS.LINK)
            .values(userLink.tgUser(), userLink.link())
            .execute();
    }

    @Override
    public void removeById(Long id) {
        dslContext.deleteFrom(USERS_LINKS)
            .where(USERS_LINKS.ID.eq(id))
            .execute();
    }

    @Override
    public Integer removeByUserId(Long userId) {
        return dslContext.deleteFrom(USERS_LINKS)
            .where(USERS_LINKS.TG_USER.eq(userId))
            .execute();
    }

    @Override
    public Integer removeByLinkId(Long userId) {
        return dslContext.deleteFrom(USERS_LINKS)
            .where(USERS_LINKS.LINK.eq(userId))
            .execute();
    }

    @Override
    public void removeByUserIdAndLinkId(Long userId, Long linkId) {
        dslContext.deleteFrom(USERS_LINKS)
            .where(USERS_LINKS.TG_USER.eq(userId))
            .and(USERS_LINKS.LINK.eq(linkId))
            .execute();
    }

    @Override
    public Optional<UserLink> findById(Long id) {
        return dslContext.selectFrom(USERS_LINKS)
            .where(USERS_LINKS.ID.eq(id))
            .fetchOptionalInto(UserLink.class);
    }

    @Override
    public List<UserLink> findByUserId(Long userId) {
        return dslContext.selectFrom(USERS_LINKS)
            .where(USERS_LINKS.TG_USER.eq(userId))
            .fetchInto(UserLink.class);
    }

    @Override
    public List<UserLink> findByLinkId(Long linkId) {
        return dslContext.selectFrom(USERS_LINKS)
            .where(USERS_LINKS.LINK.eq(linkId))
            .fetchInto(UserLink.class);
    }

    @Override
    public List<UserLink> findAll() {
        return dslContext.selectFrom(USERS_LINKS)
            .fetchInto(UserLink.class);
    }

    @Override
    public Optional<UserLink> findByUserIdAndLinkId(Long userId, Long linkId) {
        return dslContext.selectFrom(USERS_LINKS)
            .where(USERS_LINKS.TG_USER.eq(userId))
            .and(USERS_LINKS.LINK.eq(linkId))
            .fetchOptionalInto(UserLink.class);
    }

}
