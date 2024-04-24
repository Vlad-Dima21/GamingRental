package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.helpers.BaseService;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;

public interface RentalService extends BaseService<Rental, RentalDTO> {
    PageableResponseDTO<RentalDTO> getRentals(String clientEmail, String deviceName, Boolean returned, boolean pastDue, Integer page, SortDirection sort);
    RentalDTO createRental(String clientEmail, RentalRequestDTO rental);
    RentalDTO rentalReturned(Long id);
}
