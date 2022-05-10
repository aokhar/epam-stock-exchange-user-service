package com.epam.rd.stock.exchange.config;

import com.epam.rd.stock.exchange.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.anonymousEndPoints}")
    private final String[] anonymousEndPoints;

    private final OAuth2AuthenticationSuccessHandler successHandler;

    @Value("${security.freeEndPoints}")
    private final String[] freeEndPoints;

    @Value("${security.oauth2.endpoint}")
    private String authorizationRequestBaseUri;

    @Value("${security.adminEndpoints}")
    private String[] adminEndpoints;

    @Value("${security.userEndPoints}")
    private final String[] userEndPoints;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(freeEndPoints).permitAll()
                .antMatchers(anonymousEndPoints).anonymous()
                .antMatchers(adminEndpoints).hasAuthority("ADMIN")
                .antMatchers(userEndPoints).hasAuthority("USER")
                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                .baseUri(authorizationRequestBaseUri)
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .successHandler(successHandler)
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())

                .and()
                .failureUrl("/loginError")

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }


    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

}
