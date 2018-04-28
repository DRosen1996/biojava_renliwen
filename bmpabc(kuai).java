package bmpos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;

public class bmpabc extends JFrame
{
	//����ȫ�ֱ���
	int nWidth;
	int nHeight;
	int nBitCount;
	int nSizeImage;
	int[] nData;
	int[] nB;
	int[] nR;
	int[] nG;
	public Image image,img1;
	int[] GGrayData;
	
	public bmpabc()
	{
		// TODO Auto-generated constructor stub
		super("PCR��Ӿͼ�������ʵ��");
		this.setSize(1000,700);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		try
		{
			this.getBMPImage("E:\\06460211\\splash.bmp");
			this.GGrayData=this.graytongjiData();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.image = this.getmyimage(this.nData);
		//this.img1 = this.getmyimage(this.erzhiProcess(this.nData,90));
		this.img1=this.getGrayImage(95);
	}
	
	//��ȡ�����ͼ�� �����ݱ��������� nData[]��nB[] ��nG[] ��nR[]��
	public void getBMPImage(String source) throws Exception
	{
		FileInputStream fs = null;
		try
		{
			fs = new FileInputStream(source);
			int bfLen = 14;
			byte bf[] = new byte[bfLen];
			fs.read(bf, 0, bfLen); // ��ȡ14�ֽ�BMP�ļ�ͷ
			int biLen = 40;
			byte bi[] = new byte[biLen];
			fs.read(bi, 0, biLen); // ��ȡ40�ֽ�BMP��Ϣͷ

			// Դͼ���
			nWidth = (((int) bi[7] & 0xff) << 24)
					| (((int) bi[6] & 0xff) << 16)
					| (((int) bi[5] & 0xff) << 8) | (int) bi[4] & 0xff;

			// Դͼ�߶�
			nHeight = (((int) bi[11] & 0xff) << 24)
					| (((int) bi[10] & 0xff) << 16)
					| (((int) bi[9] & 0xff) << 8) | (int) bi[8] & 0xff;

			// λ��
			nBitCount = (((int) bi[15] & 0xff) << 8) | (int) bi[14] & 0xff;

			// Դͼ��С
			nSizeImage = (((int) bi[23] & 0xff) << 24)
					| (((int) bi[22] & 0xff) << 16)
					| (((int) bi[21] & 0xff) << 8) | (int) bi[20] & 0xff;

			// ��24λBMP���н���
			if (nBitCount == 24)
			{
				int nPad = (nSizeImage / nHeight) - nWidth * 3;
				nData = new int[nHeight * nWidth];
				nB = new int[nHeight * nWidth];
				nR = new int[nHeight * nWidth];
				nG = new int[nHeight * nWidth];
				byte bRGB[] = new byte[(nWidth + nPad) * 3 * nHeight];
				fs.read(bRGB, 0, (nWidth + nPad) * 3 * nHeight);
				int nIndex = 0;
				for (int j = 0; j < nHeight; j++)
				{
					for (int i = 0; i < nWidth; i++)
					{
						nData[nWidth * (nHeight - j - 1) + i] = (255 & 0xff) << 24
								| (((int) bRGB[nIndex + 2] & 0xff) << 16)
								| (((int) bRGB[nIndex + 1] & 0xff) << 8)
								| (int) bRGB[nIndex] & 0xff;
						nB[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex] & 0xff;
						nG[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex + 1] & 0xff;
						nR[nWidth * (nHeight - j - 1) + i] = (int) bRGB[nIndex + 2] & 0xff;
						nIndex += 3;
					}
					nIndex += nPad;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		} finally
		{
			if (fs != null)
			{
				fs.close();
			}
		}
		//   return image;
	}
	

	
	public int[] erzhiProcess(int []data,int gate)
	{
	     int k;
		int [] processData=new int[data.length];
	     for(int i=0;i<nWidth*nHeight;i++)
        {
	    	 k=(int)(0.3*nB[i]+0.59*nG[i]+0.11*nR[i]);
	         k=k>255?255:k;	    	 
	         if(k>gate)
	              processData[i]=(255 << 24) | (255 << 16) |(255 << 8)|255;
	        else 
	             processData[i]=(255 << 24) | (0 << 16) |(0 << 8)|0;
System.out.println(processData[i]);  
	       
	    }	      
	    return processData;    
	}
	public  int[] graytongjiData()
	{ 
	     int k=0;
	     GGrayData = new int[256];
		 int grayData[]=new int[256];
	    for(int i=0;i<nB.length;i++)
	    {
	         k=(int)(0.3*nB[i]+0.59*nG[i]+0.11*nR[i]);
	         k=k>255?255:k;
	         //System.out.println(k);
	         grayData[k]++;	       
	   }	         
	   GGrayData = grayData; 
	   return grayData;      
    }
	
	public Image getmyimage(int[] data)
	{
		//Toolkit kit = Toolkit.getDefaultToolkit();
		//return  kit.createImage(new MemoryImageSource(nWidth, nHeight,
		                      // data, 0, nWidth));	
		return  createImage(new MemoryImageSource(nWidth, nHeight,data, 0, nWidth));
	}
	
	public void paint(Graphics g)
	{
		 super.paint(g);     
		 g = this.getGraphics();
		 g.setColor(Color.WHITE);
		 g.clearRect(0, 0, 900, 700);
		 g.setColor(Color.BLUE);
		 
		 g.drawString("ԭʼͼ��", 30, 50);
		 g.drawImage(image, 20, 60, 300,300,this);	
		 
		 g.drawString("�����ͼ��", 30, 370);
		 g.drawImage(img1, 20, 380, 300,300,this);
		//** 
		 int x0,y0;
		 x0 = 420;
		 y0 = 650;
		 
		 //��x��
	     g.drawLine(x0,y0, x0+550,y0);
         
	     //��y��
		 g.drawLine(x0,y0, x0,y0-600);
		 g.drawString("�Ҷ�ֱ��ͼ", x0+250, y0+20);		
	     //��Ҷ�	
		 g.setColor(Color.RED);
		 for(int i=1;i<256;i++)
		 {	 
			 g.drawLine(x0+i*2,y0,x0+i*2,y0- GGrayData[i]/50);	
		 }
		//*/ 
	 }
	public Image getGrayImage(int gate){
		int pix[] = new int[nWidth * nHeight];
        int index = 0;
        int k;
        for(int i=0;i<nWidth*nHeight;i++)
        {
	    	 k=(int)(0.3*nB[i]+0.59*nG[i]+0.11*nR[i]);
	         k=k>255?255:k;	 
	         if(k>gate)            
                pix[i] = (255 << 24) | (255 << 16) | (255<<8)|255;
	         else
	        	pix[i] = (255 << 0) | (0 << 16) | (0<<8)|0;
        }
        return createImage(new MemoryImageSource(nWidth, nHeight, pix, 0, nWidth));	
	}
	
	 public static void main(String[] args)throws Exception
	 {
		 bmpabc t=new bmpabc();
		 //int[] d;
		 //t.getBMPImage("D://zcs//eclipse//pcrbmp.bmp");
		 //d=t.erzhiProcess(t.nData,0.5, 100);
		 //t.graytongjiData();
		 //t.image=t.getmyimage(d);
		 //t.paint(t.getGraphics());
	 }
}
