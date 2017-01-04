#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>
#include <unistd.h>
#include <signal.h>
#include "Users.h"

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

//==========================================================================

#define MAX_CONECTED 10
#define COUNT_KNOBS 4
#define COUNT_Game 10

//--------------------------------------------------------------------------

User_conected* conected_users[MAX_CONECTED];
User_database* database_users[MAX_CONECTED];
pthread_t threads[MAX_CONECTED];
FILE *file = NULL;
int lastInt = -1;
int srv_port = 22434;
char address[20];
int is_address = 1;

Game* game[MAX_CONECTED];
pthread_mutex_t lock;

/*
 * void help();
 *
 * Vypise jednoduchy navod jak spustit program z prikazove radky
 */
void help() {
	printf(
			"Usage:\n   port \"<port>\" where \"<port>\" is number of server port\n"
					"address \"<address>\" where \"<address>\" is address of server for example 10.10.10.38 \n");

}

void sigint_handler(int sig)
{
    /*do something*/
    printf("killing process %d\n",getpid());
	fclose(file);
    exit(0);
}

void nacti_soubor() {

	file = fopen("logy.log", "w");
	printf("open file\n");
	if (file == NULL) {
		printf("ERR: Non-existent file!");
	}

}

/*
 * nacti_port(int argc, char **argv)
 *
 * argc pocet zadanych parametru
 * argv pole se zadanymi parametry
 *
 * Overi zde byl opravdu zadan parametr se jmenem souboru
 * Nacte soubor pokud existuje
 * Overi existenci souboru
 */
void nacti_Port(int argc, char **argv) {

	if (argc == 3) {
		srv_port = atoi(argv[1]);
	}
		fprintf(file,"Server bezi na portu: %d\n", srv_port);
}

void read_address(int argc, char **argv) {

	if (argc == 3) {

		strcpy(address, argv[2]);
		is_address = 0;

	}

}
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
	conected_users[user->id] = (User_conected*) NULL;
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

int nickname_control(char* nickname) {
	if (strlen(nickname) > 30) {
		return 0;
	}
	return 1;
}

int passwd_control(char* passwd) {
	if (strlen(passwd) > 32) {
		return 0;
	}
	return 1;
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

			send_message(user, "Registrace,bad1,This nickname is exist\n");
			return;

		}

	}

	User_database* reg_user = malloc(sizeof(User_database));
	memset(reg_user, 0, sizeof(User_database));

	if (nickname_control(buffer) == 1 && passwd_control(ret) == 1) {
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
		fprintf(file,"Registrace uzivatele: %s\n", user->nickname);
	} else {
		send_message(user, "Registrace,bad2,\n");

	}

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
			}
		}
	}

	return 0;

}

int find_game(User_conected* user) {

	int i;
	for (i = 0; i < MAX_CONECTED; ++i) {
		if (game[i] != NULL) {
			if (strcmp(game[i]->chellanger, user->nickname) == 0
					|| strcmp(game[i]->player, user->nickname) == 0) {
				return i;

			}
		}
	}
	return -1;
}

void send_good_colors(Game* game, User_conected* user, int i) {

	char* good_colors = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(good_colors, "Game,goodColors,");
	sprintf(pom, "%d", game->good_color[i].identifikace);
	strcat(good_colors, pom);
	sprintf(pom, "%d", game->good_color[i].good_color);
	strcat(good_colors, ",");
	strcat(good_colors, pom);
	strcat(good_colors, ",\n");

	send_message(user, good_colors);
}

