#include <stdio.h>

void bubble_sort(int* ints, int length){
    int swapped = 1;
    while (swapped > 0) {
        swapped = 0;
        for (int i = 1; i < length; i++) {
            if (ints[i-1] > ints[i]) {
                swapped ++;
                int temp = ints[i];
                ints[i] = ints[i-1];
                ints[i-1] = temp;
            }
        }
    }
}

void print_array(int* ints, int length) {
    for (int i = 0; i < length-1; i++) {
        printf("%d,", ints[i]);
    }
    printf("%d\n", ints[length-1]);
}

int main() {
    int i[] = {1,3,4,2};
    bubble_sort(i, 4);
    print_array(i, 4);
    return 0;
}
