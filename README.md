# Problem 1: Minotaur's Birthday Party

![The cake at the end of the maze.](https://github.com/NicholasR32/MinotaurMindgames/blob/main/imgs/mazecake.png)

Image credits: [Ashmolean Museum](https://www.ashmolean.org/article/myths-of-the-labyrinth), [Evan Amos](https://en.wikipedia.org/wiki/Cupcake#/media/File:Hostess-Cupcake-Whole.jpg)

This problem is similar to Exercise 4 in Chapter 1 in *The Art of Multiprocessor Programming* by Herlihy and Shavit, where the main object of shared interaction is a simple light switch, rather than a tasty cupcake.

## The problem
#### The rules
- **N** guests are invited to the Minotaur's birthday party.
- The Minotaur has a labyrinth (or for the sake of simplicity, a room) that only one guest can enter and be in at a time.
- The labyrinth contains a plate, which may or may not have a cupcake on top.
- Whenever a guest enters the labyrinth, he/she has three options:
    1. If the plate has a cupcake, eat it.
    2. If the plate is empty, request a new cupcake.
    3. Leave the plate alone.
- At the start of the game, the plate has a cupcake.

#### The challenge
- The Minotaur will repeatedly select guests at random to enter and exit the labyrinth, until a guest announces that everyone has visited the labyrinth. If anyone announces in error, he may get disappointed and eat them all or something.
- The Minotaur wants to challenge the guest's logical abilities, and gently requests that they do not communicate after the game has started.
- However, they can devise a strategy beforehand.

## The strategy
Having only a single bit (the cupcake) to work with between all the guests is challenging. However, there is some more information we can work with: Every guest can eventually see the state of the cupcake, and each guest knows how many times he/she has visited the labyrinth, and what state the cupcake was in each time.

The key is to control who can eat the cupcake, and who can request new ones. Therefore, a winning strategy is as follows:

- Assign Guest 1 to be the **counter**.
- If Guest 1 is selected:
    - If the cupcake is there, leave it alone.
    - If the cupcake is gone, request a new one and increment his counter by one.
- If any of Guest 2 through Guest N are selected:
    - If this guest has never eaten a cupcake *and* the cupcake is there, eat it.
    - Otherwise, leave the plate alone.
- If Guest 1's counter ever reaches N-1, announce that everyone has visited the labyrinth.

The idea is that Guest 1 knows exactly how many cupcakes have been eaten, and therefore whether every guest has visited, because he is the only one who can request new cupcakes. The other guests can report their attendance by eating a cake if they see one, but may have to visit multiple times if there's an empty plate; Guest 1 has not yet reset the cupcake's state and counted the last person to have eaten it. Regardless, this strategy guarantees that every guest will be accounted for and the Minotaur will be satisfied.
\
\
\
\
*But wait, he's not done!*

# Problem 2: Minotaur's Crystal Vase
