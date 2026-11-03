package com.recipemanager.subscription;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanCode code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "price_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private boolean active;

    protected SubscriptionPlan() {
    }

    public SubscriptionPlan(UUID id, PlanCode code, String name, BigDecimal priceAmount) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.priceAmount = priceAmount;
        this.currency = "USD";
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public PlanCode getCode() {
        return code;
    }
}
