package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceConcreteTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void testThatCanDeleteUser() throws DiaryApplicationException {
        UserDto userDto = userService.createAccount("new-user@gmail.com", "password");
        User user = userRepository.findById(userDto.getId()).get();
        userService.deleteUser(user);
        Optional<User> fromDatabase = userRepository.findById(userDto.getId());
        assertThat(fromDatabase).isEqualTo(Optional.empty());
    }
}
