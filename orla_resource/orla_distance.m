%% ORLA�㷨��ʵ�֣�����֮ǰ�������ʵ�ֵġ��һ�ȡ������
%% ��ȡ�������ĳ�����Ϣ
ymax = 31.30;
ymin = 31.15;
xmax = 121.45;
xmin= 121.25;
load('../data/basepoint.mat');

%% �õ���γ�����ݣ��滮���ݣ�ͬһʱ�䣬��γ����Ϣ,
load('../data/newVehicle.mat');


%% �õ���γ�����ݣ��滮���ݣ�ͬһʱ�䣬��γ����Ϣ
%����ʹ�õĲ�����T = 6*30���ӣ���������ȡ6��Сʱ

%�ж���������Tʱ�̵��ٶ�����ļ��Ƿ����
speedexist = exist('../orla_data/orla_index.mat');
%speedexist = exist('../data/orla_index.mat');

delta = 6/24;                                    %����ȡ6��СʱΪʱ���
begt = datenum('2015-04-01 00:00:00');%����ȡ��ʼʱ����2015-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%�ж����ݵĽ���ʱ��
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

%% ������ͳ��һ�£���ѡȡ����������ڵĳ�����������ÿ��ʱ��㳵��������
% �ж�һ��ѡȡ�������Ƿ����

for i = 1:length(orla_index)
    ax = orla_index{i};
    axindex = ~isnan(ax(:,1));
    veindex(i) = sum(axindex);
end
averindex = mean(veindex);
maxindex = max(veindex);
minindex = min(veindex);


%% ������֪��ͬһʱ�̵�����ĳ�������ͻ�վ�����꣨������������ָ��γ�ȣ�
globalr = 6378.1; % ���������ǵ���İ뾶����
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
%�������������������ڵ�������
maxdistance = distance(ymax,xmax,ymin,xmin,globalr)*1000;
save ../orla_data/maxdistance.mat maxdistance;

save ../orla_data/orla_distance_cell.mat orla_distance_cell;
