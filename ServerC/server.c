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
#define COUNT_KNOBS 4
#define COUNT_Game 10

//--------------------------------------------------------------------------

User_conected* conected_users[MAX_CONECTED];
User_database* database_users[MAX_CONECTED];
pthread_t threads[MAX_CONECTED];
int lastInt = -1;

Game* game[MAX_CONECTED];
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

	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	int i;
	char strednik[2];

	strcpy(message, "PlayerList,");
	strcpy(strednik, ";");

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

	send_message(user, finish);

	//free(message);
	//free(finish);

}

void send_invitation(User_conected* user, char* player) {
	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Challenge,");

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {

				finish = strcat(message, user->nickname);
				message = strcat(finish, ",invite,");
				finish = strcat(finish, ",invite,");

				send_message(conected_users[i], message);
			}

		}

	}

	//free(message);
	//free(finish);

}

void find_free_game(User_conected* player, User_conected* chellanger) {

	int i;

	Game* game1 = malloc(sizeof(Game));

	memset(game1, 0, sizeof(Game));

	game1->gamer1 = player->id;
	game1->gamer2 = chellanger->id;
	player->game = game1;
	chellanger->game = game1;

	for (i = 0; i < MAX_CONECTED; i++) {

		if (game[i] == NULL) {

			game1->id = i;
			game[i] = game1;

		}
	}
}

void send_accept_chellange(User_conected* user, char* player) {
	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Challenge,");
	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {
				find_free_game(conected_users[i], user);
				finish = strcat(message, player);
				message = strcat(finish, ",accept\n");
				send_message(conected_users[i], message);
			}

		}

	}

	//free(message);
	//free(finish);

}

void send_refuse_chellange(User_conected* user, char* player) {
	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Challenge,");

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {

				finish = strcat(message, player);
				message = strcat(finish, ",refuse\n");

				send_message(conected_users[i], message);

			}

		}

	}

	//free(message);
	//free(finish);

}

void cut_result(char* message, Game* game) {

	game->result.identifikace = 100;

	char *ret1 = strchr(message, ';');
	*ret1 = '\0';
	ret1++;

	char *ret2 = strchr(ret1, ';');
	*ret2 = '\0';
	ret2++;

	char *ret3 = strchr(ret2, ';');
	*ret3 = '\0';
	ret3++;

	game->result.colors[0] = atoi(message);
	game->result.colors[1] = atoi(ret1);
	game->result.colors[2] = atoi(ret2);
	game->result.colors[3] = atoi(ret3);
	game->result.free = 1;

}

void cut_colors(char* message, Game* game) {

	char *identifikaceCh = strchr(message, ',');
	*identifikaceCh = '\0';
	identifikaceCh++;

	int identifikaceI = atoi(message);

	game->knobs[identifikaceI].identifikace = identifikaceI;

	char *ret1 = strchr(identifikaceCh, ';');
	*ret1 = '\0';
	ret1++;

	char *ret2 = strchr(ret1, ';');
	*ret2 = '\0';
	ret2++;

	char *ret3 = strchr(ret2, ';');
	*ret3 = '\0';
	ret3++;

	game->knobs[identifikaceI].colors[0] = atoi(identifikaceCh);
	game->knobs[identifikaceI].colors[1] = atoi(ret1);
	game->knobs[identifikaceI].colors[2] = atoi(ret2);
	game->knobs[identifikaceI].colors[3] = atoi(ret3);
	game->knobs[identifikaceI].free = 1;

}

void result_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Game,colorResult,");
	finish = strcat(message, message1);

	message = strcat(finish, "\n");

	send_message(conected_users[game1->gamer1], message);
	cut_result(message1, game1);
	printf("zprava %s %d \n", message, game1->result.colors[0]);

	//free(message);
	//free(finish);
}

void good_color_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* messagePom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(messagePom, message1);

	char *identifikaceCh = strchr(message1, ',');

	*identifikaceCh = '\0';
	identifikaceCh++;

	int identifikaceI = atoi(message1);


	game1->good_color[identifikaceI].identifikace = identifikaceI;
	game1->good_color[identifikaceI].good_color = atoi(identifikaceCh);
	game1->good_color[identifikaceI].free = 1 ;

	strcpy(message, "Game,goodColors,");
	finish = strcat(message, messagePom);

	message = strcat(finish, "\n");
	printf("good %s \n", message);
	send_message(conected_users[game1->gamer2], message);

	//free(message);
	//free(finish);
}

void great_color_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* messagePom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(messagePom, message1);

	char *identifikaceCh = strchr(message1, ',');

	*identifikaceCh = '\0';
	identifikaceCh++;

	int identifikaceI = atoi(message1);
	game1->great_color[identifikaceI].identifikace = identifikaceI;
	game1->great_color[identifikaceI].great_color = atoi(identifikaceCh);
	game1->great_color[identifikaceI].free = 1;

	strcpy(message, "Game,greatColors,");
	finish = strcat(message, messagePom);

	message = strcat(finish, "\n");
	printf("great %s \n", message);

	send_message(conected_users[game1->gamer2], message);

	//free(message);
	//free(finish);
}

