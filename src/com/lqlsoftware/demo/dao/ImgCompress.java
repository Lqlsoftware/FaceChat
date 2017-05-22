package com.lqlsoftware.demo.dao;

import java.io.*;  
import java.awt.*;  
import java.awt.image.*;  

import javax.imageio.ImageIO;  
/** 
 * ͼƬѹ������ 
 * @author ����ǿ 
 */  
public class ImgCompress {  
	
    private Image img = null;

    private String type;
    
    private String newFilePath;
    
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Image getImg() {
		return img;
	}
	public void setImg(Image img) {
		this.img = img;
	}
	public String getNewFilePath() {
		return newFilePath;
	}
	public void setNewFilePath(String newFilePath) {
		this.newFilePath = newFilePath;
	}
    /** 
     * ���տ�Ȼ��Ǹ߶Ƚ���ѹ�� 
     * @param w int ����� 
     * @param h int ���߶� 
     */  
    public void resizeFix(int w, int h) throws IOException {  
        if (img.getWidth(null) / img.getHeight(null) > w / h) {  
            resizeByWidth(w);  
        } else {  
            resizeByHeight(h);  
        }  
    }  
    /** 
     * �Կ��Ϊ��׼���ȱ�������ͼƬ 
     * @param w int �¿�� 
     */  
    public void resizeByWidth(int w) throws IOException {  
        int h = (int) (img.getHeight(null) * w / img.getWidth(null));  
        resize(w, h);  
    }  
    /** 
     * �Ը߶�Ϊ��׼���ȱ�������ͼƬ 
     * @param h int �¸߶� 
     */  
    public void resizeByHeight(int h) throws IOException {  
        int w = (int) (img.getWidth(null) * h / img.getHeight(null));  
        resize(w, h);  
    }  
    /** 
     * ǿ��ѹ��/�Ŵ�ͼƬ���̶��Ĵ�С 
     * @param w int �¿�� 
     * @param h int �¸߶� 
     */  
    public void resize(int w, int h) throws IOException {  
    	if (img.equals(null)) {
    		return;
    	}
        // SCALE_SMOOTH �������㷨 ��������ͼƬ��ƽ���ȵ� ���ȼ����ٶȸ� ���ɵ�ͼƬ�����ȽϺ� ���ٶ���  
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );   
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // ������С���ͼ  
        ImageIO.write(image, type, new File(newFilePath));  
    }  
}