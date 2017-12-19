import java.util.Hashtable;

public class QRCodeEvents {
	  public static void main(String []args)throws Exception{   
	        String text = "ÄãºÃ";   
	        int width = 100;   
	        int height = 100;   
	        String format = "png";   
	        Hashtable hints= new Hashtable();   
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   
	         BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);   
	         File outputFile = new File("new.png");   
	         MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);   
	            
	    }   
}
