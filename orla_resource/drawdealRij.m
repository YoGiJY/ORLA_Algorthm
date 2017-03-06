%% 通过ORLA 算法来计算得到R(i,j)的值
%这里通过每次收敛之后得到的R(i,j)的值
%导入R(i,j)文件
RijExist = exist('../orla_data/dataRij.mat');
if(RijExist)
    load('../orla_data/dataRij.mat');
    load('../orla_data/filenametest.mat');
else
    dirstring = '../orla_dataRij/';        %获取文件的目录
    filename = dir('../orla_dataRij/'); %得到文件目录下的所有文件
    filenum = size(filename);   %获取当前目录下的文件大小
    dataRij={};                           %使用cell元包 
    k = 1;
   filenametest={};
     for i = 1:1:filenum-2
          datafile = strcat(dirstring,strcat('Rij',num2str(i+20),'.txt'));
          data =  importfileRij(datafile);
          filenametest{k} =datafile;
          dataRij{k} = data;
           k = k+1;
     end
     save ../orla_data/dataRij.mat dataRij;
    save ../orla_data/filenametest.mat filenametest;
end




%% 这里我需要计算各个车辆的R(i,j)
%这里的理解就是，计算一辆车，对应而是个基站的累积曲线图
%在读取的R(i,j)数据中其中前面两个数据是分别是表示迭代次数和迭代收敛的时间
%即 iterationnum 和 iterationtime
%其他数据分别是R(i,j)数据117*20


%load('../orla_data/orla_distance_cell.mat');%导入之前参加计算的距离数据统计
 load('../data/newdistance_cell.mat');%导入之前参加计算的距离数据统计
AllBsplace = [];
sumRij = [];
allRij = {};
Rijtest = {};
iterationtime = [];
iterationnum = [];
for k = 1:length(dataRij)
     data = dataRij{k};
     Rij = [];
     %iterationnum(k) = data(1);
     iterationtime(k) = data(1);
     Bsplace = data(2:21);
%       Rij = data(23:2362);
  
      Rij = reshape(data(22:2361),20,117);
     %这里进行转置，实现117*20这样的矩阵其中i表示车辆，j表示基站(Base Station)
      Rij = Rij';
      allRij{k} = Rij;
%      Rij = data(22:2361);
     distance = newdistance_cell{k+20};
      index_row  = (distance(:,1)>=0);
     TRij = Rij(index_row,:);
     [m,n] = size(TRij);
     Rijtest{k} = TRij;
      %将R(i,j)矩阵变成向量
     TRijx = TRij(:);
     %将没有连接的基站或者说是NaN的值去除
    % index_x = ~isnan(TRijx(:,1));
%      index_x = ~(TRijx(:,1)==0);
%      TRijxy = TRijx(index_x,:);
%      hold off;
%      cdfplotex(TRijxy);
%      figname = strcat('../orla_image/Rij/','Rij',num2str(k+20),'.fig');
%      saveas(gcf,figname);
     
     AllBsplace = [AllBsplace;Bsplace'];
     sumRij(k) = sum(TRijx);
end
%% 计算所有车辆的R(i,j)相加得到的数值，再画累积曲线图
     %这里我的理解是就是对于一个基站而言各个车辆的R(i,j)相加，然后化累积曲线图
     % hold off;
      save ../orla_data/new_rij.mat sumRij;
      x = cdfplot(sumRij);
      figname = strcat('../orla_image/Rij/','AllRij.fig');
      saveas(gcf,figname);
      
      hold on;
      sumRijold = load('../data/rij_old');
      y =cdfplot(sumRijold.sumRij);
      set(x,'color','r');
      figname = strcat('../orla_image/Rij/','All2Rij.fig');
      saveas(gcf,figname);
%上述画出所需要的累积曲线图
save  ../orla_data/iterationnum.mat iterationnum;   % 保存所有运行迭代的次数
save ../orla_data/iterationtime.mat iterationtime;   % 保存所有运行时间
save ../orla_data/AllBsplace.mat AllBsplace;           %保存所有基站的分布情况
save ../orla_data/iterationtime.mat iterationtime;   %保存迭代收敛所需要的时间
save ../orla_data/iterationnum.mat iterationnum;    %保存迭代收敛所需要的次数
save ../orla_data/Rijtest.mat Rijtest;
save ../orla_data/allRij.mat allRij;

%% 统计各个类型基站上连接的车辆数
existbase = exist('../orla_data/AllBsplace.mat');
if(existbase)
    load('../orla_data/AllBsplace.mat');
else
    fprintf('请计算所有基站的分布情况!\n');
end
%将基站分布的情况保存在数据文件中，其路径是../data/AllBsplace.txt
filename = '../orla_data/AllBsplace.txt';
 fid = fopen(filename,'w');
for k = 1:length(AllBsplace)
     xxyy = AllBsplace(k,:);
     x1 = sum(xxyy(1:5));
     x2 = sum(xxyy(6:10));
     x3 = sum(xxyy(11:20));
     fprintf(fid,'第%d迭代，有%d辆车连在最大强度基站上，%d辆连在中等强度基站，%d辆连小基站\n',k,x1,x2,x3);
end
 fclose(fid);