void send_great_colors(Game* game, User_conected* user, int i) {

	char* great_colors = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	strcpy(great_colors, "Game,greatColors,");
	sprintf(pom, "%d", game->great_color[i].identifikace);
	strcat(great_colors, pom);
	sprintf(pom, "%d", game->great_color[i].great_color);
	strcat(great_colors, ",");
	strcat(great_colors, pom);
	strcat(great_colors, ",\n");

	send_message(user, great_colors);
}
void send_knobs_colors(User_conected* user, char* knob_panel) {
	send_message(user, knob_panel);
}
void send_player(Game* game, User_conected* user) {

	char* player = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

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

	sleep(1);
}
void send_result_colors(Game* game, User_conected* user) {

	char* result_colors = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	int i;
	strcpy(result_colors, "Game,colorResult,R,");
	for (i = 0; i < COUNT_KNOBS; ++i) {
		sprintf(pom, "%d", game->result.colors[i]);
		strcat(result_colors, pom);
		strcat(result_colors, ";");

	}

	strcat(result_colors, ";\n");

	send_message(user, result_colors);
}
void reload_game(char* buffer, User_conected* user) {

	char* knob_panel = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	user->game = game[atoi(buffer)];

	Game* game = user->game;

	send_player(game, user);

	int i = 0;
	int j;

	for (i = 0; i < COUNT_Game; ++i) {
		if (game->knobs[i].free == 1) {
			strcpy(knob_panel, "Game,knobPanel,");

			sprintf(pom, "%d", game->knobs[i].identifikace);
			strcat(knob_panel, pom);
			strcat(knob_panel, ",");

			for (j = 0; j < COUNT_KNOBS; ++j) {

				sprintf(pom, "%d", game->knobs[i].colors[j]);
				strcat(knob_panel, pom);
				strcat(knob_panel, ";");

			}
			send_result_colors(game, user);
			sleep(1);
			strcat(knob_panel, "\n");
			send_knobs_colors(user, knob_panel);

			sleep(1);
			send_good_colors(game, user, i);

			sleep(1);
			send_great_colors(game, user, i);

		}

	}
}

void check_game(User_conected* user) {
	int i = find_game(user);

	if (i != -1) {
		char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
		char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

		sprintf(pom, "%d", i);

		strcpy(message, "Reload,");
		strcat(message, pom);
		strcat(message, ",");

		if (strcmp(conected_users[game[i]->gamer1]->nickname, user->nickname)
				== 0) {
			strcat(message, conected_users[game[i]->gamer2]->nickname);
			strcat(message, ",1,\n");
			send_message(user, message);
		} else {
			strcat(message, conected_users[game[i]->gamer1]->nickname);
			strcat(message, ",0,\n");

			send_message(user, message);
		}
	}
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

				user->isLog = 1;
				strcpy(user->nickname, buffer);
				send_message(user, "Log,yes\n");
				printf("Prihlaseny uzivatel %s %d \n", buffer, i);
				fprintf(file,"Prihlaseny uzivatel: %s\n", user->nickname);
				break;
			}
		}
	}

	if (user->isLog == 0) {
		if (is_bad_loggin(buffer) == 0) {
			send_message(user, "Log,no,badLog\n");
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
	send_message(user, finish);
	sleep(1);
	check_game(user);

//free(message);
//free(finish);

}

void send_invitation(User_conected* user, char* player) {
	int i;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* finish = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Challenge,");
	sleep(1);
	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {
				finish = strcat(message, user->nickname);
				message = strcat(finish, ",invite,\n");
				finish = strcat(finish, ",invite,\n");

				send_message(conected_users[i], message);
			}

		}

	}

//free(message);
//free(finish);

}

void find_free_game(User_conected* player, User_conected* chellanger) {

	pthread_mutex_lock(&lock);

	int i;
	for (i = 0; i < MAX_CONECTED; i++) {
		if (game[i] == NULL) {
			game[i] = malloc(sizeof(Game));
			memset(game[i], 0, sizeof(Game));
			game[i]->gamer1 = player->id;
			game[i]->gamer2 = chellanger->id;
			strcpy(game[i]->chellanger, chellanger->nickname);
			strcpy(game[i]->player, player->nickname);

			player->game = game[i];
			chellanger->game = game[i];

			game[i]->free = 1;
			game[i]->id = i;
			break;
		}
	}

	pthread_mutex_unlock(&lock);

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
	strcpy(message, "Challenge,");

	for (i = 0; i < MAX_CONECTED; ++i) {

		if (conected_users[i] != NULL) {
			if (strcmp(conected_users[i]->nickname, player) == 0) {

				message = strcat(message, player);
				message = strcat(message, ",refuse\n");

				send_message(conected_users[i], message);

			}

		}

	}

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

	strcpy(message, "Game,colorResult,");
	message = strcat(message, message1);
	message = strcat(message, "\n");

	send_message(conected_users[game1->gamer1], message);
	cut_result(message1, game1);

}

