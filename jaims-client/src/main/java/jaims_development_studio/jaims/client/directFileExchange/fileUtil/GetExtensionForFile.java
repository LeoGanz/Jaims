package jaims_development_studio.jaims.client.directFileExchange.fileUtil;

import java.io.File;

public class GetExtensionForFile {

	public static EFileType getExtensionForFile(File f) {

		String[] split = f.getName().split("\\.");
		String extension = split[split.length - 1];

		if (isAudioFile(extension))
			return EFileType.FILETYPE_AUDIO;
		else if (isExcelFile(extension))
			return EFileType.FILETYPE_EXCEL;
		else if (isPDFFile(extension))
			return EFileType.FILETYPE_PDF;
		else if (isVideoFile(extension))
			return EFileType.FILETYPE_VIDEO;
		else if (isTextFile(extension))
			return EFileType.FILETYPE_TEXT;
		else if (isWordFile(extension))
			return EFileType.FILETYPE_WORD;
		else if (isODTFile(extension))
			return EFileType.FILETYPE_ODT;
		else if (isZIPFile(extension))
			return EFileType.FILETYPE_ZIP;
		else if (isPPTFile(extension))
			return EFileType.FILETYPE_PPT;
		else if (isImageFile(extension))
			return EFileType.FILETYPE_IMAGE;
		else if (isJavaFile(extension))
			return EFileType.FILETYPE_JAVA;
		else
			return EFileType.FILETYPE_OTHER;
	}

	private static boolean isAudioFile(String extension) {

		if (extension.equals("aac") || extension.equals("aiff") || extension.equals("mid") || extension.equals("mp3")
				|| extension.equals("wav"))
			return true;
		else
			return false;
	}

	private static boolean isExcelFile(String extension) {

		if (extension.equals("xls") || extension.equals("xlsx"))
			return true;
		else
			return false;
	}

	private static boolean isPDFFile(String extension) {

		if (extension.equals("pdf"))
			return true;
		else
			return false;
	}

	private static boolean isVideoFile(String extension) {

		if (extension.equals("avi") || extension.equals("mp4") || extension.equals("mpg"))
			return true;
		else
			return false;
	}

	private static boolean isTextFile(String extension) {

		if (extension.equals("txt"))
			return true;
		else
			return false;
	}

	private static boolean isWordFile(String extension) {

		if (extension.equals("doc") || extension.equals("docx"))
			return true;
		else
			return false;
	}

	private static boolean isODTFile(String extension) {

		if (extension.equals("odt"))
			return true;
		else
			return false;
	}

	private static boolean isZIPFile(String extension) {

		if (extension.equals("zip") || extension.equals("rar"))
			return true;
		else
			return false;
	}

	private static boolean isPPTFile(String extension) {

		if (extension.equals("ppt") || extension.equals("pptx"))
			return true;
		else
			return false;
	}

	private static boolean isImageFile(String extension) {

		if (extension.equals("gif") || extension.equals("jpg") || extension.equals("png"))
			return true;
		else
			return false;
	}

	private static boolean isJavaFile(String extension) {

		if (extension.equals("java") || extension.equals("jar"))
			return true;
		else
			return false;
	}

}
