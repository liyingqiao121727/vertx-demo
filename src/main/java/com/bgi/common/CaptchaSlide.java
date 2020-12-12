package com.bgi.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CaptchaSlide {
    private int width = 160;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 20;
    // 验证码
    private String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;
    Random random = new Random();

    public CaptchaSlide() {
        creatImage();
    }

    public CaptchaSlide(int width, int height) {
        this.width = width;
        this.height = height;
        creatImage();
    }

    public CaptchaSlide(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        creatImage();
    }

    public CaptchaSlide(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        creatImage();
    }

    // 生成图片
    private void creatImage() {
        int fontWidth = width / codeCount;// 字体的宽度
        int fontHeight = height - 5;// 字体的高度
        int codeY = height - 8;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);



        // 设置字体
        // Font font= getFont(fontHeight);
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

// 添加噪点
        float yawpRate = 0.01f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            buffImg.setRGB(x, y, random.nextInt(255));
        }


        String str1 = randomStr(codeCount);// 得到随机字符
        this.code = str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处

            g.drawString(strRand, i*fontWidth+3, codeY);
        }


    }

    // 得到随机字符
    private String randomStr(int n) {
        String str1= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2= "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    // 得到随机颜色
    private Color getRandColor(int fc, int bc) {
        // 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 产生随机字体
     */
    private Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Wide Latin", Font.PLAIN, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
        return font[random.nextInt(5)];
    }

    // 扭曲方法
    private void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private void shearX(Graphics g, int w1, int h1, Color color) {

        int period = random.nextInt(2);

        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }

    }

    private void shearY(Graphics g, int w1, int h1, Color color) {

        int period = random.nextInt(40) + 10; // 50;

        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }



    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    private int targetLength = 30, targetWidth = 20;
    private double d1 = 30.0, d2 = 5.0, circleR = 5.0, r1 = 2.0;

    public int[][] getBlockData() {
        int[][] data = new int[targetLength][targetWidth];
        double x2 = targetLength-circleR-2;
        //随机生成圆的位置
        double h1 = circleR + Math.random() * (targetWidth-3*circleR-r1);
        double po = circleR*circleR;

        double xbegin = targetLength-circleR-r1;
        double ybegin = targetWidth-circleR-r1;

        for (int i = 0; i < targetLength; i++) {
            for (int j = 0; j < targetWidth; j++) {
                //右边○
                double d3 = Math.pow(i - x2,2) + Math.pow(j - h1,2);

                if (d1 <= po
                        || (j >= ybegin && d2 >= po)
                        || (i >= xbegin && d3 >= po)) {
                    data[i][j] = 0;

                }  else {
                    data[i][j] = 1;
                }
            }
        }
        return data;
    }

    public void cutByTemplate(BufferedImage oriImage,BufferedImage targetImage, int[][] templateImage, int x,
                               int y){
        for (int i = 0; i < targetLength; i++) {
            for (int j = 0; j < targetWidth; j++) {
                int rgb = templateImage[i][j];
                // 原图中对应位置变色处理

                int rgb_ori = oriImage.getRGB(x + i, y + j);

                if (rgb == 1) {
                    //抠图上复制对应颜色值
                    //int rgb1 = targetImage.getRGB(i, y + j);
                    targetImage.setRGB(i, y + j, rgb_ori);
                    //int r = (0xff & rgb_ori);
                    //int g = (0xff & (rgb_ori >> 8));
                    //int b = (0xff & (rgb_ori >> 16));
                    //rgb_ori = r + (g << 8) + (b << 16) + (200 << 24);
                    //原图对应位置颜色变化
                    //oriImage.setRGB(x + i, y + j, rgb_ori);
                    oriImage.setRGB(x + i, y + j, 0x7a7a7a);
                }
            }
        }
    }

    public static ConvolveOp getGaussianBlurFilter(int radius,
                                                   boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public static void simpleBlur(BufferedImage src,BufferedImage dest) {
        BufferedImageOp op = getGaussianBlurFilter(2,false);
        op.filter(src, dest);
    }

    /*public static byte[] fromBufferedImage2(BufferedImage img,String imagType) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.reset();
        // 得到指定Format图片的writer
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(imagType);
        ImageWriter writer = (ImageWriter) iter.next();

        // 得到指定writer的输出参数设置(ImageWriteParam )
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
        iwp.setCompressionQuality(1f); // 设置压缩质量参数

        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,
                colorModel.createCompatibleSampleModel(16, 16)));

        writer.setOutput(ImageIO
                .createImageOutputStream(bos));
        IIOImage iIamge = new IIOImage(img, null, null);
        writer.write(null, iIamge, iwp);

        byte[] d = bos.toByteArray();
        return d;
    }*/

    public BufferedImage getImg() {
        BufferedImage oriImage = this.buffImg;
        BufferedImage targetImage = new BufferedImage(oriImage.getWidth(), oriImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.cutByTemplate(oriImage, targetImage, this.getBlockData(), 20, 20);
        //oriImage = targetImage;
        BufferedImage targetImage1 = new BufferedImage(oriImage.getWidth(), oriImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        CaptchaSlide.simpleBlur(oriImage, targetImage1);
        return targetImage1;
        /*try {
            return fromBufferedImage2(oriImage, "png");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code.toLowerCase();
    }




    public static void main(String[] args) {
        //CaptchaSlide.getImg();
        System.out.println("finish");
    }
}
