% 这个文件的处理是对于整个的上海市，每一时间内的车辆的数据处理
%基站的分布是采用的以中心位置，以R1为半径，随机分布大量的基站，
%以R2为半径随机分布在半径大于R1而小于R2的区域内
%% 将数据从文件中导入到向量Vehicle
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

%% 得到经纬度坐标，通过坐标投影画出车辆的分布图
%获取车辆的经纬度坐标，LatiLong
% load('matlab.mat');
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));


%% 得到经纬度数据，规划数据，同一时间，经纬度信息
%这里使用的策略是T = 30分钟，delta = 30/(60*24) ;
% load('speedindex.mat');
delta = 30/(60*24);                                      %这里取30分钟为时间戳
begt = datenum('2015-04-01 00:00:00');%这里取开始时间是2014-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%判断数据的结束时间
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


%% 考虑40个基站的分布策略
%这里我使用的策略是聚类分析，就是我讲3千万个数据，分成40个类，这样实现分类，然后考虑点数多的分布为大基站，中等的为小基站，其次为小基站
%这里我觉得是通过覆盖面积来分布基站的
%基站的分布是考虑，随机分布
%采取的策略是以中心来分布,中心距离多大分布20
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
  
 %% 在圆内随机取点，这里有两个策略就是，1，在市区随机分布30个基站
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

%%  2在外环区算计分布10个基站
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


%% 当得到基站数据在随机分布一下大基站（10），中基站（10）小基站（20）
%通过随机数产生，排序之后，前面10个是大基站，后面10个是中基站，最后20个为小基站
alphabet=[1 2 3];
prob = [0.25 0.25 0.50];
k = randsrc(40,1,[alphabet; prob]);
basepoint = [basepoint,k];
basepoint = sortrows(basepoint,3);

%得到基站的坐标数据
basepoint(:,3) = [];
save basepoint.mat basepoint ;


%% 根据已知的同一时刻的坐标的车辆坐标和基站的坐标（这里的坐标就是指经纬度）
globalr = 6378.1; % 这里计算的是地球的半径长度
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


