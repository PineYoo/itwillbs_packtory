package kr.co.itwillbs.de.sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MakeTestDataByChatGPT {

	@SuppressWarnings("unused")
	public static void toMakeWarehouseTransactionData() {
		int setCount = 4;
		LocalDate startDate = LocalDate.of(2025, 4, 14);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO material_transaction (\n")
				.append("    trans_type, code, product_idx, material_idx, unit, quantity,\n")
				.append("    status, manufacturing_date, source_location, destination_location,\n")
				.append("    memo, is_deleted, reg_id, reg_date\n").append(") VALUES\n");

		Random random = new Random();

		int[][] materials = { { 43, 10000, 9000, "면 시트지".hashCode() }, { 24, 10000, 9000, "에센스 원액".hashCode() },
				{ 30, 5000, 4500, "알루미늄 파우치".hashCode() } };

		int entryCount = 0;
		
//		String reg_id = "chatGPT";
//		String reg_date = "now()";
		
		for (int i = 0; i < setCount; i++) {
			LocalDate inDate = startDate.plusDays(i * 2);
			LocalDate inputDate = inDate.plusDays(1);
			LocalDate prodDate = inDate.plusDays(2);
			LocalDate outDate = inDate.plusDays(3);
			LocalDate returnDate = inDate.plusDays(4);

			for (int[] mat : materials) {
				int materialIdx = mat[0];
				int inQty = mat[1];
				int inputQty = mat[2];
				String matName = materialIdx == 43 ? "면 시트지" : (materialIdx == 24 ? "에센스 원액" : "알루미늄 파우치");

				sql.append(String.format("(1, '입고', NULL, %d, 1, %d, '양호', '%s', 1, 2, '%s 입고', 'N', 'chatGPT', 'now()'),\n", materialIdx,
						inQty, inDate.format(formatter), matName));
				entryCount++;

				sql.append(String.format("(3, '생산 투입', NULL, %d, 1, %d, '사용함', '%s', 2, 3, '%s 생산 투입', 'N', 'chatGPT', 'now()'),\n",
						materialIdx, inputQty, inputDate.format(formatter), matName));
				entryCount++;
			}

			int prodQty = (int) ((9000 + 9000 + 4500) * 0.95);
			sql.append(String.format("(4, '생산제품', 1, NULL, 1, %d, '생산됨', '%s', 3, 4, '마스크팩 완제품 생산', 'N', 'chatGPT', 'now()'),\n", prodQty,
					prodDate.format(formatter)));
			entryCount++;

			int outQty = (int) (prodQty * 0.8);
			sql.append(String.format("(2, '출고', 1, NULL, 1, %d, '출고됨', '%s', 4, 5, '온라인몰 출고', 'N', 'chatGPT', 'now()'),\n", outQty,
					outDate.format(formatter)));
			entryCount++;

			if (random.nextBoolean()) {
				int returnQty = random.nextInt(201) + 100; // 100~300
				sql.append(String.format("(5, '반품', 1, NULL, 1, %d, '반품', '%s', 5, 4, '소량 반품', 'N', 'chatGPT', 'now()'),\n", returnQty,
						returnDate.format(formatter)));
				entryCount++;
			}
		}

		// 마지막 쉼표 제거 후 세미콜론 추가
		int lastComma = sql.lastIndexOf(",");
		sql.replace(lastComma, lastComma + 1, ";");

		// 출력
		System.out.println(sql);
	}
	
	public static void main(String...a) {
//		MakeTestDataByChatGPT make = new MakeTestDataByChatGPT();
//		make.
		toMakeWarehouseTransactionData();
	}
}
