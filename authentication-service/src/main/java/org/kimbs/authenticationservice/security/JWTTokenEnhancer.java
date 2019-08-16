package org.kimbs.authenticationservice.security;

import org.kimbs.authenticationservice.exception.ResourceNotFoundException;
import org.kimbs.authenticationservice.model.UserOrganization;
import org.kimbs.authenticationservice.repository.OrgUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JWTTokenEnhancer implements TokenEnhancer {

    @Autowired
    private OrgUserRepository orgUserRepo;

    private String getOrgId(String username){
        UserOrganization orgUser = orgUserRepo.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException(String.format("Resource Not Found Exception With user_org username: [%s]", username)));
        return orgUser.getOrganizationId();
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        String orgId =  getOrgId(authentication.getName());

        additionalInfo.put("organizationId", orgId);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}