void knobs_panel_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Game,knobPanel,");
	finish = strcat(message, message1);

	message = strcat(finish, "\n");
	printf("konbs %s\n", message);
	send_message(conected_users[game1->gamer2], message);
	cut_colors(message1, game1);
	//free(message);
	//free(finish);
}

void game_is_over(User_conected* user) {

	Game* game1 = user->game;

	send_message(conected_users[game1->gamer2], "Game,gameOver,\n");
	free(game1);

}

void game_is_done(User_conected* user, char* message1) {

	Game* game1 = user->game;
	send_message(conected_users[game1->gamer2], "Game,gameDone,\n");
	free(game1);
}

void reload_game(Game* game, User_conected* user) {

	char* player = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* good_colors = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* great_colors = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* knob_panel = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(player, "Game,player,");

	if (strcmp(conected_users[game->gamer1]->nickname, user->nickname) == 0) {

		strcat(player, "challenger,");
		strcat(player, conected_users[game->gamer1]->nickname);
		strcat(player, "\n");
	} else {
		strcat(player, "challenger,");
		strcat(player, conected_users[game->gamer2]->nickname);
		strcat(player, "\n");
	}

	send_message(user, player);

	int i = 0; int j;

	for (i = 0; i < COUNT_Game; ++i) {
		strcpy(good_colors, "Game,goodColors,");
		strcpy(great_colors, "Game,greatColors,");
		strcpy(knob_panel, "Game,knobPanel,");


		if (game->knobs[i].free == 1) {

			sprintf(pom, "%d", game->good_color[i].identifikace);

			printf(" %s %d\n", pom, game->knobs[i].free);

			strcat(good_colors, pom);
			sprintf(pom, "%d", game->good_color[i].good_color);
			strcat(good_colors, ",");
			strcat(good_colors, pom);
			strcat(good_colors, ",\n");

			sprintf(pom, "%d", game->great_color[i].identifikace);
			strcat(great_colors, pom);
			sprintf(pom, "%d", game->great_color[i].great_color);
			strcat(great_colors, ",");
			strcat(great_colors, pom);
			strcat(great_colors, ",\n");


			sprintf(pom, "%d", game->knobs[i].identifikace);
			strcat(knob_panel, pom);
			strcat(knob_panel, ",");

			for (j = 0; j < COUNT_KNOBS; ++j) {

				sprintf(pom, "%d", game->knobs[i].colors[j]);
				strcat(knob_panel, pom);
				strcat(knob_panel, ";");

			}

			strcat(knob_panel, "\n");


		}

	}
			printf(" %s \n", good_colors);

			printf(" %s \n", great_colors);
			printf(" %s \n", knob_panel);

}

void check_game(User_conected* user) {

	int i;
	for (i = 0; i < MAX_CONECTED; ++i) {

		if (strcmp(conected_users[game[i]->gamer2]->nickname, user->nickname)
				== 0
				|| strcmp(conected_users[game[i]->gamer1]->nickname,
						user->nickname) == 0) {

			printf("user %s \n", user->nickname);

			reload_game(game[i], user);

			break;

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

		} else if (strcmp(buffer, "CheckGame") == 0) {
			printf("check \n");
			check_game(user);

		} else if (strcmp(buffer, "Log") == 0) {

			log_control(user, ret2);
		} else if (strcmp(buffer, "LogOut") == 0) {

			user->isLog = 0;
			if (strcmp(ret2, "end") == 0) {

				pthread_join(pthread_self(), PTHREAD_CANCELED);
			}

		} else if (strcmp(buffer, "PlayerList") == 0) {

			send_free_players(user);

		} else if (strcmp(buffer, "Challenge") == 0) {

			char *ret3 = strchr(ret2, ',');

			*ret3 = '\0';

			ret3++;

			if (strcmp(ret2, "accept") == 0) {
				printf("invite\n");
				send_accept_chellange(user, ret3);
			} else if (strcmp(ret2, "refuse") == 0) {
				send_refuse_chellange(user, ret3);
			} else if (strcmp(ret2, "invite") == 0) {
				send_invitation(user, ret3);
			}
		} else if (strcmp(buffer, "Game") == 0) {

			char *ret3 = strchr(ret2, ',');

			*ret3 = '\0';

			ret3++;

			if (strcmp(ret2, "colorResult") == 0) {

				result_to_game(user, ret3);

			} else if (strcmp(ret2, "goodColors") == 0) {

				good_color_to_game(user, ret3);

			} else if (strcmp(ret2, "greatColors") == 0) {

				great_color_to_game(user, ret3);

			} else if (strcmp(ret2, "knobPanel") == 0) {

				knobs_panel_to_game(user, ret3);

			} else if (strcmp(ret2, "gameOver") == 0) {

			}
		}

	}

	free(buffer);
	pull_user(user);
	close(socket);
	return NULL;
}

int find_free_index() {

	pthread_mutex_lock(&lock);
	int i;
	for (i = 0; i < MAX_CONECTED; ++i) {
		if (conected_users[i] == NULL) {

			return i;

		}
	}

	pthread_mutex_unlock(&lock);

	return -1;

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


