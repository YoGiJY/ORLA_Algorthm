/*
 * @Author:yao
 * @Email: yao.jiang@tongji.edu.cn
 * @Time:  2016��7��18�� 14:34:55
 * @Brief: allocation C(k,p) C(c,p) 
 */
package com.orla.alogrithm;

import java.util.Random;

/*
 * public void orla_alloAction(int k)  //ʵ�з������
 * public void binselect(int l1,int l2) //���ַ���ʵ��
 *  
 */
public class orla_allocation {
	//����ʵ�ֵ� Cpdotʵ���³��ֵĴ������������Ϣ
	//Cp�Ǵ洢��history�е��Ѿ�����õ�������Ϣ
	public static final double Theta  = 0.1;
	public orla_structCp Cpdot;   
	public orla_structCp Cp;
	public double []X1 = new double[orla_structCp.Vnum];
	public double []X2 = new double[orla_structCp.Vnum];
	public double []Y = new double[orla_structCp.Vnum];
	public int ynum;
	public int NUM;
	
	public orla_allocation(){
	}
	
	public orla_allocation(orla_structCp a,orla_structCp b){
		 this.Cpdot = a;
		 this.Cp = b;
	}
	
	public void orla_allpara(orla_structCp a,orla_structCp b)
	{
		 this.Cpdot = a;
		 this.Cp = b;
	}
	
