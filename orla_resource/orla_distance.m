%% ORLA算法的实现，是在之前的情况下实现的。我获取的数据
%% 获取这个区域的车俩信息
ymax = 31.30;
ymin = 31.15;
xmax = 121.45;
xmin= 121.25;
load('../data/basepoint.mat');

%% 得到经纬度数据，规划数据，同一时间，经纬度信息,
load('../data/newVehicle.mat');


%% 得到经纬度数据，规划数据，同一时间，经纬度信息
%这里使用的策略是T = 6*30分钟，我们这里取6个小时

%判断数据汽车T时刻的速度情况文件是否存在
speedexist = exist('../orla_data/orla_index.mat');
%speedexist = exist('../data/orla_index.mat');

delta = 6/24;                                    %这里取6个小时为时间戳
begt = datenum('2015-04-01 00:00:00');%这里取开始时间是2015-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%判断数据的结束时间
orla_index = {};

if(speedexist)
    load('../orla_data/orla_index.mat');
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
            orla_index{k} = index;
            k = k+1;
        end
        save ../orla_data/orla_index.mat orla_index;
end

%% 这里我统计一下，在选取的这个区域内的车辆的数量，每个时间点车辆的数量
% 判断一下选取的区域是否合理

for i = 1:length(orla_index)
    ax = orla_index{i};
    axindex = ~isnan(ax(:,1));
    veindex(i) = sum(axindex);
end
averindex = mean(veindex);
maxindex = max(veindex);
minindex = min(veindex);


%% 根据已知的同一时刻的坐标的车辆坐标和基站的坐标（这里的坐标就是指经纬度）
globalr = 6378.1; % 这里计算的是地球的半径长度
orla_distance_cell = {};
for k = 1:length(orla_index)
    matrix = orla_index{k};
    distanceij = [];
    filehead = '../orla_distance/java';
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
    orla_distance_cell{k} =distanceij; 
end
%计算出在这个矩阵区域内的最大距离
maxdistance = distance(ymax,xmax,ymin,xmin,globalr)*1000;
save ../orla_data/maxdistance.mat maxdistance;

save ../orla_data/orla_distance_cell.mat orla_distance_cell;
