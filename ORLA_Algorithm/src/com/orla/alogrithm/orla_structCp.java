/*
 * @Author:Yao
 * @Email:yao.jiang@tongji.edu.cn
 * @Time:2016年07月17日 20:34:55
 * @Brief:Struct of C(k,p)  其中有C(i,j),A(i,j),R(i,j)这三个重要参数
 * 这个类中有多种类方法，其中主要就是对某一列（也就是一个基站的车辆数据进行处理的） 
 */
package com.orla.alogrithm;

/*
 * public orla_structCp(); //构造函数初始化信息
 * public orla_structCp(double [][]C,int[][]A,double []U,double [][] Rij) ;//构造函数，赋值
 * public void orla_structAC(double [][]C); //将C(i,j)数组信息读入
 * public void orla_structAA(int [][]A);    //将A(i,j)数组信息读入
 * public void orla_struct2C(int v,int b,double cij); //对c(i,j）数组中的一个数进行操作
 * public void orla_struct2U(int j,double u);         //对u价格数组进行操作
 * public void orla_structSortR(int k);               //获取k基站的信息，并且进行排序
 * public void orla_structRow(int k)                  //获取k基站的信息，不进行排序
 * 上述我仅仅介绍了一部分，类方法的实现，下面在内方法的具体地方我会进行详细的介绍
 */

public class orla_structCp {
	 public final static int Vnum = 117;
	 public final static int Bnum = 20;
	 //Cij数组表示可达速率
     public double [][] Cij = new double[orla_structCp.Vnum][orla_structCp.Bnum];
     //Aij数组表示
     public int [][] Aij = new int [orla_structCp.Vnum][orla_structCp.Bnum];
     //价格数组U表示，其中每一个基站有一个价格
     public double []U = new double [orla_structCp.Bnum];
     //R(i,j),服务速率
     public double [][]Rij = new double [orla_structCp.Vnum][orla_structCp.Bnum];

     //排好序之后的，分配了基站的Cij和其具体的数量Ynum
     public double[] Y = new double[orla_structCp.Vnum];
     public int Ynum;
   
    //下面的real开头的数据是，我们在实际运算中需要的车辆信息
    //比如117辆车，可能参加迭代的车辆只有50,我们需要对这些信息过滤掉没有用的数据   
     public int [] realvsort = new int[orla_structCp.Vnum];
    //过滤之后的Cij
     public double[][] realCij = new double [orla_structCp.Vnum][orla_structCp.Bnum];
     public int [][] realAij = new int [orla_structCp.Vnum][orla_structCp.Bnum];
     public double []realCrow = new double [orla_structCp.Vnum];
     public int []realArow = new int [orla_structCp.Vnum];
     public double[] realCsort = new double [orla_structCp.Vnum];
     public int[] realAsort = new int [orla_structCp.Vnum];
     //过滤之后的参加运算的车辆数据
     public int Venum;
     
     //这些数据是对原始数据进行排序C(i,j),A(i,j)
     public double[] Csortrow = new double[orla_structCp.Vnum];
     public int[] Asortrow = new int[orla_structCp.Vnum];
     public double[] Crow = new double[orla_structCp.Vnum];
     public int[] Arow = new int [orla_structCp.Vnum];
     //记录之前的排序时候的原始位置
     /*因为我们在计算完毕之后我们必须回到原来的位置，而且计算完毕之后，我们是回到之前状态*/
     public int[] RealPosition = new int[orla_structCp.Vnum]; /* very important */
     
     //记录过滤时，没有过滤的元素，为了还原原来
     public int [] realv = new int [orla_structCp.Vnum];           /* very important */
     public int [] realvy = new int [orla_structCp.Vnum];
     
     
     //新建类对象，将其中的一些关键信息进行初始化
     public orla_structCp() 
     {
		// TODO Auto-generated constructor stub
    	 this.Venum = 0;
    	 for(int i = 0;i<orla_structCp.Vnum;++i)
    	 {
    		 for (int j = 0;j<orla_structCp.Bnum;++j)
    		 {
    			 Cij[i][j] = 0;
    			 Aij[i][j] = 0;
    			 Rij[i][j] = 0;
    		 }
    	 }
    	 
    	 for(int j = 0;j<orla_structCp.Bnum;++j)
    	 {
    		 U[j] = 1;
    	 }
	}//orla_structCp
     
