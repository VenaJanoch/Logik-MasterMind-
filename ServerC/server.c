#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>
#include "Users.h"

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//==========================================================================

#define SRV_PORT 22434
#define MAX_CONECTED 10

//--------------------------------------------------------------------------

User_conected* conected_users[MAX_CONECTED];
User_database* database_users[MAX_CONECTED];
pthread_mutex_t lock;

int sgetline(int fd, char ** out) {
	int buf_size = 128;
	int bytesloaded = 0;
	int ret;
	char buf;
	char * buffer = malloc(buf_size);
	char * newbuf;

	if (NULL == buffer)
		return -1;

	while (1) {
		// read a single byte
		ret = read(fd, &buf, 1);
		if (ret < 1) {
			// error or disconnect
			free(buffer);
			return -1;
		}

		// has end of line been reached?
		if (buf == '\n') {
			break; // yes
		}

		buffer[bytesloaded] = buf;
		bytesloaded++;
		// is more memory needed?
		if (bytesloaded >= buf_size) {
			buf_size += 128;
			newbuf = realloc(buffer, buf_size);

			if (NULL == newbuf) {
				free(buffer);
				return -1;
			}

			buffer = newbuf;
		}
	}

	// if the line was terminated by "\r\n", ignore the
	// "\r". the "\n" is not in the buffer
	if ((bytesloaded) &&(buffer[bytesloaded - 1] == '\r'))
		bytesloaded--;

	*out = buffer; // complete line
	return bytesloaded; // number of bytes in the line, not counting the line break
}

void pull_user(User_conected* user) {

	pthread_mutex_lock(&lock);
	conected_users[user->id] = NULL;
	pthread_mutex_unlock(&lock);

	free(user);

}

User_conected* put_user(int socket) {
	User_conected* user = malloc(sizeof(User_conected));

	memset(user, 0, sizeof(User_conected));

	user->socket = socket;

	pthread_mutex_lock(&lock);
	int i;
	for (i = 0; i < MAX_CONECTED; ++i) {
		if (conected_users[i] == NULL) {
			conected_users[i] = user;
			user->id = i;

			break;
		}
	}

	pthread_mutex_unlock(&lock);

	return user;

}

void send_message(User_conected* user, char* message) {

	send(user->socket, message, strlen(message), 0);

}

void reg_user(char* buffer, User_conected* user) {

	char *ret = strchr(buffer, ',');

	*ret = '\0';

	ret++;
	pthread_mutex_lock(&lock);
	int i;
	for (i = 0; i < MAX_CONECTED; ++i) {

		if (database_users[i] != NULL
				&& strcmp(database_users[i]->nickname, buffer) == 0) {

			send_message(user, "Registrace,bad,This nickname is exist\n");

			pthread_mutex_unlock(&lock);
			return;

		}

	}

	User_database* reg_user = malloc(sizeof(User_database));

	memset(reg_user, 0, sizeof(User_database));

	strcpy(reg_user->nickname, buffer);
	strcpy(reg_user->passwd, ret);

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (database_users[i] == NULL) {

			database_users[i] = reg_user;

			break;
		}
	}

	pthread_mutex_unlock(&lock);
	send_message(user, "Registrace,yes\n");
	printf("nickname %s \n", reg_user->nickname);
}

int is_bad_passwd(char* passwd) {

	int i = 0;

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (database_users[i] != NULL) {

			if (strcmp(database_users[i]->passwd, passwd) == 0) {

				return 1;
			}
		}
	}

	return 0;

}

int is_bad_loggin(char* log) {

	int i = 0;

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (database_users[i] != NULL) {
			if (strcmp(database_users[i]->nickname, log) == 0) {

				return 1;
				printf("bad log %s  ", database_users[i]->nickname);
			}
		}
	}

	printf("bad %s  ", database_users[i]->nickname);

	return 0;

}

void log_control(User_conected* user, char* buffer) {

	char *ret = strchr(buffer, ',');

	*ret = '\0';

	ret++;

	int i = 0;
	for (i = 0; i < MAX_CONECTED; ++i) {

		if (database_users[i] != NULL) {
			if (strcmp(database_users[i]->nickname, buffer) == 0
					&& strcmp(database_users[i]->passwd, ret) == 0) {

				printf("Prihlaseny uzivatel %s %d \n", buffer, i);

				user->isLog = 1;
				strcpy(user->nickname, buffer);
				send_message(user, "Log,yes\n");

				break;

			}

		}
	}

	if (user->isLog == 0) {
		if (is_bad_loggin(buffer) == 0) {
			send_message(user, "Log,no,badLog\n");
			printf("spatne jmeno %s  \n", buffer);

		} else {
			send_message(user, "Log,no,badPasswd\n");
		}
	}

}

