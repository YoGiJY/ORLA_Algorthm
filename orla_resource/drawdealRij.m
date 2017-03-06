%% ͨ��ORLA �㷨������õ�R(i,j)��ֵ
%����ͨ��ÿ������֮��õ���R(i,j)��ֵ
%����R(i,j)�ļ�
RijExist = exist('../orla_data/dataRij.mat');
if(RijExist)
    load('../orla_data/dataRij.mat');
    load('../orla_data/filenametest.mat');
else
    dirstring = '../orla_dataRij/';        %��ȡ�ļ���Ŀ¼
    filename = dir('../orla_dataRij/'); %�õ��ļ�Ŀ¼�µ������ļ�
    filenum = size(filename);   %��ȡ��ǰĿ¼�µ��ļ���С
    dataRij={};                           %ʹ��cellԪ�� 
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




%% ��������Ҫ�������������R(i,j)
%����������ǣ�����һ��������Ӧ���Ǹ���վ���ۻ�����ͼ
%�ڶ�ȡ��R(i,j)����������ǰ�����������Ƿֱ��Ǳ�ʾ���������͵���������ʱ��
%�� iterationnum �� iterationtime
%�������ݷֱ���R(i,j)����117*20


%load('../orla_data/orla_distance_cell.mat');%����֮ǰ�μӼ���ľ�������ͳ��
 load('../data/newdistance_cell.mat');%����֮ǰ�μӼ���ľ�������ͳ��
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
     %�������ת�ã�ʵ��117*20�����ľ�������i��ʾ������j��ʾ��վ(Base Station)
      Rij = Rij';
      allRij{k} = Rij;
%      Rij = data(22:2361);
     distance = newdistance_cell{k+20};
      index_row  = (distance(:,1)>=0);
     TRij = Rij(index_row,:);
     [m,n] = size(TRij);
     Rijtest{k} = TRij;
      %��R(i,j)����������
     TRijx = TRij(:);
     %��û�����ӵĻ�վ����˵��NaN��ֵȥ��
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
%% �������г�����R(i,j)��ӵõ�����ֵ���ٻ��ۻ�����ͼ
     %�����ҵ�����Ǿ��Ƕ���һ����վ���Ը���������R(i,j)��ӣ�Ȼ���ۻ�����ͼ
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
%������������Ҫ���ۻ�����ͼ
save  ../orla_data/iterationnum.mat iterationnum;   % �����������е����Ĵ���
save ../orla_data/iterationtime.mat iterationtime;   % ������������ʱ��
save ../orla_data/AllBsplace.mat AllBsplace;           %�������л�վ�ķֲ����
save ../orla_data/iterationtime.mat iterationtime;   %���������������Ҫ��ʱ��
save ../orla_data/iterationnum.mat iterationnum;    %���������������Ҫ�Ĵ���
save ../orla_data/Rijtest.mat Rijtest;
save ../orla_data/allRij.mat allRij;

%% ͳ�Ƹ������ͻ�վ�����ӵĳ�����
existbase = exist('../orla_data/AllBsplace.mat');
if(existbase)
    load('../orla_data/AllBsplace.mat');
else
    fprintf('��������л�վ�ķֲ����!\n');
end
%����վ�ֲ�����������������ļ��У���·����../data/AllBsplace.txt
filename = '../orla_data/AllBsplace.txt';
 fid = fopen(filename,'w');
for k = 1:length(AllBsplace)
     xxyy = AllBsplace(k,:);
     x1 = sum(xxyy(1:5));
     x2 = sum(xxyy(6:10));
     x3 = sum(xxyy(11:20));
     fprintf(fid,'��%d��������%d�����������ǿ�Ȼ�վ�ϣ�%d�������е�ǿ�Ȼ�վ��%d����С��վ\n',k,x1,x2,x3);
end
 fclose(fid);


