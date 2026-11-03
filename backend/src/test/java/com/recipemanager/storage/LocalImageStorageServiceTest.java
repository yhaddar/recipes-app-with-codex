package com.recipemanager.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class LocalImageStorageServiceTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void storesImageUnderOwnerDirectoryWithGeneratedFilename() throws Exception {
        LocalImageStorageService service = new LocalImageStorageService(temporaryDirectory, 1024);
        UUID ownerId = UUID.randomUUID();
        MockMultipartFile image = new MockMultipartFile("file", "../../unsafe.png", "image/png", new byte[] {1, 2, 3});

        StoredImage stored = service.store(image, ownerId);

        assertThat(stored.path()).isEqualTo(temporaryDirectory.resolve(ownerId.toString()).resolve(stored.storedFilename()).toAbsolutePath());
        assertThat(stored.storedFilename()).matches("[0-9a-f-]{36}\\.png");
        assertThat(Files.readAllBytes(stored.path())).containsExactly(1, 2, 3);
    }

    @Test
    void rejectsUnsupportedType() {
        LocalImageStorageService service = new LocalImageStorageService(temporaryDirectory, 1024);
        MockMultipartFile file = new MockMultipartFile("file", "file.svg", "image/svg+xml", new byte[] {1});

        assertThatThrownBy(() -> service.store(file, UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported image type");
    }

    @Test
    void rejectsOversizedFile() {
        LocalImageStorageService service = new LocalImageStorageService(temporaryDirectory, 2);
        MockMultipartFile file = new MockMultipartFile("file", "file.png", "image/png", new byte[] {1, 2, 3});

        assertThatThrownBy(() -> service.store(file, UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Image file exceeds the maximum size");
    }
}
