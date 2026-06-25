package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.ResourceNotFoundException;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;
import com.ironhack.torneo_germans_bisbal_api.repository.FieldRepository;
import com.ironhack.torneo_germans_bisbal_api.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;

    @Override
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    @Override
    public Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));
    }

    @Override
    public Field createField(Field field) {
        return fieldRepository.save(field);
    }

    @Override
    public Field updateField(Long id, Field field) {
        Field existingField = getFieldById(id);

        existingField.setName(field.getName());
        existingField.setLocation(field.getLocation());

        return fieldRepository.save(existingField);
    }

    @Override
    public void deleteField(Long id) {
        getFieldById(id);
        fieldRepository.deleteById(id);
    }
}