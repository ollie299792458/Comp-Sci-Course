#include <stdio.h>

void bubble_sort(char* ints, int length){
    int swapped = 1;
    while (swapped > 0) {
        swapped = 0;
        for (int i = 1; i < length; i++) {
            if (ints[i-1] > ints[i]) {
                swapped ++;
                char temp = ints[i];
                ints[i] = ints[i-1];
                ints[i-1] = temp;
            }
        }
    }
}

void print_array(char* ints, int length) {
    for (int i = 0; i < length-1; i++) {
        printf("%c,", ints[i]);
    }
    printf("%c\n", ints[length-1]);
}

int main() {
    char i[] = {'1','3','4','2','b','a'};
    bubble_sort(i, 6);
    print_array(i, 6);
    return 0;
}
