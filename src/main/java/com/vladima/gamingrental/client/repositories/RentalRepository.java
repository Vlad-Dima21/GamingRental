package com.vladima.gamingrental.client.repositories;

import com.vladima.gamingrental.client.models.Rental;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r " +
            "WHERE COALESCE((r.rentalClient.clientEmail = :clientEmail), true)" +
            "AND COALESCE(((r.rentalDevice.deviceBase.deviceBaseName = :deviceName)), true)" +
            "AND CASE WHEN :returned = true THEN (r.rentalReturnDate IS NOT NULL) WHEN :returned = false THEN (r.rentalReturnDate IS NULL) ELSE true END " +
            "AND (:pastDue = false OR r.rentalReturnDate > r.rentalDueDate)")
    List<Rental> getRentals(
        String clientEmail, String deviceName, Boolean returned, boolean pastDue, PageRequest pageRequest
    );
}
