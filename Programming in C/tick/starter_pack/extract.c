/* extract.c */
#include <stdio.h>
#include <string.h>

#define IP_SUFFIX ".ip"
#define BUFFER_SIZE
}

typedef struct BufferedFile {
    FILE *f; //file name
    int loc; //init to 0 - location with in buffer
    int r; //init to 0 - number of bytes last read to buffer
    int bufcount; //init to 0 - number of full buffers read so far
    unsigned char buf[BUFFER_SIZE];
} bf;

/* Get unsigned char (from buffer or from file) */
unsigned char guc(bf *f) {
    return f->buf[f->loc];
}

/* Move location (along buffered file) - returns new location or -1 for EOF */
int mvl(bf *f, int inc) {
    if (f->loc + inc < f->r) {
        f->loc += inc;
        return 0;
    } else {
    }
}

int main(int argc, char *argv[]) {
    FILE *fm, *fo, *fip;
    unsigned char bytes[BUFSIZE];
   
   //check arguments
    if (argc != 3) {
        perror("Usage: extract <file-message> <file-output>");
        return 1;
    }
    
    //open files
    if ((fm=fopen(argv[1],"rb")) == 0) {
        perror("Cannot find file to extract message from");
        return 2;
    }
    
    if ((fo=fopen(argv[2],"wb")) == 0) {
        perror("Cannot find file to output extracted message to")
        return 3;
    }
    
    char str[50]
    strcpy(str, argv[2]);
    strcat(str, IP_SUFFIX);
    if ((fip=fopen(str,"wb")) == 0) {
        perror("Cannot create temporary file")
        return 4;
    }
    
    //remove ip headers
    remove_ip(fm, fip);
    
    //remove tcp header
    remove_tcp(fip, fo);
    
    //remove temporary file
    remove(str);
}
