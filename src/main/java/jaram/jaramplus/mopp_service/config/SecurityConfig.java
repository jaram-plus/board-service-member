package jaram.jaramplus.mopp_service.config;

import jaram.jaramplus.mopp_service.config.oauth.*;
import jaram.jaramplus.mopp_service.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Qualifier("corsConfigurationSource")
    private final CorsConfigurationSource corsConfig;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtLogoutHandler jwtLogoutHandler, AuthLogoutSuccessHandler authLogoutSuccessHandler) throws Exception{
          http.csrf(AbstractHttpConfigurer::disable)
                  .httpBasic((AbstractHttpConfigurer::disable))
                  .formLogin(AbstractHttpConfigurer::disable)
                  .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                  .authorizeHttpRequests(auth -> auth
                          .requestMatchers("/", "/auth/**", "/login/**", "/logout", "/oauth2/**").permitAll()
                  .anyRequest().authenticated()
                  )
                  .oauth2Login(oauth2 -> oauth2
                          .userInfoEndpoint(userInfo ->
                                          userInfo.userService(customOAuth2MemberService))
                          .successHandler(oAuth2SuccessHandler)
                          .failureHandler(oAuth2FailureHandler)
                  )
		          .logout(logout -> logout
				          .logoutRequestMatcher(
						          PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/logout")
				          )
				          .addLogoutHandler(jwtLogoutHandler)
				          .logoutSuccessHandler(authLogoutSuccessHandler)
				          .invalidateHttpSession(false)
				          .clearAuthentication(true)
		          );

        http.cors(cors -> cors.configurationSource(corsConfig));
        return  http.build();
    }

}
