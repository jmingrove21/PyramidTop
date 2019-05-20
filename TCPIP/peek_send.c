#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>

void error_handling(char * message){
	fputs(message,stderr);
	fputc('\n',stderr);
	exit(1);
}

int main(int argc, char * argv[])
{
	int sock;
	struct sockaddr_in serv_adr;
	
	if(argc!=3){
		printf("Usage: %s <IP> <port>\n",argv[0]);
		exit(1);
	}

	sock = socket(PF_INET,SOCK_STREAM,0);
	if(sock==-1){
		error_handling("socket() error");
	}
	memset(&serv_adr, 0, sizeof(serv_adr));
	serv_adr.sin_family = AF_INET;
	serv_adr.sin_addr.s_addr = inet_addr(argv[1]);
	serv_adr.sin_port = htons(atoi(argv[2]));

	if(connect(sock,(struct sockaddr*)&serv_adr,sizeof(serv_adr))==-1){
		error_handling("connect() error");
	}

	write(sock,"123",strlen("123"));
	close(sock);
	return 0;
}
