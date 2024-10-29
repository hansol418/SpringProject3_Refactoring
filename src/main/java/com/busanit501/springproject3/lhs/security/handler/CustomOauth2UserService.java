package com.busanit501.springproject3.lhs.security.handler;

import com.busanit501.springproject3.lhs.dto.APIUserDTO;
import com.busanit501.springproject3.lhs.entity.MemberRole;
import com.busanit501.springproject3.lhs.entity.User;
import com.busanit501.springproject3.lhs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("CustomOauth2UserService : userRequest = " + userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("CustomOauth2UserService : clientRegistration : clientName = " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((k, v) -> {
            log.info("CustomOauth2UserService : k = " + k + " v = " + v);
        });

        String email = null;
        String profileUrlThumbnail = null;

        switch (clientName) {
            case "kakao":

                email = getKakaoEmail(paramMap);

                profileUrlThumbnail = getKakaoProfile(paramMap);
                break;
        }

        log.info("CustomOauth2UserService : email = " + email);


        return generateDTO(email,profileUrlThumbnail, paramMap);
    }

    private APIUserDTO generateDTO(String email, String profile_img , Map<String, Object> paramMap) {
        log.info("lsy generateDTO : email = " + email + " profile_img = " + profile_img);
        Optional<User> result = memberRepository.findByEmail(email);
        log.info("lsy result :  = " + result );

        if (result.isEmpty()) {

            User member = User.builder()
                    .username(email)
                    .password(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .profileImageServer(profile_img)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            APIUserDTO memberSecurityDTO =
                    new APIUserDTO(email, "1111", email,null,null,null,null, Arrays.asList(
                            new SimpleGrantedAuthority("ROLE_USER")
                    ),profile_img,true);
            memberSecurityDTO.setProps(paramMap);
            return memberSecurityDTO;
        }
        else {
            User member = result.get();
            log.info("lsy generateDTO else " +member );
            log.info("lsy generateDTO else2 " +member.getRoleSet());
            APIUserDTO memberSecurityDTO =
                    new APIUserDTO(
                            member.getUsername(),
                            member.getPassword(),
                            member.getEmail(),
                            member.getProfileImageId(),
                            member.getName(),
                            member.getPhone(),
                            member.getAddress(),
                            member.getRoleSet().stream().map(
                                    memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())

                            ).collect(Collectors.toList()),
                            member.getProfileImageServer(),
                            member.isSocial()
                    );
            log.info("lsy generateDTO else3 " +memberSecurityDTO );
            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("CustomOauth2UserService : kakao = ");

        Object value = paramMap.get("kakao_account");
        log.info("CustomOauth2UserService : kakao_account = " + value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");
        log.info("CustomOauth2UserService : email = " + email);
        return email;
    }
    private String getKakaoProfile(Map<String, Object> paramMap) {
        log.info("CustomOauth2UserService : kakao = ");

        Object value = paramMap.get("properties");
        log.info("CustomOauth2UserService : properties = " + value);

        LinkedHashMap propertiesMap = (LinkedHashMap) value;

        String thumbnail_image = (String) propertiesMap.get("thumbnail_image");
        log.info("CustomOauth2UserService : thumbnail_image = " + thumbnail_image);
        return thumbnail_image;
    }

}