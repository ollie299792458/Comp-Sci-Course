#include <stdio.h>

//question 3
#define SWAP(t,x,y) t temp = x; x = y; y = temp
#define SWAP2(t,x,y) t *ax = &x; t *ay = &y; t temp = *ax; *ax = *ay; *ay = temp 

void merge(int in[], int out[], int start, int end) {
    if (start + 1 == end) {
       return;
    }
    int middle = (start + end) / 2;
    merge(in, out, start, middle);
    merge(in, out, middle, end);
    for (int i = start; i < end; i++) {
        in[i] = out[i];
    }
    int l, r, i;
    for (l = start, r = middle, i = start;
        l < middle && r < end; i++) {
        if (in[l] <= in[r]) {
            out[i] = in[l];
            l++;
        } else {
            out[i] = in[r];
            r++;
        }
    }
    while (l < middle) {
        out[i] = in[l];
        i++;
        l++;
    }
    while (r < end) {
        out[i] = in[r];
        i++;
        r++;
    }
}

void mergesort(int ints[], int length) {
    int start = 0;
    int end = length;
    int res[length];
    merge(res, ints, start, end);
    ints = res;
}

void print_array(int* ints, int length) {
    for (int i = 0; i < length-1; i++) {
        printf("%d,", ints[i]);
    }
    printf("%d\n", ints[length-1]);
}

int main() {
    int i[] = {1,2,4,3,9,2,3,4};
    mergesort(i, 8);
    print_array(i, 8);
    
    int a = 0;
    int b = 1;
    SWAP2(int, a, b);
    printf("a:%d,b:%d\n", a, b);
    
    return 0;
}
