package oit.is.z3252.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {
  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後に / にリダイレクト
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/janken*/**").authenticated() // /janken で始まるURLは認証済みであること
            .requestMatchers("/match/**", "/fight/**").authenticated() // 対戦画面も認証済みであること
            .anyRequest().permitAll()) // 上記以外は全員アクセス可能
        // h2-console 用：CSRF チェックを外す
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**"))
        // h2-console 用：同じサイト内の iframe 表示を許可
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()));
    return http.build();
  }

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    // パスワードはどちらも isdev を bcrypt でハッシュ化したもの
    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2a$10$clV3mxZ0YkJ5JtaGBOZGFOssv3UXFd/XixXNHbz4eP7HZ/cM5p/0.").roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2a$10$clV3mxZ0YkJ5JtaGBOZGFOssv3UXFd/XixXNHbz4eP7HZ/cM5p/0.").roles("USER").build();
    UserDetails honda = User.withUsername("ほんだ")
        .password("{bcrypt}$2a$10$clV3mxZ0YkJ5JtaGBOZGFOssv3UXFd/XixXNHbz4eP7HZ/cM5p/0.").roles("USER").build();

    return new InMemoryUserDetailsManager(user1, user2, honda);
  }

}
