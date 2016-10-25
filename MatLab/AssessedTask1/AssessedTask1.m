clear;

invest = 399;

startrow = 180;
endrow = 1;

appledata = dlmread('apple.csv', ',', 1, 0);
microsoftdata = dlmread('microsoft.csv', ',', 1, 0);
ibmdata = dlmread('ibm.csv', ',', 1, 0);

appledatax = appledata(:,1);
appledatay = appledata(:,2);

ibmdatax = ibmdata(:,1);
ibmdatay = ibmdata(:,2);

microsoftdatax = microsoftdata(:,1);
microsoftdatay = microsoftdata(:,2);

appleshares = invest/appledatay(startrow);
ibmshares = invest/ibmdatay(startrow);
microsoftshares = invest/microsoftdatay(startrow);

appledatay = appledatay.*appleshares;
ibmdatay = ibmdatay.*ibmshares;
microsoftdatay = microsoftdatay.*microsoftshares;

startdate = appledatax(startrow);
enddate = appledatax(endrow);

hold on;
plot(appledatax,appledatay);
plot(ibmdatax,ibmdatay);
plot(microsoftdatax,microsoftdatay);
hold off;

xlabel('Date / Years');
ylabel('Investment Value / USD');
title('Share price of based on an initial $399 investment');
legend('Apple', 'IBM', 'Microsoft', 'location', 'northwest');

appledataend = appledatay(endrow)
ibmdataend = ibmdatay(endrow)
microsoftdataend = microsoftdatay(endrow)


xlim([startdate enddate]);