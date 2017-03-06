%% ͨ��The Distributed Algorithm����õ�R(i,j)��ֵ
%����ͨ��ÿ������֮��õ���R(i,j)��ֵ
%����R(i,j)�ļ�
RijExist = exist('../data/dataRij.mat');
if(RijExist)
    load('../data/dataRij.mat');
    load('../data/filenametest.mat');
else
    dirstring = '../dataRij/';        %��ȡ�ļ���Ŀ¼
    filename = dir('../dataRij/'); %�õ��ļ�Ŀ¼�µ������ļ�
    filenum = size(filename);   %��ȡ��ǰĿ¼�µ��ļ���С
    dataRij={};                           %ʹ��cellԪ�� 
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




%% ��������Ҫ�������������R(i,j)
%�����������ǣ�����һ��������Ӧ���Ǹ���վ���ۻ�����ͼ
%�ڶ�ȡ��R(i,j)����������ǰ�����������Ƿֱ��Ǳ�ʾ���������͵���������ʱ��
%�� iterationnum �� iterationtime
%�������ݷֱ���R(i,j)����117*20
load('../data/newdistance_cell.mat');%����֮ǰ�μӼ���ľ�������ͳ��
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
     %�������ת�ã�ʵ��117*20�����ľ�������i��ʾ������j��ʾ��վ(Base Station)
     Rij = Rij';                               
     %ͨ������ĲμӾ������ݣ��ҿ��Եõ���Щ�����μӵ���
     distance = newdistance_cell{k};
     index_row  = ~(distance(:,1)==-1);
     TRij = Rij(index_row,:);
      [m,n] = size(TRij);
      %��R(i,j)����������
     TRijx = TRij(:);
     %��û�����ӵĻ�վ����˵��NaN��ֵȥ��
     index_x = ~isnan(TRijx(:,1));
     TRijxy = TRijx(index_x,:);
     hold off;
     cdfplotex(TRijxy);
     figname = strcat('../image/Rij/','Rij',num2str(k),'.fig');
     saveas(gcf,figname);
     
     AllBsplace{k} = Bsplace;
     sumRij(k) = sum(TRijxy);
end
%% �������г�����R(i,j)��ӵõ�����ֵ���ٻ��ۻ�����ͼ
     %�����ҵ������Ǿ��Ƕ���һ����վ���Ը���������R(i,j)��ӣ�Ȼ���ۻ�����ͼ
      hold off;
      cdfplotex(sumRij);
      figname = strcat('../image/Rij/','AllRij.fig');
      saveas(gcf,figname);
%������������Ҫ���ۻ�����ͼ
save ../data/AllBsplace.mat AllBsplace;      %�������л�վ�ķֲ����
save ../data/iterationtime.mat iterationtime;%���������������Ҫ��ʱ��
save ../data/iterationnum.mat iterationnum;  %���������������Ҫ�Ĵ���



