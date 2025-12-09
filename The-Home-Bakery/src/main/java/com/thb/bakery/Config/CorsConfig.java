package com.thb.bakery.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("*") // Temporarily allow all
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(false) // Must be false with *
//                .maxAge(3600);
//    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://127.0.0.1:5500",    // VS Code Live Server
                        "http://localhost:5500",     // agar localhost:5500 pe chal raha hai
                        "http://127.0.0.1:5501",
                        "http://127.0.0.1:3000",     // agar React/Vite use karega future mein
                        "https://thehomebakerypune.com"  // final live site
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)   // Yeh TRUE hona chahiye session ke liye
                .maxAge(3600);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins(
//                        "https://oyjewells.com",
//                        "https://admin.oyjewells.com",
//                        "http://127.0.0.1:3000",
//                        "http://127.0.0.1:5500",
//                        "http://127.0.0.1:5501",
//                        "http://127.0.0.1:5502",
//                        "http://localhost:63342"
//                )
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}