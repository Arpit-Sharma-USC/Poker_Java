package javapoker;

import java.util.ArrayList;
import java.util.HashMap;

//making the class final ensure the Hand is immutable as in the requirements
final class Hand implements Comparable<Hand> {
    //Our Hand has 5 cards, each has a 'value'. I will instantiate them in the Hand Constructor 
	private Card[] cards;
	
	//This is variable is used to index the type of Hand from the array 'VALUES'
    private int[] value;
    
    //This is the resultant string storing the type of Hand
    private String resultStr;
    
    //The set of possible values for a type of Hand. VALUES[0] is left blank intentionally for debugging.
    private static String[] VALUES= {" ","High Card","One Pair","Two Pair","Three of a Pair","Straight","Flush","Full House","Four of a Kind","Straight Flush","Royal Flush"};
    
    // This HashMap is used to ensure no hand has card combinations like say, 2S 2S or 4D 4D. In other words 2 duplicate cards.
    
    private HashMap<Character,Character> duplicateChecker;
    
    
    /*The parameterized Constructor responsible for creating the objects of class Hand
     * Having not declared the default constructor ensures the Hand(Card1 Card2 Card3 Card4 Card5) is the only way to initialize the Hand as requested in the requirements*/
    
    Hand(String str)
    {
    
    	duplicateChecker = new HashMap<>(); 
        cards = new Card[5];
        
        //string array to store split array of cards in the Hand 
        String[] splitStr=new String[5];
    	splitStr=str.split(" ");

    	//set objects of Card here from str and also add to HashMap
    	//i runs from 0 to 4 to create 5 Card in the Hand
        for (int i=0; i<5; i++)
        {     	
             		
        	//For example:In the card 5C char At 0, which is 5, is the Rank and char At 1, which is C is the Suit. That is exactly what is passed to the Card class contructor
        	
        		cards[i]=new Card(splitStr[i].charAt(0),splitStr[i].charAt(1));
    	  	
        		//Checking for duplicate cards. If the key(Rank) already exists in the map for the Hand with the same value mapped to it then it is a duplicate
         	
        		if(duplicateChecker.containsKey(splitStr[i].charAt(0)) 
         				&& splitStr[i].charAt(1)==duplicateChecker.get(splitStr[i].charAt(0)))
         			{
        				//Exit the application 
         				System.out.println("Duplicates in Hand, enter a valid hand");
         				System.exit(1);
         			}
        		
        		//If the Rank is not between 1-13(1-A,2,3,4,5,6,7,8,9,10-T,11-J,12-Q,13-K) then Hand has an invalid rank card
         		if((cards[i].getRank() < 1 && cards[i].getRank()>14))
         			{
         				//Exit the application 
         				System.out.println("Hand has an invalid Rank card");
         				System.exit(1);
         			}
         		//If the Suit is not H, C, S or D then Hand has an invalid suit card
         		
         		if(cards[i].getSuit()!='H'
         				&&cards[i].getSuit()!='C'
         				&&cards[i].getSuit()!='S'
         				&&cards[i].getSuit()!='D')
         		{	
         			//Exit the application 
         			System.out.println("Hand has an invalid Suit card");
 					System.exit(1);
     			
         			
         		}
         		
         		//if there were no problems with the Card in the Hand then add it to the duplicateChecker Map for testing future Cards in the same Hand
         		duplicateChecker.put(splitStr[i].charAt(0),splitStr[i].charAt(1) );
         		
    	}
        /*
         * The fromString Method is the crux of the application and performs all the logic to classify the Hand and assign it a value  
         * */
        this.fromString();
        
        /*The display method is used to map the value obtained in fromString method to 'VALUES' and assigns the Hand a type from 'VALUES'*/
        this.display();
     }
        
        
    public void fromString()
    {
    	// to determine the type of hand 
    	value = new int[6];
    	

        //determining hand type 
    	/*For simplicity's sake, I've used card ranks starting at 1
    	  for ace instead of 0 for ace. If I use 0 for ace, 
    	  then the application would be using 9 for 10, which is just confusing.
    	   Since the card ranks run 1-13, the first index of our array (0) will be empty.*/
        int[] ranks = new int[14];
        
        
        /*Okay, so now, the array of card ranks is set-up, we need to find if there are actually any pairs. 
         * There needs to be a check if there is a pair?, and if there is, what rank the pair is? 
         * So I made use of an int sameCards to record how many cards are of the same rank,
         *  and an int groupRank to hold the rank of the pair. 
         *  I used an int sameCards because we can have more than two cards of the same value, maybe even 3 or 4 (hopefully not 5). 
      */
      
        /*sameCards starts at 1, so if we find a rank of which there are two cards, then we record 2 in sameCards and rank (x) as groupRank.
         *  This will work fine if there's a pair, three of a kind, or four of a kind.

* Let's say we have a full house. There is a pair of kings, so we record 2 as sameCards and 13 as groupRank. But we keep going through the other ranks,
*  and if there are 3 fives, then we overwrite sameCards with 3 since the number of cards of that rank is more than the current value of sameCards. 
*
* Similar situation: we have two pairs, it records the first pair, but not the other one. We can do hands with one group of cards, 
* but not hands with 2. We need a way to keep track of at least two different groups of cards, tracking the number of cards and the rank of each.
* 
* 
*  We have to keep track of two ranks of cards and how many cards are of each rank, 
*  so I had had two variables represent the ranks (largeGroupRank, smallGroupRank) and two to represent the number of cards that have that rank (sameCards, sameCards2).

*/
        //miscellaneous cards that are not otherwise significant
        int[] orderedRanks = new int[5];
        
        boolean flush=true, straight=false;//assume there is a flush and assume no straight
        int sameCards=1,sameCards2=1;
        int largeGroupRank=0,smallGroupRank=0;
        int index=0;
        int topStraightValue=0;

        
        
        
        for (int x=0; x<=13; x++)
        {
            ranks[x]=0;
        }
        for (int x=0; x<=4; x++)
        {
            ranks[ cards[x].getRank() ]++;
        }
        //I assume there is a flush. If two cards are not the same suit, then there's no flush.
        for (int x=0; x<4; x++) {
            if ( cards[x].getSuit() != cards[x+1].getSuit() )
                flush=false;//found there was no flush
        }

        for (int x=13; x>=1; x--)
        {
                 if (ranks[x] > sameCards)
                 {
                     if (sameCards != 1)
                     //if sameCards was not the default value
                     {
                         sameCards2 = sameCards;
                         smallGroupRank = largeGroupRank;
                     }

                     sameCards = ranks[x];
                     largeGroupRank = x;

                 } else if (ranks[x] > sameCards2)
                 {
                     sameCards2 = ranks[x];
                     smallGroupRank = x;
                 }
        }
        

        /* ranks[x] is greater than sameCards, I assigned the data there; otherwise, if it is greater than sameCards2, I assigned the data there. 
         * 'The Nested If': Say the 'if' wasn't there: we find a pair of 8's, then we find three 5's. sameCards contains the pair of 8's,
         *  and since the three 5's is more than the two 8's, we overwrite sameCards. But the pair we found earlier is just overwritten and not recorded anywhere, 
         *  when it should have been stuck into sameCards2. So, the if statement checks if sameCards was previously assigned to something before overwriting it, 
         *  and if it was, we take care of that.*/

        if (ranks[1]==1) //if ace, run this before because ace is highest card
        {
            orderedRanks[index]=14;
            index++;
        }

        for (int x=13; x>=2; x--)
        {
            if (ranks[x]==1)
            {
                orderedRanks[index]=x; //if ace
                index++;
            }
        }
        /*I check to see if there is one card of 5 consecutive ranks. 
         * There's a loop to do straights up to king high, and we add a special separate if for an ace high straight,
         *  since the number of aces is contained in ranks[1].*/
        
        for (int x=1; x<=9; x++)
        //can't have straight with lowest value of more than 10
        {
            if (ranks[x]==1 && ranks[x+1]==1 && ranks[x+2]==1 && 
                ranks[x+3]==1 && ranks[x+4]==1)
            {
                straight=true;
                topStraightValue=x+4; //4 above bottom value
                break;
            }
        }

        if (ranks[10]==1 && ranks[11]==1 && ranks[12]==1 && 
            ranks[13]==1 && ranks[1]==1) //ace high
        {
            straight=true;
            topStraightValue=14; //higher than king
        }
        
        for (int x=0; x<=5; x++)
        {
            value[x]=0;
        }
        
/*In our Hand class, we have a private array value that holds six integers.
 *  I used this to contain the values of the hands. This array will hold all the data necessary to compare two poker hands. 
 *  
 *  **********Process of comparing:************* "Say you have a pair, we know that a pair is the second lowest ranked hand. 
 *  If the hand we're comparing it to is also a pair, then we need to compare the rank of the pair. If the rank of the pair is equal,
 *   we need to go to the next highest card, then the next highest card, and then the next highest card."
 *
 * This sets up a list of the things we need to compare. The most important thing is what kind of hand, so that will go in the first position. 
 * The rest of the positions will hold the data needed to break a tie between two hands of the same type.*/

        
        //starting hand evaluation
        if ( sameCards==1 ) { //if we have no pair...
            value[0]=1;          //this is the lowest type of hand, so it gets the lowest value
            value[1]=orderedRanks[0];  //the first determining factor is the highest card,
            value[2]=orderedRanks[1];  //then the next highest card,
            value[3]=orderedRanks[2];  //and so on
            value[4]=orderedRanks[3];
            value[5]=orderedRanks[4];
        }

        if (sameCards==2 && sameCards2==1)//if 1 pair
        {
            value[0]=2;                //pair ranked higher than high card
            value[1]=largeGroupRank;   //rank of pair
            value[2]=orderedRanks[0];  //next highest cards.
            value[3]=orderedRanks[1];
            value[4]=orderedRanks[2];
        }

        if (sameCards==2 && sameCards2==2) //two pair
        {
            value[0]=3;
            //rank of greater pair
            value[1]= largeGroupRank>smallGroupRank ? largeGroupRank : smallGroupRank;
            value[2]= largeGroupRank<smallGroupRank ? largeGroupRank : smallGroupRank;
            value[3]=orderedRanks[0];  //extra card
        }

        if (sameCards==3 && sameCards2!=2)//three of a kind (not full house)
        {
            value[0]=4;
            value[1]= largeGroupRank;
            value[2]=orderedRanks[0];
            value[3]=orderedRanks[1];
        }

        if (straight && !flush)
        {
            value[0]=5;
        }

        if (flush && !straight)
        {
            value[0]=6;
            value[1]=orderedRanks[0]; //tie determined by ranks of cards
            value[2]=orderedRanks[1];
            value[3]=orderedRanks[2];
            value[4]=orderedRanks[3];
            value[5]=orderedRanks[4];
        }

        if (sameCards==3 && sameCards2==2)//full house
        {
            value[0]=7;
            value[1]=largeGroupRank;
            value[2]=smallGroupRank;
        }

        if (sameCards==4)  //four of a kind
        {
            value[0]=8;
            value[1]=largeGroupRank;
            value[2]=orderedRanks[0];
        }

        if (straight && flush)//straight flush
        {
            value[0]=9;
        }
        //royal flush: straight+ flush+ contains A J Q K
        if(straight && flush && duplicateChecker.containsKey('A')&& duplicateChecker.containsKey('J')&& duplicateChecker.containsKey('K')&& duplicateChecker.containsKey('Q')&&duplicateChecker.containsKey('T'))
        {
        	value[0]=10;
        }
    }

