package com.recipemanager.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import com.recipemanager.user.AppUser;
import com.recipemanager.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("postgres")
@Transactional
class SubscriptionRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private SubscriptionRepository repository;

    @Test
    void persistsTrialSubscriptionForUser() {
        AppUser user = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        SubscriptionPlan plan = planRepository.saveAndFlush(new SubscriptionPlan(
                UUID.randomUUID(), PlanCode.FREE_TRIAL, "Free trial", new BigDecimal("0.00")));

        Subscription subscription = repository.saveAndFlush(
                new Subscription(UUID.randomUUID(), user, plan));

        assertThat(repository.findById(subscription.getId()).orElseThrow().getStatus())
                .isEqualTo(SubscriptionStatus.TRIAL);
    }
}
