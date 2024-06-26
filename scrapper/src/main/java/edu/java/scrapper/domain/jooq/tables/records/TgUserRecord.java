/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.tables.records;

import edu.java.scrapper.domain.jooq.tables.TgUser;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class TgUserRecord extends UpdatableRecordImpl<TgUserRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>TG_USER.USER_CHAT_ID</code>.
     */
    public void setUserChatId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>TG_USER.USER_CHAT_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getUserChatId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>TG_USER.USER_STATE</code>.
     */
    public void setUserState(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>TG_USER.USER_STATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUserState() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return TgUser.TG_USER.USER_CHAT_ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return TgUser.TG_USER.USER_STATE;
    }

    @Override
    @NotNull
    public Long component1() {
        return getUserChatId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUserState();
    }

    @Override
    @NotNull
    public Long value1() {
        return getUserChatId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUserState();
    }

    @Override
    @NotNull
    public TgUserRecord value1(@NotNull Long value) {
        setUserChatId(value);
        return this;
    }

    @Override
    @NotNull
    public TgUserRecord value2(@NotNull String value) {
        setUserState(value);
        return this;
    }

    @Override
    @NotNull
    public TgUserRecord values(@NotNull Long value1, @NotNull String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TgUserRecord
     */
    public TgUserRecord() {
        super(TgUser.TG_USER);
    }

    /**
     * Create a detached, initialised TgUserRecord
     */
    @ConstructorProperties({"userChatId", "userState"})
    public TgUserRecord(@NotNull Long userChatId, @NotNull String userState) {
        super(TgUser.TG_USER);

        setUserChatId(userChatId);
        setUserState(userState);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised TgUserRecord
     */
    public TgUserRecord(edu.java.scrapper.domain.jooq.tables.pojos.TgUser value) {
        super(TgUser.TG_USER);

        if (value != null) {
            setUserChatId(value.getUserChatId());
            setUserState(value.getUserState());
            resetChangedOnNotNull();
        }
    }
}
