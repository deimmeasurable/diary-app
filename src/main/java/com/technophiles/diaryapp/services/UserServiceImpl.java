package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.exceptions.UserNotFoundException;
import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.User;
import com.technophiles.diaryapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Validated
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public Diary addDiary(@NotNull Long id, @NotNull Diary diary) throws DiaryApplicationException {
        User user = userRepository.findById(id).orElseThrow(()-> new DiaryApplicationException("user does not exist"));
        user.addDiary(diary);
        userRepository.save(user);
        return diary;
    }

    @Override
    public User findById(Long id) throws DiaryApplicationException {
        return userRepository.findById(id).orElseThrow(() -> new DiaryApplicationException("user does not exist"));
    }

    @Override
    public boolean deleteUser(User user) {
        userRepository.delete(user);
        return true;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user name not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("user not found"));
        org.springframework.security.core.userdetails.User returnedUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
        log.info("Returned user --> {}", returnedUser);
        return returnedUser;
    }

}
