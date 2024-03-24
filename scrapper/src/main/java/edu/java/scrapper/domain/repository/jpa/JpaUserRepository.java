package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.jpa.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaUserRepository extends JpaRepository<TgUser, Long> {
}
