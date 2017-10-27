#include <stdio.h>
#include <stdint.h>

int encode(char *in_file, char *out_file) {
    FILE *in;
    FILE *out;
    in = fopen(in_file, "r");
    out = fopen(out_file, "w");
    if (in&&out) {
        char c;
        uint16_t buffer;
        int i = 15;
        while ((c = getc(in)) != EOF) {
            switch(c) {
                case 'a' :
                    buffer &= ~(1<<i);
                    i--;
                    break;
                case 'b' :
                    buffer |= (1<<i);
                    i--;
                    buffer &= ~(1<<i);
                    i--;
                    break;
                case 'c' :
                    buffer |= (1<<i);
                    i--;
                    buffer |= (1<<i);
                    i--;
                    buffer &= ~(1<<i);
                    i--;
                    buffer &= ~(1<<i);
                    i--;
                    break;
                case 'd' :
                     buffer |= (1<<i);
                     i--;
                     buffer |= (1<<i);
                     i--;
                     buffer &= ~(1<<i);
                     i--;
                     buffer |= (1<<i);
                     i--;
                    break;
            }
            if (i < 8) {
                uint8_t write = buffer >> 8;
                putc((char) write, out);
                i = i+8;
                buffer = buffer << 8;
            }
        }
        buffer |= (1<<i);
        i--;
        buffer |= (1<<i);
        i--;
        buffer |= (1<<i);
        i--;
        if (i < 8) {
            uint8_t write = buffer >> 8;
            putc((char) write, out);
            i = i+8;
            buffer = buffer << 8;
        }
        if (i < 15) {
            //ends up padding the end of the byte with 1s rather than 0s, but a non issue
            uint8_t write = buffer >> 8;
            putc((char) write, out);
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
    int res = encode("encoder_in_test","out_file");
    printf("%d\n",res);
}
