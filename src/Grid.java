/* This class contains attributes and methods for Battleships - part 1. */
class Grid{

    /* grids is a 3-D String array that holds five different 2-D String arrays, which represent the playing field:
    grids[0] holds an empty grid. Empty squares are represented byt the caret symbole, "^"
    grids[1] through grids[4] hold preset playing fields where ships are reprented by "A" */
    private String grids[][][] =
            {
                    {
                            {"^","^","^","^","^"},
                            {"^","^","^","^","^"},
                            {"^","^","^","^","^"},
                            {"^","^","^","^","^"},
                            {"^","^","^","^","^"},
                    },{
                    {"A","^","^","^","A"},
                    {"^","^","^","^","^"},
                    {"^","^","^","^","^"},
                    {"^","^","^","^","^"},
                    {"A","^","^","^","A"},
            },{
                    {"A","^","^","^","^"},
                    {"^","A","^","^","^"},
                    {"^","^","A","^","^"},
                    {"^","^","^","A","^"},
                    {"^","^","^","^","A"},
            },{
                    {"^","^","^","^","^"},
                    {"^","^","A","^","^"},
                    {"^","A","A","A","^"},
                    {"A","^","A","^","A"},
                    {"^","^","A","^","^"},
            },{
                    {"^","^","^","^","^"},
                    {"^","A","A","A","^"},
                    {"^","A","A","A","^"},
                    {"^","A","A","A","^"},
                    {"^","^","^","^","^"},
            }
            };

    /* Declare two 2-D String Arrays.
       1. playerGrid contains the array the the player can see. It starts as an empty grid, and is updated based
          on the player's decisions.
       2. enemyGrid contains the preset playing field, populated with ships. */
    private String playerGrid[][];
    private String enemyGrid[][];


    /* This method initializes instances of the two grids. It takes in an integer arguments that picks one of
    the playing fileds in grids, based player's decisions */
    public Grid(int seed){

        //initializes playerGrid instance
        this.playerGrid = grids[0];
        // initializes enemyGrid instance
        this.enemyGrid = grids[seed];

        //tells the player how many ships are on the playing field
        if (seed == 1){
            System.out.println("Four ships spotted");
        }else if (seed == 2){
            System.out.println("Five ships spotted");
        }else if (seed == 3){
            System.out.println("Eight ships spotted");
        }else if (seed == 4){
            System.out.println("Nine ships spotted");
        }
    }

    /* this method prints the player grid only. It takes no arguments, hiding the enemy grid from the user.*/
    public void printGrid(){
        for (int row = 0; row < 5; row ++){
            for(int col =0; col < 5; col ++){
                System.out.print(playerGrid[row][col]);
            }
            System.out.println();
        }
    }
    /* This method handles the game's core mechanic. It takes in two integers that represents the coordinates "XY"
       picked by the player, where X and Y are integers corresponding to the rows and columns of the game grid.
       If the coordinates correspond to a square containing a ship ("A") then the string is replaced by an "X".
       If the coordinate correspond to an empty square ("^"), them then the string is replaced by an "0".
       Note that this method returns, and does not print.*/
    public String fire(int row, int col){
        if (enemyGrid[row][col] == "^"){
            playerGrid[row][col] = "O";
            enemyGrid[row][col] = "O";
            return "MISS";
        }
        else if (enemyGrid[row][col] == "A"){
            playerGrid[row][col] = "X";
            enemyGrid[row][col] = "X";
            return "HIT";
        }
        // if the square has already been checked.
        else return "You already shot there";
    }

    public boolean checkGrid(){

        boolean isShipPresent = false;
		/* You must complete this method!
		   It must search the grid  for remaining ships. It must return 'true' if there are,
		   and 'false' if there are not. */

		//It's checking the enemy grid for remaining ships.
        for (int i = 0; i < enemyGrid.length; i++)
        {
            for (int j = 0; j < enemyGrid[i].length; j++)
            {
                if (enemyGrid[i][j] == "A")
                {
                    isShipPresent = true;
                }
            }
        }
		return isShipPresent;
    }
}
