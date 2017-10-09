#include <ctype.h>
#include <string.h>
#include "revwords.h"

void reverse_substring(char str[], int start, int end) {
    int middle = (start + end) / 2;
    end--;
    for (int i = start; i < middle; i++) {
        char temp = str[i];
        str[i] = str[end];
        str[end] = temp;
        end--;
    }
}

int find_next_start(char str[], int len, int i) {
    i--;
    while (i < len) {
        i++;
        if (isalpha(str[i])) {
            break;
        }
    }
    return isalpha(str[i]) ? i : -1;
}

int find_next_end(char str[], int len, int i) {
    i--;
    while (i < len) {
        i++;
        if (!isalpha(str[i])) {
            break;
        }
    }
    return i;
}

void reverse_words(char s[]) {
    int len = strlen(s);

    int start = 0;
    int end = 0;

    while (start != -1) {
        start = find_next_start(s, len, end);
        end = find_next_end(s, len, start);

        reverse_substring(s, start, end);
    }
}
