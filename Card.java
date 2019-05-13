package javapoker;
public class Card{
	
	/*I have used two variables to represent rank and suit of every Card*/
	private int rank;
	private char suit;
    
	/*The suits and ranks arrays will be used later in rankAsString method. The rankAsString method is a utility method for taking a number and turning it into the appropriate string for the rank (we'll use it later).*/
	private static char[] suits = { 'C', 'D', 'H', 'S' };
    private static char[] ranks  = { ' ','A','2', '3', '4','5', '6', '7', 
                                       '8', '9','T', 'J', 'Q', 'K' };

    public static String rankAsString( int __rank ) {
        return  Character.toString(ranks[__rank]);
    }

    /*This parameterized contructor instatntiates every card and also converts 'T','J','Q','K','A' into 10,11,12,13,1. Using them as integers helped me recycle the same methods for these values too. 
     * Note I used 1 to represent 'A'.*/
    Card(char rank,char suit)
    {
    	if(rank=='T')
			this.rank=10;
		else if(rank=='J')
				this.rank=11;
		else if(rank=='Q')
			this.rank=12;
		else if(rank=='K')
			this.rank=13;
		else if(rank=='A')
			this.rank=1;
		else
		{
			this.rank=Integer.parseInt(String.valueOf(rank));
		}
    	this.suit=suit;
    }
    
    //Getter methods for this class. The Setter methods are not required as we create objects inside the 'Hand' class
 
    public int getRank() {
         return rank;
    }

    public char getSuit() {
        return suit;
    }

      public @Override String toString()
    {
          return ranks[rank] + " of " + suits[suit];
    }
   
}