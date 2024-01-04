package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface RentalService extends BaseService<Rental, RentalDTO> {
    List<RentalDTO> getRentals(String clientName, String deviceName, Boolean returned, boolean pastDue);
    RentalDTO createRequest(RentalRequestDTO rental);
    RentalDTO rentalReturned(Long id);
}
