package com.scent.field;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping("/post/upload")
    public ResponseEntity<Field> uploadFieldImage(@RequestParam("file") MultipartFile file) throws IOException{
            Field savedField = fieldService.saveField(file);
            return ResponseEntity.ok().body(savedField);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Field> getFieldById(@PathVariable Long id) {
        try {
            Field field = fieldService.getFieldById(id);
            return ResponseEntity.ok().body(field);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Field>> getAllFields() {
        List<Field> fields = fieldService.getAllFields();
        return ResponseEntity.ok().body(fields);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Field> updateFieldDetail(@PathVariable Long id, @RequestBody Field fieldDetails) {
        try {
            Field updatedField = fieldService.updateFieldDetail(id, fieldDetails);
            return ResponseEntity.ok().body(updatedField);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/put/{id}/image")
    public ResponseEntity<Field> updateFieldImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Field updatedField = fieldService.updateFieldImage(id, file);
            return ResponseEntity.ok().body(updatedField);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
