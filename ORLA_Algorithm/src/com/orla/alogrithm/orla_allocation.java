/*
 * @Author:yao
 * @Email: yao.jiang@tongji.edu.cn
 * @Time:  2016年7月18日 14:34:55
 * @Brief: allocation C(k,p) C(c,p) 
 */
package com.orla.alogrithm;

import java.util.Random;

/*
 * public void orla_alloAction(int k)  //实行分配策略
 * public void binselect(int l1,int l2) //二分法的实现
 *  
 */
public class orla_allocation {
	//这里实现的 Cpdot实现新出现的代分配的数据信息
	//Cp是存储在history中的已经分配好的数据信息
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
		//分配标志数组,记录哪辆车进行分配
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
		Cp.orla_mustex();      //过滤数据，这一操作将Cij进行一些过滤，只留下有用的车辆的信息，
		                       //因为我们这里是假设有117辆车但是实际中没有这么多车参加
		Cp.orla_exSortEx(k);   //过滤数据，进行排序，in Cp cij is not 0
		X1 = Cp.realCsort;
		
		Cp.orla_real2Y(k);     // 获取分配数据之后Cij信息，而且是排序之后的cp A(i,k) == 1		
		Y = Cp.Y;		
		ynum = Cp.Ynum;        // 数据过滤，排序之后，分配基站资源的个数
		
		Cpdot.orla_mustex();     //过滤数据，sort the elements c(i,k) in cp-dot is not 0;
		Cpdot.orla_exSortEx(k);  //进行排序
		X2 = Cpdot.realCsort;
		
