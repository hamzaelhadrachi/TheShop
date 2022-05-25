package com.zerotohero.admin.user;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

public class PasswordEncoderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "28032018Hn1&";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        LOGGER.info(encodedPassword);
        assert (passwordEncoder.matches(rawPassword, encodedPassword));
     }
}
