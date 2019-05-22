#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <sys/socket.h>

#define BUF_SIZE 1024
#define OPSZ 4

void error_handling(char * message)
{
	fputs(message,stderr);
	fputc('\n',stderr);
	exit(1);
}

int calculate(int opnum,int opnds[], char operator);

int main(int argc,char * argv[])
{
	int serv_sock,clnt_sock;
	char opinfo[BUF_SIZE];
	int result,opnd_cnt;
	int recv_cnt, recv_len;

	struct sockaddr_in serv_adr,clnt_adr;
	socklen_t clnt_adr_sz;
	if(argc!=2){
		printf("Usage: %s <port>\n",argv[0]);
		exit(1);
	}

	serv_sock = socket(PF_INET,SOCK_STREAM,0);
	if(serv_sock==-1)
	{
		error_handling("socket() error");
	}

	memset(&serv_adr,0,sizeof(serv_adr));
	serv_adr.sin_family = AF_INET;
	serv_adr.sin_addr.s_addr = htonl(INADDR_ANY);
	serv_adr.sin_port = htons(atoi(argv[1]));

	if(bind(serv_sock,(struct sockaddr*)&serv_adr,sizeof(serv_adr))==-1){
		error_handling("bind() error");
	}

	if(listen(serv_sock,5)==-1){
		error_handling("listen() error");
	}
	clnt_adr_sz = sizeof(clnt_adr);

	for(int i=0;i<5;i++){
		opnd_cnt = 0;
		clnt_sock = accept(serv_sock,(struct sockaddr*)&clnt_adr,&clnt_adr_sz);
		if(clnt_sock == -1){
			error_handling("accept() error");
		}
		read(clnt_sock,&opnd_cnt,1);
		recv_len = 0;

		while((opnd_cnt*OPSZ+1)>recv_len){
			recv_cnt = read(clnt_sock,&opinfo[recv_len],BUF_SIZE-1);
			recv_len += recv_cnt;
			printf("test");			
		}

		result = calculate(opnd_cnt,(int*)opinfo,opinfo[recv_len-1]);
		write(clnt_sock,(char*)&result,sizeof(result));
		close(clnt_sock);
	}
	close(serv_sock);
	return 0;
}

int calculate(int opnum, int opnds[], char op){
	int result = opnds[0];
	switch(op){
		case '+':
		for(int i=1;i<opnum;i++){
			result+=opnds[i];
		}
		break;

		case '-':
		for(int i=1;i<opnum;i++){
			result-=opnds[i];
		}
		break;
		
		case '*':
		for(int i=1;i<opnum;i++){
			result*=opnds[i];
		}
		break;
	}
	return result;
}
