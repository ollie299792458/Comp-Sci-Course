#include <stdio.c>

int main(int argc, char *argv[]) {
    ecode("encode_in_test","out_file");
}

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
        if (i < 8) {
            uint8_t write = buffer >> 8;
            putc((char) write, out);
            i = i+8;
            buffer = buffer << 8;
        }
        close(file);
        close(file); //should really close files better... but...
    }
    
}
