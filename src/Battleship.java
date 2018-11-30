/* This class must instantiate a Grid object and use its methods to let the player pick a game grid,
update the game grid according to player input, print a feedback grid for the player, and check if
the player has sunk all the ships. See the Sample I/O in the assignment brief for details on what you
you should print to the console */

import java.util.Scanner;

public class Battleship{
    public static void main(String[] args)
    {
    int numofShots = 12;

    Scanner scn = new Scanner(System.in);


    System.out.println("Pick your grid (1-4):");
    int gridPick = scn.nextInt();
    Grid player1 = new Grid(gridPick);

    System.out.println("The battle begins!");

    do
    {
        System.out.println("You have " + numofShots-- + " shots remaining.");
        System.out.println("choose your square:");

        //int rowSqre = scn.nextInt(); int colSqr = scn.nextInt();
        String rowCol = scn.next();

        int rowSqre = Integer.parseInt(rowCol.substring(0,1));
        int colSqr = Integer.parseInt(rowCol.substring(1));

        //Player chooses where to guess for a ship
        System.out.println(player1.fire(rowSqre, colSqr));
        //Print updated grid
        player1.printGrid();

        if (player1.checkGrid() == false)
        {
            System.out.println("You won! and in only " + (12 - numofShots) + " shots!");
            break;
        }

        if (numofShots == 0)
        {
            System.out.println("You're out of ammunition! Game Over!");
            break;
        }
    }while (numofShots != 0);
    }
}
