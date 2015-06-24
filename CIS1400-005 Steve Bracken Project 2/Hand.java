import javax.swing.*;

public class Hand
 {
 public static void main(String[] args) {

    for (int i=0; i<1; i++)
    {
        Deck deck= new Deck();
        Hand hand= new Hand(deck);
        Hand hand2= new Hand(deck);
        hand.display();
        hand.displayAll();
        hand2.display();
        hand2.displayAll();
        JOptionPane.showMessageDialog(null, hand.compareTo(hand2));

    }
}
    private Card[] cards;
    private int[] value;

    Hand(Deck d)
    {
        value = new int[6];
        cards = new Card[5];
        for (int x=0; x<5; x++)
        {
            cards[x] = d.drawFromDeck();
        }

        int[] ranks = new int[14];
        //miscellaneous cards 
        int[] orderedRanks = new int[5];
        boolean flush=true, straight=false;
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
        for (int x=0; x<4; x++) {
            if ( cards[x].getSuit() != cards[x+1].getSuit() )
                flush=false;
        }

        for (int x=13; x>=1; x--)
        {
                 if (ranks[x] > sameCards)
                 {
                     if (sameCards != 1)
                    
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

        if (ranks[1]==1) 
        {
            orderedRanks[index]=14;
            index++;
        }

        for (int x=13; x>=2; x--)
        {
            if (ranks[x]==1)
            {
                orderedRanks[index]=x; 
                index++;
            }
        }
        
        for (int x=1; x<=9; x++)
        
        {
            if (ranks[x]==1 && ranks[x+1]==1 && ranks[x+2]==1 && 
                ranks[x+3]==1 && ranks[x+4]==1)
            {
                straight=true;
                topStraightValue=x+4; 
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


        //start hand evaluation
        if ( sameCards==1 ) {
            value[0]=1;
            value[1]=orderedRanks[0];
            value[2]=orderedRanks[1];
            value[3]=orderedRanks[2];
            value[4]=orderedRanks[3];
            value[5]=orderedRanks[4];
        }

        if (sameCards==2 && sameCards2==1)
        {
            value[0]=2;
            value[1]=largeGroupRank; //pair rank
            value[2]=orderedRanks[0];
            value[3]=orderedRanks[1];
            value[4]=orderedRanks[2];
        }

        if (sameCards==2 && sameCards2==2) //two pair
        {
            value[0]=3;
            //higher value pairs
            value[1]= largeGroupRank>smallGroupRank ? largeGroupRank : smallGroupRank;
            value[2]= largeGroupRank<smallGroupRank ? largeGroupRank : smallGroupRank;
            value[3]=orderedRanks[0];  //extra card
        }

        if (sameCards==3 && sameCards2!=2)
        {
            value[0]=4;
            value[1]= largeGroupRank;
            value[2]=orderedRanks[0];
            value[3]=orderedRanks[1];
        }

        if (straight && !flush)
        {
            value[0]=5;
            value[1]=0;
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

        if (sameCards==3 && sameCards2==2)
        {
            value[0]=7;
            value[1]=largeGroupRank;
            value[2]=smallGroupRank;
        }

        if (sameCards==4)
        {
            value[0]=8;
            value[1]=largeGroupRank;
            value[2]=orderedRanks[0];
        }

        if (straight && flush)
        {
            value[0]=9;
            value[1]=0;
        }

    }

    void display()
    {
	 		JTextArea outputArea = new JTextArea(14, 140);
      	JScrollPane scroller = new JScrollPane( outputArea );
       	String steve = "";
		 
        switch( value[0] )
        {

            case 1:
                steve="High card";
                break;
            case 2:
                steve="Pair of " + Card.rankAsString(value[1]) + "\'s";
                break;
            case 3:
                steve="Two pair " + Card.rankAsString(value[1]) + " " + 
                                Card.rankAsString(value[2]);
                break;
            case 4:
                steve="Three of a kind " + Card.rankAsString(value[1]) + "\'s";
                break;
            case 5:
                steve=Card.rankAsString(value[1]) + "High straight";
                break;
            case 6:
                steve="Flush";
                break;
            case 7:
                steve="Full house " + Card.rankAsString(value[1]) + "over" + 
                                  Card.rankAsString(value[2]);
                break;
            case 8:
                steve="Four of a kind" + Card.rankAsString(value[1]);
                break;
            case 9:
                steve="Straight flush" + Card.rankAsString(value[1]) + "high";
                break;
            default:
                steve="error in Hand.display: value[0] contains invalid value";
        }
        steve = "" + steve;
      	//JOptionPane.showMessageDialog(null,steve);
			outputArea.setText( steve ); 
      	JOptionPane.showMessageDialog(
         null, scroller, "Winning Cards:" + steve,
         JOptionPane.INFORMATION_MESSAGE );
    }

    void displayAll()
    {
        for (int x=0; x<5; x++)
            JOptionPane.showMessageDialog(null,cards[x]);
    }

    int compareTo(Hand that)
    {
        for (int x=0; x<6; x++)
        {
            if (this.value[0]>that.value[x])
                return 1; // Hand 1 Wins
            else if (this.value[x]<that.value[x])
                return 2; //Hand 2 Wins
				
        }
        return 0; //Tie 
		  
    }
}