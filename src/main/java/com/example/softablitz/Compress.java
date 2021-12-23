package com.example.softablitz;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public class Compress {
    public void compressImage(Image img, double compressingFactor) throws IOException {
        BufferedImage image = SwingFXUtils.fromFXImage(img, null);
        File output = new File("D:/compressedImage.png");
        OutputStream out = new FileOutputStream(output);
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality((float) compressingFactor);
        }
        writer. write(null, new IIOImage(image, null, null), param);
        out.close();
        ios.close();
        writer.dispose();
    }
}
