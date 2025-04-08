package kr.co.itwillbs.de.sample;

public class StringTest {

	public static void main(String...strings) {
		String requestURI = "/admin/log";
		if(requestURI.startsWith("/admin")) {
			System.out.println("2");
		} else if(requestURI.startsWith("/partner")) {
			System.out.println("3");
		} else {
			System.out.println("1");
		}
		
		
		System.out.println(!"Y".equalsIgnoreCase("N"));
	}
}
