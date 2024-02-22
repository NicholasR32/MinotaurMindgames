// Nicholas Rolland
// Assignment 2
// COP4520, Spring 2024, Prof. Juan Parra
// 2/23/2024

import java.util.*;
import java.io.*;

public class Problem1
{
    public static int NUM_GUESTS = 10;

    public static boolean DEBUG = true;

    public static void main(String[] args)
    {
        GuestThread[] guests = new GuestThread[NUM_GUESTS];


        // This loop acts as the Minotaur, selecting guests at random until they succeed.
        while (true)
        {
            // Pick a guest at random
            int nextGuest = (int)(Math.random() * NUM_GUESTS);

            // Send guest into labyrinth
        }
    }
}

class GuestThread extends Thread
{
    // How many guests have eaten a cupcake. Only used by Guest 1
    private int counter;
    // Whether this guest has eaten a cupcake
    private boolean eaten;

    public GuestThread()
    {
        this.counter = 0;
        this.eaten = false;
    }

    public int getCounter()
    {
        return this.counter;
    }

    public boolean hasEaten()
    {
        return this.eaten;
    }
}