package com.example.service;

import com.example.dto.TypeRequest;
import com.example.model.Type;
import com.example.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Type createType(TypeRequest request) {
        Type type = new Type();
        type.setName(request.getName());
        type.setDescription(request.getDescription());
        return typeRepository.save(type);
    }

    public Type updateType(Long id, TypeRequest request) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тип не найден"));
        type.setName(request.getName());
        type.setDescription(request.getDescription());
        return typeRepository.save(type);
    }

    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
