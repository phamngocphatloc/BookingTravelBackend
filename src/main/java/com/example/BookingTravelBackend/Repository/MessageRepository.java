package com.example.BookingTravelBackend.Repository;

import com.example.BookingTravelBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.receiver.id = ?2 AND m.sender.id = ?1) OR " +
            "(m.receiver.id = ?1 AND m.sender.id = ?2) " +
            "ORDER BY m.sentAt asc")
    List<Message> findChatHistory(int senderId, int receiverId);
}
