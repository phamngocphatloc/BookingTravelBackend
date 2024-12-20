package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.PostMedia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse {
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private boolean isLast;
    private int totalPage;
    private Object content;
}
