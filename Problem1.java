// Nicholas Rolland
// Assignment 2
// COP4520, Spring 2024, Prof. Juan Parra
// 2/23/2024

import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class Problem1
{
    public static int NUM_GUESTS = 10;
    public static GuestThread[] guests = new GuestThread[NUM_GUESTS];

    public static boolean DEBUG = true;

    public static void main(String[] args)
    {
        // Start the threads
        for (int i = 0; i < NUM_GUESTS; i++)
        {
            guests[i] = new GuestThread(i+1);
            guests[i].start();
        }

        Labyrinth lab = new Labyrinth();
        // This loop acts as the Minotaur, selecting guests at random until they succeed.
        int c = 0;
        while (!GuestThread.allVisited && c < 10000)
        {
            // Labyrinth occupied, can't send anyone in yet.
            c++;
            //if (DEBUG) System.out.println("attempt " + c);
            // Send guest into labyrinth
            lab.sendGuest();
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
        System.out.println("The Minotaur is appeased.");
        System.out.println("It took " + totalVisits + " labyrinth visits to reach success.");
    }
}

// Represents the labyrinth containing the plate.
class Labyrinth
{
    // Semaphore that controls access to the labyrinth
    public static Semaphore lock = new Semaphore(1);
    // The plate, on which a cupcake is offered to the guests.
    public static boolean plateHasCupcake = true;

    public static boolean DEBUG = true;
    // Select a random guest to enter next.
    public void sendGuest()
    {
        int nextGuest = (int)(Math.random() * Problem1.NUM_GUESTS);
        if (DEBUG) System.out.println("Sending guest " + (nextGuest+1));
        Problem1.guests[nextGuest].enterLabyrinth();
    }

}
class GuestThread extends Thread
{
    // Whether any guest has announced victory.
    // Only used by Guest 1, when all guests have certainly visited at least once.
    public static boolean allVisited = false;

    // What number guest this is
    private int id;
    // How many guests have eaten a cupcake. Only used by Guest 1
    private int counter;
    // Whether this guest has eaten a cupcake
    private boolean eaten;
    // How many times this guest has visited the labyrinth
    private int numVisits;
    // Whether this guest is in the labyrinth
    private boolean chosen;
    // Whether to keep running
    private boolean done;

    public static boolean DEBUG = true;

    public GuestThread(int id)
    {
        this.id = id;
        this.counter = 0;
        this.eaten = false;
        this.numVisits = 0;
        this.chosen = false;
        this.done = false;
    }

    public void run()
    {
        // Spin while partying outside the labyrinth.
        while (!done)
        {
            if (chosen)
            {
                try
                {
                    // Attempt to acquire the lock and enter the labyrinth.
                    if (DEBUG) System.out.println("Guest " + id + " attempting to enter...");
                    Labyrinth.lock.acquire();
                    visitLabyrinth();
                    Labyrinth.lock.release();
                    chosen = false;
                }
                catch (InterruptedException e) {}
            }
            if (done) break;
        }
    }

    public void visitLabyrinth()
    {
        if (DEBUG) System.out.println("\tGuest " + this.id + " has entered");
        this.numVisits++;

        // If this is guest 1, mark one more guest as visited if cupcake eaten.
        if (this.id == 1)
        {
            // Increment counter and request new cupcake.
            if (!Labyrinth.plateHasCupcake)
            {
                this.counter++;
                Labyrinth.plateHasCupcake = true;
                if (DEBUG) System.out.println("\tGuest " + this.id + " logged new visit - TOTAL: " + counter);
                if (DEBUG) System.out.println("\tGuest " + this.id + " requested new cupcake");
            }
            // If everyone else is sure to have eaten, announce victory.
            if (this.counter == Problem1.NUM_GUESTS-1)
            {
                System.out.println("Guest " + this.id + " correctly announces that everyone has visited the labyrinth at least once.");
                allVisited = true;
            }
        }
        // Eat the cupcake if there is one, and this guest has not eaten yet.
        else if (!this.eaten && Labyrinth.plateHasCupcake)
        {
            this.eaten = true;
            Labyrinth.plateHasCupcake = false;
            if (DEBUG) System.out.println("\tGuest " + this.id + " ate the cupcake");
        }
        else if (this.eaten)
        {
            if (DEBUG) System.out.println("\tGuest " + this.id + " already ate");
        }
        else if (!this.eaten && !Labyrinth.plateHasCupcake)
        {
            if (DEBUG) System.out.println("\tGuest " + this.id + " has nothing to eat :(");
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
        this.chosen = true;
    }

    public void finish()
    {
        this.done = true;
    }
}