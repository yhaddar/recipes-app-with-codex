package com.recipemanager.subscription;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
}
