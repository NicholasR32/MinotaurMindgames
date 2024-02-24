// Nicholas Rolland
// Assignment 2, Problem 2 - Minotaur's Crystal Vase
// COP4520, Spring 2024, Prof. Juan Parra
// 2/23/2024

import java.util.*;
import java.io.*;
import java.util.concurrent.Semaphore;

public class Problem2
{
    public static int NUM_GUESTS = 10;
    public static GuestThread[] guests = new GuestThread[NUM_GUESTS];

    public static boolean DEBUG = false;

    public static void main(String[] args)
    {
        // Start the threads
        for (int i = 0; i < NUM_GUESTS; i++)
        {
            int numVisitsToVase = (int)(Math.random() * NUM_GUESTS/2);
            guests[i] = new GuestThread(i+1, numVisitsToVase);
            guests[i].start();
        }

        // Wait for all threads to finish and sum visits.
        int totalVisits = 0;
        for (GuestThread guest : guests)
        {
            try
            {
                guest.join();
                totalVisits += guest.getNumVisits();
            }
            catch (InterruptedException e) {}
        }
        System.out.println("All guests have seen the crystal vase.");
        System.out.println("They have collectively seen it " + totalVisits + " times.");
    }
}

// Represents the labyrinth - I mean, showroom, containing the vase.
class Showroom
{
    // Semaphore that locks the room
    public static Semaphore lock = new Semaphore(1, true);

    // Toggle this to see detailed actions.
    public static boolean DEBUG = false;

    // Process this guest's presence in the room.
    public static void enterShowroom(int guestID)
    {
        String[] responses = {"sparkling","radiant","crystalline","monochromatic","transparent","bland","pure","vague"};
        System.out.println(guestID + " thinks the vase is " + responses[(int)(Math.random() * responses.length)]);
    }
}

// A running thread representing a single guest.
class GuestThread extends Thread
{
    // What number guest this is
    private int id;
    // How many times this guest wishes to see the vase
    private int numVisits;

    // Toggle this to see detailed thread actions.
    public static boolean DEBUG = true;

    public GuestThread(int id, int numVisits)
    {
        this.id = id;
        this.numVisits = numVisits;
    }

    public void run()
    {
        try
        {
            // Attempt to enter the showroom once.
            if (DEBUG) System.out.println("Guest " + id + " has queued up...");
            Showroom.lock.acquire();
            Showroom.enterShowroom(this.id);
            Showroom.lock.release();

            // See it again.
            for (int i = 0; i < this.numVisits-1; i++)
            {
                if (DEBUG) System.out.println("\tGuest " + id + " has queued up... again.");
                Showroom.lock.acquire();
                Showroom.enterShowroom(this.id);
                Showroom.lock.release();
            }
        }
        catch (InterruptedException e) {}
    }

    public int getNumVisits()
    {
        return this.numVisits;
    }
}