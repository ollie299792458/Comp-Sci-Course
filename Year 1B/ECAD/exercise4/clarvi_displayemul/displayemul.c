#include <stdio.h>
#include <stdlib.h>

#include <SDL2/SDL.h>
#include "avalon_addr.h"

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <netdb.h>
#include <errno.h>
#include <unistd.h>


/* COMMENT THIS LINE TO DISABLE MORE VERBOSE PRINTOUTS */
#define VERBOSE



#ifdef VERBOSE
#define VPRINT(fmt, ...)	printf(fmt, __VA_ARGS__)
#else
#define VPRINT(fmt, ...)
#endif


// a bitfield for the buttons, to match the hardware positions
typedef union {
  struct {
    int temperature_alarm:1;
    int diall_click:1;
    int dialr_click:1;
    int nav_click:1;
    int nav_d:1;
    int nav_r:1;
    int nav_l:1;
    int nav_u:1;
    int spare2:1;
    int spare1:1;
    int touch_irq:1;
    int spare0:1;
    int x:1;
    int y:1;
    int a:1;
    int b:1;
  } bits;
  int word;
} display_buttons_t;


int *fb = NULL;
SDL_Surface *sdl_fb = NULL;
SDL_Window *sdl_win = NULL;
SDL_Renderer *sdl_ren = NULL;
SDL_Thread *sdl_thread = NULL;

unsigned char rotary_left = 0;
unsigned char rotary_right = 0;
display_buttons_t buttons = {0};

struct sockaddr_storage remote_addr;
socklen_t remote_size;
struct addrinfo hints, *res=NULL;
int listenfd=0, connfd=0;


static int cdb_fb_byteena_to_mask(char byteena)
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

void cdb_fb_render()
{
	if (sdl_win) {
		SDL_Texture *tex = SDL_CreateTextureFromSurface(sdl_ren, sdl_fb);
		
		//First clear the renderer
		SDL_RenderClear(sdl_ren);
		//Draw the texture
		SDL_RenderCopy(sdl_ren, tex, NULL, NULL);
		//Update the screen
		SDL_RenderPresent(sdl_ren);
		
		SDL_DestroyTexture(tex);
	}
}

int cdb_keystroke_receiver(void *userdata, SDL_Event *e)
{
	if (e->type == SDL_KEYDOWN) {
		switch (e->key.keysym.sym) {
			case SDLK_LEFT:
				rotary_left--;
				break;
			case SDLK_RIGHT:
				rotary_left++;
				break;
			case SDLK_UP:
				rotary_right--;
				break;
			case SDLK_DOWN:
				rotary_right++;
				break;
		}
	}
	if ((e->type == SDL_KEYDOWN) || (e->type == SDL_KEYUP)) {
		switch (e->key.keysym.sym) {
			case SDLK_n:
				buttons.bits.diall_click = (e->type == SDL_KEYDOWN);
				break;
			case SDLK_m:
				buttons.bits.dialr_click =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_a:
				buttons.bits.a =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_b:
				buttons.bits.b =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_x:
				buttons.bits.x =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_y:
				buttons.bits.y =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_s:
				buttons.bits.nav_l =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_f:
				buttons.bits.nav_r =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_e:
				buttons.bits.nav_u =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_c:
				buttons.bits.nav_d =  (e->type == SDL_KEYDOWN);
				break;
			case SDLK_d:
				buttons.bits.nav_click =  (e->type == SDL_KEYDOWN);
				break;
		}
		printf("Key %s %x, rotary_left=%02x, rotary_right=%02x, buttons=%04x\n", 
			(e->type == SDL_KEYDOWN) ? "down" : "up",
			e->key.keysym.sym, rotary_left, rotary_right, buttons.word);
	}
	return 1;
}

