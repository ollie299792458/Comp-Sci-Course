/* Clarvi display emulator: Modelsim side
 *
 * The display emulator communicates with Modelsim over a TCP socket on port 60589 (0xECAD)
 * using a very simple protocol:
 *
 * Send: 'w <address> <byte enable> <data>' to write a word
 * Send: 'r <address> <byte enable>' to read, which will return 'd <data>'
 * All values in hex (with or without '0x') and lines are \n terminated
 * Only one client may connect to the socket at any one time
 * <byte enable> is a 4-bit byte mask - eg 0xf to write four bytes
 */

#include "svdpi.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <sys/socket.h>

#include "clarvi_avalon_external.h"
#include "avalon_addr.h"

int emulator_socket = 0;

#define ADDR_MASK ((1 << 26) - 1)

#define TCP_PORT  "60589"  // 0xECAD

static int clarvi_avalon_byteena_to_mask(char byteena)
{
	int i;
	unsigned int result = 0;
	
	for (i = 3; i >= 0; i--) {
		result <<= 8;
		
		if (byteena & (1 << i))
			result |= 0xff;
	}
	
	return result;
}


int clarvi_avalon_read_mem(int address, char byteena)
{
	unsigned int mask;
	int data = 0xECADECAD;
	char buffer[4096];
	char *bufpos, *end;
	int got=0, len=0;
	int ret=0;
	
	mask = clarvi_avalon_byteena_to_mask(byteena);

	// Modelsim addresses omit the lower two bits
	address = address<<2;

	if (emulator_socket) {
		len = snprintf(buffer, sizeof(buffer), "r %#x %#x\n", address, byteena);
		ret = send(emulator_socket, buffer, len, 0);
		if (ret == -1) {
			perror("Display emulator giving up, error");
			emulator_socket = 0;
			return -1;
		}
		bufpos = buffer;
		got = 0;
		while (!(end=memchr(buffer,'\n',got))) {
			ret = recv(emulator_socket, bufpos, sizeof(buffer)-got, 0);
			if (ret == -1) {
				data &= mask;
				printf("Avalon read failed, returning %#x\n", data);
				perror("recv");
				close(emulator_socket);
				emulator_socket = 0;
				return data;
			}
			got += ret;
			bufpos += ret;
		}
		*end = '\0';
		if (buffer[0] != 'd') {
			printf("Unexpectedly received response: %s\n",buffer);
			close(emulator_socket);
			emulator_socket = 0;
		}
		sscanf(buffer+1,"%x", &data);
		data &= mask;
		printf("Read of Avalon[%#x] returned %#08x\n", address, data);
	} else {
		data &= mask;
		printf("Display emulator connection previously failed, returning %#x for read of avalon[%#x] byteenable %1x\n", data, address, byteena);
	}

	return data;
}

void clarvi_avalon_write_mem(int address, int data, char byteena)
{
	unsigned int mask;
	char buffer[1024];
	int len, ret;
	
	// Modelsim addresses omit the lower two bits
	address = address<<2;

	if (address == (DEBUG_PRINT) )
	{
		printf("DEBUG PRINT %#x \n", (clarvi_avalon_byteena_to_mask(byteena) & data));
	} else {
		if (emulator_socket) {
			len = snprintf(buffer, sizeof(buffer), "w %#x %#x %#x\n", address, byteena, data);
			ret = send(emulator_socket, buffer, len, 0);
			if (ret == -1) {
				perror("Display emulator giving up, error");
				emulator_socket = 0;
				return;
			}
		} else {
			printf("Display emulator connection previously failed, discarding write avalon[%#x] byteenable %1x <- %#x\n", address, byteena, data);
		}
		printf("Display emulator write avalon[%#x] byteenable %1x <- %#x\n", address, byteena, data);
	}
}

void clarvi_avalon_setup()
{
	if(emulator_socket) return; // only setup once!

	struct addrinfo hints, *servinfo, *p;
	int ret, sockfd;

	printf("Display emulator setup...\n");

	memset(&hints, 0, sizeof(hints));
	hints.ai_family = AF_UNSPEC; // use IPv4 or IPv6
	hints.ai_socktype = SOCK_STREAM;

	if ((ret = getaddrinfo("localhost", TCP_PORT, &hints, &servinfo))) {
		fprintf(stdout, "getaddrinfo: %s\n", gai_strerror(ret));
		return;
	}

	// we may have received multiple IP addresses.  Find one we can connect to
	for (p = servinfo; p !=NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1) {
			perror("socket");
			continue;
		}
		if (connect(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("connect");
			continue;
		}
		break;
	}

	if (p == NULL) {
		fprintf(stdout, "Failed to connect to display emulator\n");
		return;
	}

	freeaddrinfo(servinfo);
	emulator_socket = sockfd;
	printf("Successful connection to display emulator\n");
}
