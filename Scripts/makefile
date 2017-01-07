slozka: server	
	mkdir -p ../APP
server: ../src/ServerC/server.o
	gcc -o ../APP/server ../src/ServerC/server.o -pthread
server.o: ../src/ServerC/server.c
	gcc -c -o ../src/ServerC/server.o ../src/ServerC/server.c