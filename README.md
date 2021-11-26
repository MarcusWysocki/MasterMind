# MasterMind
Use Spring boot REST to play a game of Mastermind 


Change user and password in src/main/res/application.properties
to whatever your sql user and pass are.
Whack this in MySQL:

DROP DATABASE IF EXISTS masterminddb;

CREATE DATABASE masterminddb;

use masterminddb;

create table games (
	GameId int not null auto_increment,
    TimeStarted char(50),
    Completed boolean default false,
    NoGuesses int default 0,
    Answer int,
    primary key(GameId)
);
create table guesses(
	GuessId int not null auto_increment,
    GameId int not null,
    Guess int not null,
    Result char(7),
    TimeOfGuess char(50),
    primary key (GuessId),
    foreign key (GameId) references games(GameId)
);


select * from games;

