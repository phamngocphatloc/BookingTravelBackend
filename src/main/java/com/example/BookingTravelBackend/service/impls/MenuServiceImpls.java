package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.MenuRepository;
import com.example.BookingTravelBackend.entity.Menu;
import com.example.BookingTravelBackend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpls implements MenuService {
    private final MenuRepository menuRepository;
    @Override
    public Menu findById(int id) {
        return menuRepository.findById(id).get();
    }
}
