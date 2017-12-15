/* extract.c */
#include <stdio.h>

#define HEADERBUFFER 4

int main(int argc, char *argv[]) {
    FILE *fm, *fo;
   
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
        perror("Cannot find file to output extracted message to");
        return 3;
    }
    
    unsigned char buf[HEADERBUFFER];
    int r = 1;
    
    while (r > 0) {
        //read useful ip header information
        r = fread(buf, 1, HEADERBUFFER, fm);
        
        //get header length and total length
        int ihl = buf[0] & 0x0F;
        int tl = (buf[2] << 8) + buf[3];
        
        //move to end of ip header
        int mv = ihl * 4 - HEADERBUFFER;
        fseek(fm, mv, SEEK_CUR);
        
        //skip first 12 bytes of tcp header
        fseek(fm, 12, SEEK_CUR);
        
        //read useful tcp header information
        fread(buf, 1, HEADERBUFFER, fm);
        
        //get data offset (tcp header size)
        int doff = buf[0] >> 4;
        
        //move to end of tcp header
        mv = doff * 4 - 12 - HEADERBUFFER;
        fseek(fm, mv, SEEK_CUR);
        
        //calculate data length
        int dl = tl - ihl * 4 - doff * 4;
        
        //copy data to output file
        unsigned char dbuf[dl];
        fread(dbuf, 1, dl, fm);
        fwrite(dbuf, 1, dl, fo);
    }
    
    //close files
    fclose(fm);
    fclose(fo);
    
    return 0;
}
