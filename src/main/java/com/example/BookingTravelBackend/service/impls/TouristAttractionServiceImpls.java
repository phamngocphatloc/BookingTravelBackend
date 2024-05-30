package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.TouristAttractionRepsitory;
import com.example.BookingTravelBackend.entity.TouristAttraction;
import com.example.BookingTravelBackend.payload.respone.TouristAttractionRespone;
import com.example.BookingTravelBackend.payload.respone.TouristAttractionTopRespone;
import com.example.BookingTravelBackend.service.TouristAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristAttractionServiceImpls implements TouristAttractionService {

    private final TouristAttractionRepsitory tourDao;

    @Override
    public List<TouristAttractionRespone> SelectAll() {
        List<TouristAttractionRespone> list = new ArrayList<>();
        tourDao.findAll().stream().forEach(item -> {
            list.add(new TouristAttractionRespone(item));
        });

        return list;
    }

    @Override
    public TouristAttraction selectByName(String name) {
        return tourDao.findByNameLike(name);
    }

    @Override
    public List<TouristAttractionTopRespone> findTouristAttractionWithTotalRoomsBooked() {
        List<TouristAttractionTopRespone> listRespone = new ArrayList<>();
        tourDao.findTouristAttractionWithTotalRoomsBooked().stream().forEach(item -> {
            listRespone.add(new TouristAttractionTopRespone(item));
        });
        return listRespone;
    }
}
