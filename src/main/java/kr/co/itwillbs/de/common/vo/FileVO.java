package kr.co.itwillbs.de.common.vo;

import java.time.LocalDateTime;

import kr.co.itwillbs.de.common.aop.annotation.RequiredSessionIds;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RequiredSessionIds(fields = {"regId"})
public class FileVO {

	private String idx;
	private String type;
	private String majorIdx;
	private String filePath;
	private String fileName;
	private String fileSize;
	private String fileOriginalName;
	private String isDeleted;
	private String rankNumber;
	private String regId;
	private LocalDateTime regDate;
	
	/**
	 * FileUtil.setFile return 필수 값 셋
	 * @param filePath 업로드된/다운로드될 파일경로
	 * @param fileName 업로드된/다운로드될 파일명
	 * @param fileOriginalName 업로드된/다운로드될 파일명
	 * @param fileSize 업로드된/다운로드될 파일사이즈
	 */
	public FileVO(String filePath, String fileName, String fileOriginalName, String fileSize) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileOriginalName = fileOriginalName;
		this.fileSize = fileSize;
	}
}
