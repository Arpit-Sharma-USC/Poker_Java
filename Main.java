package javapoker;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
	//This is a Tester Class to test the Hand class

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	        Hand hand= new Hand("5S 6S 7S 8S 9S");
	        Hand hand2= new Hand("TS JS QS KS AS");
	        Hand hand3=new Hand("4D 5D 6D 7H 8D");
	        Hand hand4= new Hand("2D 3D 7D QD AD");
	        Hand hand5=new Hand("5H 5C QD QC QS");
	        Hand hand6=new Hand("7S TC TH TS TD");
	  
	        System.out.println("Can compare individual Hands too! \n");
	        
	        //the cases will be reveresed as sort is in decreasing order of ''VALUE
	        switch(hand.compareTo(hand2))
	        {
	        	case -1:	        
	        		System.out.println(hand+" is better than "+ hand2);
    				break;
	        	case 1:
	        		System.out.println(hand2+" is better than "+ hand);
	        		break;
	        	case 0:
	        		System.out.println(hand+" and "+ hand2+ " are equal");
	        		break;
	        }
	        		
	    
	        /*Adding hands to an ArrayList and using the sort method on the collection*/
	        
	        ArrayList<Hand> ListOfHands=new ArrayList<>();
	        
	        ListOfHands.add(hand);
	        ListOfHands.add(hand2);
	        ListOfHands.add(hand3);
	        ListOfHands.add(hand4);
	        ListOfHands.add(hand5);
	        ListOfHands.add(hand6);
	        
	        Collections.sort(ListOfHands);
	        
	        /*Lets us print each Hand in the list to */
	        System.out.println("\nAfter sorting:"+"\n-----------------------------");
	        for(Hand tempHand:ListOfHands)
	        {
	        	System.out.println("\n"+tempHand.toString());
	        }
	}

}
