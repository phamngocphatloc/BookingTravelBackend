package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.MenuDetailsRepository;
import com.example.BookingTravelBackend.entity.MenuDetails;
import com.example.BookingTravelBackend.service.menuDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuDetailsImpls implements menuDetailsService {
    private final MenuDetailsRepository menuDetailsRepository;
    @Override
    public MenuDetails findByMenuIdAndSize(int menuId, String size) {
        return menuDetailsRepository.findMenuDetailsByMenuIdAndSize(menuId,size);
    }
}
