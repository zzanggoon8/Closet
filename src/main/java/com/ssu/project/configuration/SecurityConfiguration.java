package com.ssu.project.configuration;

import com.ssu.project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;
    private final DataSource dataSource;

    /**
     * 로그인 유지(Persistence Login)
     * @return
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);

        return repository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * 로그인 기능
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/find-pw");  //
        http.cors()
                .and().formLogin()
                .loginPage("/login")
                .permitAll()

                .and().authorizeRequests()
                .antMatchers(
                        "/common.css",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "**/*.ico"
                )
                .permitAll()

                .mvcMatchers(
                        "/",
                        "/login",
                        "/signup",
                        "/check-email-result",
                        "/check-email-token",
                        "/send-reset-password-link",
                        "/reset-password",
                        "/view/notify",
                        "/store/detail/**",
                        "/store/like",
                        "/find-pw/**",
                        "/cody",
                        "/review",
                        "/best",
                        "/delete/**",
                        "/weather/**",
                        "/review/delete",
                        "/review/modify",
                        "/view/category"

                )
                .permitAll()

                .mvcMatchers(HttpMethod.GET, "/item/*")
                .permitAll()

                .anyRequest().permitAll()

                // login 유지 기능 추가
//                .and().rememberMe()
//                .userDetailsService(memberService) // 인증 관련 buisiness logig을 담당하는 Service 객체를 설정해줌

//
                // logout 기능 추가
                .and().logout()
                .logoutUrl("/logout") // 해당 location은 default
                .invalidateHttpSession(true) // logout 시 session을 갱신한다.
                .logoutSuccessUrl("/"); // logout 성공 시 이동할 경로

        http.cors().configurationSource(corsConfigurationSource());
    }
}
