package com.example.tquan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类
 * Created by chenjin on 2021/5/18 9:47
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.httpBasic()     //security提供的登录页面
                //.and()

                .authorizeRequests()    //认证请求
                .antMatchers("/register", "/doRegister", "/login", "/doLogin", "/css/**", "/js/**", "/img/**", "**/favicon.ico").permitAll()     //除了***能够无认证访问
                .anyRequest()
                .authenticated()    //任何请求都需要认证
                .and()
                .csrf()
                .disable();     //CSRF跨站请求伪造直接关闭

       /* http
                .formLogin()                          //使用表单登录页面
                .loginPage("/login")                 //登录url
                .loginProcessingUrl("/doLogin")     //登录提交url
                .and()
                .authorizeRequests()
                .antMatchers("/register", "/doRegister", "/login", "/doLogin")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable();*/

        //http.logout().logoutUrl("/logout").logoutSuccessUrl("/loginPage").invalidateHttpSession(true);
    }
}
