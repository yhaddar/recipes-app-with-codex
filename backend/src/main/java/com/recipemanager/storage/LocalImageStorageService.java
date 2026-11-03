package com.recipemanager.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalImageStorageService {

    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp");

    private final Path storageDirectory;
    private final long maxFileSize;

    public LocalImageStorageService(
            @Value("${app.storage.image-directory:storage/images}") Path storageDirectory,
            @Value("${app.storage.max-file-size:5242880}") long maxFileSize) {
        this.storageDirectory = storageDirectory.toAbsolutePath().normalize();
        this.maxFileSize = maxFileSize;
    }

    public StoredImage store(MultipartFile file, UUID ownerId) {
        validate(file, ownerId);

        String contentType = file.getContentType();
        String storedFilename = UUID.randomUUID() + EXTENSIONS.get(contentType);
        Path ownerDirectory = storageDirectory.resolve(ownerId.toString()).normalize();
        Path destination = ownerDirectory.resolve(storedFilename).normalize();

        if (!destination.startsWith(ownerDirectory)) {
            throw new IllegalArgumentException("Invalid image path");
        }

        try {
            Files.createDirectories(ownerDirectory);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return new StoredImage(ownerId, storedFilename, contentType, file.getSize(), destination);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to store image", exception);
        }
    }

    public void delete(StoredImage image) {
        if (image == null || image.path() == null) {
            return;
        }

        Path normalizedPath = image.path().toAbsolutePath().normalize();
        Path ownerDirectory = storageDirectory.resolve(image.ownerId().toString()).normalize();
        if (!normalizedPath.startsWith(ownerDirectory)) {
            throw new IllegalArgumentException("Invalid image path");
        }

        try {
            Files.deleteIfExists(normalizedPath);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to delete image", exception);
        }
    }

    private void validate(MultipartFile file, UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Image owner is required");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Image file exceeds the maximum size");
        }
        if (!Set.copyOf(EXTENSIONS.keySet()).contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported image type");
        }
    }
}
