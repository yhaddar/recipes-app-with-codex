package com.recipemanager.task;

import static org.assertj.core.api.Assertions.assertThat;

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
class UserTaskRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private UserTaskRepository repository;

    @Test
    void returnsTasksOnlyForRequestedOwner() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));
        AppUser otherOwner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Grace", "Hopper"));
        UserTask ownedTask = repository.saveAndFlush(new UserTask(UUID.randomUUID(), owner, "Buy ingredients"));
        repository.saveAndFlush(new UserTask(UUID.randomUUID(), otherOwner, "Private task"));

        assertThat(repository.findAllByOwnerId(owner.getId()))
                .extracting(UserTask::getId)
                .containsExactly(ownedTask.getId());
    }

    @Test
    void defaultsTaskToIncomplete() {
        AppUser owner = userRepository.saveAndFlush(new AppUser(
                UUID.randomUUID(), UUID.randomUUID() + "@example.test", "Ada", "Lovelace"));

        UserTask task = repository.saveAndFlush(new UserTask(UUID.randomUUID(), owner, "Plan dinner"));

        assertThat(task.isCompleted()).isFalse();
    }
}
