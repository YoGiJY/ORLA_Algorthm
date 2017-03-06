% ��δ���ʵ�ֵ��ǻ�ȡ��һ�������ڵ���Ϣλ��Ȼ��õ���ʱ��T�����г�������վ�ľ���
%��δ����ȡÿ��ʱ��T�ڵĳ�����ƽ����47�����ң���Сʱ������15�����ʱ������75
%��ѡȡ�������󳤶��ǶԱ߳��ȣ�����Ľ����2.5*10^4��m���������������ݴ��ڱ������Ļ������������⡣
%��ÿ�μ���֮�󶼻ᱣ�����ݣ���Ϊÿ�μ��㶼��Ҫ������ʱ�䣬�ⵥ�������ݴ����������ѵ�ʱ��
%�ڴ���ʵ�ֹ�������ѡ����������ֶ�������귶Χ���������BS��վ������ʵ�ֵġ�

%% �����ݴ��ļ��е��뵽����Vehicle
%�ж������ļ��Ƿ���ڣ��������ֱ��load���ݣ������ٴδ��ļ��ж�������
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

%% �õ���γ�����꣬ͨ������ͶӰ���������ķֲ�ͼ
%��ȡ�����ľ�γ�����꣬LatiLong

figure;
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));
grid on;

%% ȡ��վ��BS���ĵڶ������ԣ�ѡȡ�Ϻ��������ܼ��ĵط�ѡ��Ȼ���ȡһ����������������п��Ƿֲ�20����վ
% ������ȡ�����ݣ�����Ҫѡ��һ������Ȼ����˵��������������ľ�γ����Ϣ
figure;
LatiLong = [];
for i = 1:1:117
    latilong = Vehicle{i};
    LatiLong = [LatiLong;latilong];
end
plot(LatiLong(:,3),LatiLong(:,4));
grid off;
hold on;
% ѡȡ��������ѡȡ������Ϊ[31.15 31.30] [121.25 121.45]
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

% ��������������ڽ������ȡ��
hold on;
basepoint = [];
xbasepoint=rand(basenum,1)*(xmax-xmin)+xmin;
ybasepoint=rand(basenum,1)*(ymax-ymin)+ymin;
basepoint = [xbasepoint,ybasepoint];
% ��ȡ����Ļ�վ�ֲ������ڶԻ�վ�������������У�С�����ȼ�
% ���з����վ�ĸ�����[0.25 0.25 0.5]
%���������������վ֮�����ٴ���������������ݣ��������ʶ��վ��
%���У�С�������ݣ����������յĽ��֮����
alphabet=[1 2 3];
prob = [0.25 0.25 0.50];
k = randsrc(basenum,1,[alphabet; prob]);
basepoint = [basepoint,k];
basepoint = sortrows(basepoint,3);

%�õ���վ����������
basepoint(:,3) = [];
xb = basepoint(:,1);
yb = basepoint(:,2);
plot(xb(1:5),yb(1:5),'r*');          %���վ����ɫ
plot(xb(6:10),yb(6:10),'y*');     %�л�վ����ɫ
plot(xb(11:20),yb(11:20),'k*');%С��վ����ɫ
save ../data/basepoint.mat basepoint ;


%% �õ���γ�����ݣ��滮���ݣ�ͬһʱ�䣬��γ����Ϣ,
%1.����ʹ�õĲ�����T = 30���ӣ�delta = 30/(60*24) ;
%2.��������Ҫ�����ݽ��д��������������������޳�

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

%% �õ���γ�����ݣ��滮���ݣ�ͬһʱ�䣬��γ����Ϣ
%����ʹ�õĲ�����T = 30���ӣ�delta = 30/(60*24) ;

%�ж���������Tʱ�̵��ٶ�����ļ��Ƿ����
speedexist = exist('../data/newspeedindex.mat');

delta = 30/(60*24);                                      %����ȡ30����Ϊʱ���
begt = datenum('2015-04-01 00:00:00');%����ȡ��ʼʱ����2014-04-01 00:00:00
endt = datenum('2015-04-30 23:00:59') ;%�ж����ݵĽ���ʱ��
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



%% ������ͳ��һ�£���ѡȡ����������ڵĳ�����������ÿ��ʱ��㳵��������
% �ж�һ��ѡȡ�������Ƿ����

for i = 1:length(newspeedindex)
    ax = newspeedindex{i};
    axindex = ~isnan(ax(:,1));
    veindex(i) = sum(axindex);
end
averindex = mean(veindex);
maxindex = max(veindex);
minindex = min(veindex);


%% ������֪��ͬһʱ�̵�����ĳ�������ͻ�վ�����꣨������������ָ��γ�ȣ�
globalr = 6378.1; % ���������ǵ���İ뾶����
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
%�������������������ڵ�������
maxdistance = distance(ymax,xmax,ymin,xmin,globalr)*1000;
save ../data/maxdistance.mat maxdistance;

save ../data/newdistance_cell.mat newdistance_cell;
