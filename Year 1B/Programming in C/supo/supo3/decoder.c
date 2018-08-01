#include <stdio.h>
#include <stdint.h>

int decode(char *in_file, char *out_file) {
    FILE *in;
    FILE *out;
    in = fopen(in_file, "r");
    out = fopen(out_file, "w");
    if (in&&out) {
        uint16_t buffer;
        uint8_t c;
        while ((c = (uint8_t) getc(in)) != EOF) {
            //not to sure how to do it nicely
        }
        fclose(in);
        fclose(out);
        return 0;
    } else {
        if (in) {
            fclose(in);
            return -3;
        } else if (out) {
            fclose(out);
            return -2;
        } else {
            return -1;
        }
    }
}

int main(int argc, char *argv[]) {
    int res = decode("encoder_in_test","out_file");
    printf("%d\n",res);
}
