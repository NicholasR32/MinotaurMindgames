# Problem 1: Minotaur's Birthday Party

![The cake at the end of the maze.](https://github.com/NicholasR32/MinotaurMindgames/blob/main/imgs/mazecake.png)

Image credits: [Ashmolean Museum](https://www.ashmolean.org/article/myths-of-the-labyrinth), [Evan Amos](https://en.wikipedia.org/wiki/Cupcake#/media/File:Hostess-Cupcake-Whole.jpg)

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
- The Minotaur wants to challenge the guest's logical abilities, and gently requests that they do not communicate directly through any other means after the game has started.
- However, they can devise a strategy beforehand.

## The strategy
Having only a single bit (the cupcake) to work with between all the guests is challenging. However, there is some more information we can work with: Every guest can eventually see the state of the cupcake, and each guest knows how many times he/she has visited the labyrinth, and what state the cupcake was in each time.

The key is to control who can eat the cupcake, and who can request new ones. Therefore, a winning strategy is as follows:

- Assign Guest 1 to be the **counter**.
- If Guest 1 is selected:
    - If the cupcake is there, leave it alone.
    - If the cupcake is gone, request a new one and increment his counter by one.
        - If the counter ever reaches N-1, announce that everyone has visited the labyrinth.
- If any of Guest 2 through Guest N are selected:
    - If this guest has never eaten a cupcake *and* the cupcake is there, eat it.
    - Otherwise, leave the plate alone.

The idea is that Guest 1 knows exactly how many cupcakes have been eaten, and therefore whether every guest has visited, because he is the only one who can request new cupcakes. The other guests can report their attendance by eating a cake if they see one, but may have to visit multiple times if there's an empty plate; Guest 1 has not yet reset the cupcake's state and counted the last person to have eaten it. Regardless, this strategy guarantees that every guest will be accounted for and the Minotaur will be satisfied.

## The Implementation
The `GuestThread` class constructs the threads which can generate some number of guests, e.g., 10. The main method (AKA the Minotaur) executes the process by which a guest is randomly selected to enter the `Labyrinth`, which is another class containing the boolean representing the cupcake, as well as a Semaphore to lock access to the cupcake.

Every time a `GuestThread` is sent in, it attempts to acquire the lock, and if successful, it then decides what to do with the cupcake. Then, it releases the lock so another guest can enter.

Once Guest 1 knows that everyone has visited, it will announce success by toggling a global boolean, which is then seen by the main method/Minotaur. This concludes Problem 1.

For N=10, an average of about 100 collective visits to the labyrinth are needed to win. (Mean of last 10 trials = 96.3 visits)

Note: This problem is similar to Exercise 4 in Chapter 1 in *The Art of Multiprocessor Programming* by Herlihy and Shavit, where the main object of shared interaction is a simple light switch, rather than a tasty cupcake.
\
\
\
\
*But wait, there's more!*

# Problem 2: Minotaur's Crystal Vase
![His beloved crystal vase.](https://github.com/NicholasR32/MinotaurMindgames/blob/main/imgs/vaseroom.png)

Image credits: [Evannovostro](https://www.shutterstock.com/image-photo/dark-castle-tower-round-room-interior-103286093), [Crystal Classics](https://www.crystalclassics.com/waterford/1057803.htm)

## The rules
The Minotaur also possesses a showroom containing his favorite crystal vase, and will only allow one guest in the showroom at a time for safety. Like before, he wants every guest to view the vase at least once, and allows them to choose from 3 strategies:

1. A guest can decide to check the showroom at an arbitrary time to try and enter it. This allows guests to party elsewhere in the castle, but also to crowd around the door. This strategy does not guarantee a given guest can ever see the vase.

2. When a guest enters the showroom, he/she must place a "BUSY" sign on the door, and change it to an "AVAILABLE" sign when leaving. This effectively communicates whether the vase can be seen at any time.

3. The guests can form a queue to wait to enter the showroom, and every time a guest leaves the room, he/she must notify the guest at the head of the queue that it is empty. Guests can also queue multiple times

## The strategy

Strategy 1 is not starvation-free, since it does not guarantee a given guest will ever be able to see the vase. This system lacks order and simply allows guests in at random.

Strategy 2 is similar to 1, but implements a lock in the form of a sign. Still, there is not much order to the process.

Strategy 3 seems like the most orderly one, and is starvation-free. Once a guest enters the queue, he/she is guaranteed to see the vase, assuming everyone ahead finishes in a finite amount of time. While the guests will have to wait in line instead of partying elsewhere in the castle, we only care that they will get to see the vase.

Therefore, I implement **Strategy 3** in my solution to Problem 2.
Similar to the labyrinth in Problem 1, the showroom is represented by a class containing a lock. When a guest thread starts up, it attempts to acquire the lock, and due to the behavior of Java's Semaphore class, access will be provided on a FIFO basis.

A guest may randomly decide to see the vase more than once, and will come up with an opinion about the vase each time he/she sees it.

Happy Birthday, Minotaur.