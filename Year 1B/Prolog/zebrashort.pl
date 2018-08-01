exists(A,(A,_,_,_,_)).
exists(A,(_,A,_,_,_)).
exists(A,(_,_,A,_,_)).
exists(A,(_,_,_,A,_)).
exists(A,(_,_,_,_,A)).
nextTo(A,B,(A,B,_,_,_)).
nextTo(A,B,(_,A,B,_,_)).
nextTo(A,B,(_,_,A,B,_)).
nextTo(A,B,(_,_,_,A,B)).
nextTo(A,B,H) :- rightOf(A,B,H).
rightOf(A,B,(B,A,_,_,_)).
rightOf(A,B,(_,B,A,_,_)).
rightOf(A,B,(_,_,B,A,_)).
rightOf(A,B,(_,_,_,B,A)).
middleHouse(A,(_,_,A,_,_)).
firstHouse(A,(A,_,_,_,_)).

:-  exists(house(british,_,_,_,red),Houses),
    exists(house(spanish,dog,_,_,_),Houses),
    exists(house(_,_,_,coffee,green),Houses),
    exists(house(ukranian,_,_,tea,_),Houses),
    rightOf(house(_,_,_,_,green),house(_,_,_,_,ivory),Houses),
    exists(house(_,snail,oldgold,_,_),Houses),
    exists(house(_,_,kools,_,yellow),Houses),
    middleHouse(house(_,_,_,milk,_),Houses),
    firstHouse(house(norwegian,_,_,_,_),Houses),
    nextTo(house(_,_,chesterfields,_,_),house(_,fox,_,_,_),Houses),
    nextTo(house(_,_,kools,_,_),house(_,horse,_,_,_),Houses),
    exists(house(_,_,luckystrike,orangejuice,_),Houses),
    exists(house(japanese,_,parliaments,_,_),Houses),
    nextTo(house(norwegian,_,_,_,_),house(_,_,_,_,blue),Houses),
    exists(house(WaterDrinker,_,_,water,_),Houses),
    exists(house(ZebraOwner,zebra,_,_,_),Houses),
    print(ZebraOwner),print('\n'),
    print(WaterDrinker),print('\n').

