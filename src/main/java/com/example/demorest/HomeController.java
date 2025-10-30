package com.example.demorest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user){
       return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getall(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }


//    @GetMapping("/findById/{uid}")
//    public ResponseEntity<Object> findById(@PathVariable int uid) {
//        return service.findById(uid)
//                .map(user -> ResponseEntity.ok(user)) // 200 OK with user JSON
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(Map.of(
//                                "status", 404,
//                                "message", "User not found with id " + uid
//                        )));
//    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") String data ){
        try {
            // Convert raw JSON string → DTO
            DocumentRequest docData = objectMapper.readValue(data, DocumentRequest.class);

            // save file
            String savedPath = service.saveDocument(file);

            return ResponseEntity.ok(
                    "File '" + docData.getDocumentName() + "' uploaded successfully to: " + savedPath
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    private final Path uploadDir = Paths.get("uploads"); // or "/data/uploads" if using a disk

    // ✅ List all uploaded files
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() throws IOException {
        if (!Files.exists(uploadDir)) {
            return ResponseEntity.ok(List.of());
        }

        List<String> files = Files.list(uploadDir)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());

        return ResponseEntity.ok(files);
    }
}
