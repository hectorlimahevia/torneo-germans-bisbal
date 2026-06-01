package com.ironhack.torneo_germans_bisbal_api.controller;


import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;
import com.ironhack.torneo_germans_bisbal_api.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    //Lista de campos
    @GetMapping
    public List<Field> getAllFields() {
        return fieldService.getAllFields();
    }

    //Get field by id
    @GetMapping("/{id}")
    public Field getFieldById(@PathVariable Long id) {
        return fieldService.getFieldById(id);
    }

    //Crear campo
    @PostMapping
    public Field createField(@RequestBody Field field) {
        return fieldService.createField(field);
    }

    //Modificar campo
    @PutMapping("/{id}")
    public Field updateField(@PathVariable Long id, @RequestBody Field field) {
        return fieldService.updateField(id, field);
    }

    //Eliminar campo de juego
    @DeleteMapping("/{id}")
    public void deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
    }
}

