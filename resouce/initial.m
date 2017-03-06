% ����ļ��Ĵ����Ƕ����������Ϻ��У�ÿһʱ���ڵĳ��������ݴ���
%��վ�ķֲ��ǲ��õ�������λ�ã���R1Ϊ�뾶������ֲ������Ļ�վ��
%��R2Ϊ�뾶����ֲ��ڰ뾶����R1��С��R2��������
%% �����ݴ��ļ��е��뵽����Vehicle
dirstring = './final_data/final_data/';
datafilename = dir('./final_data/final_data/');
datafilenum = size(datafilename);
Vehicle={};
k = 1;
 for i = 1:1:117
      datafile = strcat(dirstring,datafilename(i+2).name);
      vehicle =  importfile(datafile);
      Vehicle{k} = vehicle;
       k = k+1;
 end
save matlab.mat Vehicle;

%% �õ���γ�����꣬ͨ������ͶӰ���������ķֲ�ͼ
%��ȡ�����ľ�γ�����꣬LatiLong
% load('matlab.mat');
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));


%% �õ���γ�����ݣ��滮���ݣ�ͬһʱ�䣬��γ����Ϣ
%����ʹ�õĲ�����T = 30���ӣ�delta = 30/(60*24) ;
% load('speedindex.mat');
delta = 30/(60*24);                                      %����ȡ30����Ϊʱ���
begt = datenum('2015-04-01 00:00:00');%����ȡ��ʼʱ����2014-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%�ж����ݵĽ���ʱ��
speedindex = {};
begint = begt;
tx = begt;
k = 1;
while(tx < endt)
    tx = begint;
    ty = tx+delta;
    index = [];
    for i = 1:1:117
        x = Vehicle{i};
        row_index = x(:,2) >=tx&x(:,2)<ty;
        indextemp =x(row_index,:);
        tempmean = mean(indextemp,1);
       tempmean(2) = tx;
       index = [index;tempmean];
    end
    begint = ty;
    speedindex{k} = index;
    k = k+1;
end

save speedindex.mat speedindex;


%% ����40����վ�ķֲ�����
%������ʹ�õĲ����Ǿ�������������ҽ�3ǧ������ݣ��ֳ�40���࣬����ʵ�ַ��࣬Ȼ���ǵ�����ķֲ�Ϊ���վ���еȵ�ΪС��վ�����ΪС��վ
%�����Ҿ�����ͨ������������ֲ���վ��
%��վ�ķֲ��ǿ��ǣ�����ֲ�
%��ȡ�Ĳ��������������ֲ�,���ľ�����ֲ�20
basepoint=[];
lax = [LatiLong(:,3),LatiLong(:,4)];
mean_xy = mean(lax);

alpha = 0:0.1:2*pi;
R1 = 0.22;
 x=R1*cos(alpha)+mean_xy(1,1); 
 y=R1*sin(alpha)+mean_xy(1,2);
 hold on;
 
 plot(mean_xy(1,1),mean_xy(1,2),'r*');
 plot(x,y,'r');

 R2 = 0.50;
 x=R2*cos(alpha)+mean_xy(1,1); 
 y=R2*sin(alpha)+mean_xy(1,2);
 hold on;
 plot(x,y,'r');
  
 %% ��Բ�����ȡ�㣬�������������Ծ��ǣ�1������������ֲ�30����վ
a=zeros(28,2);
i=1;
while i<=28
    temp1=(-1+(1-(-1))*rand(1))*R1+mean_xy(1,1);
    temp2=(-1+(1-(-1))*rand(1))*R1+mean_xy(1,2);
    if (temp1-mean_xy(1,1))^2+(temp2-mean_xy(1,2))^2<R1^2
        a(i,1)=temp1;
        a(i,2)=temp2;
        i=i+1;
    end
end
hold on 
plot(a(:,1),a(:,2),'r*');
basepoint = [basepoint;a];

%%  2���⻷����Ʒֲ�10����վ
b=zeros(12,2);
i=1;
while i<=12
    temp1=(-1+(1-(-1))*rand(1))*R2+mean_xy(1,1);
    temp2=(-1+(1-(-1))*rand(1))*R2+mean_xy(1,2);
    if ((temp1-mean_xy(1,1))^2+(temp2-mean_xy(1,2))^2>R1^2)&&((temp1-mean_xy(1,1))^2+(temp2-mean_xy(1,2))^2<R2^2)
        b(i,1)=temp1;
        b(i,2)=temp2;
        i=i+1;
    end
end
plot(b(:,1),b(:,2),'r*');
basepoint = [basepoint;b];


%% ���õ���վ����������ֲ�һ�´��վ��10�����л�վ��10��С��վ��20��
%ͨ�����������������֮��ǰ��10���Ǵ��վ������10�����л�վ�����20��ΪС��վ
alphabet=[1 2 3];
prob = [0.25 0.25 0.50];
k = randsrc(40,1,[alphabet; prob]);
basepoint = [basepoint,k];
basepoint = sortrows(basepoint,3);

%�õ���վ����������
basepoint(:,3) = [];
save basepoint.mat basepoint ;


%% ������֪��ͬһʱ�̵�����ĳ�������ͻ�վ�����꣨������������ָ��γ�ȣ�
globalr = 6378.1; % ���������ǵ���İ뾶����
distance_cell = {};
for k = 1:length(speedindex)
    matrix = speedindex{k};
    distanceij = [];
    filehead = './distance/java';
    filehou = '.txt';
    filename = strcat(filehead,num2str(k));
    filename = strcat(filename,filehou);
    fid = fopen(filename,'w');
    for i = 1:1:117
        for j = 1:1:40
            lx = matrix(i,3);
            ly = matrix(i,4);
            bx = basepoint(j,1);
            by = basepoint(j,2);
           distanceij(i,j) = distance(ly,lx,by,bx,6378.1);
        end
        fprintf(fid,'%f\n',distanceij(i,:));
    end
    fclose(fid);
    distance_cell{k} =distanceij; 
end
save distance_cell.mat distance_cell;


