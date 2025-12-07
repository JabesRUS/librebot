package com.jabes.librebot.service;

import com.jabes.librebot.exception.UserServiceException;
import com.jabes.librebot.model.entity.User;
import com.jabes.librebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User registerUser(Long chatId) {

        checkInputData(chatId);

        if (userExists(chatId)) {
            log.debug("Пользователь с chatId {} уже зарегистрирован", chatId);
            return repository.findByChatId(chatId).orElseThrow(
                    () -> new UserServiceException("User не найден, непредвиденная ошибка")
            );
        }

        User user = createUser(chatId);
        User savedUser = repository.save(user);

        log.info("Зарегистрирован новый пользователь: chatId={}, id={}",
                chatId, savedUser.getId());

        return savedUser;    }

    public Optional<User> findByChatId(Long chatId) {
        return repository.findByChatId(chatId);
    }

    public boolean userExists(Long chatId) {
        return repository.existsByChatId(chatId);
    }

    private User createUser(Long chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    private void checkInputData(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("Переменная chatId пустая.");
        } else if (chatId == 0) {
            throw new IllegalArgumentException("chatId не может быть равен 0");
        }
    }
}