    void display()
    {    	
    	/*Based on the value[0] ranging from 0 to 10 I used that to index 'VALUES' and if there is any other value then the application sets an error message*/
    	if(value[0] > 0 && value[0] < 11)
    		resultStr=VALUES[value[0]];
    	else
    		resultStr="error in Hand.display: value[0] contains invalid value";
      
    }
    
    
    @Override
    public String toString()
    {
    	String result="";
    	//return the arraylist <hand [full hand], 'Type'> in this fashion
    	for(int i=0;i<this.cards.length;i++)
    	{
    		int rankTempVariable=this.cards[i].getRank();
    		char rankCharValue='Z';
    		// set this temporary variable to Z if its value was changed in the next few steps then it implies A J Q K T was updated accordingly, 
    		//if not it means that the rank to be printed was between 2 and 9
    		
    		if(i==this.cards.length-1)// do not add ',' to the resultant string if this is the last card in hand
    			{
	    			if(rankTempVariable==10)
	    				rankCharValue='T';
	    			else if(rankTempVariable==1)
	    				rankCharValue='A';
	    			else if(rankTempVariable==12)
	    				rankCharValue='Q';
	    			else if(rankTempVariable==11)
	    				rankCharValue='J';
	    			else if(rankTempVariable==13)
	    				rankCharValue='K';
	    			
    				if(rankCharValue=='Z')
    					result+=Integer.toString(this.cards[i].getRank())+Character.toString(this.cards[i].getSuit())+"],";
    				else
    					result+=Character.toString(rankCharValue)+Character.toString(this.cards[i].getSuit())+"],";
    			}
    		else//// Add ',' to the resultant string if this is not the last card in hand
    		{
    			if(rankTempVariable==10)
    				rankCharValue='T';
    			else if(rankTempVariable==1)
    				rankCharValue='A';
    			else if(rankTempVariable==12)
    				rankCharValue='Q';
    			else if(rankTempVariable==11)
    				rankCharValue='J';
    			else if(rankTempVariable==13)
    				rankCharValue='K';
    			
				if(rankCharValue=='Z')
					result+=Integer.toString(this.cards[i].getRank())+Character.toString(this.cards[i].getSuit())+",";
				else
					result+=Character.toString(rankCharValue)+Character.toString(this.cards[i].getSuit())+",";
			}
    			
    	}
    	return "<hand["+result+"'"+resultStr+"'>";
    }
  
   
/*to compare Hands I implemneted function overriding for 'compareTo' method.
 * The Collections.sort method uses this method to sort the list passed as argument*/

	@Override
	public int compareTo(Hand that) {
		// TODO Auto-generated method stub
		
		 //sort in decreasing order of ranks from Royal Flush being the highest to High card being the lowest
		if(this.value[0]>that.value[0])
    		return -1;
    	else if(this.value[0]<that.value[0])
    		return 1;
    	//If the Hands are equal return 0
		return 0;
	}





}