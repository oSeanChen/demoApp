package com.oseanchen.demoapp.config;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig{
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(customizer -> customizer.disable())
//                .authorizeHttpRequests((registry) -> registry //對所有訪問HTTP端點的HttpServletRequest進行限制
//                        .requestMatchers(HttpMethod.GET, "/register", "/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/user/?*").permitAll()
//                        .anyRequest().authenticated()//其他尚未匹配到的路徑都需要身份驗證
//                )
//                .httpBasic(Customizer.withDefaults()); // HTTP Basic 身份驗證作為默認的驗證方式
//        return http.build();
//    }
//}
