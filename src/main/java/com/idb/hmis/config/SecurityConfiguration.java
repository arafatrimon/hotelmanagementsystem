package com.idb.hmis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private LoggingAccessDeniedHandler loggingAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall defaultHtttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/static/**", "/uploads/**", "/dist/**", "/css/**", "/js/**", "/img/**", "/dist/images/**").permitAll()
                .antMatchers("/fontawesome-free/**", "/webfonts/**", "/admin/register").permitAll()
                .antMatchers("/", "/register", "/home").permitAll()
                .antMatchers("/hostel/*", "/employee/manager/*", "/employee/form/manager").hasRole("ADMIN")
                .antMatchers("/branch/display", "/branch/form", "/branch/update", "/branch/details").hasRole("ADMIN")
                .antMatchers("/branch/upload", "/branch/delete", "/branch/", "/branch/save").hasRole("ADMIN")
                .antMatchers("/branch/{branchId}").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .successForwardUrl("/loginsuccess")
                .and().logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and().exceptionHandling().accessDeniedHandler(loggingAccessDeniedHandler);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
    }

}
