
/*
 * @Author:Yao
 * @Email:yao.jiang@tongji.edu.cn
 * @Time:2016��07��17�� 19:40:50
 * @Brief:sim compare
 */

package com.orla.alogrithm;
/*
 * public orla_sim(orla_structCp a,orla_structCp b);                 //���캯��
 * public void orla_simAction();                                     //��һ��Cij����������ƶȼ���
 * public double orla_simsingle(int k)                               //��һ����վ�������ƶȼ���
 * public double PearsonD(double [] a,double[]b);                    //����pearson���ϵ��
 * public double KullbackD(double [] a,double[]b,double u1,double u2)//����Kullback ���ϵ��
 */

public class orla_sim {
	
	/*
	 * @Author:
	 * @Brief:���class orla_sim ������C(i,j)�������ƶȼ���
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
   
   //������Ϣ������Ϣ�������ิ��
   public void orla_simpara(orla_structCp a,orla_structCp b)
   {
	   this.comdoP = a;
	   this.compC = b;
   }
   
   //�����Ǽ��������ľ�������ƶȼ���
   public void orla_simAction()
   { 
	   for(int j = 0;j<Bnum;++j)
	   {
		   similarity[j] = this.orla_simsingle(j);
	   }
   }
   
   //���㵥�������ƶȼ���
   public double orla_simsingle(int k)
   {
	   double []orla_c1row = new double[orla_sim.Bnum];
	   double []orla_c2row = new double[orla_sim.Bnum];
	   double pd = 0;
	   double kl = 0;
	   double sim = 0;

	   //��������ݽ��й���,���ǲμӷ���������ǹ���֮�������
	   compC.orla_mustex();
	   compC.orla_exSortEx(k);//�������ݣ���ȡ��k����վCij��Ϣ�����ҽ�������
	   //ͬ��
	   comdoP.orla_mustex();
	   comdoP.orla_exSortEx(k);

	   orla_c1row = compC.realCsort;  //��ȡ����֮�������
	   orla_c2row = comdoP.realCsort; //��ȡ����֮�������

	   //����pearson���ϵ��
	   pd = this.PearsonD(orla_c1row, orla_c2row,compC.Venum,comdoP.Venum);  //�����ݷ�Χ��[-1,1]
	   //����Kullback���ϵ��
	   kl = this.KullbackD(orla_c1row, orla_c2row, compC.U[k], comdoP.U[k],compC.Venum,comdoP.Venum); //�����ݷ�Χ[0,1]
	   sim = orla_sim.alpha*pd - orla_sim.beta*kl;
       return sim;
   }
   
   //�෽��ʵ��pearson �������ϵ��
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
	   /*Ƥ��ѷ������㹫ʽЭ������Ա�׼��*/
	   sim = (xyaver - aver*bver)/(Math.sqrt(px/cnum-Math.pow(aver,2))*Math.sqrt(py/dnum-Math.pow(bver,2)));
	   return sim;
   }
   
   //ʵ��KullBack���ϵ����ʵ��
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

