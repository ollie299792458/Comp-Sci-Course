%% Clear the current figure
clear;

%% Value of the initial investment
invest = 399;

%% Row in the CSV files that the data starts and ends
startrow = 180;
endrow = 1;

%% Read in the data from the csv files
appledata = dlmread('apple.csv', ',', 1, 0);
microsoftdata = dlmread('microsoft.csv', ',', 1, 0);
ibmdata = dlmread('ibm.csv', ',', 1, 0);

%% Get an array of x and y values from the data read in
appledatax = appledata(:,1);
appledatay = appledata(:,2);
ibmdatax = ibmdata(:,1);
ibmdatay = ibmdata(:,2);
microsoftdatax = microsoftdata(:,1);
microsoftdatay = microsoftdata(:,2);

%% Calculate the number of shares at the start that is equivalent to $399
appleshares = invest/appledatay(startrow);
ibmshares = invest/ibmdatay(startrow);
microsoftshares = invest/microsoftdatay(startrow);

%% Multiply the y values by the respective number of shares
appledatay = appledatay.*appleshares;
ibmdatay = ibmdatay.*ibmshares;
microsoftdatay = microsoftdatay.*microsoftshares;

%% Get the start and end date
startdate = appledatax(startrow);
enddate = appledatax(endrow);

%% Plot the data
hold on;
plot(appledatax,appledatay);
plot(ibmdatax,ibmdatay);
plot(microsoftdatax,microsoftdatay);
hold off;

%% Label the graph
xlabel('Date / Years');
ylabel('Investment Value / USD');
title('Value of an initial $399 investment made on 23rd October 2001');
legend('Apple', 'IBM', 'Microsoft', 'location', 'northwest');

%% Output the most recent investment values to the command line
appledataend = vpa(appledatay(endrow))
ibmdataend = ibmdatay(endrow)
microsoftdataend = microsoftdatay(endrow)

%% Set the limits of the graph
xlim([startdate enddate]);