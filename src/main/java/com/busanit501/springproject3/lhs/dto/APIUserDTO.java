package com.busanit501.springproject3.lhs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class APIUserDTO extends User implements OAuth2User {

    private String username;
    private String password;
    private String email;
    private String profileImageId;
    private String name;
    private String phone;
    private String address;
    private boolean social;

    private Map<String, Object> props;

    private String profileImageServer;

    public APIUserDTO(String username, String password, String email, String profileImageId,String name, String phone, String address, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.profileImageId = profileImageId;
    }

    public APIUserDTO(String username, String password, String email,
                      String profileImageId,String name, String phone,
                      String address, Collection<GrantedAuthority> authorities,
                      String profileImageServer, boolean social) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.profileImageId = profileImageId;
        this.profileImageServer = profileImageServer;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

}