void send_free_players(User_conected* user) {

	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char strednik[2];

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "PlayerList,");
	strcpy(strednik, ";");
	pthread_mutex_lock(&lock);

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (conected_users[i]->isLog == 1
					&& strcmp(conected_users[i]->nickname, user->nickname)
							!= 0) {

				finish = strcat(message, conected_users[i]->nickname);
				message = strcat(finish, strednik);

			}
		}
	}

	finish = strcat(message, "\n");
	printf("zprava %s \n", finish);
	pthread_mutex_unlock(&lock);

	send_message(user, finish);

}

void send_invitation(User_conected* user, char* player) {
	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "ChoosePlayer,");

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {

				finish = strcat(message, user->nickname);
				message = strcat(finish, ",invite\n");

				send_message(conected_users[i], message);
			}

		}

	}

}

void *createThread(void *incoming_socket) {

	char* buffer = malloc(1024);
	int socket = *(int *) incoming_socket;

	size_t buffersize = sizeof(buffer);
	User_conected* user = put_user(socket);

	while (1) {
		// recv_count = recv(socket, buffer, sizeof(buffer), 0);

		int ret = sgetline(socket, &buffer);
		if (ret < 0)
			break; // error/disconnect

		// is it a 0-length line?
		if (ret == 0)

		{
			printf("End of Headers detected.\n");

			break;

		}

		printf("obsah  %s \n", buffer);

		char *ret2 = strchr(buffer, ',');

		*ret2 = '\0';

		ret2++;

		if (strcmp(buffer, "Registrace") == 0) {
			reg_user(ret2, user);
		} else if (strcmp(buffer, "Log") == 0) {

			log_control(user, ret2);
		} else if (strcmp(buffer, "LogOut") == 0) {

			user->isLog = 0;
		} else if (strcmp(buffer, "PlayerList") == 0) {

			send_free_players(user);

		} else if (strcmp(buffer, "ChoosePlayer") == 0) {

			send_invitation(user, ret2);

		}

	}

	free(buffer);
	pull_user(user);
	close(socket);
	return NULL;
}

void print_err(char* msg) {
	printf("error (%i, %s): %s\n", errno, strerror(errno), msg);
}

//--------------------------------------------------------------------------

//--------------------------------------------------------------------------

int main(int argc, char** argv) {
	memset(conected_users, 0, sizeof(User_conected*) * MAX_CONECTED);

	int sock = -1, incoming_sock = -1;
	int optval = -1;
	struct sockaddr_in addr, incoming_addr;
	unsigned int incoming_addr_len = 0;

	if (pthread_mutex_init(&lock, NULL) != 0) {
		printf("\n mutex init failed\n");
		return 1;
	}
	/* create socket */
	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (sock < 0) {
		print_err("socket()");
		return 1;
	}

	/* set reusable flag */
	optval = 1;
	setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));

	/* prepare inet address */
	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(SRV_PORT);
	addr.sin_addr.s_addr = htonl(INADDR_ANY); /* listen on all interfaces */
	if (bind(sock, (struct sockaddr*) &addr, sizeof(addr)) < 0) {
		print_err("bind");
		return 1;
	}

	if (listen(sock, 10) < 0) {
		print_err("listen");
		return 1;
	}

	for (;;) {
		incoming_addr_len = sizeof(incoming_addr);
		incoming_sock = accept(sock, (struct sockaddr*) &incoming_addr,
				&incoming_addr_len);
		if (incoming_sock < 0) {
			print_err("accept");
			close(sock);
			continue;
		}

		printf("connection from %s:%i\n", inet_ntoa(incoming_addr.sin_addr),
				ntohs(incoming_addr.sin_port));

		pthread_t thread;

		/* create a second thread which executes inc_x(&x) */
		if (pthread_create(&thread, NULL, createThread, &incoming_sock)) {

			fprintf(stderr, "Error creating thread\n");
			return 1;

		}

	}

	pthread_mutex_destroy(&lock);
	return 0;
}

