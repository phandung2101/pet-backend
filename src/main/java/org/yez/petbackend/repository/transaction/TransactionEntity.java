package org.yez.petbackend.repository.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.yez.petbackend.domain.transaction.TransactionType;
import org.yez.petbackend.repository.category.CategoryEntity;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Column(nullable = false)
    private Instant transactionTime;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToMany
    @JoinTable(
            name = "transaction_categories",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
