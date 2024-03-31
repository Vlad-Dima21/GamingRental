package com.vladima.gamingrental.security.services.implementations;

import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.security.repositories.UserRepository;
import com.vladima.gamingrental.security.services.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByClient_ClientEmail(username).orElseThrow(() -> new EntityOperationException("User not found", "Email not found", HttpStatus.NOT_FOUND));
    }
}
