package com.recipemanager.recipe;

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
@Table(name = "recipes")
public class Recipe {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;

    @Column(name = "cooking_time_minutes")
    private Integer cookingTimeMinutes;

    @Column(name = "total_time_minutes")
    private Integer totalTimeMinutes;

    @Column(nullable = false)
    private Integer servings;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = false, length = 20)
    private PublicationStatus publicationStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Recipe() {
    }

    public Recipe(UUID id, AppUser owner, String title, int servings) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.servings = servings;
        this.difficulty = Difficulty.MEDIUM;
        this.publicationStatus = PublicationStatus.DRAFT;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public AppUser getOwner() {
        return owner;
    }

    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public String getTitle() {
        return title;
    }

    public void updateDetails(String description, Integer totalTimeMinutes, Difficulty difficulty) {
        this.description = description;
        this.totalTimeMinutes = totalTimeMinutes;
        this.difficulty = difficulty;
        this.updatedAt = Instant.now();
    }
}
