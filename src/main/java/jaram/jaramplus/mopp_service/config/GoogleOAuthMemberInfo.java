package jaram.jaramplus.mopp_service.config;

import java.util.Map;

public class GoogleOAuthMemberInfo extends OAuth2MemberInfo {

    public GoogleOAuthMemberInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
