tName(acr31,'Andrew Rice').
tName(arb33,'Alastair Beresford').
tName(olb22,'Oliver Black').

tCollege(acr31,'Churchill').
% tCollege(arb33,'Robinson').
tCollege(olb22,'Selwyn').
tCollege(jadh2,'Selwyn').

tGrade(acr31,'IA',2.1).
tGrade(acr31,'IB',1).
tGrade(acr31,'II',1).
tGrade(arb33,'IA',2.1).
tGrade(arb33,'IB',1).
tGrade(arb33,'II',1).
tGrade(olb22,'IA',2.1).

tDOB(olb22,19970407).

% 22S.1
qFullNameAndCollege(A,B) :- tName(ID,A), tCollege(ID,B).

% 22S.2
qFullNameAndCollege(ID,A,B) :- tName(ID,A), tCollege(ID,B).

% 22S.3
qFullNameAndCollegeCollegeOuter(A,B) :- tName(ID,A), tCollege(ID,B).
qFullNameAndCollegeCollegeOuter(A,B) :- tName(ID,A), not(tCollege(ID,B)), B = ''.
% but this results in false, rather than true

% 22S.4
qFullNameAndCollegeFullOuter(A,B) :- qFullNameAndCollegeCollegeOuter(A,B).
qFullNameAndCollegeFullOuter(A,B) :- tCollege(ID,B), not(tName(ID,A)), A = ''.

% 22S.5
qLowestGrade(ID,A) :- tGrade(ID,_,A),tGrade(ID,_,B),A < B, !.
