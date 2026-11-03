package com.recipemanager.subscription;

import java.time.Instant;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at")
    private Instant endsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Subscription() {
    }

    public Subscription(UUID id, AppUser user, SubscriptionPlan plan) {
        this.id = id;
        this.user = user;
        this.plan = plan;
        this.status = SubscriptionStatus.TRIAL;
        this.startsAt = Instant.now();
        this.createdAt = this.startsAt;
        this.updatedAt = this.startsAt;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }
}
