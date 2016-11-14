%% Setup the first figure
figure;
hold on;

%% Read in the data from the dat files
cambridgetable = readtable('cambridge.dat');
cambridgedata = table2array(cambridgetable);

%% Get a single time variable
year = cambridgedata(:,1);
month = cambridgedata(:,2);
time = year+(month - 1)/12;

%% Get an average temperature for each month
maxtemp = cambridgedata(:,3);
mintemp = cambridgedata(:,4);
averagetemp = (maxtemp + mintemp)/2;

%% Plot average temperature against time
plot(time,averagetemp);

%% Work out values for fitting function
tau = 1;
fit = @(a, x) a(1) + a(2).*(x - 1961) + a(3).*sin(((2*pi)/tau).*x + a(4)); 
fcn = @(a) sum((fit(a,time) - averagetemp).^2);
a = fminsearch(fcn, [1,1,1,1]);

%% Plot fitting function
y = fit(a, time);
plot(time, y);

%% Label and format the line graph
xlim([min(time) max(time)]);
xlabel('Date / Years');
ylabel('Average Temperature / �C');
title('Monthly Temperature variation in Cambridge from 1961 to 2011 showing the upward trend');
legend('Average Temperature', 'Sinusoidal Fit', 'location', 'northwest');

%% Report parameters
a0 = a(1)
a1 = a(2)
a2 = a(3)
tau
delta = a(4)
change = (max(time) - min(time))*a(2)

%% Prepare new chart
figure;
hold on;

%% Plot bar chart
rainfall = cambridgedata(:,6);
% Not the bar method, but why on earth would I use the bar method
histogram(rainfall);

%% Label and format the histogram
xlabel('Rainfall / mm');
ylabel('Occurence / Months');
title('Histogram of rainfall in Cambridge from 1961 to 2011');