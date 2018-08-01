#include <stdio.h>
#include <stdlib.h>

int main (int argc, char** argv) {
    int stack[20];
    int stackpointer = -1;
    for (int i = 1; i < argc; i++) {
        if ((argv[i][0]) == '+') {
            if (stackpointer < 1) {
                printf("Error: not enough operands\n");
                return -1;
            }
            stack[stackpointer - 1] = stack[stackpointer] + stack[stackpointer-1];
            stackpointer -= 1;
        }
        else if ((argv[i][0]) == '-') {
            if (stackpointer < 1) {
                printf("Error: not enough operands\n");
                return -1;
            }
            stack[stackpointer - 1] = stack[stackpointer-1] - stack[stackpointer];
            stackpointer -= 1;
        } 
        else if ((argv[i][0]) == '*') {
            if (stackpointer < 1) {
                printf("Error: not enough operands\n");
                return -1;
            }
            stack[stackpointer - 1] = stack[stackpointer] * stack[stackpointer-1];
            stackpointer -= 1;
        } 
        else if ((argv[i][0]) == '/') {
            if (stackpointer < 1) {
                printf("Error: not enough operands\n");
                return -1;
            }
            stack[stackpointer - 1] = stack[stackpointer-1] / stack[stackpointer];
            stackpointer -= 1;
        } 
        else {
            int res = atoi(argv[i]);
            if (res == 0 && argv[i][0] != '0') {
                printf("Error: invalid argument: %d\n",i);
                return -1;
            }
            stackpointer++;
            stack[stackpointer] = res;
        }
    }
    if (stackpointer != 0) {
        printf("Error: stack not nearly empty: %d != 0\n", stackpointer);
        return -1;
    }
    
    printf("Result: %d\n", stack[0]);
    
    return stack[0];
}
