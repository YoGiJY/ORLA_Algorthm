% 这段代码实现的是获取，一段区域内的信息位置然后得到在时间T上所有车辆到基站的距离
%这次代码获取每个时间T内的车辆数平均是47辆左右，最小时车辆是15，最大时车辆是75
%我选取区域的最大长度是对边长度，计算的结果是2.5*10^4（m），如果计算的数据存在比这个大的话，就是有问题。
%我每次计算之后都会保存数据，因为每次计算都需要大量的时间，这单单是数据处理部分所花费的时间
%在代码实现过程中我选择的区域是手动添加坐标范围，随机产生BS基站坐标来实现的。

%% 将数据从文件中导入到向量Vehicle
%判断数据文件是否存在，如果存在直接load数据，无需再次从文件中读入数据
exitmat = exist('../data/matlab.mat');
 if(exitmat)
     load('../data/matlab.mat');
 else
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
      save ../data/matlab.mat Vehicle;
 end

%% 得到经纬度坐标，通过坐标投影画出车辆的分布图
%获取车辆的经纬度坐标，LatiLong

figure;
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));
grid on;

%% 取基站（BS）的第二个策略，选取上海城区，密集的地方选择然后获取一段面积，在这段面积中考虑分布20个基站
% 这样获取的数据，我需要选择一下区域，然后过滤掉不属于这个区域的经纬度信息
figure;
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));
grid off;
hold on;
% 选取区域，这里选取的区域为[31.15 31.30] [121.25 121.45]
basenum = 20;
ymax = 31.30;
ymin = 31.15;
xmax = 121.45;
xmin= 121.25;
y = [31.15 31.30 31.30 31.15 ];
x = [121.25 121.25 121.45 121.45];
X = [x,x(1)];
Y = [y,y(1)];
plot(X,Y,'r:');

% 在这个矩形区域内进行随机取点
hold on;
basepoint = [];
xbasepoint=rand(basenum,1)*(xmax-xmin)+xmin;
ybasepoint=rand(basenum,1)*(ymax-ymin)+ymin;
basepoint = [xbasepoint,ybasepoint];
% 获取随机的基站分布，现在对基站进行随机分配大，中，小三个等级
% 其中分配基站的概率是[0.25 0.25 0.5]
%这里在随机产生基站之后，我再次随机产生三个数据，来随机标识基站的
%大，中，小三个数据，这里在最终的结果之后我
alphabet=[1 2 3];
prob = [0.25 0.25 0.50];
k = randsrc(basenum,1,[alphabet; prob]);
basepoint = [basepoint,k];
basepoint = sortrows(basepoint,3);

%得到基站的坐标数据
basepoint(:,3) = [];
xb = basepoint(:,1);
yb = basepoint(:,2);
plot(xb(1:5),yb(1:5),'r*');          %大基站，红色
plot(xb(6:10),yb(6:10),'y*');     %中基站，黄色
plot(xb(11:20),yb(11:20),'k*');%小基站，黑色
save ../data/basepoint.mat basepoint ;


%% 得到经纬度数据，规划数据，同一时间，经纬度信息,
%1.这里使用的策略是T = 30分钟，delta = 30/(60*24) ;
%2.这里我需要对数据进行处理将不再这个区域的数据剔除

newVehicle = {};
dataexist = exist('../data/newVehicle.mat');
if(dataexist)
    load('../data/newVehicle.mat');
else
    for i = 1:1:117
         x = Vehicle{i};
         a = ((x(:,3)>=xmin)&(x(:,3)<=xmax))&((x(:,4)>=ymin)&(x(:,4)<=ymax));
         b =x(a,:);
        newVehicle{i} =b; 
    end
    save ../data/newVehicle.mat newVehicle;
end

%% 得到经纬度数据，规划数据，同一时间，经纬度信息
%这里使用的策略是T = 30分钟，delta = 30/(60*24) ;

%判断数据汽车T时刻的速度情况文件是否存在
speedexist = exist('../data/newspeedindex.mat');

delta = 30/(60*24);                                      %这里取30分钟为时间戳
begt = datenum('2015-04-01 00:00:00');%这里取开始时间是2014-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%判断数据的结束时间
newspeedindex = {};

if(speedexist)
    load('newspeedindex.mat');
else
        begint = begt;
        tx = begt;
        k = 1;
        while(tx < endt)
            tx = begint;
            ty = tx+delta;
            index = [];
            for i = 1:1:117
                x = newVehicle{i};
                row_index = x(:,2) >=tx&x(:,2)<ty;
                indextemp =x(row_index,:);
                tempmean = mean(indextemp,1);
               tempmean(2) = tx;
               index = [index;tempmean];
            end
            begint = ty;
            newspeedindex{k} = index;
            k = k+1;
        end
        save ../data/newspeedindex.mat newspeedindex;
end



%% 这里我统计一下，在选取的这个区域内的车辆的数量，每个时间点车辆的数量
% 判断一下选取的区域是否合理

for i = 1:length(newspeedindex)
    ax = newspeedindex{i};
    axindex = ~isnan(ax(:,1));
    veindex(i) = sum(axindex);
end
averindex = mean(veindex);
maxindex = max(veindex);
minindex = min(veindex);


%% 根据已知的同一时刻的坐标的车辆坐标和基站的坐标（这里的坐标就是指经纬度）
globalr = 6378.1; % 这里计算的是地球的半径长度
newdistance_cell = {};
for k = 1:length(newspeedindex)
    matrix = newspeedindex{k};
    distanceij = [];
    filehead = './distance/java';
    filehou = '.txt';
    filename = strcat(filehead,num2str(k));
    filename = strcat(filename,filehou);
    fid = fopen(filename,'w');
    for i = 1:1:117
        for j = 1:1:20
            lx = matrix(i,3);
            ly = matrix(i,4);
            bx = basepoint(j,1);
            by = basepoint(j,2);
           distanceij(i,j) = distance(ly,lx,by,bx,globalr)*1000;
           if isnan(distanceij(i,j) )
                distanceij(i,j) = -1;
           end
        end
        fprintf(fid,'%f\n',distanceij(i,:));
    end
    fclose(fid);
    newdistance_cell{k} =distanceij; 
end
%计算出在这个矩阵区域内的最大距离
maxdistance = distance(ymax,xmax,ymin,xmin,globalr)*1000;
save ../data/maxdistance.mat maxdistance;

save ../data/newdistance_cell.mat newdistance_cell;
