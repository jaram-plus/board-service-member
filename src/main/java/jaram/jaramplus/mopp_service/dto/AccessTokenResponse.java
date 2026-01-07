package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenResponse {

    private String accessToken;
}
