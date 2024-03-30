package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.UserRepository;
import edu.java.scrapper.dto.TgUser;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.jooq.Tables.TG_USER;
import static org.jooq.impl.DSL.row;

public class JooqUserRepository implements UserRepository {

    private final DSLContext dslContext;

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
    public void updateTgUser(TgUser tgUser) {
        dslContext.update(TG_USER)
            .set(
                row(TG_USER.USER_STATE),
                row(tgUser.userState())
            ).where(TG_USER.USER_CHAT_ID.eq(tgUser.userChatId()))
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
