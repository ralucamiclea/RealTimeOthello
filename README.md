This is a simple implementation of a real-time chat and a strategy game called
Othello. Follow these steps to compile and run:

//compile
% make target
OR
% make clobber
% make idl
% make c
% make s

//start the name server in the first terminal window:
% make orbd

//start the chat server in a second terminal window:
% make server

//start up the chat client in the third, forth ... window:
% make client

Finally, follow the instructions and enjoy the game!
Join a team by specifying a color and insert marks on the board by specifying
the coordinates. When the board is fully marked, the team with the color that
covers the largest number of squares on the board wins.
*A faster player can place more markers than a slower player.
**A team with many players may be able to place more markers than a team with
fewer players.
