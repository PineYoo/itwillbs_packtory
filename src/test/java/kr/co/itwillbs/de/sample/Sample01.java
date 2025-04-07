package kr.co.itwillbs.de.sample;

import java.io.File;
import java.util.regex.Matcher;

public class Sample01 {
	public static void main(String[] args) {

		// '/' 또는 '\'가 있는지 확인하는 정규표현식
		String regex = "[\\\\/]";
		boolean case1 = "/".matches(regex);
		System.out.println("/ = " + case1);

		boolean case2 = "\\".matches(regex);
		System.out.println("/ = " + case2);

		// 프로그램이 실행되는 환경에 따라 JVM이 파일 경로 구분자를 정해준다
		// File.separator를 사용하여 파일 경로 구분자로 바꾼다
		System.out.println("File.separator : " + File.separator);
		String replacedText = Matcher.quoteReplacement(File.separator);
		System.out.println("replacedText : " + replacedText);
		String replace1 = "./".replaceAll(regex, replacedText);
		System.out.println("./ replace --> " + replace1);

		String replace2 = ".\\".replaceAll(regex, replacedText);
		System.out.println(".\\ replace --> " + replace2);

		String replace3 = "./test/log".replaceAll(regex, replacedText);
		System.out.println("./test/log replace --> " + replace3);

		String replace4 = ".\\test\\log".replaceAll(regex, replacedText);
		System.out.println(".\\test\\log replace --> " + replace4);

		String replace5 = "./test\\log".replaceAll(regex, replacedText);
		System.out.println("./test\\log replace --> " + replace5);

		String replace6 = "/packtory/uploads".replaceAll(regex, replacedText);
		System.out.println("/packtory/uploads replace --> " + replace6);
		
		String replace7 = ".\\test/log".replaceAll(regex, replacedText);
		System.out.println(".\\test/log replace --> " + replace7);
	}
}
