%% 通过The Distributed Algorithm计算得到R(i,j)的值
%这里通过每次收敛之后得到的R(i,j)的值
%导入R(i,j)文件
RijExist = exist('../data/dataRij.mat');
if(RijExist)
    load('../data/dataRij.mat');
    load('../data/filenametest.mat');
else
    dirstring = '../dataRij/';        %获取文件的目录
    filename = dir('../dataRij/'); %得到文件目录下的所有文件
    filenum = size(filename);   %获取当前目录下的文件大小
    dataRij={};                           %使用cell元包 
    k = 1;
   filenametest={};
     for i = 1:1:filenum-2
          datafile = strcat(dirstring,strcat('Rij',num2str(i),'.txt'));
          data =  importfileRij(datafile);
          filenametest{k} =datafile;
          dataRij{k} = data;
           k = k+1;
     end
    save ../data/dataRij.mat dataRij;
    save ../data/filenametest.mat filenametest;
end




%% 这里我需要计算各个车辆的R(i,j)
%这里的理解就是，计算一辆车，对应而是个基站的累积曲线图
%在读取的R(i,j)数据中其中前面两个数据是分别是表示迭代次数和迭代收敛的时间
%即 iterationnum 和 iterationtime
%其他数据分别是R(i,j)数据117*20
load('../data/newdistance_cell.mat');%导入之前参加计算的距离数据统计
AllBsplace = {};
sumRij = [];
iterationtime = [];
iterationnum = [];
for k = 1:length(newdistance_cell)
     data = dataRij{k};
     iterationnum(k) = data(1);
     iterationtime(k) = data(2);
     Bsplace = data(3:22);
 %     Rij = data(23:2362);
     Rij = reshape(data(23:2362),20,117);
     %这里进行转置，实现117*20这样的矩阵其中i表示车辆，j表示基站(Base Station)
     Rij = Rij';                               
     %通过导入的参加距离数据，我可以得到哪些车辆参加迭代
     distance = newdistance_cell{k};
     index_row  = ~(distance(:,1)==-1);
     TRij = Rij(index_row,:);
      [m,n] = size(TRij);
      %将R(i,j)矩阵变成向量
     TRijx = TRij(:);
     %将没有连接的基站或者说是NaN的值去除
     index_x = ~isnan(TRijx(:,1));
     TRijxy = TRijx(index_x,:);
     hold off;
     cdfplotex(TRijxy);
     figname = strcat('../image/Rij/','Rij',num2str(k),'.fig');
     saveas(gcf,figname);
     
     AllBsplace{k} = Bsplace;
     sumRij(k) = sum(TRijxy);
end
%% 计算所有车辆的R(i,j)相加得到的数值，再画累积曲线图
     %这里我的理解是就是对于一个基站而言各个车辆的R(i,j)相加，然后化累积曲线图
      hold off;
      cdfplotex(sumRij);
      figname = strcat('../image/Rij/','AllRij.fig');
      saveas(gcf,figname);
%上述画出所需要的累积曲线图
save ../data/AllBsplace.mat AllBsplace;      %保存所有基站的分布情况
save ../data/iterationtime.mat iterationtime;%保存迭代收敛所需要的时间
save ../data/iterationnum.mat iterationnum;  %保存迭代收敛所需要的次数




