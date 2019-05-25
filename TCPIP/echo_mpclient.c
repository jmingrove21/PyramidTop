#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define BUF_SIZE 30

void error_handling(char * message){
	fputs(message,stderr);
	fputc('\n',stderr);
	exit(1);
}

void read_routine(int sock,char*buf){
	while(1){
		int str_len = read(sock,buf,BUF_SIZE);
		if(str_len==0){
			return;
		}

		buf[str_len]=0;
		printf("Message from server: %s\n",buf);
	}
}

void write_routine(int sock,char*buf){
	while(1){
		fgets(buf,BUF_SIZE,stdin);
		if(!strcmp(buf,"q\n")||!strcmp(buf,"Q\n")){
			shutdown(sock,SHUT_WR);
			return;
		}
		write(sock,buf,strlen(buf));
	}
}

int main(int argc,char * argv[]){
	int sock;
	struct sockaddr_in adr;

	char buf[BUF_SIZE];

	pid_t pid;

	if(argc!=3){
		printf("Usage: %s <IP> <port>\n",argv[0]);
		exit(1);
	}

	sock = socket(PF_INET,SOCK_STREAM,0);
	if(sock==-1){
		error_handling("socket() error");
	}

	memset(&adr,0,sizeof(adr));
	adr.sin_family = AF_INET;
	adr.sin_addr.s_addr = inet_addr(argv[1]);
	adr.sin_port = htons(atoi(argv[2]));

	if(connect(sock,(struct sockaddr*)&adr,sizeof(adr))==-1){
		error_handling("connect() error");
	}

	pid = fork();
	if(pid==0){
		write_routine(sock, buf);
	}else{
		read_routine(sock,buf);
	}
	close(sock);
	return 0;
}
