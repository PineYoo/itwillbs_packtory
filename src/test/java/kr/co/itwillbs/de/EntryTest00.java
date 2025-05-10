package kr.co.itwillbs.de;

import java.util.HashMap;
import java.util.Map;

public class EntryTest00 {

	public static void main(String... strings) {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");
		
		System.out.println(map);
		
		
		for (Map.Entry<String, String> entry : map.entrySet()) {
		System.out.println("key : " + entry.getKey());
		System.out.println("value : " + entry.getValue());
		}
	}
}
