#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int d;
    struct BinaryTree *l;
    struct BinaryTree *r;
} BinaryTree;

BinaryTree *heapify(int ints[]) {
    BinaryTree *t = (BinaryTree*) malloc(sizeof(BinaryTree));
    t->d = ints[0];
    //No time to implement heapify and debug, focused on exam q's
    return t;
}

int main() {
    int ints[] = {1};
    BinaryTree *t = heapify(ints);
    printf("%d\n",t->d);
    return 0;
}