	public void orla_alloAction(int k)
	{
		//�����־����,��¼���������з���
		int flag[] = new int[orla_structCp.Vnum];
		int z;
		int fnum;
		int [] rank = new int [orla_structCp.Vnum];
		int binnum;
		int N1;
		int N2;
		int num;
		int low;
		int high;
		int middle;
		int l1;
		int l2;
	        int temp; 
                int y;
		Cp.orla_mustex();      //�������ݣ���һ������Cij����һЩ���ˣ�ֻ�������õĳ�������Ϣ��
		                       //��Ϊ���������Ǽ�����117��������ʵ����û����ô�೵�μ�
		Cp.orla_exSortEx(k);   //�������ݣ���������in Cp cij is not 0
		X1 = Cp.realCsort;
		
		Cp.orla_real2Y(k);     // ��ȡ��������֮��Cij��Ϣ������������֮���cp A(i,k) == 1		
		Y = Cp.Y;		
		ynum = Cp.Ynum;        // ���ݹ��ˣ�����֮�󣬷����վ��Դ�ĸ���
		
		Cpdot.orla_mustex();     //�������ݣ�sort the elements c(i,k) in cp-dot is not 0;
		Cpdot.orla_exSortEx(k);  //��������
		X2 = Cpdot.realCsort;
		
		//���ѡȡ�������վ��û��һ�����ӣ���ôֱ�Ӹ�ֵΪ��Ϳ�����
		if(ynum == 0) 
		{
			for(int i = 0;i<Cpdot.Venum;++i)
			{
				Cpdot.orla_struct2A(i, k, 0);//ֱ�Ӷ�ԭʼ���ݽ��д���
			}
		}

		////////////////////////////////////////////////////////////////
		else
		{
		    //Cpdot.Venum method of the length of a vector
		    //Cp.Venum method of the length of a vector
		    //calculate the number of vehicles that requires to be associated as:
			double xnum = Cpdot.U[k]/Cp.U[k];
			double znum = (double)Cpdot.Venum/Cp.Venum;
			double denum = xnum*znum*ynum;
		    this.NUM = (int) Math.ceil(denum);
		    /*Let|V|denote the length of a vector */

	        if(this.NUM < ynum)
			{
				//��ʼ����ʶ������Ϣ
				for(int i = 0;i<orla_structCp.Vnum;++i)
				{
					 flag[i] = 0;
				}
				//Use binary approaching method to obtain association matrix A(k,pdot)
	       
	            //flag�Ǳ�ʾ�ڷ������ݵ�λ���ϣ����б�ʶ
	            //���ַ���Ҫ�ṩ�Ĳ�����X1��������X2����������ʷ����,ѡȡ������
	            flag = this.orla_binselect(X1,X2,Cp.realAsort,this.NUM);
	  			//set the corresponding values of set Y in matrix A(k,pdot) as 1
	            //��������׼�����ǵ��ǣ���ν�����õ����ݣ����ҷ���õĳ�������Ϣ��ԭ
	            //��һ��������flag��ԭ������A(i,j)
	            //�ڶ�������ʹ������������������һ�±�ǻ�ԭ������ǰ����Ϣ״̬

	            /* ��һ����ʵ�� */
	            //����ʹ�õ���orla_structCp.Vnum
	            for(int i = 0;i<orla_structCp.Vnum;++i)
	            {   //flag�ı�ž�����Ҫ����ĳ�����Ϣ
	            	if(flag[i]==1)
	            	{
	                   Cpdot.realAsort[i] = 1;
	            	}
	            	else
	            	{
	            		Cpdot.realAsort[i] = 0;
	            	}
	            }
	            /*�ڶ�����ʵ��*/
	            //����ʵ���ˣ��������A(i,j)�У�ʵ����һ�η���
	            Cpdot.orla_exback(k);
			}//��һ�ַ��䷽������
			else
			{
                //���㱶��
                
                for(int i = 0;i<orla_structCp.Vnum;++i)
				{
					 flag[i] = 0;
				}

				z = (int)Math.floor(((double)NUM/ynum));

				/*��ʼ��flag��ʾ��ѡ�б�־Ϊ1,����û�б�ѡ�оͱ�ʶΪ0*/
				for(int i = 0;i<orla_structCp.Vnum;++i)
				{
	                 flag[i] = 0;
				}

	            /*��ʼ����Ϣrank*/
	            for(int i = 0;i<orla_structCp.Vnum;++i)
	            {
	                 rank[i] = -1;        
	            }

				//initial an object rank that is used to label the rank location of an element in a vector;
				//����֪������ʷ��ѡ�еĳ����ź���֮���λ����Ϣ
				//realvy[]��¼���ǣ���ʷ����,����֮ǰ��λ��
				//����Ҫ������ʷ��Ϣ��ѡȡһЩ����������Ϣ

				for(int i = 0;i<ynum;++i)
				{
					rank[i] =(int)(((double)Cpdot.Venum/Cp.Venum)*Cp.realvy[i]);
				}//end for

	            /*��ʼ����Ҫѡȡ����*/
				for(int i = 0;i<ynum;++i)
				{
	                temp = rank[i];// ѡȡһ��λ�ã�Ȼ�������λ�ã�������չ���ڴ˻����Ͻ�����չz����
	                fnum = z;
	                l2 = Cpdot.Venum;

	                for(int j=0;j<(int)l2/2;++j)
	                {
	                	//�ж��ϰ벿�ֵ���Ϣ�Ƿ���Է��䣬�����temp
	                    if(temp >= 0)
	                    {
	                       if(temp-j>=0)
		                	{
		                		if(flag[temp-j]==0)
		                		{
		                			flag[temp-j] = 1;
		                			fnum--;
		                		}
		                	}
		                    //ִ��һ�Σ��ж��Ƿ�ѡ�����
		                    if(fnum == 0)  break;

		                	if(temp+j<l2)
		                	{
		                        if(flag[temp+j]==0)
		                        {
		                        	flag[temp+j] = 1;
		                        	fnum--;
		                        }
		                	}
		                   //ִ��һ�Σ��ж��Ƿ�ѡ�����
		                   if(fnum == 0)  break;
	                    }
	                } //end for
				}//for end ��������Բο���Ϣִ�����
			    //��������Ƕ���ÿһ��ѡ���־λ��Ȼ���ڴ˻������ϻ������½�����չ
	            //����ʹ�ö��ַ���ʵ�֣�����ʣ�»�û��ѡ������������ʹ�ö��ַ�������ѡ��
	            if((this.NUM - ynum*z)>=0)//����պò���z�ı��������ַ�
	            {
	                N1 = 0;
			        N2 = 0;
			        binnum = this.NUM - ynum * z;
			        l1 = Cp.Venum;
			        l2 = Cpdot.Venum;
					for(int i = 0;i<l1;++i)
				    {
						if(Cp.realAsort[i] == 1 && i<(int)(l1/2))
						{
							N1 = N1 + 1;
						}
						else if(Cp.realAsort[i] == 1 && i>=(int)(l1/2))
						{
							N2 = N2 + 1;
						}
				    }//end for

				    if(((double)(Math.abs(N1-N2)/Cp.Ynum)>orla_allocation.Theta)&&(N1>N2))
				    {
						num = binnum;
						while(num !=0)
						{
							low = 0;
							high = l2;
							middle = (int)((low+high)/2);
							while(low!=middle && num!=0)
							{
								Random rand = new Random();
								//ȡ�������(middle-low)
								y = rand.nextInt(middle-low)+low;
								if(flag[y] == 0)
								{
									flag[y] = 1;
									num--;
								}
								//�ϰ벿����������low = 0
								if(num == 0) break;
								low = 0;
								middle = (int)((middle+low)/2);	
							}
							
							if(num != 0)
							{
								low = (int)(l2/2);
								middle = high = l2;
							    while(low!=middle && num !=0)
							    {
							    	Random rand = new Random();
							    	y = rand.nextInt(middle-low)+low;
							    	if(flag[y] == 0)
							    	{
							    		flag[y] = 1;
							    		num--;
							    	}
							    	if(num == 0) break;
							    	low = (int)(l2/2);//�����ϰ벿��
							    	middle = (int)((low+middle)/2);
							    }	
							}	
						}
				    } // if
				    else if(((double)(Math.abs(N1-N2)/Cp.Ynum)>orla_allocation.Theta)&&(N1<N2))
				    {
                        num = binnum;
						while(num!=0)
						{
							//�����°벿��
							low = (int)(l2/2);
							high = l2;
							middle = (int)((low+high)/2);
						    while(low!=middle && num !=0)
						    {
						    	Random rand = new Random();
						    	//���°벿�����ѡ��
						    	y = rand.nextInt(high - middle)+middle;
						    	if(flag[y] == 0)
						    	{
						    		flag[y] = 1;
						    		num --;
						    	}
						    	if(num == 0) break;
						    	middle = (int)(middle+high)/2;
						    	high = l2;
						    }//�°벿���������
						    
						    //�°벿��ѡ�����֮�����û��ѡ�������ô��������ѡ��
						    if(num!=0)
						    {
						    	low = 0;
						    	high = l2;
						    	middle = (int)(l2/2);
						    	while(low!=middle && num!=0)
						    	{
						    		Random rand = new Random();
						    		
						    		y = rand.nextInt(middle-low)+low;
						    		if(flag[y]==0)
						    		{
						    			flag[y] = 1;
						    			num --;
						    		}
					               low = (int)(middle+low)/2;
					               middle = (int)l2/2;
						    	}//
						    }
			            }//while
				    }
				    else if(((double)(Math.abs(N1-N2)/Cp.Ynum)<=orla_allocation.Theta)&&(this.NUM!=1))
				    {
						num = binnum;
						low = 0;
						high = l2;
						middle = (int)(low+high)/2;
						
						//���ѡ����Կ�������ʽ
						while(num!=0)
						{
							Random rand = new Random();
							y = rand.nextInt(middle-low)+low;
							if(flag[y] == 0)
							{
								flag[y] = 1;
								num--;
							}

						   if(num == 0) break;

							Random rand1 = new Random();
							y = rand1.nextInt(high-middle+1)+middle;
							if(flag[y] == 0)
							{
								flag[y] = 1;
								num--;
							}
						}//while
				    }
				    else if(((double)(Math.abs(N1-N2)/Cp.Ynum)<=orla_allocation.Theta)&&(this.NUM==1))
				    {
						num = binnum;
						//��������ֻ��Ҫ���ȥһ�����Ϳ���
						low = 0;
						high = l2;
						Random rand = new Random();
						y = rand.nextInt(high-low) + low;
						if(flag[y]==0)
						{
							flag[y] = 1;
						}
					}//end if 
				}
				//�������
				//��Ҫ����ʶ����flag����ԭ��Cpdot.Asortrow
				//
				for(int i = 0;i<orla_structCp.Vnum;++i)
	            {   //flag�ı�ž�����Ҫ����ĳ�����Ϣ
	            	if(flag[i]==1)
	            	{
	                   Cpdot.realAsort[i] = 1;
	            	}
	            	else
	            	{
	            		Cpdot.realAsort[i] = 0;
	            	}
	            }
	            //����ʵ���ˣ��������A(i,j)�У�ʵ����һ�η���
	            Cpdot.orla_exback(k);
			}//�ڶ��ַ����㷨���
		}//end if
	} //orla_alloAction
	  


