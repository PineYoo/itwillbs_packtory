package kr.co.itwillbs.de.common.constant;

/**
 * <pre>
 * 찾아보다 좋은걸 주워옴
 * HREF: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
 * [부록: ASCII Art]
 * HREF: https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=Packtory
 * HREF2: https://fsymbols.com/generators/tarty/
 * </pre>
 */
public enum ConsoleColor {
	// Color end string, color reset
	RESET("\033[0m"),

	// Regular Colors. Normal color, no bold, background color etc.
	BLACK("\033[0;30m"), // BLACK
	RED("\033[0;31m"), // RED
	GREEN("\033[0;32m"), // GREEN
	YELLOW("\033[0;33m"), // YELLOW
	BLUE("\033[0;34m"), // BLUE
	MAGENTA("\033[0;35m"), // MAGENTA
	CYAN("\033[0;36m"), // CYAN
	WHITE("\033[0;37m"), // WHITE

	// Bold
	BLACK_BOLD("\033[1;30m"), // BLACK
	RED_BOLD("\033[1;31m"), // RED
	GREEN_BOLD("\033[1;32m"), // GREEN
	YELLOW_BOLD("\033[1;33m"), // YELLOW
	BLUE_BOLD("\033[1;34m"), // BLUE
	MAGENTA_BOLD("\033[1;35m"), // MAGENTA
	CYAN_BOLD("\033[1;36m"), // CYAN
	WHITE_BOLD("\033[1;37m"), // WHITE

	// Underline
	BLACK_UNDERLINED("\033[4;30m"), // BLACK
	RED_UNDERLINED("\033[4;31m"), // RED
	GREEN_UNDERLINED("\033[4;32m"), // GREEN
	YELLOW_UNDERLINED("\033[4;33m"), // YELLOW
	BLUE_UNDERLINED("\033[4;34m"), // BLUE
	MAGENTA_UNDERLINED("\033[4;35m"), // MAGENTA
	CYAN_UNDERLINED("\033[4;36m"), // CYAN
	WHITE_UNDERLINED("\033[4;37m"), // WHITE

	// Background
	BLACK_BACKGROUND("\033[40m"), // BLACK
	RED_BACKGROUND("\033[41m"), // RED
	GREEN_BACKGROUND("\033[42m"), // GREEN
	YELLOW_BACKGROUND("\033[43m"), // YELLOW
	BLUE_BACKGROUND("\033[44m"), // BLUE
	MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
	CYAN_BACKGROUND("\033[46m"), // CYAN
	WHITE_BACKGROUND("\033[47m"), // WHITE

	// High Intensity
	BLACK_BRIGHT("\033[0;90m"), // BLACK
	RED_BRIGHT("\033[0;91m"), // RED
	GREEN_BRIGHT("\033[0;92m"), // GREEN
	YELLOW_BRIGHT("\033[0;93m"), // YELLOW
	BLUE_BRIGHT("\033[0;94m"), // BLUE
	MAGENTA_BRIGHT("\033[0;95m"), // MAGENTA
	CYAN_BRIGHT("\033[0;96m"), // CYAN
	WHITE_BRIGHT("\033[0;97m"), // WHITE

	// Bold High Intensity
	BLACK_BOLD_BRIGHT("\033[1;90m"), // BLACK
	RED_BOLD_BRIGHT("\033[1;91m"), // RED
	GREEN_BOLD_BRIGHT("\033[1;92m"), // GREEN
	YELLOW_BOLD_BRIGHT("\033[1;93m"), // YELLOW
	BLUE_BOLD_BRIGHT("\033[1;94m"), // BLUE
	MAGENTA_BOLD_BRIGHT("\033[1;95m"), // MAGENTA
	CYAN_BOLD_BRIGHT("\033[1;96m"), // CYAN
	WHITE_BOLD_BRIGHT("\033[1;97m"), // WHITE

	// High Intensity backgrounds
	BLACK_BACKGROUND_BRIGHT("\033[0;100m"), // BLACK
	RED_BACKGROUND_BRIGHT("\033[0;101m"), // RED
	GREEN_BACKGROUND_BRIGHT("\033[0;102m"), // GREEN
	YELLOW_BACKGROUND_BRIGHT("\033[0;103m"), // YELLOW
	BLUE_BACKGROUND_BRIGHT("\033[0;104m"), // BLUE
	MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"), // MAGENTA
	CYAN_BACKGROUND_BRIGHT("\033[0;106m"), // CYAN
	WHITE_BACKGROUND_BRIGHT("\033[0;107m"); // WHITE

	private final String code;

	ConsoleColor(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}

	/**
	 * 선택한 색 + System.out.print(text)
	 * 
	 * @param text
	 */
	public void print(String text) {
		System.out.print(this.code + text + RESET);
	}

	/**
	 * 선택한 색 + System.out.println(text)
	 * 
	 * @param text
	 */
	public void println(String text) {
		System.out.println(this.code + text + RESET);
	}

