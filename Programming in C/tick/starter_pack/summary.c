/* summary.c */
#include <stdio.h>

#define BUFSIZE 1024
#define IP_HEADER_MIN_SIZE_BYTES 20
#define TCP_HEADER_MIN_SIZE_BYTES 20

void ip_string(int ip, char* output) {
  unsigned char bytes[4];
  bytes[0] = ip & 0xFF;
  bytes[1] = (ip >> 8) & 0xFF;
  bytes[2] = (ip >> 16) & 0xFF;
  bytes[3] = (ip >> 24) & 0xFF;
  sprintf(output,"%d.%d.%d.%d\n", bytes[3], bytes[2], bytes[1], bytes[0]);
}

int main(int argc, char *argv[]) {

  FILE *fp;
  char bytes[BUFSIZE];

  if (argc != 2) {
    perror("Usage: summary <file>");
    return 1;
  }

  if ((fp=fopen(argv[1],"rb")) == 0) {
    perror("Cannot find file to summarise");
    return 2;
  }

  //read start into buffer
  int r = fread(bytes,1,BUFSIZE,fp);
  int loc = 0;
  
  printf("Read %d bytes\n",r);

  if (r < IP_HEADER_MIN_SIZE_BYTES + TCP_HEADER_MIN_SIZE_BYTES) {
    perror("File doesn't contain at least one IP and TCP packet");
    return 3;
  }

  //get the IHL, second 4 bits of first byte
  int ihl = bytes[loc] & 0x0F;
  loc += 1;

  //ignore the second byte
  loc += 1;

  //get the total length
  int total_length = (bytes[loc]<<8) + bytes[loc+1];
  loc += 2;

  //ignore next 8 bytes
  loc += 8;

  //get the source address
  int source_int = (bytes[loc] << 24) + (bytes[loc+1] << 16) + (bytes[loc+2] << 8) + bytes[loc+3];
  char source[15];
  ip_string(source_int, source);
  loc += 4;

  //get the destination address
  int dest_int = (bytes[loc] << 24) + (bytes[loc+1] << 16) + (bytes[loc+2] << 8) + bytes[loc+3];
  char dest[15];
  ip_string(dest_int, dest);
  loc += 4;

  //if the IP header contains options
  if (ihl > 5) {
    loc += 4*(ihl-5);
  }

  //skip first 12 tcp bytes
  loc += 12;

  //get the TCP packet's header size
  int header_size = bytes[loc] >> 4;
  printf("\n%d\n", header_size);

  loc = total_length;
  int n_ip = 1;

  while (loc < r) {
    //skip the first two bytes
    loc += 2;

    //get length and skip to end of packet
    int length = (bytes[loc]<<8) + bytes[loc+1];
    n_ip++;
    loc += length - 2;
  }

  return 0;
}
