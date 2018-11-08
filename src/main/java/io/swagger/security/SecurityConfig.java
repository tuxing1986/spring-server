package io.swagger.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;
  
  @Autowired
  private AccessDeniedHandler accessDeniedHandler;
  
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler);

    httpSecurity.authorizeRequests().antMatchers("/").permitAll();
    /*
    // Basic auth
    httpSecurity.httpBasic().realmName("tco");

    // Authorization
    httpSecurity.authorizeRequests()

        // Admin
        .antMatchers("/users/**").hasRole(Role.Admin.toString())

        // Admin and Read/Write
        .antMatchers(HttpMethod.POST).hasAnyRole(Role.Admin.toString(), Role.RW.toString())
        .antMatchers(HttpMethod.PUT).hasAnyRole(Role.Admin.toString(), Role.RW.toString())
        .antMatchers(HttpMethod.DELETE).hasAnyRole(Role.Admin.toString(), Role.RW.toString())

        // All roles can have read permission
        .anyRequest().authenticated();*/
  }
  
  @Autowired
  public void configureInMemoryBasicAuth(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("readOnlyUser").password("secret").roles(Role.RO.toString()).and()
        .withUser("readWriteUser").password("secret").roles(Role.RW.toString()).and()
        .withUser("admin").password("secret").roles(Role.Admin.toString()).and();
  }
}