int cdb_event_check(void)
{
	int quit = 0;
//		printf("Rotary_left=%02x, rotary_right=%02x\n", rotary_left, rotary_right);

	SDL_Event e;

		while (SDL_PollEvent(&e)) {
			if (e.type == SDL_QUIT) {			
				quit = 1;
			}
		}
//	sleep(1);

	return quit;
}
int cdb_fb_read_mem(int address, char byteena)
{
	unsigned int mask;
	int data = 0;
	
	mask = cdb_fb_byteena_to_mask(byteena);
	
	if ((address)>=FRAMEBUFFER_BASE && (address)<FRAMEBUFFER_BASE+FRAMEBUFFER_LENGTH)
	{
		data = fb[(address & ADDR_MASK)>>2];
		data &= mask;
		VPRINT("----- FRAMEBUFFER read address 0x%08x byte enable 0x%1x => 0x%08x\n", ((address & ADDR_MASK)), mask, data);
	} else if ((address) == PIO_ROTARY_L) {
		data = rotary_left;
		data &= mask;
		VPRINT("---- Left rotary dial PIO <- 0x%08x (byte enable 0x%1x\n", data, mask);
	} else if ((address) == PIO_ROTARY_R) {
		data = rotary_right;
		data &= mask;
		VPRINT("---- Right rotary dial PIO <- 0x%08x (byte enable 0x%1x\n", data, mask);
	} else if ((address) == PIO_BUTTONS) {
		data = buttons.word;
		data &= mask;
		VPRINT("---- Buttons PIO <- 0x%08x (byte enable 0x%1x\n", data, mask);
	} else {
		data = 0xdeadbeef;
		data &= mask;
		VPRINT("----- Unknown Avalon Master read address 0x%08x byte enable 0x%08x => 0x%08x (always returning 0xdeadbeef or part thereof)\n", ((address & ADDR_MASK)), mask, data);
	}

	// keep the GUI happy
	
//	cdb_event_check();
	
	return data;
}

void cdb_fb_write_mem(int address, int data, char byteena)
{
	unsigned int mask;
	
	mask = cdb_fb_byteena_to_mask(byteena);
	data &= mask;
	
	if ((address)>=FRAMEBUFFER_BASE && (address)<FRAMEBUFFER_BASE+FRAMEBUFFER_LENGTH)
	{
		VPRINT("---- FRAMEBUFFER write address 0x%08x byte enable 0x%1x <= 0x%08x\n", ((address & ADDR_MASK)), mask, data);
	
		fb[(address & ADDR_MASK)>>2] &= ~mask;
		fb[(address & ADDR_MASK)>>2] |= data;
	
//		if(address < (480*272 >> 1))
//			cdb_fb_render();
	} else	if ((address) == PIO_LED_BASE) {
		VPRINT("---- LED PIO set to %#x\n", data);
	} else	if ((address) == PIO_HEX_BASE) {
		VPRINT("---- Hex PIO set to %#x\n", data);
	} else {
		VPRINT("---- Unknown Avalon Master write 0x%08x byte enable 0x%1x <= 0x%08x\n", ((address & ADDR_MASK)), mask, data);
	}
	
	// keep the GUI happy
//	cdb_event_check();
	
}

static int cdb_control_channel(int conn)
{
	int read=-1;
	char buffer[4096];
	char sendbuffer[4096];
	char *bufpos, *datastart, *dataend;
	int len, sendlen;
	char *nlpos=0;
	int address=0, data=0;
	int byteenable=0;

	bufpos = buffer;
	len = 0;

	// Main loop to read socket data.
	// It comes in two parts:
	// First read data into the buffer until we receive at least one newline.
	// Then scan through, parsing sections between newlines.
	// Finally, we may have some data at the end of the buffer beyond the last newline,
	// so copy it down to the start of the buffer ready to receive again.
	// This is almost a circular buffer, but with a less efficient copy.
	while (read != 0) {
		datastart = buffer;
		// drain the socket until we reach a newline
		while (!(nlpos=memchr(buffer, '\n', len))) {
			read=recv(conn, bufpos, sizeof(buffer)-len, 0);
			//printf("buffer=%p, bufpos=%p, len=%d, read=%d\n", buffer,	bufpos, len, read);
			if (read == 0)
				return -1;
			len += read;
			dataend = bufpos+read;
			bufpos += read;
		}
		while (nlpos && nlpos < dataend) {
			//printf("nlpos=%p, datastart=%p, dataend=%p, bufpos=%p\n", nlpos,	datastart, dataend, bufpos);
			*nlpos = '\0';
			switch (datastart[0])
			{
				case 'W':
				case 'w':
					sscanf(datastart+1, " %x %x %x", &address, &byteenable, &data);
					VPRINT("Write address=%#x, byteenable=%#x, data=%#x\n", address, byteenable, data);
					cdb_fb_write_mem(address, data, (char) byteenable);
					break;
				case 'R':
				case 'r':
					sscanf(datastart+1, " %x %x", &address, &byteenable);
					VPRINT("Read address=%#x, byteenable=%#x\n", address, byteenable);
					data = cdb_fb_read_mem(address, (char) byteenable);
					sendlen = snprintf(sendbuffer, sizeof(sendbuffer), "d %#x\n", data);
					VPRINT("Read address=%#x, byteenable=%#x. returned=%#x. response=%s", address, byteenable, data, sendbuffer);
					send(connfd, sendbuffer, sendlen, 0);
					break;
				default:
					printf("Unknown command \"%s\"\n",datastart);
					break;
			}
			datastart = nlpos+1;
			if (datastart < dataend) {
				nlpos=memchr(datastart, '\n', sizeof(buffer)-(datastart-buffer));
			} else {
				nlpos = dataend;
			}
		}
		len = dataend-datastart;
		memcpy(buffer, datastart, len);
		bufpos = buffer + len;

	}
	return 0;
}

