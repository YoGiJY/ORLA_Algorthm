package com.orla.alogrithm;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//Author WCCSAMA
public class getdata {

	public double[][] C=new double[117][20];//Cij���� ��Ϊ�ɴ�����
	public double[] U=new double[20];//Uj���鼴Ϊ�۸�
	public int[][] X = new int[117][20];//Xij���鼴Ϊ�����Ƿ����ӻ�վ
	public double[][] R=new double[117][20];//��������
	
	public double[][] D=new double[117][20];//Dij���� ��Ϊ��������վ�ľ���
	public static double[] power={46,35,20};//���� ���ֻ�վ�Ĺ��ʣ�˳��Ϊ��46����35��С20
	double[] K=new double[20];//Kj����
	public static double cf=10;//Ƶ��C
	public static double apha=-104;//��λdBm
	/**
	 * @param args
	 */
	
	  
	  /*
	   * 
	   * �ж�Xij�����Ƿ񱻸�ֵ����Ϊ�ڿͻ��˵ĸ�Xij����ֵ��ʱ��
	   * ��ҪXij����ȫ��Ϊ��ʼֵ��Ϊ0����java���鲻��ֵ��Ϊ0����
	   * 
	   */
		public boolean Zero(int a[][])
		{
		   boolean flag=true;
		   for(int i=0;i<117;i++)
		   {
			   for(int j=0;j<20;j++)
			   {
				   if(a[i][j]==1)
				   {
					   flag=false;   
				   }
			   }
		   }
		 return flag;
		 }
		
		
		/*
		 * 
		 * �˺����Ǹ��txt�ļ���ȡ��Dij��������ݣ� �����Cij
		 * 
		 */
		public void UpdateC(int a){
			String path="C:\\Users\\Yao\\Desktop\\distance\\java"+a+".txt";
			try 
			{
	            String encoding="GBK";
	            File file=new File(path);
	            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
	                InputStreamReader read = new InputStreamReader(
	                new FileInputStream(file),encoding);//���ǵ������ʽ
	                BufferedReader bufferedReader = new BufferedReader(read);
	                String lineTxt = null;
	                int p=0,q=0;//���D[][]���飻
	                int p1=0;
	                while((lineTxt = bufferedReader.readLine()) != null&&p1<2340)
	                {
	                	if(q<20)
	                	{
	                		D[p][q]=Double.parseDouble(lineTxt);
	                		p1++;
	                		q++;
	                	}else
	                	{
	                	    p++;
	                	    q=0;
	                	    D[p][q]=Double.parseDouble(lineTxt);
	                	    p1++;
	                	    q++;
	                	}
	                	
	                }
	                read.close();
	                }else
	                {
	                	System.out.println("文件未找到！");
	                }
	                } catch (Exception e) {
	            	System.out.println("读入数据失败！");
	            	e.printStackTrace();
	            	}
			
			double[] sum=new double[117];//����PjGij���ܺ�(i=0.1....116)
			double[][] temp=new double[117][20];//��¼PjGij
			//������Pj*Gij,sum[i]
			double b=-1.0;
			for(int i=0;i<117;i++){
				if(D[i][0]!=b&&D[i][1]!=b){
				for(int j=0;j<20;j++){
					if(j>=0&&j<5){
						temp[i][j] =-power[0]/(34+40*Math.log10(D[i][j]));
						sum[i]=sum[i]+temp[i][j];
					}else if(j>=5&&j<10){
						temp[i][j]=-power[1]/(34+40*Math.log10(D[i][j]));
						sum[i]=sum[i]+temp[i][j];
					}else if(j>=10&&j<20){
						temp[i][j]=-power[2]/(37+30*Math.log10(D[i][j]));
						sum[i]=sum[i]+temp[i][j];
					}
				}
				}
				
			}
			//����Cij
		
