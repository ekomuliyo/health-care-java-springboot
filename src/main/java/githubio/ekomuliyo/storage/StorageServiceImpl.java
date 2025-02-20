package githubio.ekomuliyo.storage;

import githubio.ekomuliyo.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.util.unit.DataSize;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public StorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }

            // Validate file size
            if (file.getSize() > getMaxFileSizeBytes()) {
                throw new StorageException("File size exceeds maximum limit of " + maxFileSize);
            }

            // Validate image format
            validateImageFormat(file.getOriginalFilename());

            // Generate unique filename
            String originalExtension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + originalExtension;
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(fileName))
                    .normalize()
                    .toAbsolutePath();

            // Security check
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }

            // Save file
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return baseUrl + "/images/" + fileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path file = load(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Files.walk(rootLocation)
                    .filter(path -> !path.equals(rootLocation))
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            throw new StorageException("Failed to delete " + file.getAbsolutePath());
                        }
                    });
        } catch (IOException e) {
            throw new StorageException("Failed to delete stored files", e);
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    private void validateImageFormat(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        boolean isValidImage = false;
        for (String allowedExtension : ALLOWED_IMAGE_EXTENSIONS) {
            if (extension.equals(allowedExtension)) {
                isValidImage = true;
                break;
            }
        }
        if (!isValidImage) {
            throw new StorageException("Invalid image format. Allowed formats: " + String.join(", ", ALLOWED_IMAGE_EXTENSIONS));
        }
    }

    private long getMaxFileSizeBytes() {
        return DataSize.parse(maxFileSize).toBytes();
    }
}