% Harmonic oscillations in 3D
% Anisotropic (different spring constant in different cartesian directions)
%
% Newtonian dynamics via velocity Verlet integration
%
% Algorithm
%    x(t) = x(t-dt) + v(t-dt)dt + a(t-dt)/2 dt^2
%    a(t) = F[x(t)]/m
%    v(t) = v(t-dt) + [a(t) + a(t-dt)]/2 dt
%
% Force: harmonic towards the centre (origin), kx, ky, kz different.
%    F = -kx x - ky y - kz z

% Establish parameters (so they can be easily changed)
dm  = 3;                 % Dimensions
m   = 1;                 % Mass of the moving particle
k   = [5; 5; 5];        % Spring constants, kx, ky, and kz
% k = [10; 5; 2];          % 2&3D Anisotropic case
% k = [5; 5; 5];           % Isotropic case and QofN
dt  = 9;                % time step
% dt  = 0.05;              % Quality of the numerics small
% dt  = 0.6;               % QofN large
% dt  = 9;                 % QofN Xtra Large
nt  = 60;               % number of time steps
% nt = 60;                 % QofN
% nt  = 4000;              % 3D Anisotropic
% nt = 400;                % Isotropic and 2D Anisotropic
ti  = 0.;                % Initial time
tf  = ti + (nt-1) * dt;  % Final time
xcen = [0; 0; 0];        % Position of center

% Initial conditions (the dimensions must match dm)
x0 = [5.; 5.; 5]; v0 = [0.1; -5; 0.1];
% x0 = [10.; 6.; 3]; v0 = [0.4; -1; 2.3];   % 3D Anisotropic case
% x0 = [10.; 10.; 0]; v0 = [0.4; -0.4; 0];  % 2D Anisotropic case
% x0 = [10.; 10.; 5]; v0 = [0.4; -0.4; 0.3];% Isotropic case and QofN

% Initiate arrays
t = linspace(ti,tf,nt);
x = zeros(dm,nt); v = zeros(dm,nt); a = zeros(dm,nt);

% Initiate the algorithm
x(:,1) = x0;  v(:,1) = v0;
a(:,1) = harmforce3D(x(:,1),xcen,k)/m;

for it = 2:nt
   x(:,it) = x(:,it-1) + v(:,it-1)*dt + a(:,it-1)/2 * dt^2;
   a(:,it) = harmforce3D(x(:,it),xcen,k)/m;
   v(:,it) = v(:,it-1) + [a(:,it) + a(:,it-1)]/2 * dt;
end

plot3(x(1,:),x(2,:),x(3,:));            % Plot it
grid on
set(gca,'xlim',[-15 15]); 
set(gca,'ylim',[-15 15]); 
set(gca,'zlim',[-15 15])
set(gca,'fontsize',16,'linewidth',1)

hold on
cen=plot3(xcen(1),xcen(2),xcen(3),'ro'); % plot the centre
set(cen,'MarkerSize',8)
set(cen,'MarkerFaceColor','r')
xlabel('x'); ylabel('y'); zlabel('z');

% Print the plot
set(gcf,'PaperPosition',[1 1 13 12])   % The last two numbers define size
print -deps2c orbit3D.eps              % print it to file
print -dpng -r300 orbit3D.png          % print it as a png

hold off

% end program