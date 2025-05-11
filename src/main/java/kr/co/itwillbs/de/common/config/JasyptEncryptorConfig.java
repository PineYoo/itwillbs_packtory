package kr.co.itwillbs.de.common.config;

import java.util.Properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * application.yml 암호화 된 값을 복호화 하기 위한 클래스
 */
@Configuration
@EnableEncryptableProperties
public class JasyptEncryptorConfig {

	private final String PROPERTY_PASSWORD = "encryptor.password";
	private final String PROPERTY_ALGORITHM = "encryptor.algorithm";
	//private final String PROPERTY_SLAT = "encryptor.password";
	
	@Bean(name = "jasyptStringEncryptor")
	StringEncryptor stringEncryptor() {
		Properties secrets = EnvPropertyConfig.load(); // 이렇게 가져와서 쓰는거면.. 그냥 이 클래스 밑에 메서드가 낫지 않나? 고민이다.

		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(secrets.getProperty(PROPERTY_PASSWORD));
		config.setAlgorithm(secrets.getProperty(PROPERTY_ALGORITHM, "PBEWITHHMACSHA512ANDAES_256"));
		config.setKeyObtentionIterations("2000");
		config.setPoolSize("1");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(config);
		return encryptor;
	}
}