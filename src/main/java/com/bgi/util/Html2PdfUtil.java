package com.bgi.util;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.codec.Base64;
import io.netty.util.internal.logging.InternalLogger;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

//https://my.oschina.net/960823/blog/1588166
public class Html2PdfUtil {

    private static class Base64ImgReplacedElementFactory implements ReplacedElementFactory {

        /**
         * 实现createReplacedElement 替换html中的Img标签
         *
         * @param c 上下文
         * @param box 盒子
         * @param uac 回调
         * @param cssWidth css宽
         * @param cssHeight css高
         * @return ReplacedElement
         */
        public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac,
                                                     int cssWidth, int cssHeight) {
            Element e = box.getElement();
            if (e == null) {
                return null;
            }
            String nodeName = e.getNodeName();
            // 找到img标签
            if (nodeName.equals("img")) {
                String attribute = e.getAttribute("src");
                FSImage fsImage;
                try {
                    // 生成itext图像
                    fsImage = buildImage(attribute, uac);
                } catch (BadElementException e1) {
                    fsImage = null;
                } catch (IOException e1) {
                    fsImage = null;
                } catch (com.itextpdf.text.BadElementException e1) {
                    fsImage = null;
                    e1.printStackTrace();
                }
                if (fsImage != null) {
                    // 对图像进行缩放
                    if (cssWidth != -1 || cssHeight != -1) {
                        fsImage.scale(cssWidth, cssHeight);
                    }
                    return new ITextImageElement(fsImage);
                }
            }


            return null;
        }


        /**
         * 编解码base64并生成itext图像
         */
        protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException,
                BadElementException, com.itextpdf.text.BadElementException {
            FSImage fiImg=null;
            if (srcAttr.toLowerCase().startsWith("data:image/")) {
                String base64Code= srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(),
                        srcAttr.length());
                // 解码
                byte[] decodedBytes = Base64.decode(base64Code);


                fiImg= new ITextFSImage(Image.getInstance(decodedBytes));
            } else {
                fiImg= uac.getImageResource(srcAttr).getImage();
            }
            return fiImg;
        }


        public void reset() {}

        @Override
        public void remove(Element arg0) {}

        @Override
        public void setFormSubmissionListener(FormSubmissionListener arg0) {}

    }

    public static String html2Pdf(String html, String fontPath, InternalLogger logger)  {

        if (null == html || "".equals(html.trim())) {
            return null;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
        iTextRenderer.getSharedContext().getTextRenderer().setSmoothingThreshold(0);
        //iTextRenderer.setDocument(html);
        html = wrapHtml(html);
        iTextRenderer.setDocumentFromString(html);

        try {
            //解决中文编码
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            if ("linux".equals(getCurrentOperationSystem())) {
                //"/usr/share/fonts/chiness/simsun.ttc,1"
                fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } else {
                //"C:/Users/Liyingqiao/Desktop/simsun.ttf"
                fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }

            iTextRenderer.layout();
            OutputStream os = java.util.Base64.getEncoder().wrap(bos);
            iTextRenderer.createPDF(os);
            os.flush();
            os.close();

            return bos.toString();
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    private static String getCurrentOperationSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os;
    }

    private static String wrapHtml(String html) {
        html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body style=\"font-family:SimSun;\">" +

                html +

                "</body>\n" +
                "</html>";
        return html;
    }

    /*public static void main(String[] args){
        //File file = new File("C:/Users/Liyingqiao/Desktop/notice.html");

        String pdfPath = "C:/Users/Liyingqiao/Desktop/notice.pdf";
        //Object o = System.getProperties();
        //System.out.println(o);//

        try{
            String pdf = htmlToPdf(html, "C:/Users/Liyingqiao/Desktop/simsun.ttf");
            byte[] bytes = java.util.Base64.getDecoder().decode(pdf);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            baos.write(bytes);
            FileOutputStream fileOutputStream = new FileOutputStream(pdfPath);
            baos.writeTo(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

}
