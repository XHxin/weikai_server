package cc.modules.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author tanxinye ��ά�빤����
 * 
 */
public class QRCodeHelper {

	/**
	 * ��ɶ�ά��
	 * 
	 * @param content
	 * @return
	 */
	public static String generateQRCode(String content, String outdir) {
		int width = 190;
		int height = 190;

		HashMap hints = new HashMap();
		hints.put(EncodeHintType.MARGIN, Integer.valueOf(0));
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, width, height, hints);
			File file = new File(outdir + "qrcode_" + formatDate() + ".png");
			MatrixToImageWriter.writeToFile(bitMatrix, "png", file);
			return file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param bgdir
	 *            ����ͼƬ·��
	 * @param fgdir
	 * @return
	 */
	private static String composePictureToRightBottom(String bgdir,
			String fgdir, String target) {
		if ("".equals(bgdir) || "".equals(fgdir) || "".equals(target)) {
			return "";
		}
		InputStream imagein = null;
		InputStream imagein2 = null;
		OutputStream outImage = null;
		String pString = "img_" + formatDate() + ".png";
		String t = target + pString;
		
		try {
			imagein = new FileInputStream(bgdir);
			imagein2 = new FileInputStream(fgdir);

			BufferedImage image = ImageIO.read(imagein);
			BufferedImage image2 = ImageIO.read(imagein2);
			Graphics g = image.getGraphics();
			
			g.drawImage(image2, image.getWidth() - image2.getWidth() - 63,
					image.getHeight() - image2.getHeight() -105,
					image2.getWidth() + 10, image2.getHeight() + 10, null);
			outImage = new FileOutputStream(t);
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);
			enc.encode(image);
			imagein.close();
			imagein2.close();
			outImage.close();
//			return t;
			return pString;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (imagein != null) {
				try {
					imagein.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imagein2 != null) {
				try {
					imagein2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outImage != null) {
				try {
					outImage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	private static String formatDate() {
		return new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date());
	}

	/**
	 * �ϳɶ�ά��
	 * 
	 * @param content
	 *            ����
	 * @param outdir
	 *            ��ά�����Ŀ¼
	 * @param bgdir
	 *            ����ͼ·��
	 * @param target
	 *            �ϳɺ����Ŀ¼
	 * @return
	 */
	public static String composeQRCode(String content, String outdir,
			String bgdir, String target) {
		return composePictureToRightBottom(bgdir,
				generateQRCode(content, outdir), target);
	}
}
