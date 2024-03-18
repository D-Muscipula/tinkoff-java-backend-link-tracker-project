package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.dto.TgUser;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.Tables.TG_USER;

@Repository
public class JooqUserRepository implements UserRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqUserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void add(TgUser tgUser) {
        dslContext.insertInto(TG_USER, TG_USER.USER_CHAT_ID, TG_USER.USER_STATE)
            .values(tgUser.userChatId(), tgUser.userState())
            .execute();
    }

    @Override
    public void remove(Long id) {
        dslContext.deleteFrom(TG_USER)
            .where(TG_USER.USER_CHAT_ID.eq(id))
            .execute();
    }

    @Override
    public Optional<TgUser> findById(Long id) {
        return dslContext.selectFrom(TG_USER)
            .where(TG_USER.USER_CHAT_ID.eq(id))
            .fetchOptionalInto(TgUser.class);
    }

    @Override
    public List<TgUser> findAll() {
        return dslContext.selectFrom(TG_USER)
            .fetchInto(TgUser.class);
    }
}