		//如果选取的这个基站，没有一个连接，那么直接赋值为零就可以了
		if(ynum == 0) 
		{
			for(int i = 0;i<Cpdot.Venum;++i)
			{
				Cpdot.orla_struct2A(i, k, 0);//直接对原始数据进行处理
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
				//初始化标识数据信息
				for(int i = 0;i<orla_structCp.Vnum;++i)
				{
					 flag[i] = 0;
				}
				//Use binary approaching method to obtain association matrix A(k,pdot)
	       
	            //flag是表示在分配数据的位置上，进行标识
	            //二分法需要提供的参数，X1列向量，X2列向量，历史分配,选取的数量
	            flag = this.orla_binselect(X1,X2,Cp.realAsort,this.NUM);
	  			//set the corresponding values of set Y in matrix A(k,pdot) as 1
	            //在这里我准备考虑的是，如何将排序好的数据，而且分配好的车辆将信息还原
	            //第一步，先用flag还原排序后的A(i,j)
	            //第二步，再使用排序是我们所做的一下标记还原到排序前的信息状态

	            /* 第一步的实现 */
	            //这里使用的是orla_structCp.Vnum
	            for(int i = 0;i<orla_structCp.Vnum;++i)
	            {   //flag的标号就是需要分配的车辆信息
	            	if(flag[i]==1)
	            	{
	                   Cpdot.realAsort[i] = 1;
	            	}
	            	else
	            	{
	            		Cpdot.realAsort[i] = 0;
	            	}
	            }
	            /*第二步的实现*/
	            //这里实现了，将分配的A(i,j)中，实现了一次分配
	            Cpdot.orla_exback(k);
			}//第一种分配方法结束
			else
			{
                //计算倍数
                
                for(int i = 0;i<orla_structCp.Vnum;++i)
				{
					 flag[i] = 0;
				}

				z = (int)Math.floor(((double)NUM/ynum));

				/*初始化flag表示被选中标志为1,否则没有被选中就标识为0*/
				for(int i = 0;i<orla_structCp.Vnum;++i)
				{
	                 flag[i] = 0;
				}

	            /*初始化信息rank*/
	            for(int i = 0;i<orla_structCp.Vnum;++i)
	            {
	                 rank[i] = -1;        
	            }

				//initial an object rank that is used to label the rank location of an element in a vector;
				//这里知道，历史被选中的车辆排好序之后的位置信息
				//realvy[]记录的是，历史数据,排序之前的位置
				//我们要根据历史信息，选取一些将分批的信息

				for(int i = 0;i<ynum;++i)
				{
					rank[i] =(int)(((double)Cpdot.Venum/Cp.Venum)*Cp.realvy[i]);
				}//end for

	            /*初始化需要选取车辆*/
				for(int i = 0;i<ynum;++i)
				{
	                temp = rank[i];// 选取一个位置，然后在这个位置，进行拓展，在此基础上进行拓展z辆车
	                fnum = z;
	                l2 = Cpdot.Venum;

	                for(int j=0;j<(int)l2/2;++j)
	                {
	                	//判断上半部分的信息是否可以分配，相对于temp
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
		                    //执行一次，判断是否选择完毕
		                    if(fnum == 0)  break;

		                	if(temp+j<l2)
		                	{
		                        if(flag[temp+j]==0)
		                        {
		                        	flag[temp+j] = 1;
		                        	fnum--;
		                        }
		                	}
		                   //执行一次，判断是否选择完毕
		                   if(fnum == 0)  break;
	                    }
	                } //end for
				}//for end 对所有相对参考信息执行完毕
			    //上述完成是对于每一个选择标志位，然后在此基础向上或者向下进行扩展
	            //下面使用二分法来实现，对于剩下还没有选完的情况，我们使用二分法来进行选择
	            if((this.NUM - ynum*z)>=0)//这里刚好不是z的倍数，二分法
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
								//取随机数据(middle-low)
								y = rand.nextInt(middle-low)+low;
								if(flag[y] == 0)
								{
									flag[y] = 1;
									num--;
								}
								//上半部分优先所以low = 0
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
							    	low = (int)(l2/2);//优先上半部分
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
							//优先下半部分
							low = (int)(l2/2);
							high = l2;
							middle = (int)((low+high)/2);
						    while(low!=middle && num !=0)
						    {
						    	Random rand = new Random();
						    	//在下半部分随机选择
						    	y = rand.nextInt(high - middle)+middle;
						    	if(flag[y] == 0)
						    	{
						    		flag[y] = 1;
						    		num --;
						    	}
						    	if(num == 0) break;
						    	middle = (int)(middle+high)/2;
						    	high = l2;
						    }//下半部分优先完毕
						    
						    //下半部分选择完毕之后，如果没有选择完成那么继续向上选择
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
						
						//多次选择可以考虑其形式
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
						//这里我们只需要随机去一辆车就可以
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
				//分配完成
				//需要将标识数组flag，还原成Cpdot.Asortrow
				//
				for(int i = 0;i<orla_structCp.Vnum;++i)
	            {   //flag的标号就是需要分配的车辆信息
	            	if(flag[i]==1)
	            	{
	                   Cpdot.realAsort[i] = 1;
	            	}
	            	else
	            	{
	            		Cpdot.realAsort[i] = 0;
	            	}
	            }
	            //这里实现了，将分配的A(i,j)中，实现了一次分配
	            Cpdot.orla_exback(k);
			}//第二种分配算法完成
		}//end if
	} //orla_alloAction
	  


	/*
	 * flag 需要分配资源的车辆的位置进行标识
	 * l1 Cp矩阵中有车辆的数量
	 * l2 Cpdot中车辆的数量
	 * X11 数据[0,l1/2]
	 * X12 C(i,j)数据[(l1+1)/2,l1]
	 * X21 Cpdot(i,j)数据(0,l2/2);
	 * X22 Cpdot(i,j)数据(l2/2,l2);
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
			//except the elements in set Y￡o
			//set Y = Y and {y}
			//set x(p,k) = X(p,k):[1,l1/2] and X(pdot,k):[1,l2/2];
			//if the binary search ends |X(p,k):[1,l1/2]| = 1,we set X(p,k) = X(p,k):[l1+1/2,l1];
			//标识数组进行初始化为0
			for(int i = 0;i<orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			num = number;
			//这里是上半部分优先于下半部分，实行的策略是上半部分选择完之后，继续后半部分选择
			//如果选择完成之后，没有选够，继续之前得策略
			while(num !=0)
			{
				low = 0;
				high = l2;
				middle = (int)((low+high)/2);
				while(low!=middle && num!=0)
				{
					Random rand = new Random();
					//取随机数据(middle-low)
					y = rand.nextInt(middle-low)+low;
					if(flag[y] == 0)
					{
						flag[y] = 1;
						num--;
					}
					//上半部分优先所以low = 0
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
				    	low = (int)(l2/2);//优先上半部分
				    	middle = (int)((low+middle)/2);
				    }	
				}	
			 }
			//二分上半部分优先执行完毕
		   }
		else if((((double)Math.abs(N1-N2)/Cp.Ynum)>orla_allocation.Theta)&&(N1<N2))
		{
		    //如果排序之后上半部分的数据少于下半部分，而且相差系数比较大的时候，
		    //我们采取的策略是优先下半部分
			/*初始化分配标识flag[]*/
			for(int i = 0;i<orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			/*初始化参数，需要选择车辆的数量*/
			num = number;
			
			while(num!=0)
			{
				//优先下半部分
				low = (int)(l2/2);
				high = l2;
				middle = (int)((low+high)/2);
			    while(low!=middle)
			    {
			    	Random rand = new Random();
			    	//在下半部分随机选择
			    	y = rand.nextInt(high - middle)+middle;
			    	if(flag[y] == 0)
			    	{
			    		flag[y] = 1;
			    		num --;
			    	}
			    	if(num ==0)  break;
			    	middle = (int)(middle+high)/2;
			    	high = l2;
			    }//下半部分优先完毕
			    
			    //下半部分选择完毕之后，如果没有选择完成那么继续向上选择
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
			//如果上下两个部分的，数据差不多，那么就上下两个部分随机选择
			for(int i=0;i<orla_structCp.Vnum;++i)
			{
			    flag[i] = 0;	
			}
			/*初始化数据车辆信息*/
			num = number;
			low = 0;
			high = l2;
			middle = (int)(low+high)/2;
			
			//多次选择可以考虑其形式
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
			//选取的车辆只有一辆，我们就对于整个存在的车辆中随机抽取一辆车
			/*初始化flag信息*/
			for(int i=0;i<=orla_structCp.Vnum;++i)
			{
				flag[i] = 0;
			}
			
			/* 初始化车辆信息，这里num = 1*/
			num = number;
			//这里我们只需要随机去一辆车就可以
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