     //构造函数,初始化一些数据,对于这个Class (Cij,Aij,Rij,U)四个数据
     public orla_structCp(double [][]C,int[][]A,double []U,double [][] Rij) 
     {
		// TODO Auto-generated constructor stub
    	 for(int i = 0;i<orla_structCp.Vnum;++i)
    	 {
    		 for (int j = 0;j<orla_structCp.Bnum;++j)
    		 {
    			 this.Cij[i][j] = C[i][j];
    			 this.Aij[i][j] = A[i][j];
    			 this.Rij[i][j] = Rij[i][j];
    		 }
    	 }
    	 
    	 for(int j = 0;j<orla_structCp.Bnum;++j)
    	 {
    		 this.U[j] = U[j];
    	 } 	 
	}//orla_structCp
    
     //更新类中的数据
    public void orla_structpara(double [][]C,int[][]A,double []U,double [][] Rij)
    {
    	 for(int i = 0;i<orla_structCp.Vnum;++i)
    	 {
    		 for (int j = 0;j<orla_structCp.Bnum;++j)
    		 {
    			 this.Cij[i][j] = C[i][j];
    			 this.Aij[i][j] = A[i][j];
    			 this.Rij[i][j] = Rij[i][j];
    		 }
    	 }
    	 
    	 for(int j = 0;j<orla_structCp.Bnum;++j)
    	 {
    		 this.U[j] = U[j];
    	 } 	 
    }
    
    //对数据Cij进行操作
    public void orla_structAC(double [][]C)
    {
    	for (int i=0;i<orla_structCp.Vnum;++i)
    	{
    		for (int j = 0;j<orla_structCp.Bnum;++j)
    		{
    			Cij[i][j] = C[i][j];
    		}
    	}
    }//orla_structAC
    
    
    //对所有的Aij进行操作
    public void orla_structAA(int [][]A)
    {
    	for(int i=0;i<orla_structCp.Vnum;++i)
    	{
    		for (int j=0;j<orla_structCp.Bnum;++j)
    		{
    			Aij[i][j] = A[i][j];
    		}
    	}
    }
    
    //对 v辆车 b基站的cij进行操作 
    public void orla_struct2C(int v,int b,double cij)
    {
			   Cij[v][b] = cij;
    }//orla_struct2C
    
    
    // v辆 b基站的Aij进行操作
    public void orla_struct2A(int v,int b,int aij)
    {
		   Aij[v][b] = aij;
    }//orla_struct2A
    
    //第j个基站的价格参数进行操作
    public void orla_struct2U(int j,double u)
    {
        this.U[j] = u;
    }//orla_struct2U

    //原始数据一列信息进行排序
    public void orla_structSortR(int k)
    {
        int Atemp;
        double Ctemp;
        /*调用类方法*/
        this.orla_structRow(k);
        this.Csortrow = this.Crow;
        this.Asortrow = this.Arow;
        
        for(int i= 0;i<orla_structCp.Vnum-1;++i)
        {
            for(int j = i+1;j<orla_structCp.Vnum;++j)
            {
                if(Csortrow[j]<Csortrow[i])
                {
                    Ctemp = Csortrow[i];
                    Csortrow[i] = Csortrow[j];
                    Csortrow[j] = Ctemp;
                    
                    Atemp = Asortrow[i];
                    Asortrow[i] = Asortrow[j];
                    Asortrow[j] = Atemp;
                }
            }
        }
    }//orla_structSortR

    //原始数据，获取原始数据的一列信息
    public void orla_structRow(int k)
    {
        for (int i = 0;i < orla_structCp.Vnum;++i)
        {
            this.Crow[i] = Cij[i][k];
            this.Arow[i] = Aij[i][k];
        }
    } //orla_structRow
    

