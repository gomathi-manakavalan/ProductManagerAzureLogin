package net.codejava;


import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationProperties;
import com.microsoft.azure.spring.autoconfigure.aad.AADOAuth2UserService;
import com.microsoft.azure.spring.autoconfigure.aad.ServiceEndpointsProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomADOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public static final String ROLE_COACH = "ROLE_coach";
    public static final String ROLE_STUDENT = "ROLE_student";
/*    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CoachRepository coachRepository;*/

    AADOAuth2UserService aadoAuth2UserService;

    public CustomADOAuth2UserService(AADAuthenticationProperties aadAuthProps,
                                     ServiceEndpointsProperties serviceEndpointsProps) {
        aadoAuth2UserService = new AADOAuth2UserService(aadAuthProps, serviceEndpointsProps);
    }

/*    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = aadoAuth2UserService.loadUser(userRequest);
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(user.getAuthorities());
        String userName = (String) user.getIdToken().getClaims().get("unique_name");
        Optional<Student> optionalStudent = studentRepository.findByUserId(userName);
        optionalStudent.ifPresent(vendorUser ->
                mappedAuthorities.add(new SimpleGrantedAuthority(ROLE_STUDENT)));
        if (!optionalStudent.isPresent()) {
            Optional<Coach> optionalCoach = coachRepository.findByUsername(userName);
            optionalCoach.ifPresent(companyUser -> mappedAuthorities
                    .add(new SimpleGrantedAuthority(ROLE_COACH)));

        }
        return new DefaultOidcUser(mappedAuthorities, user.getIdToken(),
                this.getUserNameAttrName(userRequest));
    }*/

/*    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        System.out.println("CustomOAuth2UserService invoked");
        return new CustomOAuth2User(user);
    }*/

    private String getUserNameAttrName(OAuth2UserRequest userRequest) {
        String userNameAttrName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        if (StringUtils.isEmpty(userNameAttrName)) {
            userNameAttrName = "name";
        }
        return userNameAttrName;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = aadoAuth2UserService.loadUser(userRequest);
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(user.getAuthorities());
/*        String userName = (String) user.getIdToken().getClaims().get("unique_name");
        Optional<Student> optionalStudent = studentRepository.findByUserId(userName);
        optionalStudent.ifPresent(vendorUser ->
                mappedAuthorities.add(new SimpleGrantedAuthority(ROLE_STUDENT)));
        if (!optionalStudent.isPresent()) {
            Optional<Coach> optionalCoach = coachRepository.findByUsername(userName);
            optionalCoach.ifPresent(companyUser -> mappedAuthorities
                    .add(new SimpleGrantedAuthority(ROLE_COACH)));

        }*/
        return new DefaultOidcUser(mappedAuthorities, user.getIdToken(),
                this.getUserNameAttrName(userRequest));
    }
}
