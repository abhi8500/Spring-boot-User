package com.userService.Config;


import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.NtpTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TotpConfig {

    @Bean
    public SecretGenerator secretGenerator() {
        return new DefaultSecretGenerator();
    }

    @Bean
    public QrGenerator qrGenerator() {
        return new ZxingPngQrGenerator();
    }

    @Bean
    public CodeVerifier codeVerifier() throws Exception {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator(), timeProvider());
        verifier.setTimePeriod(30);
        verifier.setAllowedTimePeriodDiscrepancy(1);
        return verifier;
    }

    @Bean
    public TimeProvider timeProvider() throws Exception {
        return new NtpTimeProvider("pool.ntp.org");
    }


    @Bean
    public CodeGenerator codeGenerator() {
        return new DefaultCodeGenerator(HashingAlgorithm.SHA256, 6);
    }
}
