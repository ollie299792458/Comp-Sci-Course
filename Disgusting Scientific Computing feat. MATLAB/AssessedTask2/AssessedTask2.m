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
fit = polyfit(time, averagetemp, 1);
a1 = fit(1);
a0 = fit(2)+1961*a1;
tau = 1.0;
delta = -1*pi/2;
fit = @(a2, x) a0 + a1.*(x - 1961) + a2.*sin(((2*pi)/tau).*x + delta); 
fcn = @(b) sum((fit(b,time) - averagetemp).^2);
ar = max(averagetemp) - min(averagetemp);
a2 = fminsearch(fcn, ar);

%% Plot fitting function
y = fit(a2, time);
plot(time, y);

%% Label and format the line graph
xlim([min(time) max(time)]);
xlabel('Date / Years');
ylabel('Average Temperature / °C');
title('Monthly Temperature variation in Cambridge from 1961 to 2011 showing the upward trend');
legend('Average Temperature', 'Sinusoidal Fit', 'location', 'northwest');

%% Report parameters
a0
a1
a2
tau
delta
change = (max(time) - min(time))*a1

%% Prepare new chart
figure;
hold on;

%% Plot bar chart
rainfall = cambridgedata(:,6);
% Not the bar method, but why on earth would I use the bar method
histogram(rainfall);

%% Label and format the histogram
xlabel('Rainfall / mm');
ylabel('Occurunces / Months');
title('Histogram of rainfall in Cambridge from 1961 to 2011');