package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    public UserDto createAccount(String email, String password) throws DiaryApplicationException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()){
            User user = new User(email, password);
            User savedUser = userRepository.save(user);
            return mapper.map(savedUser, UserDto.class);
        }
        throw new DiaryApplicationException("email already exists");
    }

    @Override
    public UserDto addDiary(@NotNull Long id, @NotNull Diary diary) throws DiaryApplicationException {
        User user = userRepository.findById(id).orElseThrow(()-> new DiaryApplicationException("user does not exist"));
        user.addDiary(diary);
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public User findById(Long id) throws DiaryApplicationException {
        return userRepository.findById(id).orElseThrow(() -> new DiaryApplicationException("user does not exist"));
    }
}
