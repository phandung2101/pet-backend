package org.yez.petbackend.repository.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> members;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    private String description;
}
