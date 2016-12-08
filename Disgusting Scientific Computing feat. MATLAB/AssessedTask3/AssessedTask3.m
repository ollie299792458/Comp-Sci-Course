%% Setup the figure
figure;

%% Initialise arrays
in = -7;
fin = 7;
stp = 100;
[X,Y] = meshgrid(linspace(in,fin,stp));

%% Plot and save the P and D orbitals, 3d mesh representation
Zp = porbital(X,Y);
Zd = dorbital(X,Y);
mesh(Zp);
hold on;
mesh(Zd);
xlabel('X distance');
ylabel('Y distance');
zlabel('Magnitude of orbital function');
title('Graph showing electron distribution in p and d orbitals');
print -dpng -r300 2.2.1MeshPD.png;

%% Replot and save seperately due to nonspecific question in the assessed task
clf;
mesh(Zd);
xlabel('X distance');
ylabel('Y distance');
zlabel('Magnitude of orbital function');
title('Graph showing electron distribution in d orbitals');
print -dpng -r300 2.2.1MeshD.png;
clf;
mesh(Zp);
xlabel('X distance');
ylabel('Y distance');
zlabel('Magnitude of orbital function');
title('Graph showing electron distribution in p orbitals');
print -dpng -r300 2.2.1MeshP.png;

%% Plot and save both contour plots
clf;
contour(Zp);
xlabel('X distance');
ylabel('Y distance');
title('Graph showing contours of electron distribution in p orbitals');
print -dpng -r300 2.2.2ContourP.png;
clf;
contour(Zd);
xlabel('X distance');
ylabel('Y distance');
title('Graph showing contours of electron distribution in d orbitals');
print -dpng -r300 2.2.2ContourD.png;

%% Plot d orbital and x = y in 2d plot (by dxy I assume you mean the contour)
clf;
contour(Zd);
xlabel('X distance');
ylabel('Y distance');
title('Graph showing d orbitals contours and the line y =x');
hold on;
x = linspace(0,100,100);
plot(x,x);
print -dpng -r300 2.2.3ContourDandYequalsX.png;

%% Obtain derivative function
h = (fin - in)/stp;
dZd = diff(Zd)/h;

%% Plot derivative function
clf;
mesh(dZd);
xlabel('X distance');
ylabel('Y distance');
zlabel('Derivative of magnitude of orbital function');
title('Graph showing rate of change of electron distribution in d orbitals');
print -dpng -r300 2.2.4Diffdorbital.png;

%% Functions for p and d orbitals
function [f] = porbital (x,y)
    f = x.*exp(-sqrt(x.*x + y.*y));
end

function [f] = dorbital (x,y)
    f = y.*porbital(x,y);
end