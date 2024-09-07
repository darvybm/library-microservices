package pucmm.eict.library.ClientService.config;


import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {

    @Value("${crypto.secret-key}")
    private String secretKey;

    @Bean
    public PooledPBEStringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPoolSize(4);
        return encryptor;
    }

    public String encrypt(String input) {
        return stringEncryptor().encrypt(input);
    }

    public String decrypt(String encryptedInput) {
        return stringEncryptor().decrypt(encryptedInput);
    }
}