    //分配策略的时候，我们需要过滤数据
    //orla_mustex实现的是过滤数据
    //并且保留了，没被过滤数据的原始地址realv[]
    public void orla_mustex()
    {
        //初始化realv数组,这里取值为-1是为了避免出现，0的情况的误判断
        for(int i = 0;i<orla_structCp.Vnum;++i)
        {
             this.realv[i] = -1;
        }
        this.Venum = 0;
        for(int i=0;i<orla_structCp.Vnum;++i)
        {
            if(Cij[i][0]!=0&&Cij[i][1]!=0&&Cij[i][2]!=0)
            {
                for(int j = 0;j<orla_structCp.Bnum;++j)
                {
                    realCij[Venum][j] = Cij[i][j];
                    realAij[Venum][j] = Aij[i][j];
                }
                //记录的是最原始的位置，其他的位置都为零
                this.realv[Venum] = i;
                this.Venum++;
            }
        }
    }//musttex
    
    //过滤数据，获取一列信息
    public void orla_exRowEx(int k)
    {
         for(int i = 0;i<this.Venum;++i)
         {
             this.realCrow[i] = realCij[i][k];
             this.realArow[i] = realAij[i][k];
         }
    }
    
    //过滤信息，对一列信息进行排序
    //其中RealPosition记录了排序之前的位置
    //为了后面的数据还原提供载体帮助
    public void orla_exSortEx(int k)
    {
        this.orla_exRowEx(k);
        this.realCsort = this.realCrow;
        this.realAsort = this.realArow;
        double Ctemp;
        int Atemp;
        int Rtemp;
        //我们假设标号是从0-117是正常的位置
        //初始化信息
        for(int i = 0;i<this.Venum;++i)
        {
            RealPosition[i] = i;
        }

        for(int i= 0;i<this.Venum-1;++i)
        {
            for(int j = i;j<this.Venum;++j)
            {
                if(realCsort[j]<realCsort[i])
                {
                    Ctemp = realCsort[i];
                    realCsort[i] = realCsort[j];
                    realCsort[j] = Ctemp;
                    
                    Atemp = realAsort[i];
                    realAsort[i] = realAsort[j];
                    realAsort[j] = Atemp;
                    //这里记录位置信息,存储变顺序的位置
                    //下次还原顺序是只需要对这个进行排序就可以了.
                    Rtemp = RealPosition[i];
                    RealPosition[i] = RealPosition[j];
                    RealPosition[j] = Rtemp;
                }
            }
        }
    }

    //过滤数据，v辆车 b基站进行操作
    public void orla_struct2RA(int v,int b,int aij)
    {
    	  this.realAij[v][b] = aij;
    }

    
    //过滤数据，排序之后，获取已经分配基站的cij的数据保存在Y中
    //realvy[]并且记录排序之后位置
    //数量Ynum
    public void orla_real2Y(int k)
    {
        this.Ynum = 0;
    	for (int j = 0;j<this.Venum;++j)
    	{
    		if(this.realAsort[j]==1)
    		{
    			this.Y[this.Ynum] = this.realCsort[j];
                //排序好了之后，被选中的车辆对应的历史位置相对于（after sort）
    			realvy[this.Ynum] = j;//
    			this.Ynum = this.Ynum+ 1;
    		}
    	}
    }
    
