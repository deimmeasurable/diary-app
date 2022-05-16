package com.technophiles.diaryapp.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<Permission> permissions;

    public Role(String name, HashSet<Permission> permissionSet) {
        this.name = name;
        permissions = permissionSet;
    }
}
