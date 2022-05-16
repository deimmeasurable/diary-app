package com.technophiles.diaryapp.services;

import com.technophiles.diaryapp.dtos.UserDto;
import com.technophiles.diaryapp.exceptions.DiaryApplicationException;
import com.technophiles.diaryapp.exceptions.UserNotFoundException;
import com.technophiles.diaryapp.models.Diary;
import com.technophiles.diaryapp.models.Permission;
import com.technophiles.diaryapp.models.Role;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new UserNotFoundException("user not found"));
        org.springframework.security.core.userdetails.User returnedUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));
        log.info("Returned user --> {}", returnedUser);
        return returnedUser;
    }

    @Transactional
    Set<? extends GrantedAuthority> getAuthorities(
            Set<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    Set<String> getPrivileges(Set<Role> roles) {

        Set<String> permissions = new HashSet<>();
        Set<Permission> collection = new HashSet<>();
        for (Role role : roles) {
            permissions.add(role.getName());
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            permissions.add(item.getName());
        }
        log.info("Permissions --> {}", permissions);
        return permissions;
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Set<String> permissions) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission: permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        log.info("Simple Granted Authorities --> {}", permissions);
        return authorities;
    }
}
