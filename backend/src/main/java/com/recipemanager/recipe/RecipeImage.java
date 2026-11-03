package com.recipemanager.recipe;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_images")
public class RecipeImage {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "storage_path", nullable = false, length = 1000)
    private String storagePath;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "file_size_bytes", nullable = false)
    private long fileSizeBytes;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected RecipeImage() {
    }

    public RecipeImage(UUID id, Recipe recipe, String storagePath, String originalFilename,
            String contentType, long fileSizeBytes, int displayOrder) {
        this.id = id;
        this.recipe = recipe;
        this.storagePath = storagePath;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSizeBytes = fileSizeBytes;
        this.displayOrder = displayOrder;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
