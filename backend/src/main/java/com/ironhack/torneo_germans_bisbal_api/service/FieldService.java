package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Field;

import java.util.List;

public interface FieldService {

    List<Field> getAllFields();

    Field getFieldById(Long id);

    Field createField(Field field);

    Field updateField(Long id, Field field);

    void deleteField(Long id);
}
