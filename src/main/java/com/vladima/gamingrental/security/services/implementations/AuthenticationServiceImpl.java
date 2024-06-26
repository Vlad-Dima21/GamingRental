package com.vladima.gamingrental.security.services.implementations;

import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.security.repositories.UserRepository;
import com.vladima.gamingrental.security.services.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(username).matches()) {
            return repository.findByAdminUsername(username).orElseThrow(() -> new EntityOperationException("User not found", "Invalid admin", HttpStatus.NOT_FOUND));
        }
        return repository.findByClient_ClientEmail(username).orElseThrow(() -> new EntityOperationException("User not found", "Email not found", "userEmail", HttpStatus.NOT_FOUND));
    }
}
