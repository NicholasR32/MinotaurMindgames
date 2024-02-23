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
        // Start the threads
        for (int i = 0; i < NUM_GUESTS; i++)
        {
            guests[i] = new GuestThread(i+1);
            guests[i].start();
        }

        // This loop acts as the Minotaur, selecting guests at random until they succeed.
        int c = 0;
        while (!GuestThread.allVisited && c < 1000)
        {
            // Labyrinth occupied, can't send anyone in yet.
            c++;
            if (GuestThread.occupied)
            {
                if (DEBUG) System.out.println("\toccupied " + c);
                continue;
            }
            if (DEBUG) System.out.println("attempt " + c);
            // Pick a guest at random
            int nextGuest = (int)(Math.random() * NUM_GUESTS);
            // Send guest into labyrinth
            GuestThread.occupied = true;
            if (DEBUG) System.out.println("Sending guest " + (nextGuest+1));
            guests[nextGuest].enterLabyrinth();

                
        }
        // Stop every thread
        for (GuestThread guest : guests)
        {
            guest.finish();
        }

        // Add up total visits
        int totalVisits = 0;
        for (GuestThread guest : guests)
        {
            try
            {
                guest.join();
            }
            catch (InterruptedException e) {}
            totalVisits += guest.getNumVisits();
        }

        if (DEBUG) System.out.println("GGGGGGGGG");
        System.out.println("It took " + totalVisits + " to reach success.");
    }
}

class GuestThread extends Thread
{
    // Whether any guest has announced victory.
    // Only used by Guest 1, when all guests have certainly visited at least once.
    public static boolean allVisited = false;
    // The plate, on which a cupcake is offered to the guests.
    public static boolean plateHasCupcake = true;
    // Whether the labyrinth is occupied.
    public static boolean occupied = false;

    // What number guest this is
    private int id;
    // How many guests have eaten a cupcake. Again, only used by Guest 1
    private int counter;
    // Whether this guest has eaten a cupcake
    private boolean eaten;
    // How many times this guest has visited the labyrinth
    private int numVisits;
    // Whether this guest is in the labyrinth
    private boolean inLabyrinth;
    // Whether to keep running
    private boolean done;

    public static boolean DEBUG = true;

    public GuestThread(int id)
    {
        this.id = id;
        this.counter = 0;
        this.eaten = false;
        this.numVisits = 0;
        this.inLabyrinth = false;
        this.done = false;
    }

    public void run()
    {
        // Spin while partying outside the labyrinth
        while (!done)
        {
            if (inLabyrinth)
            {
                occupied = true;
                visitLabyrinth();
                occupied = false;
                inLabyrinth = false;
            }
        }
    }

    public int getCounter()
    {
        return this.counter;
    }

    public boolean hasEaten()
    {
        return this.eaten;
    }

    public int getNumVisits()
    {
        return this.numVisits;
    }

    public void enterLabyrinth()
    {
        this.inLabyrinth = true;
    }

    public void finish()
    {
        this.done = true;
    }

    public void visitLabyrinth()
    {
        if (DEBUG) System.out.println("Guest " + this.id + " has entered");
        this.numVisits++;

        // If this is guest 1, mark one more guest as visited if cupcake eaten.
        if (this.id == 1)
        {
            // Increment counter and request new cupcake.
            if (!plateHasCupcake)
            {
                this.counter++;
                plateHasCupcake = true;
                System.out.println("\tnew visit - TOTAL: " + counter);
                System.out.println("\tcupcake is now " + plateHasCupcake);
            }
            // If everyone else is sure to have eaten, announce victory.
            if (this.counter == Problem1.NUM_GUESTS-1)
            {
                allVisited = true;
            }
        }
        // Eat the cupcake if there is one, and this guest has not eaten yet.
        else if (!this.eaten && plateHasCupcake)
        {
            this.eaten = true;
            plateHasCupcake = false;
            if (DEBUG) System.out.println("\tate the cupcake");
        }
        else if (this.eaten)
        {
            if (DEBUG) System.out.println("\talready ate");
        }
        else if (!this.eaten && !plateHasCupcake)
        {
            if (DEBUG) System.out.println("\tnothing to eat :(");
        }
    }
}