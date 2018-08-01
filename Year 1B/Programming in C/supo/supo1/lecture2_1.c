#include <stdio.h>
#include <string.h>
#include <ctype.h>

int cntlower(char str[]) {
    int count = 0;
    for (int i = 0; i < strlen(str); i++) {
        count += islower(str[i]) ? 1 : 0;
    }
    return count;
}

int main() {
    char str[] = "Hello You Smell";
    int count = cntlower(str);
    printf("%d\n", count);
    return 0;
}
