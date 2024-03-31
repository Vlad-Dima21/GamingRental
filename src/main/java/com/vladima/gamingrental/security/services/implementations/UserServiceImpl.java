package com.vladima.gamingrental.security.services.implementations;

import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import com.vladima.gamingrental.security.dto.UserDTO;
import com.vladima.gamingrental.security.dto.UserResponseDTO;
import com.vladima.gamingrental.security.models.User;
import com.vladima.gamingrental.security.repositories.RoleRepository;
import com.vladima.gamingrental.security.repositories.UserRepository;
import com.vladima.gamingrental.security.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDTO, UserRepository> implements UserService {

    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
        super(repository);
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO login(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserResponseDTO registerClient(UserClientDTO userClientDTO) {
        var existingUserEmail = getRepository().findByClient_ClientEmail(userClientDTO.getUserEmail());
        var existingUserPhone = getRepository().findByClient_ClientPhone(userClientDTO.getUserPhone());
        if (existingUserEmail.isPresent() || existingUserPhone.isPresent()) {
            throw new EntityOperationException("User not registered", "Email/Phone is already in use", HttpStatus.CONFLICT);
        }

        var clientRole = roleRepository.findByAuthority("CLIENT");
        if (clientRole == null) {
            throw new EntityOperationException("Internal error", "Client role not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        userClientDTO.setUserPassword(passwordEncoder.encode(userClientDTO.getUserPassword()));

        var newUser = new UserClientDTO(
                userClientDTO.getUserEmail(),
                passwordEncoder.encode(userClientDTO.getUserPassword()),
                userClientDTO.getUserName(),
                userClientDTO.getUserPhone()
        ).toModel();
        newUser.setAuthority(clientRole);

        getRepository().save(newUser);
        return authenticateUser(userClientDTO);
    }

    private UserResponseDTO authenticateUser(UserDTO userDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUserEmail(), userDTO.getUserPassword())
        );
        var token = generateTokenFromAuth(auth);
        return new UserResponseDTO(userDTO.getUserEmail(), token);
    }

    private String generateTokenFromAuth(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .claim("roles", scope)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO updateInfo(Long id, UserDTO userDTO) {
        return null;
    }

    @Override
    public String getLogName() {
        return "User";
    }
}
