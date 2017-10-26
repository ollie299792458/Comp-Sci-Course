#include <stdio.h>
#include <string.h>

char *strfind(const char *s, const char *f) {
    int slength = strlen(s);
    int flength = strlen(f);
    int i = 0;
    while (i < flength) {
        int j = 0;
        if (f[i] == s[j]) {
            int equal = 1;
            for (j = 0; j < slength; j++) {
                if (f[i+j] != s[j]) {
                    equal = 0;
                }
            }
            if (equal) {
                return (char *) &f[i];
            }
        }
        i++;
    }
    return NULL;
}

int main() {
    char *a = "hello";
    char *b = "hiya ello you smell";
    char *c = strfind(a, b);
    printf("%s\n", c);
    return 0;
}