void good_color_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* messagePom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(messagePom, message1);

	char *identifikaceCh = strchr(message1, ',');

	*identifikaceCh = '\0';
	identifikaceCh++;

	int identifikaceI = atoi(message1);

	game1->good_color[identifikaceI].identifikace = identifikaceI;
	game1->good_color[identifikaceI].good_color = atoi(identifikaceCh);
	game1->good_color[identifikaceI].free = 1;

	strcpy(message, "Game,goodColors,");
	message = strcat(message, messagePom);
	message = strcat(message, "\n");

	send_message(conected_users[game1->gamer2], message);

}

void great_color_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
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
	message = strcat(message, messagePom);

	message = strcat(message, "\n");
	send_message(conected_users[game1->gamer2], message);

}

void knobs_panel_to_game(User_conected* user, char* message1) {

	Game* game1 = user->game;
	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	strcpy(message, "Game,knobPanel,");
	message = strcat(message, message1);

	message = strcat(message, "\n");
	send_message(conected_users[game1->gamer2], message);
	cut_colors(message1, game1);

}

void game_is_over(User_conected* user) {

	Game* game1 = user->game;
	send_message(conected_users[game1->gamer2], "Game,gameOver,\n");
	}

void game_is_done(User_conected* user) {

	Game* game1 = user->game;
	send_message(conected_users[game1->gamer2], "Game,gameDone,\n");
}

void leave_game(User_conected* user, char* message1) {

	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);

	sprintf(pom, "%d", user->game->id);
	strcpy(message, "Game,leave,");
	strcat(message, user->nickname);
	strcat(message, ",");
	strcat(message, pom);
	strcat(message, ",\n");

	int index;

	if(user->game != NULL){
	if (strcmp(user->game->chellanger, user->nickname) == 0) {
		index = user->game->gamer1;

	} else {
		index = user->game->gamer2;
	}

	send_message(conected_users[index], message);
	index = user->game->id;
	free(game[index]);
	game[index] = NULL;
	}
}

