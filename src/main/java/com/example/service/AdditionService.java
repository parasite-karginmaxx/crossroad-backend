package com.example.service;

import com.example.dto.request.AdditionRequest;
import com.example.model.Addition;
import com.example.repository.AdditionRepository;
import com.example.validator.AdditionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdditionService {

    private final AdditionRepository additionRepository;
    private final AdditionValidator additionValidator;

    public List<Addition> getAllAdditions() {
        return additionRepository.findAll();
    }

    public Addition createAddition(AdditionRequest request) {
        additionValidator.validateRequest(request);

        Addition addition = Addition.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();

        return additionRepository.save(addition);
    }

    public Addition updateAddition(Long id, AdditionRequest request) {
        additionValidator.validateRequest(request);

        Addition addition = additionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Услуга не найдена"));

        addition.setName(request.getName());
        addition.setPrice(request.getPrice());

        return additionRepository.save(addition);
    }

    public void deleteAddition(Long id) {
        if (!additionRepository.existsById(id)) {
            throw new NoSuchElementException("Услуга не найдена");
        }
        additionRepository.deleteById(id);
    }
}