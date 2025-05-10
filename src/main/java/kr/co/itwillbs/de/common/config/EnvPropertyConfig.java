package kr.co.itwillbs.de.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.core.io.ClassPathResource;

/**
 * <pre>
 * 리눅스 환경에서는 /home/projectName/env/env.properties or 프로젝트 파티션 경로/env/env.properties 에 둘 것이고
 * 윈도우에서는 classpath:/env/env.properties 에 두고 작업을 하는 걸로!
 * 다만, 주의 해야 할 점은 evn.properties 파일은 반드시 git에 ignore가 되어 있어야 한다.
 * 
 * - 추가
 * 규모가 큰 프로젝트의 경우 역할이나 성질에 따라 foo.properties, bar.properties 로 구분할텐데
 * 작은 규모의 프로젝트에서는 env(환경).properties 라는 걸 만들어두고
 * 추후에 사용될 수 있는 api키나 token 값들도 저장해서 부트가 기동될 때 자바프로퍼티로 불러와서 사용해야 할텐데
 * 이정도에서 멈추는게 맞나...?
 * - 추가2
 * @ConfigurationProperties(prefix = "blabla") 아무거나 입력 할것 같아서 통일시킬게 없을 것 같다.
 * public class EnvProperties { 필드 입력}
 * 식의 클래스로 부트스럽게 활용할 수 있을 것 같다.
 * </pre>
 */
public class EnvPropertyConfig {

	private static final String ENV_FILE_PATH = "env/env.properties";

	public static Properties load() {
		Properties props = new Properties();
		try (InputStream input = new ClassPathResource(ENV_FILE_PATH).getInputStream()) {
			props.load(input);
		} catch (IOException e) {
			throw new IllegalStateException("env.properties not found env file: " + ENV_FILE_PATH, e);
		}
		return props;
	}

	/**
	 * 암호화 테스트
	 * @param args
	 */
	public static void main(String... args) {
		// 0) 암호화할 플래인Str 셋
		List<String> plainStrs = new ArrayList<>();
		plainStrs.add("com.mysql.cj.jdbc.Driver");
		
		// 1) env.properties 로딩
		Properties secrets = new Properties();
		try (InputStream in = new ClassPathResource(ENV_FILE_PATH).getInputStream()) {
			secrets.load(in);
		} catch (Exception e) {
			System.err.println("env properties 로드 실패: " + e.getMessage());
			System.exit(2);
		}

		// 2) Jasypt Encryptor 구성
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(secrets.getProperty("encryptor.password"));
		System.out.println(secrets.getProperty("encryptor.password"));
		config.setAlgorithm(secrets.getProperty("encryptor.algorithm", "PBEWITHHMACSHA512ANDAES_256"));
		System.out.println(secrets.getProperty("encryptor.algorithm"));
		config.setKeyObtentionIterations(secrets.getProperty("encryptor.iterations", "2000"));
		config.setPoolSize(secrets.getProperty("encryptor.pool-size", "1"));
		config.setSaltGeneratorClassName(
				secrets.getProperty("encryptor.salt-generator", "org.jasypt.salt.RandomSaltGenerator"));
		// iv-generator는 AES 알고리즘 쓸 때만 필요하므로, env 파일에 없으면 생략합니다.
		config.setIvGeneratorClassName(
				secrets.getProperty("encryptor.iv-generator", "org.jasypt.iv.RandomIvGenerator"));
		config.setStringOutputType("base64");

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(config);

		// 3) 암호화 & 출력
		for (String string : plainStrs) {
			String encrypted = encryptor.encrypt(string);
			System.out.println("ENC(" + encrypted + ")");
		}
	}
}