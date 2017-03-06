%load('../data/iterationnum.mat');
load('../orla_data/iterationtime.mat');

filename = '../orla_data/timenum.txt';
 fid = fopen(filename,'w');

   fprintf(fid,'平均时间%f\n', mean(iterationtime));
for k = 1:length(iterationtime)
     time = iterationtime(k);
     fprintf(fid,'第%d迭代，迭代次数:40次，迭代时间：%dms\n',k,time);
end
    
 fclose(fid);
 
 load('../orla_data/allRij.mat');
 filename = '../orla_data/Rij/Rij';
 for k = 1:length(allRij);
     X = allRij{k}; 
     [m,n] = size(X);
     file = strcat(filename,num2str(k+20));
     fid = fopen(strcat(file,'.txt'),'w');
     fprintf(fid,'第%d迭代:\n',k);
     for i = 1:m
         x1 = sum(X(i,:));
         if (x1 ~=0)
             fprintf(fid,'车号:%d  Rij的值:%f\n',i,x1);
         end
     end
      fclose(fid);
 end
