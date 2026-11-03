package com.recipemanager.storage;

import java.nio.file.Path;
import java.util.UUID;

public record StoredImage(
        UUID ownerId,
        String storedFilename,
        String contentType,
        long size,
        Path path) {
}
