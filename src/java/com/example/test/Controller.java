package com.example.test;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
/**
 *
 * @author cheryl
 */
public class Controller extends ActionSupport implements ServletRequestAware {
    private static String FONT_FILE_PATH = "\\Fonts\\BELLI.TTF";
    private static float SIZE = 40.0f;
    //----------------------- set color.
    private static Color BACKGROUND_COLOR = Color.black;
    private static Color FONT_COLOR = Color.white;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public File getFile2upld() {
        return file2upld;
    }

    public void setFile2upld(File file2upld) {
        this.file2upld = file2upld;
    }

    public String getFile2upldContentType() {
        return file2upldContentType;
    }

    public void setFile2upldContentType(String file2upldContentType) {
        this.file2upldContentType = file2upldContentType;
    }

    public String getFile2upldFileName() {
        return file2upldFileName;
    }

    public void setFile2upldFileName(String file2upldFileName) {
        this.file2upldFileName = file2upldFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDwnldFn() {
        return dwnldFn;
    }

    public void setDwnldFn(String dwnldFn) {
        this.dwnldFn = dwnldFn;
    }

    private InputStream fileInputStream;
    private File file2upld;
    private String file2upldContentType;
    private String file2upldFileName;
    private String title;
    private String type;
    private String dwnldFn;
    private HttpServletRequest request;
    private String realPath;
    private Map<String, Object> session;
    private byte[] imageInByte = null;
    private String newfile;
    private String colors;

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getNewfile() {
        return newfile;
    }

    public void setNewfile(String newfile) {
        this.newfile = newfile;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    @Override
    public String execute() {
        newfile = "newFile.jpg";
        System.out.println("Inside controller");
        type = "image/jpg";
        System.out.println("colors: " + colors);
//        dwnldFn = file2upldFileName + "_new";
        realPath = request.getServletContext().getRealPath("tmp");
        File dDir = new File(realPath);
        stageFile(realPath, file2upld, file2upldFileName);
        File fileObj = new File(dDir + System.getProperty("file.separator") + file2upldFileName);
        File fileNew = new File(dDir + System.getProperty("file.separator") + newfile);
        try {
            BufferedImage img1 = ImageIO.read(fileObj);
            BufferedImage titleImg = addTitle(img1, title);
            boolean success = ImageIO.write(titleImg, "jpg", fileNew);
            System.out.println("saved success? " + success);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            BufferedImage img1 = ImageIO.read(fileObj);
//            BufferedImage img2 = ImageIO.read(fileObj);
//            BufferedImage joinedImg = joinBufferedImage(img1, img2);
//            boolean success = ImageIO.write(joinedImg, "jpg", fileNew);
//            System.out.println("saved success? " + success);
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        try {
            fileInputStream = new FileInputStream(fileNew);
            System.out.println(fileInputStream);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    private void stageFile(String path, File srcFile, String filename) {
        File fileToCreate = new File(path, filename);
        try {
            FileUtils.copyFile(srcFile, fileToCreate);
        }
        catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * join two BufferedImage you can add a orientation parameter to control
     * direction you can use a array to join more BufferedImage
     *
     * @param img1
     * @param img2
     * @return
     */
    private BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
        //do some calculate first
        int offset = 5;
        int wid = img1.getWidth() + img2.getWidth() + offset;
        int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
        System.out.println("wid = " + wid);
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth() + offset, 0);
        g2.dispose();
        return newImage;
    }

    private BufferedImage addTitle(BufferedImage img1, String text) {
        int offset = 5;
        int width = 0;
        int height = 0;
        Rectangle2D rectangle2D = null;
        Font font = null;
        FontRenderContext fontRenderContext = null;
        BufferedImage bufferedImage = null;
        Graphics2D graphics2D = null;
        font = new Font("Serif", Font.PLAIN, 25);
        //----------------------- logic to estimaated size of text start.
        //----------------------- craete buffer image.
        bufferedImage
        = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        //----------------------- set graphics.
        graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        fontRenderContext = graphics2D.getFontRenderContext();
        rectangle2D = font.getStringBounds(text, fontRenderContext);
        //----------------------- calculate the size of the text.
        width = (int) rectangle2D.getWidth();
        height = (int) rectangle2D.getHeight() + img1.getHeight();
        //----------------------- logic to estimaated size of text end.
        //----------------------- create image of calculated text size.
        bufferedImage = new BufferedImage(width, height,
                                          BufferedImage.TYPE_INT_RGB);
        //----------------------- set graphics.
        graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setFont(font);
        //----------------------- set graphics.
        graphics2D.setColor(BACKGROUND_COLOR);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(FONT_COLOR);
        graphics2D.drawImage(img1, null, 0, 0);
//        graphics2D.drawString(text, 0, (int) -rectangle2D.getY());
        graphics2D.drawString(text, 0, img1.getHeight() + ((int) -rectangle2D.getY()));
        //----------------------- returned buffered image.
        return bufferedImage;
    }

}
