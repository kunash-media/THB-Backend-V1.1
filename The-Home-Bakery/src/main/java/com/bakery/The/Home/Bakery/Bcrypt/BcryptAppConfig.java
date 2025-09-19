package com.bakery.The.Home.Bakery.Bcrypt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BcryptAppConfig {

    @Bean
    BcryptEncoderConfig bCryptPasswordEncoder() {
        return new BcryptEncoderConfig();
    }
}