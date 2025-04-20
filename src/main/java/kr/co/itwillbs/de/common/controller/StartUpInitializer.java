package kr.co.itwillbs.de.common.controller;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import kr.co.itwillbs.de.common.constant.ConsoleColor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartUpInitializer {
	/**
	 * 컴포넌트 인데 컨트롤러클래스인 척 넣어 두자
	 */

	/**
	 * <pre>
	 * 스프링 부트가 구동 되고 나서 필요한 작업들을 할 수 있다.
	 * 1. 캐시를 사용 하는 경우 여기서 미리 불러올 작업을 할 수 있다.
	 * 2. 스케쥴링 작업
	 * 3. 로그 테이블에 App 시작 로그 남기기
	 * </pre>
	 */
	@Order(2) // ApplicationReadyEvent.1st 순서를 제어하고 싶다면 Order 어노테이션으로 제어 가능
	@EventListener(ApplicationReadyEvent.class) //@EventListener(ApplicationReadyEvent.class) 이벤트 리스너들의 순서는 보장되지 않는다고 함 
	public void printWelcomeMessage() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 아재 감성 그득 넣기 ASCII Art
		// HREF: https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=Packtory
		// HREF2: https://fsymbols.com/generators/tarty/

//		log.info("██████╗  █████╗  ██████╗██╗  ██╗████████╗ ██████╗ ██████╗ ██╗   ██╗");
//		log.info("██╔══██╗██╔══██╗██╔════╝██║ ██╔╝╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝");
//		log.info("██████╔╝███████║██║     █████╔╝    ██║   ██║   ██║██████╔╝ ╚████╔╝ ");
//		log.info("██╔═══╝ ██╔══██║██║     ██╔═██╗    ██║   ██║   ██║██╔══██╗  ╚██╔╝  ");
//		log.info("██║     ██║  ██║╚██████╗██║  ██╗   ██║   ╚██████╔╝██║  ██║   ██║   ");
//		log.info("╚═╝     ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ");

		// log는 감성이 부족하다. 이건 무조건 sysout 이다. """ 끝내준다. 이걸 위해 만들었나?
		String welcomeMessage = """
				██████╗  █████╗  ██████╗██╗  ██╗████████╗ ██████╗ ██████╗ ██╗   ██╗
				██╔══██╗██╔══██╗██╔════╝██║ ██╔╝╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝
				██████╔╝███████║██║     █████╔╝    ██║   ██║   ██║██████╔╝ ╚████╔╝
				██╔═══╝ ██╔══██║██║     ██╔═██╗    ██║   ██║   ██║██╔══██╗  ╚██╔╝
				██║     ██║  ██║╚██████╗██║  ██╗   ██║   ╚██████╔╝██║  ██║   ██║
				╚═╝     ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝""";
		String welcomeText = " > Packtory version.250418";
		ConsoleColor.GREEN_BRIGHT.println(welcomeMessage);
		ConsoleColor.GREEN.println(welcomeText);
	}

	// test
	public static void main(String... strings) {
		String welcomeMessage = """
				██████╗  █████╗  ██████╗██╗  ██╗████████╗ ██████╗ ██████╗ ██╗   ██╗
				██╔══██╗██╔══██╗██╔════╝██║ ██╔╝╚══██╔══╝██╔═══██╗██╔══██╗╚██╗ ██╔╝
				██████╔╝███████║██║     █████╔╝    ██║   ██║   ██║██████╔╝ ╚████╔╝
				██╔═══╝ ██╔══██║██║     ██╔═██╗    ██║   ██║   ██║██╔══██╗  ╚██╔╝
				██║     ██║  ██║╚██████╗██║  ██╗   ██║   ╚██████╔╝██║  ██║   ██║
				╚═╝     ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝   ╚═╝""";
		String welcomeMessage2 = """
			                                                                                                      
			        ***** **                            *                                                         
			     ******  ****                         **             *                                            
			    **   *  *  ***                        **            **                                            
			   *    *  *    ***                       **            **                                            
			       *  *      **                       **          ********    ****    ***  ****    **   ****      
			      ** **      **    ****       ****    **  ***    ********    * ***  *  **** **** *  **    ***  *  
			      ** **      **   * ***  *   * ***  * ** * ***      **      *   ****    **   ****   **     ****   
			    **** **      *   *   ****   *   ****  ***   *       **     **    **     **          **      **    
			   * *** **     *   **    **   **         **   *        **     **    **     **          **      **    
			      ** *******    **    **   **         **  *         **     **    **     **          **      **    
			      ** ******     **    **   **         ** **         **     **    **     **          **      **    
			      ** **         **    **   **         ******        **     **    **     **          **      **    
			      ** **         **    **   ***     *  **  ***       **      ******      ***          *********    
			      ** **          ***** **   *******   **   *** *     **      ****        ***           **** ***   
			 **   ** **           ***   **   *****     **   ***                                              ***  
			***   *  *                                                                                *****   *** 
			 ***    *                                                                               ********  **  
			  ******                                                                               *      ****    
			    ***          """;
		String welcomeMessage3 = """
			8 888888888o      .8.           ,o888888o.    8 8888     ,88' 8888888 8888888888 ,o888888o.     8 888888888o. `8.`8888.      ,8' 
			8 8888    `88.   .888.         8888     `88.  8 8888    ,88'        8 8888    . 8888     `88.   8 8888    `88. `8.`8888.    ,8'  
			8 8888     `88  :88888.     ,8 8888       `8. 8 8888   ,88'         8 8888   ,8 8888       `8b  8 8888     `88  `8.`8888.  ,8'   
			8 8888     ,88 . `88888.    88 8888           8 8888  ,88'          8 8888   88 8888        `8b 8 8888     ,88   `8.`8888.,8'    
			8 8888.   ,88'.8. `88888.   88 8888           8 8888 ,88'           8 8888   88 8888         88 8 8888.   ,88'    `8.`88888'     
			8 888888888P'.8`8. `88888.  88 8888           8 8888 88'            8 8888   88 8888         88 8 888888888P'      `8. 8888      
			8 8888      .8' `8. `88888. 88 8888           8 888888<             8 8888   88 8888        ,8P 8 8888`8b           `8 8888      
			8 8888     .8'   `8. `88888.`8 8888       .8' 8 8888 `Y8.           8 8888   `8 8888       ,8P  8 8888 `8b.          8 8888      
			8 8888    .888888888. `88888.  8888     ,88'  8 8888   `Y8.         8 8888    ` 8888     ,88'   8 8888   `8b.        8 8888      
			8 8888   .8'       `8. `88888.  `8888888P'    8 8888     `Y8.       8 8888       `8888888P'     8 8888     `88.      8 8888   """;
		String welcomeText = " > Packtory version.250418";
		ConsoleColor.GREEN_BRIGHT.println(welcomeMessage);
		ConsoleColor.GREEN.println(welcomeText);
		ConsoleColor.GREEN_BRIGHT.println(welcomeMessage2);
		ConsoleColor.BLUE_BRIGHT.println(welcomeText);
		ConsoleColor.GREEN_BRIGHT.println(welcomeMessage3);
		ConsoleColor.BLUE_BRIGHT.println(welcomeText);
//		String text = "hello world!";
//		ConsoleColor.GREEN.println(welcomeMessage);
//		ConsoleColor.MAGENTA.println(welcomeMessage);
//		ConsoleColor.CYAN.println(welcomeMessage);
//
//		System.out.println(ConsoleColor.rainbow(welcomeMessage));
//		System.out.println(ConsoleColor.gradient(welcomeMessage, ConsoleColor.RED, ConsoleColor.GREEN));
//
//		ConsoleColor.GREEN.println(text);
//		ConsoleColor.MAGENTA.println(text);
//		ConsoleColor.CYAN.println(text);
//		ConsoleColor.printBox(text, ConsoleColor.RED, ConsoleColor.YELLOW);
//		try {
//			ConsoleColor.animateText(text, ConsoleColor.CYAN, 10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		// 기본 그라데이션 (빨간색에서 파란색으로)
//		String gradientText = ConsoleColor.detailedGradient("그라데이션 텍스트", ConsoleColor.RED, ConsoleColor.BLUE, 10);
//		System.out.println(gradientText);
//		
//		String gradientText2 = ConsoleColor.detailedGradient(welcomeMessage, ConsoleColor.GREEN, ConsoleColor.BLUE, 10);
//		System.out.println(gradientText2);
//
//		// 밝은 색상 그라데이션
//		String brightGradient = ConsoleColor.detailedGradient("밝은 그라데이션", ConsoleColor.RED_BRIGHT, ConsoleColor.BLUE_BRIGHT, 8);
//		System.out.println(brightGradient);
//		
//		// 밝은 색상 그라데이션
//		String brightGradient2 = ConsoleColor.detailedGradient(welcomeMessage, ConsoleColor.GREEN_BRIGHT, ConsoleColor.BLUE_BRIGHT, 8);
//		System.out.println(brightGradient2);
		
	}
}