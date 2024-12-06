package com.example.BookingTravelBackend.payload.respone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomBookingResponse {
        private int roomId;
        private String roomName;
        private int totalBookings;

        // Constructor
        public RoomBookingResponse(int roomId, String roomName, int  totalBookings) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.totalBookings = totalBookings;
        }
}