	/*
	 * flag ��Ҫ������Դ�ĳ�����λ�ý��б�ʶ
	 * l1 Cp�������г���������
	 * l2 Cpdot�г���������
	 * X11 ����[0,l1/2]
	 * X12 C(i,j)����[(l1+1)/2,l1]
	 * X21 Cpdot(i,j)����(0,l2/2);
	 * X22 Cpdot(i,j)����(l2/2,l2);
	 */
	
	public int[] orla_binselect(double []c1,double []c2,int[] a1,int number)
	{                         //
		int l1;
		int l2;
		int N1;
		int N2;
		int num;
		double [] x11 = new double [orla_structCp.Vnum];//split the vector X(p,k)
		double [] x12 = new double [orla_structCp.Vnum];
		double [] x21 = new double [orla_structCp.Vnum];//split the vector X(dotp,k)
		double [] x22 = new double [orla_structCp.Vnum];
		int [] flag = new int [orla_structCp.Vnum];
		int y;
		int middle;
		int low;
		int high;
		
     	/* Step 1:Initialize */
		l1 = 0;
		l2 = 0;
		l1 = Cp.Venum;    //initial l1=|X(p,k)| 
		l2 = Cpdot.Venum; //l2 = |X(pdot,k)|
		
		/*  Step 2:Split the vector X(p,k) */
		//split the vector X(p,k)
		for(int i = 0;i<(int)(l1/2);++i)//
		{
			x11[i] = c1[i];
		}
		for(int j = 0,i = (int)(l1/2);i<l1;++i,++j)
		{
			x12[j] = c1[i];
		}
		//split the vector X(pdot,k)
		for(int i = 0;i<(int)(l2/2);++i)//
		{
			x21[i] = c2[i];
		}
		for(int j = 0,i =(int)(l2/2);i<l2;++i,++j)
		{
			x22[j] = c2[i];
		}
		
		/*Step 3: Count the number of common elements both in Vectors Y(p,k) and X(p,k)*/
		N1 = 0;
		N2 = 0;
		for(int i = 0;i<l1;++i)
		{
			if(a1[i] == 1 && i<(int)(l1/2))
			{
				N1 = N1 + 1;
			}
			else if(a1[i] == 1 && i>=(int)(l1/2))
			{
				N2 = N2 + 1;
			}
		}//end for

		//step 4: Then according to the different proportions, we have four different casses
		if((((double)Math.abs(N1-N2)/Cp.Ynum)>orla_allocation.Theta)&&(N1>N2))
		{
			//choose a value y uniformly from vector X(pdot,k):[1,l2/2]
			//except the elements in set Y��o
			//set Y = Y and {y}
			//set x(p,k) = X(p,k):[1,l1/2] and X(pdot,k):[1,l2/2];
			//if the binary search ends |X(p,k):[1,l1/2]| = 1,we set X(p,k) = X(p,k):[l1+1/2,l1];
			//��ʶ������г�ʼ��Ϊ0
			for(int i = 0;i<orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			num = number;
			//�������ϰ벿���������°벿�֣�ʵ�еĲ������ϰ벿��ѡ����֮�󣬼�����벿��ѡ��
			//���ѡ�����֮��û��ѡ��������֮ǰ�ò���
			while(num !=0)
			{
				low = 0;
				high = l2;
				middle = (int)((low+high)/2);
				while(low!=middle && num!=0)
				{
					Random rand = new Random();
					//ȡ�������(middle-low)
					y = rand.nextInt(middle-low)+low;
					if(flag[y] == 0)
					{
						flag[y] = 1;
						num--;
					}
					//�ϰ벿����������low = 0
					if(num == 0) break; 
					low = 0;
					middle = (int)((middle+low)/2);	
				}
				
				if(num != 0)
				{
					low = (int)(l2/2);
					middle = high = l2;
				    while(low!=middle && num !=0)
				    {
				    	Random rand = new Random();
				    	y = rand.nextInt(middle-low)+low;
				    	if(flag[y] == 0)
				    	{
				    		flag[y] = 1;
				    		num--;
				    	}
				    	if(num == 0) break;
				    	low = (int)(l2/2);//�����ϰ벿��
				    	middle = (int)((low+middle)/2);
				    }	
				}	
			 }
			//�����ϰ벿������ִ�����
		   }
		else if((((double)Math.abs(N1-N2)/Cp.Ynum)>orla_allocation.Theta)&&(N1<N2))
		{
		    //�������֮���ϰ벿�ֵ����������°벿�֣��������ϵ���Ƚϴ��ʱ��
		    //���ǲ�ȡ�Ĳ����������°벿��
			/*��ʼ�������ʶflag[]*/
			for(int i = 0;i<orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			/*��ʼ����������Ҫѡ����������*/
			num = number;
			
			while(num!=0)
			{
				//�����°벿��
				low = (int)(l2/2);
				high = l2;
				middle = (int)((low+high)/2);
			    while(low!=middle)
			    {
			    	Random rand = new Random();
			    	//���°벿�����ѡ��
			    	y = rand.nextInt(high - middle)+middle;
			    	if(flag[y] == 0)
			    	{
			    		flag[y] = 1;
			    		num --;
			    	}
			    	if(num ==0)  break;
			    	middle = (int)(middle+high)/2;
			    	high = l2;
			    }//�°벿���������
			    
			    //�°벿��ѡ�����֮�����û��ѡ�������ô��������ѡ��
			    if(num!=0)
			    {
			    	low = 0;
			    	high = l2;
			    	middle = (int)(l2/2);
			    	while(low!=middle && num!=0)
			    	{
			    		Random rand = new Random();
			    		//
			    		y = rand.nextInt(middle-low)+low;
			    		if(flag[y]==0)
			    		{
			    			flag[y] = 1;
			    			num --;
			    		}
		               low = (int)(middle+low)/2;
		               middle = (int)l2/2;
			    	}//
			    }
			}//while
		}
		else if((((double)Math.abs(N1-N2)/Cp.Ynum)<=orla_allocation.Theta)&&(this.NUM!=1))
		{
			//��������������ֵģ����ݲ�࣬��ô�����������������ѡ��
			for(int i=0;i<orla_structCp.Vnum;++i)
			{
			    flag[i] = 0;	
			}
			/*��ʼ�����ݳ�����Ϣ*/
			num = number;
			low = 0;
			high = l2;
			middle = (int)(low+high)/2;
			
			//���ѡ����Կ�������ʽ
			while(num!=0)
			{
				Random rand = new Random();
				y = rand.nextInt(middle-low)+low;
				if(flag[y] == 0)
				{
					flag[y] = 1;
					num--;
				}

				if(num == 0) break;

				Random rand1 = new Random();
				y = rand1.nextInt(high-middle+1)+middle;
				if(flag[y] == 0)
				{
					flag[y] = 1;
					num--;
				}
			}//while
		}
		else if((((double)Math.abs(N1-N2)/Cp.Ynum)<=orla_allocation.Theta)&&(this.NUM==1))
		{
			//ѡȡ�ĳ���ֻ��һ�������ǾͶ����������ڵĳ����������ȡһ����
			/*��ʼ��flag��Ϣ*/
			for(int i=0;i<=orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			
			/* ��ʼ��������Ϣ������num = 1*/
			num = number;
			//��������ֻ��Ҫ���ȥһ�����Ϳ���
			low = 0;
			high = l2;
			Random rand = new Random();
			y = rand.nextInt(high-low) + low;
			if(flag[y]==0)
			{
				flag[y] = 1;
			}
		}
		return flag;
	}
}//class

