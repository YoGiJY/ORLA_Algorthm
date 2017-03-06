
/*
 * @Author:Yao
 * @Email:yao.jiang@tongji.edu.cn
 * @Time:2016年07月17日 19:35:55
 * @Brief:Algorithm of ORLA
 */

package com.orla.alogrithm;

import getdata;

public class orla_algorithm {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num = 1;
		int numdot = 6;
        getdata data = new getdata();    //获取历史数据信息
        getdata datadot = new getdata(); //需要分配的数据信息
       
        
        /**************************************************/
        data.getData(num);
        orla_structCp orla_data = new orla_structCp(data.C,data.X,data.U,data.R);
        
        /*************************************************/
        datadot.UpdateC(numdot);
        orla_structCp orla_datadot = new orla_structCp();
        orla_datadot.orla_structAC(datadot.C);       
        /************************************************/
        for(int i = 0;i<20;++i)
        {
        	System.out.println(orla_data.Cij[26][i]);
        }
        System.out.println("---------");
        for(int i = 0;i<20;++i)
        {
        	System.out.println(data.C[26][i]);
        }
        /***********************************************/
        orla_data.orla_mustex();
        orla_datadot.orla_mustex();
        
        orla_data.orla_exSortEx(2);
        orla_datadot.orla_exSortEx(2);
        
        for(int i=0;i<orla_data.Venum;++i)
        {
        	System.out.println(orla_data.realCsort[i]);
        }
        System.out.println("---------");
        for(int i=0;i<orla_datadot.Venum;++i)
        {
        	System.out.println(orla_datadot.realCsort[i]);
        }
       /***********************************************/
       //测试相似度的计算的函数是正确的
        orla_sim smiliar = new orla_sim(orla_datadot,orla_data);
        
        double x = smiliar.orla_simsingle(1);
        System.out.println(x);
       /*********************************************/
        
       //测试分配策略的正确的性
        orla_allocation orla_alloc = new orla_allocation(orla_datadot,orla_data);
        orla_alloc.orla_alloAction(1);
	}
   
}
