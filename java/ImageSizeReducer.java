import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

//import com.test.common.AppException;
//import com.test.common.AppLogger;
public class ImageSizeReducer{

//	private static final AppLogger _logger = new AppLogger(ImageSizeReducer.class);
	
	public File excute(String path, int maxLength, int minLenth, String processDir) {

		File inputFile = new File(path);

		try {

			int length = (int) inputFile.length() / 1024;
//			_logger.debug("input size = " + String.valueOf(length));

			String extension = "";
			int i = path.lastIndexOf(".");
			if (i > 0) {
				extension = path.substring(i + 1);
			}
			if (!(extension.equalsIgnoreCase("GIF") || extension.equalsIgnoreCase("PNG")
					|| extension.equalsIgnoreCase("DIB") || extension.equalsIgnoreCase("BMP")
					|| extension.equalsIgnoreCase("JPG") || extension.equalsIgnoreCase("JPEG")
					|| extension.equalsIgnoreCase("JPE") || extension.equalsIgnoreCase("JFIF"))) {
//				AppException ae = new AppException("");
//				_logger.log(ae);
//				throw ae;
				throw new RuntimeException("File type invalid!");
			}

			if (length > maxLength) {
//				AppException ae = new AppException("");
//				_logger.log(ae);
//				throw ae;
				throw new RuntimeException("File too large!");
			}

			if(length <= minLenth) {
				return inputFile;
			}

			File dir = new File(processDir);
			File tempFile = File.createTempFile("tempimage", ".JPG", dir);
//			_logger.debug("temp file name = " + tempFile.getName());

			if (!(extension.equalsIgnoreCase("JPG") || extension.equalsIgnoreCase("JPEG")
					|| extension.equalsIgnoreCase("JPE") || extension.equalsIgnoreCase("JFIF"))) {
				File file = convertToJPG(inputFile, extension, tempFile);
				BufferedImage imageJPG = ImageIO.read(file);
				int jpgLength = (int) file.length() / 1024;
//				_logger.debug("converted jpeg size = " + String.valueOf(jpgLength));

				if (jpgLength > minLenth) {
					float imageQuality = (float) minLenth / jpgLength * 0.66f;
					int compressCount = 0;
					while (true) {
						compressCount++;
						file = compressImage(imageJPG, "JPG", imageQuality, tempFile);
						int fileLength = (int) file.length() / 1024;
//						_logger.debug("output size = " + String.valueOf(fileLength));

						if (fileLength > minLenth) {
							imageQuality = imageQuality * (minLenth * 1f / fileLength) * 0.75f;
						} else if (fileLength < (minLenth * 0.8f)) {
							//imageQuality = imageQuality * (minLenth * 1f / fileLength);
							imageQuality += 0.3 *(1f - imageQuality);
						} else {
							inputFile.delete();
							return file;
						}
						if (imageQuality < 0.0001 || imageQuality > 0.9999 || compressCount > 10) {
							inputFile.delete();
							return file;
						}
					}
				} else {
					inputFile.delete();
					return file;
				}
			} else {
				BufferedImage imageJPG = ImageIO.read(inputFile);
				float imageQuality = (float) minLenth / length * 0.66f;
				int compressCount = 0;
				while (true) {
					compressCount++;
					File file = compressImage(imageJPG, "JPG", imageQuality, tempFile);
					int fileLength = (int) file.length() / 1024;
//					_logger.debug("output size = " + String.valueOf(fileLength));

					if (fileLength > minLenth) {
						imageQuality = imageQuality * (minLenth * 1f / fileLength) * 0.75f;
					} else if (fileLength < (minLenth * 0.8f)) {
						//imageQuality = imageQuality * (minLenth * 0.8f / fileLength) * 1.25f;
						imageQuality += 0.3 *(1f - imageQuality);
					} else {
						inputFile.delete();
						return file;
					}
					if (imageQuality < 0.0001 || imageQuality > 0.9999 || compressCount > 10) {
						inputFile.delete();
						return file;
					}
				}
			}

		}
//		catch (AppException ae) {
//            _logger.log(ae);
//		}
		catch (Exception e) {
//            _logger.log(e);
			e.printStackTrace();
			return null;
		}
	}

	private File convertToJPG(File inputFile, String ext, File tempFile) {
		//File file = new File(tempFileNameJpg);
		FileOutputStream fos = null;
		try {
			File file = tempFile;
			fos = new FileOutputStream(file);
			if (ext.equalsIgnoreCase("PNG")) {
				BufferedImage inputImg = ImageIO.read(inputFile);
				BufferedImage imageJpg = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				imageJpg.createGraphics().drawImage(inputImg, 0, 0, Color.WHITE, null);
				ImageIO.write(imageJpg, "JPG", fos);
			} else if (ext.equalsIgnoreCase("GIF")) {
				BufferedImage inputImg = ImageIO.read(inputFile);
				BufferedImage imageJpg = new BufferedImage(inputImg.getWidth(null), inputImg.getHeight(null),
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = imageJpg.createGraphics();
				// Color.WHITE estes the background to white. You can use any other color
				g.drawImage(inputImg, 0, 0, imageJpg.getWidth(), imageJpg.getHeight(), Color.WHITE, null);
				ImageIO.write(imageJpg, "JPG", fos);
			} else {
				BufferedImage inputImg = ImageIO.read(inputFile);
				ImageIO.write(inputImg, "JPG", fos);
			}
			fos.close();
			return file;
		}
		catch (Exception e) {
			if(fos != null) {
				try {fos.close();} catch(Exception ex) {}
			}
//	        _logger.log(e);
			e.printStackTrace();
			return null;
		}
	}

	private File compressImage(BufferedImage image, String ext, float imageQuality, File tempFile) {

		OutputStream fos = null;

		try {
			//File file = new File(processDir + "/image/temp/converted." + ext);
			fos = new FileOutputStream(tempFile);

			Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(ext);

			if (!imageWriters.hasNext());

			ImageWriter imageWriter = (ImageWriter) imageWriters.next();
			ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fos);
			imageWriter.setOutput(imageOutputStream);

			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality(imageQuality);

			imageWriter.write(null, new IIOImage(image, null, null), imageWriteParam);

			fos.close();
			imageOutputStream.close();
			imageWriter.dispose();
			return tempFile;
		}
		catch (Exception e) {
			if(fos != null) {
				try {fos.close();} catch(Exception ex) {}
			}
//	        _logger.log(e);
			
			e.printStackTrace();
			return null;
		}
	}
}
