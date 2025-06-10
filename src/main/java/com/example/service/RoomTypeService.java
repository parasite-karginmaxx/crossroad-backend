package com.example.service;

import com.example.dto.request.TypeRequest;
import com.example.model.RoomType;
import com.example.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<RoomType> getAllTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType createType(TypeRequest request) {
        RoomType roomType = new RoomType();
        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        return roomTypeRepository.save(roomType);
    }

    public RoomType updateType(Long id, TypeRequest request) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тип не найден"));
        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        return roomTypeRepository.save(roomType);
    }

    public void deleteType(Long id) {
        roomTypeRepository.deleteById(id);
    }
}
