#ifndef USER_H_
#define USER_H_


/*
 * Definovani struktury grafu
 */
typedef struct Knobs{
	int free;
	int identifikace;
	int colors[4];


}Knobs;

typedef struct Good_color{
	int free;
	int good_color;
	int identifikace;

}Good_color;

typedef struct Great_color{
	int free;
	int great_color;
	int identifikace;

}Great_color;


typedef struct Game{
	int free;
	int id;
	int gamer1;
	int gamer2;
	char chellanger[30];
	char player[30];
	int goodColor;
	Knobs knobs[10];
	Great_color great_color[10];
	Good_color good_color[10];
	Knobs result;
}Game;

typedef struct User_database{
	char nickname[30];
	char passwd[32];
}User_database;

typedef struct User_conected{

	char nickname[30];
	int socket;
	int id;
	int isLog;
	Game* game;
}User_conected;



#endif

