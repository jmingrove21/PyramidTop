#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define BUF_SIZE 1024

void error_handling(char * message){
	fputs(message,stderr);
	fputc('\n',stderr);
	exit(1);
}

int main(int argc, char * argv[]){
	int sock;
	struct sockaddr_in adr;
	int str_len;
	char buf[BUF_SIZE];

	FILE * readfp;
	FILE * writefp;

	if(argc!=3){
		printf("Usage: %s <IP> <port>\n",argv[0]);
		exit(1);
	}

	sock = socket(PF_INET,SOCK_STREAM,0);
	if(sock == -1){
		error_handling("socket() error");
	}
	memset(&adr,0,sizeof(adr));
	adr.sin_family = AF_INET;
	adr.sin_addr.s_addr = inet_addr(argv[1]);
	adr.sin_port = htons(atoi(argv[2]));

	if(connect(sock,(struct sockaddr*)&adr,sizeof(adr))==-1){
		error_handling("connect() error");
	}
	else{
		puts("Connected..........");
	}

	readfp = fdopen(sock,"r");
	writefp = fdopen(sock,"w");
	while(1){
		fputs("Input message(Q to quit): ",stdout);
		fgets(buf,BUF_SIZE,stdin);
		if(!strcmp(buf,"q\n") || !strcmp(buf,"Q\n")){
			break;
		}

		fputs(buf,writefp);
		fflush(writefp);
		fgets(buf,BUF_SIZE,readfp);
		printf("Message from server: %s",buf);
	}
	fclose(readfp);
	fclose(writefp);
	return 0;
}
