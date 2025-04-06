package kr.co.itwillbs.de.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SampleList {

	public static void main(String...strings) {
		
		List<String> strList = new ArrayList<>();
		strList.add("수주_미출고");
		strList.add("수주_출고완료");
		strList.add("수주_취소");
		strList.add("발주_세금계산서발행");
		strList.add("발주_수금완료");
		strList.add("발주_미발행");
		strList.add("발주_미수금");
		
		List<Integer> list = new ArrayList<>();
		
		int idx=0;
		for (String str: strList) {
			if(str.indexOf("수주") < 0) list.add(idx);
			System.out.println(idx+"] " + str.indexOf("수주"));
			idx++;
		}
		
		System.out.println(list);
		System.out.println("================================");
		System.out.println("리스트 삭제 시작!");
		for (Iterator<String> it = strList.iterator(); it.hasNext();) {
			String str = it.next();
			if (str.indexOf("수주") < 0) it.remove();
		}
		System.out.println("================================");
		System.out.println(strList);
		// href : https://hi-dot.tistory.com/4
	}
}
