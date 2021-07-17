package com.project.askit.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageUploaderRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/api/upload-file")
    public Object uploadImage(@RequestBody MultipartFile file,
                              @RequestParam String name) {

        if (file == null) {
            logger.error("File not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        String basePath = Paths.get("").toAbsolutePath().toString();
        String relativePath = "\\src\\main\\resources\\uploaded_images";
        File outputFile = new File(basePath + relativePath + "/" + name);

        // Save file
        try {
            file.transferTo(outputFile);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File could not be saved");
        }

        Map<String, String> location = new HashMap<>();
        location.put("location", "uploaded_images/" + name);

        return new ResponseEntity<>(location, HttpStatus.OK);
    }

}
