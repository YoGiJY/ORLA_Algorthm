
/*
 * @Author:Yao
 * @Email:yao.jiang@tongji.edu.cn
 * @Time:2016年07月17日 19:40:50
 * @Brief:sim compare
 */

package com.orla.alogrithm;
/*
 * public orla_sim(orla_structCp a,orla_structCp b);                 //构造函数
 * public void orla_simAction();                                     //对一个Cij矩阵进行相似度计算
 * public double orla_simsingle(int k)                               //对一个基站进行相似度计算
 * public double PearsonD(double [] a,double[]b);                    //计算pearson相关系数
 * public double KullbackD(double [] a,double[]b,double u1,double u2)//计算Kullback 相关系数
 */

public class orla_sim {
	
	/*
	 * @Author:
	 * @Brief:这个class orla_sim 对数据C(i,j)进行相似度计算
	 *        
	 */
   public static final int Bnum = 20;
   public static final int Vnum = 117;
   public static final double alpha = 0.5;
   public static final double beta = 0.5;
   public orla_structCp compC;
   public orla_structCp comdoP;
   public double [] similarity = new double[orla_sim.Bnum];
   
   public orla_sim()
   {
	   
   }
   
   public orla_sim(orla_structCp a,orla_structCp b)
   {
	   this.comdoP = a;
	   this.compC = b;
   }
   
   //更新信息数据信息，方便类复用
   public void orla_simpara(orla_structCp a,orla_structCp b)
   {
	   this.comdoP = a;
	   this.compC = b;
   }
   
   //这里是计算整个的矩阵的相似度计算
   public void orla_simAction()
   { 
	   for(int j = 0;j<Bnum;++j)
	   {
		   similarity[j] = this.orla_simsingle(j);
	   }
   }
   
   //计算单个的相似度计算
   public double orla_simsingle(int k)
   {
	   double []orla_c1row = new double[orla_sim.Bnum];
	   double []orla_c2row = new double[orla_sim.Bnum];
	   double pd = 0;
	   double kl = 0;
	   double sim = 0;

	   //这里对数据进行过滤,我们参加分配的数据是过滤之后的数据
	   compC.orla_mustex();
	   compC.orla_exSortEx(k);//过滤数据，获取第k个基站Cij信息，并且进行排序
	   //同理
	   comdoP.orla_mustex();
	   comdoP.orla_exSortEx(k);

	   orla_c1row = compC.realCsort;  //获取排序之后的数据
	   orla_c2row = comdoP.realCsort; //获取排序之后的数据

	   //计算pearson相关系数
	   pd = this.PearsonD(orla_c1row, orla_c2row,compC.Venum,comdoP.Venum);  //其数据范围是[-1,1]
	   //计算Kullback相关系数
	   kl = this.KullbackD(orla_c1row, orla_c2row, compC.U[k], comdoP.U[k],compC.Venum,comdoP.Venum); //其数据范围[0,1]
	   sim = orla_sim.alpha*pd - orla_sim.beta*kl;
       return sim;
   }
   
   //类方法实现pearson 距离相关系数
   public double PearsonD(double [] a,double[]b,int cnum,int dnum)
   {
	   double tempxy;
	   double tempsumx;
	   double tempsumy;
	   double py;
	   double px;
	   double aver;
	   double bver;
	   double sim;
	   double xyaver;
	   px = 0;
	   py = 0;
	   tempsumy = 0;
	   tempsumx = 0;
	   tempxy = 0;
	   
	   
	   for (int i = 0;i<cnum;++i)
	   {
		   px = Math.pow(a[i],2)+px;
		   tempsumx = tempsumx+a[i]; 
	   }
	   aver = tempsumx/cnum;
	   
	   for(int i=0;i<dnum;++i)
	   {
		   py = Math.pow(b[i], 2) + py;
		   tempsumy = tempsumy + b[i];
	   }
       bver = tempsumy/dnum;
       
	   for(int i= 0;i<cnum;++i)
	   {
		   for(int j = 0;j<dnum;++j)
		   {
			   tempxy = a[i]*b[j]+tempxy;
		   }
	   }
	   xyaver  = tempxy/(cnum*dnum);
	   /*皮尔逊距离计算公式协方差除以标准差*/
	   sim = (xyaver - aver*bver)/(Math.sqrt(px/cnum-Math.pow(aver,2))*Math.sqrt(py/dnum-Math.pow(bver,2)));
	   return sim;
   }
   
   //实现KullBack相关系数的实现
   public double KullbackD(double [] a,double[]b,double u1,double u2,int cnum,int dnum)
   {
	   double sim = 0;
	   double omegax = 0;
	   double omegay = 0;
	   double Q1 = 0;
	   double Q2 = 0;
	   for(int i = 0;i<cnum;++i)
	   {
		   omegax = omegax + a[i];
	   }
	   for(int i = 0;i<dnum;++i)
	   {
		   omegay = omegay + b[i];   
	   }
	   Q1 = omegay/omegax;
	   Q2 = u2/u1;
	   sim = Math.log(Q1/Q2)*Q1;
	   return sim;
   }
 
}//class