    //分配好第k列信息之后，我们将回到排序之前和过滤之前的数据信息
    public void orla_exback(int k)
    {
        //将经过“筛选”，“排序”，“分配” 的Aij进行还原
        //realAsort -->realArow -->Arow -->更新Aij
        //realPosition realv    缺省的都为0
        double Ctemp;
        int Atemp;
        int Rtemp;
        for(int i= 0;i<this.Venum-1;++i)
        {
            for(int j = i+1;j<this.Venum;++j)
            {
                if(RealPosition[j]<RealPosition[i])
                {
                    Ctemp = realCsort[i];
                    realCsort[i] = realCsort[j];
                    realCsort[j] = Ctemp;
                    
                    Atemp = realAsort[i];
                    realAsort[i] = realAsort[j];
                    realAsort[j] = Atemp;
                    //这里记录位置信息,存储变顺序的位置
                    //下次还原顺序是只需要对这个进行排序就可以了.
                    Rtemp = RealPosition[i];
                    RealPosition[i] = RealPosition[j];
                    RealPosition[j] = Rtemp;
                }
           }//for
        }
        //上面的冒泡算法排序将c(i,j)的顺序还原成原来的样子
        this.realArow = this.realAsort;
        this.realCrow = this.realCsort;
        //接下来就是还原成之前的this.Arow
        for(int i = 0;i<orla_structCp.Vnum;++i)
        {
            if(this.realv[i]!=-1)
            {
                Aij[this.realv[i]][k] = this.realArow[i];
            }
            else
            {
                Aij[i][k] = 0;;   
            }
        }//for（第k个基站的车辆分配结束并且完成）
   }//orla_back
    
    //计算完毕之后，我们需要，一辆车同时出现在两个基站中的情况进行处理
    //提供数据A(i,j),计算R(i,j),如果同时出现多个数据    
    public void orla_endRij()
    {
        int[] NumJ=new int[20];
        //计算每个基站的连接数量,更新A(i,j)
        for(int j=0;j<orla_structCp.Bnum;++j)
        {
            NumJ[j] = 0;
            for(int i=0;i<orla_structCp.Vnum;++i)
            {
                NumJ[j]=NumJ[j]+Aij[i][j];
            }
        }//求每个基站的车辆连接数
        
        //求出所有的Rij信息，这是存在一辆车辆，连在多个基站的R(i,j)
        for(int i = 0;i<orla_structCp.Vnum;++i)
        {
            //一辆一辆车进行判断，判断是否连上多个基站
            for(int j = 0;j<orla_structCp.Bnum;++j)
            {
                Rij[i][j] = Cij[i][j]/NumJ[j];
            }
        }   
        //这里分配基站的情况进行处理，得到最终的情况
        int [] tempR = new int [orla_structCp.Bnum];
        int ininum;
        int initemp;
        double maxRij;
        for(int i = 0;i<orla_structCp.Vnum;++i)
        {
            ininum = 0;
            for(int j=0;j<orla_structCp.Bnum;++j)
            {
                if(Aij[i][j] == 1)
                {
                   tempR[ininum] = j;  //记录这个基站信息
                   ininum ++;
                }
            }
            //上面获取了每一辆车连接各基站的情况
            //接下来就是分析和保留一个情况
            if(ininum > 1)
            {
                maxRij = Rij[i][tempR[0]];//获取Rij[i][j]的值
                initemp = 0;
                for(int k = 1; k<ininum; ++k)
                {
                    if(maxRij<Rij[i][tempR[k]])
                    {
                        maxRij = Rij[i][tempR[k]];
                        initemp = k;
                    }
                }
                //获取到最大的R(i,j)
                //然后将之后连接的基站重新分布
                for(int k = 0;k<ininum;++k)
                {
                    if(k != initemp)
                    {
                        Aij[i][tempR[k]] = 0;
                    }
                }
            }
        }//更新好得到最终,分配矩阵

        //求解最终的R(i,j)
        for(int j=0;j<orla_structCp.Bnum;++j)
        {
            NumJ[j] = 0;
            for(int i=0;i<orla_structCp.Vnum;++i)
            {
                NumJ[j]=NumJ[j]+Aij[i][j];
            }
        }//求每个基站的车辆连接数
        
        //求出所有的Rij信息，这是存在一辆车辆，连在多个基站的R(i,j)
        for(int i = 0;i<orla_structCp.Vnum;++i)
        {
            //一辆一辆车进行判断，判断是否连上多个基站
            for(int j = 0;j<orla_structCp.Bnum;++j)
            {
                Rij[i][j] = Cij[i][j]/NumJ[j];
            }
        } 
    }
}//class