			for(int i=0;i<117;i++)
			{
				if(D[i][0]!=b&&D[i][1]!=b)
				{
				for(int j=0;j<20;j++)
				{
					C[i][j]=cf*Math.log(1+temp[i][j]/(sum[i]-temp[i][j]+apha))/Math.log(2);
				}
			    }else{
				for(int j=0;j<20;j++)
				{
					C[i][j]=0;
				}
			}
			}
			
		}
		
		//��װ��һ������
		public void getData(int Num){
			int iteraNum=10000;//��ĵ�������ļ�������
			double Buchang=0.01;//��Ĳ���1
			double tiaojian=0.1;//�����ж�����0.001
			int File=Num;//��ȡ��ʼ�ļ������
			int FileItera=20;//��ֹ�����������ж��ٸ���վ��Uj������������
			
		    	int n=1;//��¼�����ļ��ĵ�����
		    	double[] copy=new double[20];//һ��������������Uj�������һ�ε�����
		    	
		    	UpdateC(File);//���File��ȡ����ļ�������Cij����
		    	int count2=0;//�ж����ڵ��Uj��ֵС��tiaojian������վ����������������վ�ĸ���
			    
		    	while(count2<FileItera&&n<=iteraNum)
			    {
			    	count2=0;
			    	if(Zero(X))
			    	{//�״�ִ���ж������Ƿ�Ϊ��  XΪ�հ��ճ�ʼֵ����
			    		for(int i=0;i<20;i++) 
			    		{
						   K[i]=1;
						   U[i]=1;
						}//Kj��ʼ�� Uj��ʼ��
			    	}else{
			    		for(int i=0;i<20;i++)
			    		{
			    			copy[i]=U[i];//������һ��U��ֵ
			    			int count=0;//����ÿһ����վ�ĳ���������
			    			for(int j=0;j<117;j++)
			    			{
			    				count=count+X[j][i];//����
			    			}
			    			U[i]=U[i]-Buchang*K[i]+Buchang*count;//����Uj����
			    		 }
			    		for(int i=0;i<20;i++)
					    {
						  K[i]=Math.pow(Math.E,copy[i]-1)>117?117:Math.pow(Math.E,copy[i]-1);//����Kj����
					    }
				   }
				  
					for(int j=0;j<20;j++)
					{
						if(Math.abs(copy[j]-U[j])<=tiaojian)//������ڵ���Uj��ֵ�ж��Ƿ�����
						{
							count2++;
					    }
					}
					
					//�ж��Ƿ���������ӡ��������,����ʼ���ļ��ĵ��
					if(count2>=FileItera){
						 int[] NumJ=new int[20];
						 
						 for(int i=0;i<20;i++){
							 for(int j=0;j<117;j++){
								 NumJ[i]=NumJ[i]+X[j][i];
							 }
						 }//��ÿ����վ�ĳ���������

				         for(int i=0;i<117;i++)
				         {
				        	 for(int j=0;j<20;j++)
				        	 {
				        		if(NumJ[j]==0)
				        		{
				        			R[i][j]=0;
				        		}else
				        		{
				        			R[i][j]=C[i][j]/NumJ[j];
				        		}
						     }
				         }
			
					     break;
					}
					
					
				   //�û���
				   //��Xij���ȫ����0
				   for(int i=0;i<117;i++)
				   {
						for(int j=0;j<20;j++)
						{
							X[i][j]=0;
						}
					}
				    
					//��J*����Xij���鸳��ʼֵ
					for(int i=0;i<117;i++)
					{
						if(C[i][0]!=0.0&&C[i][1]!=0.0)//������Ϣ����ʱ
						{
						int p1=0,p2=0;//��¼λ��
						double J=Math.log10(C[i][0])-U[0];
						for(int j=0;j<20;j++)
						{
							if(J<=(Math.log10(C[i][j])-U[j]))
							{
							J=(Math.log10(C[i][j])-U[j]);//����Cij ��j ��J*	
							p1=i;
							p2=j;
							}
						}
						X[p1][p2]=1;//���������X��ֵ	
						}
					}	
				}//�ڲ�Whileѭ������һ������ļ�������
		}

}//����ս�
