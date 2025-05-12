package org.yez.petbackend.repository.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "groups")
@NoArgsConstructor
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
    private Set<UserEntity> members;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    private String description;

    public GroupEntity(UUID id) {
        this.id = id;
    }
}
