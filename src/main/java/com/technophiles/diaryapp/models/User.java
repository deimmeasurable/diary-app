package com.technophiles.diaryapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Validated
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    private String email;

    @Size(max = 2444)
    private String password;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<Diary> diaries;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<Role> roles;

    @Override
    public String toString() {
        return String.format("id:%d\temail:%s", id, email);
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.diaries = new HashSet<>();
        if (this.roles == null){
            roles = new HashSet<>();
        }
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.diaries = new HashSet<>();
        if (this.roles == null){
            roles = new HashSet<>();
            roles.add(role);
        }
    }

    public void addDiary(Diary diary){
        diaries.add(diary);
    }

    public void addRole(Role role){
        if (roles == null){
            roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void deleteDiary(Diary diary){
        diaries.remove(diary);
    }

    public void deleteAllDiaries(List<Diary> diariesList){
        diariesList.forEach(diaries::remove);
    }
}