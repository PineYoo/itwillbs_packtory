package kr.co.itwillbs.de.util;

import org.jasypt.encryption.StringEncryptor;

import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricStringEncryptor;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleGCMConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleGCMStringEncryptor;

public class JasyptTest {
	/**
	 * href : https://github.com/ulisesbocchio/jasypt-spring-boot
	 * @return
	 */
	void simpleEncryptor(String message) {
		SimpleAsymmetricConfig config = new SimpleAsymmetricConfig();
		config.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQfyGCvBOdgmDGU6ciGPVNB6jHsMip0b0qOrPvVTSJ/x0offjKARogA2tjGjyr3rUtwg9woMBqv/iyENR0GBnIUa0jkYsznCKeygcflnNa4mrVf7XKXLhSwtY+kCe3diPk+0QPfEsfF9/aK6pWBUFcrE8P2k2sF/8mo8dFJU1t6zQGPspHkNAgR6MLU8SjPZxnMS6EG722MdYhvSYAKsnu02Hozqb4jh/gaQ/E6NkvM3DkqIyIYsRH2smstIFEb9CCiTdiz/OsJKQLgGy/pqIVKtai3lnUxAayEV45Z61rNTOusNJf+icGhZxjqhAeoWjMxOCVmVC2GKa9sisqBgkQIDAQAB");
		StringEncryptor encryptor = new SimpleAsymmetricStringEncryptor(config);
		if(message == null) {
			message = "chupacabras";
		}
		String encrypted = encryptor.encrypt(message);
		System.out.printf("Encrypted message %s\n", encrypted);
	}
	
	void simpleEncryptor2(String message) {
		SimpleAsymmetricConfig config = new SimpleAsymmetricConfig();
		config.setKeyFormat(com.ulisesbocchio.jasyptspringboot.util.AsymmetricCryptography.KeyFormat.PEM);
		config.setPublicKey("-----BEGIN PUBLIC KEY-----\n" +
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQfyGCvBOdgmDGU6ciGP\n" +
				"VNB6jHsMip0b0qOrPvVTSJ/x0offjKARogA2tjGjyr3rUtwg9woMBqv/iyENR0GB\n" +
				"nIUa0jkYsznCKeygcflnNa4mrVf7XKXLhSwtY+kCe3diPk+0QPfEsfF9/aK6pWBU\n" +
				"FcrE8P2k2sF/8mo8dFJU1t6zQGPspHkNAgR6MLU8SjPZxnMS6EG722MdYhvSYAKs\n" +
				"nu02Hozqb4jh/gaQ/E6NkvM3DkqIyIYsRH2smstIFEb9CCiTdiz/OsJKQLgGy/pq\n" +
				"IVKtai3lnUxAayEV45Z61rNTOusNJf+icGhZxjqhAeoWjMxOCVmVC2GKa9sisqBg\n" +
				"kQIDAQAB\n" +
				"-----END PUBLIC KEY-----\n");
		StringEncryptor encryptor = new SimpleAsymmetricStringEncryptor(config);
		if(message == null) {
			message = "chupacabras";
		}
		String encrypted = encryptor.encrypt(message);
		System.out.printf("Encrypted message %s\n", encrypted);
	}
	
	/**
	 * jasypt.encryptor.gcm-secret-key-string 또는 jasypt.encryptor.gcm-secret-key-location을 통해 키를 사용하는 경우, 키를 base64로 인코딩했는지 확인
	 * <br>base64 문자열 값은 jasypt.encryptor.gcm-secret-key-string으로 설정하거나 파일에 저장하고 스프링 리소스 로케이터를 사용하여 jasypt.encryptor.gcm-secret-key-location 속성에서 해당 파일로 이동할 수 있음
	 * <br>외부 도구로 비밀을 암호화하는 경우 이 매개변수가 동일한지 확인. 선택적으로, 자신만의 StringEncryptor 빈을 만들 수 있음.
	 * @param keyPassword 비밀번호를 사용하여 AES 256-GCM을 사용하여 속성을 암호화/해독할 수 있습니다. 비밀번호는 시작 시 키를 생성하는 데 사용되므로 설정해야 하거나 설정할 수 있는 몇 가지 속성이 있음
	 * @param keySalt 선택사항 (기본값:0, 입력시 문자열을 base64 형식이어야함)
	 * @return
	 */
	StringEncryptor stringEncryptorUsingAPassword(String keyPassword) {
		SimpleGCMConfig config = new SimpleGCMConfig();
		config.setSecretKeyPassword(keyPassword == null ? "chupacabras" : keyPassword);
		config.setSecretKeyIterations(1000);
		config.setSecretKeySalt("HrqoFr44GtkAhhYN+jP8Ag==");
		config.setSecretKeyAlgorithm("PBKDF2WithHmacSHA256");
		return new SimpleGCMStringEncryptor(config);
	}
	
	public static void main(String...strings) {
		JasyptTest jt = new JasyptTest();
		jt.simpleEncryptor(null);
		jt.simpleEncryptor2(null);
		String keyPassword = "chupacabras";
		String plainStr = "test";
		String encrptedStr = jt.stringEncryptorUsingAPassword(keyPassword).encrypt(plainStr);
		System.out.printf("encrptedStr is %s\n", encrptedStr); 
		String decryptedStr = jt.stringEncryptorUsingAPassword(keyPassword).decrypt(encrptedStr);
		System.out.printf("decryptedStr is %s\n", decryptedStr);
		
		// /dv2FG2AueQnWESrtvx3VOsoaALL262p75J5+K9ZUpc=
		// /8ab9WOGU4jqzeBWXVoCgylLv10/0XbYp7xdPwgmaTvs=
		
		if(plainStr.equals(decryptedStr)) {
			System.out.println("pass!");
		} else {
			System.out.println("fail!");
		}
	}
}