void logout_user(User_conected* user, char* ret2) {

	char* message = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	char* pom = (char*) malloc(MAX_CONECTED * 30 + MAX_CONECTED);
	if(user->game != NULL){
	if (user->game->free == 1) {

		sprintf(pom, "%d", user->game->id);
		strcpy(message, "Logout,");
		strcat(message, pom);
		strcat(message, ",\n");

		user->isLog = 0;
		int index;

		if (strcmp(user->game->chellanger, user->nickname) == 0) {
			index = user->game->gamer1;
		} else {
			index = user->game->gamer2;
		}

		printf("message %s %d", pom, index);
		fprintf(file,"Odhlasen uzivatel :%s \n", user->nickname);
		send_message(conected_users[index], message);
	} else {
		user->isLog = 0;
	}

	}
	if (strcmp(ret2, "end") == 0) {
		fprintf(file,"Server konec vlakna \n");
		pthread_join(pthread_self(), PTHREAD_CANCELED);
	}

}
void *createThread(void *incoming_socket) {

	char* buffer = malloc(1024);
	int socket = *(int *) incoming_socket;
	User_conected* user = put_user(socket);

	send_message(user, "Connect,\n");

	while (1) {

		int ret = sgetline(socket, &buffer);
		if (ret < 0)
			break;

		if (ret == 0) {
			printf("End of Headers detected.\n");
			break;
		}

		printf("obsah  %s \n", buffer);
		fprintf(file,"Prijata zprava :%s \n", buffer);

		char *ret2 = strchr(buffer, ',');

		*ret2 = '\0';

		ret2++;

		if (strcmp(buffer, "Registrace") == 0) {
			reg_user(ret2, user);

		} else if (strcmp(buffer, "CheckGame") == 0) {
			reload_game(ret2, user);

		} else if (strcmp(buffer, "CheckConnect") == 0) {

			send_message(user, "CheckConnect,\n");
			sleep(1);

		} else if (strcmp(buffer, "DeleteGame") == 0) {
			int i = atoi(ret2);

			free(game[i]);
			game[i] = NULL;

		} else if (strcmp(buffer, "Log") == 0) {

			log_control(user, ret2);
		} else if (strcmp(buffer, "LogOut") == 0) {
			logout_user(user, ret2);

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
				game_is_over(user);
			} else if (strcmp(ret2, "leave") == 0) {

				leave_game(user, ret3);
			}else if (strcmp(ret2, "gameDone") == 0) {
				game_is_done(user);
			}else{
				printf("Invalid input\n");
				fprintf(file,"Prijata zprava :%s nevalidni vstup \n", buffer);
			}
		}else{
			printf("Invalid input\n");
			fprintf(file,"Prijata zprava :%s nevalidni vstup \n", buffer);
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

void start_server(int argc, char** argv) {

	if (argc == 1 || argc == 3) {
		nacti_soubor();
		signal(SIGINT, sigint_handler);
		nacti_Port(argc, argv);
		read_address(argc, argv);
	} else {

		help();
		exit(1);
	}
}
int main(int argc, char** argv) {

	memset(conected_users, 0, sizeof(User_conected*) * MAX_CONECTED);
	start_server(argc, argv);
	int sock = -1, incoming_sock = -1;
	int optval = -1;
	struct sockaddr_in addr, incoming_addr;
	unsigned int incoming_addr_len = 0;

	if (pthread_mutex_init(&lock, NULL) != 0) {
		printf("\n mutex init failed\n");
		fprintf(file,"Chyba serveru \"mutex init failed\":\n");

		return 1;
	}
	/* create socket */
	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (sock < 0) {
		print_err("socket()");
		fprintf(file,"Chyba serveru \"socket\":\n");
		return 1;
	}

	/* set reusable flag */
	optval = 1;
	setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));

	/* prepare inet address */
	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(srv_port);

	if (is_address) {
		addr.sin_addr.s_addr = htonl(INADDR_ANY); /* listen on all interfaces */
		fprintf(file,"Server bezi na vsech interfaces: \n");
	} else {
		addr.sin_addr.s_addr = inet_addr(address);
		fprintf(file,"Server bezi na adrese %s: \n",address);
	}

	if (bind(sock, (struct sockaddr*) &addr, sizeof(addr)) < 0) {
		print_err("bind");
		fprintf(file,"Chyba serveru \"bind\":\n");
		return 1;
	}

	if (listen(sock, 10) < 0) {
		print_err("listen");
		fprintf(file,"Chyba serveru \"listen\":\n");
		return 1;
	}

	for (;;) {
		incoming_addr_len = sizeof(incoming_addr);
		incoming_sock = accept(sock, (struct sockaddr*) &incoming_addr,
				&incoming_addr_len);
		if (incoming_sock < 0) {
			print_err("accept");
			fprintf(file,"Chyba serveru \"accept\":\n");
			close(sock);
			continue;
		}

		char* message = "Connect\n";
		printf("connection from %s:%i\n", inet_ntoa(incoming_addr.sin_addr),
				ntohs(incoming_addr.sin_port));
		fprintf(file,"connection from %s:%i\n", inet_ntoa(incoming_addr.sin_addr),
				ntohs(incoming_addr.sin_port));
		pthread_t thread;

		/* create a second thread which executes inc_x(&x) */
		if (pthread_create(&thread, NULL, createThread, &incoming_sock)) {

			fprintf(stderr, "Error creating thread\n");
			fprintf(file,"Chyba serveru \"creating thread\" \n ");

			return 1;

		}

	}
	pthread_mutex_destroy(&lock);
	fclose(file);
	return 0;
}

