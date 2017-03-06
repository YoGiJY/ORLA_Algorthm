%load('../data/iterationnum.mat');
load('../orla_data/iterationtime.mat');

filename = '../orla_data/timenum.txt';
 fid = fopen(filename,'w');

   fprintf(fid,'ƽ��ʱ��%f\n', mean(iterationtime));
for k = 1:length(iterationtime)
     time = iterationtime(k);
     fprintf(fid,'��%d��������������:40�Σ�����ʱ�䣺%dms\n',k,time);
end
    
 fclose(fid);
 
 load('../orla_data/allRij.mat');
 filename = '../orla_data/Rij/Rij';
 for k = 1:length(allRij);
     X = allRij{k}; 
     [m,n] = size(X);
     file = strcat(filename,num2str(k+20));
     fid = fopen(strcat(file,'.txt'),'w');
     fprintf(fid,'��%d����:\n',k);
     for i = 1:m
         x1 = sum(X(i,:));
         if (x1 ~=0)
             fprintf(fid,'����:%d  Rij��ֵ:%f\n',i,x1);
         end
     end
      fclose(fid);
 end
