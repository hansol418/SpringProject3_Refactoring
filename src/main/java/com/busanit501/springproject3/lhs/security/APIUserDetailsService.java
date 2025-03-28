package com.busanit501.springproject3.lhs.security;



import com.busanit501.springproject3.lhs.dto.APIUserDTO;
import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {


    private final UserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> result = apiUserRepository.findByUsername(username);

        User apiUser = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find mid"));

        log.info("lsy APIUserDetailsService apiUser-------------------------------------");


        APIUserDTO dto =  new APIUserDTO(
                apiUser.getUsername(),
                apiUser.getPassword(),
                apiUser.getEmail(),
                apiUser.getName(),
                apiUser.getPhone(),
                apiUser.getAddress(),
                apiUser.getProfileImageId(),
                apiUser.getRoleSet().stream().map(
                        memberRole -> new SimpleGrantedAuthority("ROLE_"+ memberRole.name())
                ).collect(Collectors.toList()));

        log.info("lsy dto : "+dto);

        return dto;
    }
}