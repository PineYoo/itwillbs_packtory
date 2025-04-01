package kr.co.itwillbs.de.common.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileVO {

	private String type;
	private String majorIdx;
	private String filePath;
	private String fileName;
	private String fileSize;
	private String isDeleted;
	private String rankNumber;
	private String regId;
	private LocalDateTime regDate;
	
	/**
	 * FileUtil.setFile return 필수 값 셋
	 * @param fileName 업로드된/다운로드될 파일명
	 * @param filePath 업로드된/다운로드될 파일경로
	 * @param fileSize 업로드된/다운로드될 파일사이즈
	 */
	public FileVO(String fileName, String filePath, String fileSize) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
	}
}
