/* extract.c */
#include <stdio.h>
#include <string.h>

#define BUFSIZE 1048576
#define BUFSIZESTRING "BUFSIZE"

/*Removes IP headers - returns total number of bytes removed, or -1 on failure*/
int remove_iptcp (FILE *fi, FILE *fo) {
    //declare buffers
    unsigned char bufi[BUFSIZE];
    unsigned char bufo[BUFSIZE];
    int loci = 0;
    int loco = 0;
    
    //read file
    int r = fread(bufi,1,BUFSIZE,fi);
    if (r > BUFSIZE) {
        return -1;
    }
    
    while (loci <= r) {
        //get the IHL, second 4 bits of first byte
        int ihl = bufi[loci] & 0x0F;
        loci += 1;
       
        //ignore the second byte
        loci += 1;

        //get the total length
        int tl = (bufi[loci]<<8) + bufi[loci+1];
        loci += 2;
        
        //if length is 0 break
        if (tl == 0) {
            break;
        }
        
        //go back to start of ip header
        loci -= 4;
        
        //skip to end of the ip header
        loci += 4*(ihl);
        
        //get bytes in tcp packet
        int btcp = tl-ihl*4;
        
        //skip first 12 tcp bytes
        loci += 12;
      
        //get the TCP packet's header size
        int dof = bufi[loci] >> 4;
        
        //go back to start of tcp header
        loci -= 12;
        
        //skip to end of TCP header
        loci += 4*(dof);
        
        //get bytes to copy
        int btc = btcp-(dof*4);
        
        //copy bytes
        memcpy(&bufi[loci], &bufo[loco], btc);
        
        //update locations
        loci += btc;
        loco += btc;
    }
    int w = fwrite(bufo,1, loco, fo);
    return r-w;
}

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
    
    //remove headers
    if (remove_iptcp(fm, fo)==-1){
        perror("Failed to remove IP/TCP headers, check your file is under "BUFSIZESTRING" bytes");
        return 4;
    }
    
    return 0;
}
