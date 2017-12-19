package com.yanxiu.util.core;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yueting
 * @version 创建时间:2016年3月30日
 * 
 */
public class FileTypeUtil {

	public static final String FILETYPE_WORD="word";
	public static final String FILETYPE_EXCEL="excel";
	public static final String FILETYPE_PPT="ppt";
	public static final String FILETYPE_PDF="pdf";
	public static final String FILETYPE_TEXT="text";
	public static final String FILETYPE_VIDEO="video";
	public static final String FILETYPE_AUDIO="audio";
	public static final String FILETYPE_IMAGE="image";
	public static final String FILETYPE_UNKNOWN="unknown";
	
	
	public static String getFileType(String extension){
		
		String fileType = "";
		extension = StringUtils.lowerCase(extension);
		if(StringUtils.isNotBlank(extension)){
			if (",doc,docx,".indexOf(extension) != -1) {
				fileType = FILETYPE_WORD;
			} else if (",xls,xlsx,".indexOf(extension) != -1) {
				fileType = FILETYPE_EXCEL;
			} else if (",ppt,pptx,".indexOf(extension) != -1) {
				fileType = FILETYPE_PPT;
			} else if (",pdf,".indexOf(extension) != -1) {
				fileType = FILETYPE_PDF;
			} else if (",txt,".indexOf(extension) != -1) {
				fileType = FILETYPE_TEXT;
			} else if (",flv,mpg,avi,mpeg,mp4,3gp,mov,wmv,".indexOf(extension) != -1) {
				fileType = FILETYPE_VIDEO;
			} else if (",mp3,wav,".indexOf(extension) != -1) {
				fileType = FILETYPE_AUDIO;
			} else if (",bmp,jpg,jpeg,png,gif,".indexOf(extension) != -1) {
				fileType = FILETYPE_IMAGE;		
			} else {
				fileType = FILETYPE_UNKNOWN;
			}
		}
		return fileType;
	}
}
