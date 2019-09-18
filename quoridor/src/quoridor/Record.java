/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4657.40ea68fec modeling language!*/

package quoridor;

// line 35 "firstdraft.ump"
public class Record
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Record Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Record(Game aGame)
  {
    if (aGame == null || aGame.getGameRecord() != null)
    {
      throw new RuntimeException("Unable to create Record due to aGame. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    game = aGame;
  }

  public Record(Menu aMainMenuForGame, Control aMainControlForGame, Board aCurBoardForGame)
  {
    game = new Game(aMainMenuForGame, aMainControlForGame, aCurBoardForGame, this);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }

  public void delete()
  {
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.delete();
    }
  }

}