static int SocketThread(void *ptr)
{
	int err=0;
	int yes=1;
	printf("Socket thread started\n");
	
	// listen for both IPv4 and IPv6
	memset(&hints, 0, sizeof(hints));
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE;
	
	if ((err=getaddrinfo("localhost", "60589" /* 0xECAD */, &hints, &res))) {
		perror("getaddrinfo");
		return err;
	}
	
	listenfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if (listenfd == -1) {
		perror("socket");
		return errno;
	}
	
	// allow restarting the server without leaving socket in TIME_WAIT
	if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int))<0) {
		perror("setsockopt SO_REUSEADDR");
		return errno;
	}
	
	if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEPORT, &yes, sizeof(int))<0) {
		perror("setsockopt SO_REUSEADDR");
		return errno;
	}
	
	if (bind(listenfd, res->ai_addr, res->ai_addrlen)) {
		perror("bind");
		return errno;
	}
	
	if (listen(listenfd, 1)) {
		perror("listen");
		return errno;
	}
	
	remote_size = sizeof(remote_addr);

	while (1) {
		connfd = accept(listenfd, (struct sockaddr *)&remote_addr, &remote_size);
		if (connfd == -1) {
			perror("accept");
			return errno;
		}
		if (setsockopt(connfd, IPPROTO_TCP, TCP_NODELAY, &yes, sizeof(int))<0) {
			perror("setsockopt TCP_NODELAY");
			return errno;
		}


		printf("Connection\n");	
		cdb_control_channel(connfd);
		close(connfd);
	}
	close(listenfd);
	
	printf("Socket thread endend\n");
	return 0;
}


void cdb_fb_setup()
{
	unsigned int i;
	/* XXX Do some SDL kind of things. For now, just be the buffer part of a framebuffer */
	if (!fb)
		fb = (int *) malloc(64*1024*1024);
	
	if (!fb)
		return;
	
	if (sdl_ren)
		SDL_DestroyRenderer(sdl_ren);
	
	if (sdl_win)
		SDL_DestroyWindow(sdl_win);
	
	if (sdl_fb)
		SDL_FreeSurface(sdl_fb);

	if (SDL_Init(SDL_INIT_VIDEO) != 0){
		printf("SDL_Init Error: %s" , SDL_GetError());
		return;
	}
	
	sdl_win = SDL_CreateWindow("Framebuffer", 100, 100, 480, 272, SDL_WINDOW_SHOWN);
	if (!sdl_win){
		printf("SDL_CreateWindow Error: %s", SDL_GetError());
		SDL_Quit();
		return;
	}
	
	sdl_ren = SDL_CreateRenderer(sdl_win, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);
	if (!sdl_ren){
		SDL_DestroyWindow(sdl_win);
		printf("SDL_CreateRenderer Error: %s", SDL_GetError());
		SDL_Quit();
		return;
	}
	
	sdl_fb = SDL_CreateRGBSurfaceFrom(fb, 480, 272, 16, 960, 0xf800, 0x7e0, 0x1f, 0);
	if(!sdl_fb) {
		SDL_DestroyRenderer(sdl_ren);
		SDL_DestroyWindow(sdl_win);
		printf("SDL_Surface Error: %s", SDL_GetError());
		SDL_Quit();
		return;
	}
	
	SDL_AddEventWatch(cdb_keystroke_receiver, NULL);
	
	sdl_thread = SDL_CreateThread(SocketThread, "CDB simulator socket thread", (void*) NULL);
	if (sdl_thread == NULL) {
		printf("\nSDL_CreateThread failed: %s\n", SDL_GetError());
	}

	cdb_fb_render();
}


int main(void)
{
//	SocketThread(NULL);
	cdb_fb_setup();
	while (!cdb_event_check()) {
		cdb_fb_render();
	}
}