	/**
	 * 선택한 색 + System.out.println(format, args)
	 * 
	 * @param format
	 * @param args
	 */
	public void printf(String format, Object... args) {
		System.out.printf(this.code + format + RESET, args);
	}

	/**
	 * 선택 색 + 배경색 + 텍스트 출력 <br>
	 * System.out.println(textColor,backgroundColor,text)
	 * 
	 * @param textColor
	 * @param backgroundColor
	 * @param text
	 */
	public static void colorizePrintln(ConsoleColor textColor, ConsoleColor backgroundColor, String text) {
		System.out.println(backgroundColor.toString() + textColor.toString() + text + RESET);
	}

	/**
	 * 텍스트 색상과 배경색 조합 메서드
	 * 
	 * @param textColor
	 * @param backgroundColor
	 * @param text
	 * @return
	 */
	public static String colorize(ConsoleColor textColor, ConsoleColor backgroundColor, String text) {
		return backgroundColor.toString() + textColor.toString() + text + RESET;
	}

	// AI 는 정말 갓인가?
	
	// 단일 색상 문자열 반환 메서드
	public String colorize(String text) {
		return this.code + text + RESET;
	}

	public static String colorizeMulti(Object... parts) {
		if (parts.length % 2 != 0) {
			throw new IllegalArgumentException("인자는 (색상, 텍스트) 쌍으로 제공해야 합니다.");
		}

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < parts.length; i += 2) {
			if (parts[i] instanceof ConsoleColor && parts[i + 1] instanceof String) {
				ConsoleColor color = (ConsoleColor) parts[i];
				String text = (String) parts[i + 1];
				result.append(color.colorize(text));
			} else {
				throw new IllegalArgumentException("인자는 (ConsoleColor, String) 쌍으로 제공해야 합니다.");
			}
		}
		return result.toString();
	}

	// 텍스트의 각 문자에 레인보우 효과 적용 메서드
	public static String rainbow(String text) {
		ConsoleColor[] colors = { RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA };
		StringBuilder rainbow = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			ConsoleColor color = colors[i % colors.length];
			rainbow.append(color).append(text.charAt(i));
		}

		rainbow.append(RESET);
		return rainbow.toString();
	}

	// 텍스트에 그라데이션 효과 적용 메서드
	public static String gradient(String text, ConsoleColor start, ConsoleColor end) {
		// 간단한 구현으로, 텍스트를 둘로 나누어 시작 색상과 끝 색상을 적용합니다.
		int mid = text.length() / 2;
		String firstHalf = text.substring(0, mid);
		String secondHalf = text.substring(mid);

		return start.toString() + firstHalf + end.toString() + secondHalf + RESET;
	}

	// 박스 생성 메서드
	public static void printBox(String text, ConsoleColor borderColor, ConsoleColor textColor) {
		int length = text.length() + 4;
		String border = "+" + "-".repeat(length - 2) + "+";

		System.out.println(borderColor.toString() + border + RESET);
		System.out.println(borderColor.toString() + "| " + RESET + textColor.toString() + text + RESET
				+ borderColor.toString() + " |" + RESET);
		System.out.println(borderColor.toString() + border + RESET);
	}

	// 애니메이션 텍스트 출력 (로딩 효과 등에 사용)
	public static void animateText(String text, ConsoleColor color, int delayMs) throws InterruptedException {
		for (char c : text.toCharArray()) {
			System.out.print(color.toString() + c + RESET);
			Thread.sleep(delayMs);
		}
		System.out.println();
	}
	
	/**
	 * 더 세밀한 그라데이션 효과를 텍스트에 적용하는 메서드 시작 색상에서 끝 색상으로 점진적으로 변화하는 효과를 생성합니다.
	 * 
	 * @param text       그라데이션을 적용할 텍스트
	 * @param startColor 시작 색상
	 * @param endColor   끝 색상
	 * @param steps      사용할 그라데이션 단계 수 (높을수록 부드러움)
	 * @return 그라데이션이 적용된 텍스트
	 */
	public static String detailedGradient(String text, ConsoleColor startColor, ConsoleColor endColor, int steps) {
		// 미리 정의된 색상 배열을 생성하여 단계별 색상을 활용
		ConsoleColor[] gradientColors = createGradientColorArray(startColor, endColor, steps);

		StringBuilder result = new StringBuilder();
		int charPerStep = Math.max(1, text.length() / steps);

		for (int i = 0; i < text.length(); i++) {
			int colorIndex = Math.min(i / charPerStep, gradientColors.length - 1);
			result.append(gradientColors[colorIndex].toString()).append(text.charAt(i));
		}

		result.append(RESET);
		return result.toString();
	}

	/**
	 * 두 색상 사이의 그라데이션에 사용할 색상 배열을 생성합니다. 미리 정의된 색상 중에서 적절한 색상을 선택합니다.
	 * 
	 * @param startColor 시작 색상
	 * @param endColor   끝 색상
	 * @param steps      단계 수
	 * @return 그라데이션에 사용할 색상 배열
	 */
	private static ConsoleColor[] createGradientColorArray(ConsoleColor startColor, ConsoleColor endColor, int steps) {
		// 이 구현은 간단한 버전으로, 실제로는 색상의 종류에 따라 더 복잡한 로직이 필요할 수 있습니다

		// 색상 그룹 정의 (일반, 밝은, 굵은, 밝은 굵은 색상)
		ConsoleColor[][] colorGroups = {
				// 일반 색상
				{ RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA, WHITE },
				// 밝은 색상
				{ RED_BRIGHT, YELLOW_BRIGHT, GREEN_BRIGHT, CYAN_BRIGHT, BLUE_BRIGHT, MAGENTA_BRIGHT, WHITE_BRIGHT },
				// 굵은 색상
				{ RED_BOLD, YELLOW_BOLD, GREEN_BOLD, CYAN_BOLD, BLUE_BOLD, MAGENTA_BOLD, WHITE_BOLD },
				// 밝은 굵은 색상
				{ RED_BOLD_BRIGHT, YELLOW_BOLD_BRIGHT, GREEN_BOLD_BRIGHT, CYAN_BOLD_BRIGHT, BLUE_BOLD_BRIGHT,
						MAGENTA_BOLD_BRIGHT, WHITE_BOLD_BRIGHT } };

		// 시작 색상과 끝 색상의 그룹 및 인덱스 찾기
		int startGroupIdx = -1, startColorIdx = -1;
		int endGroupIdx = -1, endColorIdx = -1;

		for (int g = 0; g < colorGroups.length; g++) {
			for (int c = 0; c < colorGroups[g].length; c++) {
				if (colorGroups[g][c] == startColor) {
					startGroupIdx = g;
					startColorIdx = c;
				}
				if (colorGroups[g][c] == endColor) {
					endGroupIdx = g;
					endColorIdx = c;
				}
			}
		}

		// 색상을 찾지 못한 경우 기본 색상 사용
		if (startGroupIdx == -1 || endGroupIdx == -1) {
			ConsoleColor[] defaultColors = { RED, RED_BRIGHT, YELLOW, YELLOW_BRIGHT, GREEN, GREEN_BRIGHT, CYAN,
					CYAN_BRIGHT, BLUE, BLUE_BRIGHT, MAGENTA, MAGENTA_BRIGHT };
			return interpolateColors(defaultColors, steps);
		}

		// 같은 그룹 내에서의 그라데이션
		if (startGroupIdx == endGroupIdx) {
			return interpolateColorsInGroup(colorGroups[startGroupIdx], startColorIdx, endColorIdx, steps);
		}

		// 다른 그룹 간의 그라데이션 (간단한 구현)
		ConsoleColor[] result = new ConsoleColor[steps];
		for (int i = 0; i < steps; i++) {
			if (i < steps / 2) {
				// 첫 번째 절반은 시작 색상 그룹에서 선택
				int idx = startColorIdx + (i * (colorGroups[startGroupIdx].length - startColorIdx)) / (steps / 2);
				idx = Math.min(idx, colorGroups[startGroupIdx].length - 1);
				result[i] = colorGroups[startGroupIdx][idx];
			} else {
				// 두 번째 절반은 끝 색상 그룹에서 선택
				int idx = (i - steps / 2) * endColorIdx / (steps - steps / 2);
				idx = Math.min(idx, endColorIdx);
				result[i] = colorGroups[endGroupIdx][idx];
			}
		}

		return result;
	}

	/**
	 * 같은 그룹 내에서 색상을 보간하여 그라데이션 색상 배열을 생성합니다.
	 */
	private static ConsoleColor[] interpolateColorsInGroup(ConsoleColor[] colors, int startIdx, int endIdx, int steps) {
		ConsoleColor[] result = new ConsoleColor[steps];

		// 시작 인덱스가 끝 인덱스보다 크면 순서를 바꿈
		if (startIdx > endIdx) {
			int temp = startIdx;
			startIdx = endIdx;
			endIdx = temp;
		}

		// 색상 사이의 거리
		int distance = endIdx - startIdx + 1;

		for (int i = 0; i < steps; i++) {
			double ratio = (double) i / (steps - 1);
			int colorIdx = startIdx + (int) (ratio * (distance - 1));
			colorIdx = Math.min(Math.max(colorIdx, startIdx), endIdx);
			result[i] = colors[colorIdx];
		}

		return result;
	}

	/**
	 * 주어진 색상 배열을 steps 수에 맞게 보간합니다.
	 */
	private static ConsoleColor[] interpolateColors(ConsoleColor[] colors, int steps) {
		ConsoleColor[] result = new ConsoleColor[steps];

		for (int i = 0; i < steps; i++) {
			double ratio = (double) i / (steps - 1);
			int colorIdx = (int) (ratio * (colors.length - 1));
			result[i] = colors[colorIdx];
		}

		return result;
	}
}
