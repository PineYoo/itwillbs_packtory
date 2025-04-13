package kr.co.itwillbs.de.sample;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPv6ToIPv4 {

	public static void main(String...strings) {
		String ip = "0:0:0:0:0:0:0:1";
		try {
			System.out.println(getIp(ip));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		System.out.println("Local IP: " + getLocalIp(ip));
	}
	
	// 인터넷 코드
	private static String getIp(String ip) throws UnknownHostException {
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostName() + "/" + address.getHostAddress();
			return ip;
		}
		
		if (ip.contains(":")) {
			System.out.println("ip.contains(\":\"): " + ip.contains(":"));
			// IPv6 주소를 IPv4로 변환하는 로직
			// 예: 0:0:0:0:0:ffff:192.168.1.1 -> 192.168.1.1
			String[] parts = ip.split(":");
			if (parts.length > 0) {
				System.out.println("parts.length: " + parts.length);
				String lastPart = parts[parts.length - 1];
				System.out.println("lastPart: " + parts[parts.length - 1]);
				if (lastPart.contains(".")) {
					System.out.println("lastPart.contains(\".\"): " + lastPart.contains("."));
					System.out.println("lastPart: " + lastPart);
					return lastPart;
				}
			}
		}
		System.out.println("ip: " + ip);
		return ip;
	}

	// AI는 천재가 맞구만!
	public static String getLocalIp(String ip) {
		// 루프백 주소 체크
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			ip = getLocalIPAddress();
		}
		return ip;
	}
	
	private static String getLocalIPAddress() {
		try {
			// 네트워크 인터페이스를 통해 실제 로컬 IP 주소 찾기
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					// IPv4 주소만 필터링
					if (address instanceof Inet4Address) {
						// 로컬 네트워크에서 유효한 IPv4 주소가 있다면 반환
						return address.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			// 네트워크 인터페이스를 찾는 데 오류가 발생한 경우
			System.out.println("Error getting local IP address: " + e.getMessage());
		}
		return "127.0.0.1"; // 기본값으로 로컬 IP를 반환
	}
}
