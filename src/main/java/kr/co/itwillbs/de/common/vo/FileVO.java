package kr.co.itwillbs.de.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileVO {

	private String fileName;
	private String filePath;
	private String fileSize;
	
	public FileVO(String fileName, String filePath, String fileSize) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
	}
}
