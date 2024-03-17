package edu.java.service.jdbc;

import edu.java.dto.TgUser;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ChatDoesntExistException;
import edu.java.repository.UserRepository;
import edu.java.service.TgUserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgUserService implements TgUserService {
    private final UserRepository userRepository;

    @Autowired
    public JdbcTgUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(long tgUserId) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isPresent()) {
            throw new ChatAlreadyExistsException();
        }
        userRepository.add(new TgUser(tgUserId, "registered"));
    }

    @Override
    public void unregister(long tgUserId) {
        Optional<TgUser> tgUser = userRepository.findById(tgUserId);
        if (tgUser.isEmpty()) {
            throw new ChatDoesntExistException();
        }
        userRepository.remove(tgUserId);
    }
}
