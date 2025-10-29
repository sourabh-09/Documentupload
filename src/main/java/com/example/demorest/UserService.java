package com.example.demorest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public User saveUser(User user){
      return   repo.save(user);
    }

    public List<User> getAll(){
        return  repo.findAll();
    }

    public Optional<User> findById(int uid){
        return repo.findById(uid);
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveDocument(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Make filename safe and unique if you want
        String original = Path.of(file.getOriginalFilename()).getFileName().toString();
        String filename = System.currentTimeMillis() + "-" + original; // simple unique prefix

        Path filePath = uploadPath.resolve(filename);
        // Stream copy
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Failed to store file " + filename, ex);
        }
        return filePath.toString();
    }
}
