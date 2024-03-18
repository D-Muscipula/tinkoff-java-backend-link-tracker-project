package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Link.LINK;
import static org.jooq.impl.DSL.row;

@Repository
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Autowired
    public JooqLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void add(Link link) {
        dslContext.insertInto(LINK, LINK.URL, LINK.UPDATED_AT, LINK.LAST_CHECKED_AT)
            .values(String.valueOf(link.url()), link.updatedAt(), link.lastCheckedAt())
            .execute();
    }

    @Override
    public void update(Link link) {
        dslContext.update(LINK)
            .set(
                row(LINK.UPDATED_AT, LINK.LAST_CHECKED_AT, LINK.LAST_COMMIT_SHA, LINK.ANSWERS_COUNT),
                row(link.updatedAt(), link.lastCheckedAt(), link.lastCommitSha(), link.answersCount())
            ).where(LINK.ID.eq(link.id()))
            .execute();
    }

    @Override
    public void removeById(Long id) {
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Override
    public void removeByURL(URI url) {
        dslContext.deleteFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public Optional<Link> findById(Long id) {
        return dslContext.selectFrom(LINK)
            .where(LINK.ID.eq(id))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findByURL(URI url) {
        return dslContext.selectFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(LINK)
            .fetchInto(Link.class);
    }
}

