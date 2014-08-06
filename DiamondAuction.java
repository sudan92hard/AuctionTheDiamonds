/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuctionTheDiamonds;

import java.util.*;

/**
 *
 * @author vidv
 */
class Player {

    private ArrayList<String> hand;
    private float score;
    private int playerNumber;

    public Player(int no) {
        score = 0.0F;
        this.playerNumber = no;
        hand = new ArrayList<String>();
        initializeHand();
    }

    public int getPlayerNo() {
        return this.playerNumber;
    }
    
    public ArrayList<String> getHand() {
        return this.hand;
    }

    private void initializeHand() {
        hand.add("A");
        hand.add("K");
        hand.add("Q");
        hand.add("J");
        for (int i = 10; i >= 2; i--) {
            hand.add("" + i);
        }
    }

    public float getScore() {
        return this.score;
    }

    public void removeCardFromHand(String card) {
        this.hand.remove(card);
    }

    public boolean checkBidLegality(String bid) {
        return this.hand.contains(bid);
    }

    public String toString() {
        return "Player " + this.playerNumber + " Score: " + this.score;
    }

    public void updateScore(float value) {
        this.score += value;
    }
}

public class DiamondAuction {

    private Scanner readInput;
    public HashMap<String, Integer> cardScore;
    private int noOfPlayers;
    private ArrayList<Player> Players;
    private ArrayList<String> diamondDeck;

    public DiamondAuction() {
        noOfPlayers = 0;
        readInput = new Scanner(System.in);
        cardScore = new HashMap<String, Integer>();
        Players = new ArrayList<Player>();
        diamondDeck = new ArrayList<String>();
        initializeCardScore();
    }

    private void initializeDiamondDeck() {
        diamondDeck.add("A");
        diamondDeck.add("K");
        diamondDeck.add("Q");
        diamondDeck.add("J");
        for (int i = 10; i >= 2; i--) {
            diamondDeck.add("" + i);
        }

        shuffleTheDeck();
    }

    public void setNoOfPlayers() {
        
        int playerCount = 0;
        while (playerCount != 2 && playerCount != 3) {
            System.out.print("Enter number of Players: ");
            playerCount = Integer.parseInt(readInput.nextLine());
        }

        this.noOfPlayers = playerCount;
        int i = 0;
        while (i < noOfPlayers) {
            Player player = new Player(i + 1);
            Players.add(player);
            ++i;
        }
    }

    public boolean checkIfValidInput(String input) {
        return cardScore.containsKey(input);
    }

    public void initializeCardScore() {
        cardScore.put("A", 14);
        cardScore.put("K", 13);
        cardScore.put("Q", 12);
        cardScore.put("J", 11);
        cardScore.put("10", 10);
        cardScore.put("9", 9);
        cardScore.put("8", 8);
        cardScore.put("7", 7);
        cardScore.put("6", 6);
        cardScore.put("5", 5);
        cardScore.put("4", 4);
        cardScore.put("3", 3);
        cardScore.put("2", 2);
    }

    public String getUserInput() {
        return readInput.nextLine();
    }

    public ArrayList<String> removeCardFromHand(ArrayList<String> hand, String card) {
        hand.remove(card);
        return hand;
    }

    public String displayAndRemoveTopCardOfDeck() {
        System.out.println("The Diamond being bid is : " + this.diamondDeck.get(0));
        return this.diamondDeck.remove(0);
    }

    public ArrayList<String> readPlayerBids() {
        ArrayList<String> bids = new ArrayList<String>();
        int i = 0;
        for (Player player : Players) {
            String bid = "";
            do {
                System.out.print("Player " + player.getPlayerNo() + " " + player.getHand() + " :");
                bid = getUserInput();
            } while (!legalBid(player, bid));
            updatePlayersHand(player, bid);
            bids.add("" + cardScore.get(bid));
        }
        return bids;
    }

    public void updatePlayersHand(Player player, String bid) {
        player.removeCardFromHand(bid);
    }

    public boolean legalBid(Player player, String bid) {
        return player.checkBidLegality(bid);
    }

    public int[] findMaxValue(ArrayList<String> bids) {
        int maxValue = Integer.MIN_VALUE;
        int counter = 1;
        int[] values = new int[2];
        for (String bid : bids) {

            int value = Integer.parseInt(bid);
            if (value > maxValue) {
                maxValue = value;
                counter = 1;
            } else if (value == maxValue) {
                counter++;
            }
        }
        
        values[0] = maxValue;
        values[1] = counter;
        return values;
    }

    public void shuffleTheDeck() {
        Random rand = new Random();
        for (int i = 0; i < this.diamondDeck.size(); i++) {
            int randomPosition = rand.nextInt(this.diamondDeck.size());
            String temp = this.diamondDeck.get(randomPosition);
            this.diamondDeck.remove(randomPosition);
            this.diamondDeck.add(temp);
        }
    }

    public Player[] pickWinningBid(ArrayList<String> bids, int maxValue, int counter, String topCard) {
        int points = this.cardScore.get(topCard);
        float value = points / counter;
        int index = 0;
        Player[] player = new Player[counter];
        while (counter > 0) {
            index = bids.indexOf("" + maxValue);
            bids.set(index, "0");
            Players.get(index).updateScore(value);
            player[counter-1] = Players.get(index);
            counter--;
        }
        
        return player;
    }
    
    public void printBidWinners(Player[] player) {
        System.out.println("Bid Winner(s)");
        for(Player p: player ) {
            System.out.println(p);
        }
    }

    public void startBidding() {
        initializeDiamondDeck();
        while (!diamondDeck.isEmpty()) {
            String topCard = displayAndRemoveTopCardOfDeck();
            System.out.println("Players may now place your bids");
            ArrayList<String> bids = readPlayerBids();
            int[] maxBidDetails = findMaxValue(bids);
            int maxValue = maxBidDetails[0];
            int counter = maxBidDetails[1];
            Player[] player  = pickWinningBid(bids, maxValue, counter, topCard); 
            printBidWinners(player);
            System.out.println("Next round of bidding begins");
        }
    }

    public void announceWinner() {
        float maxScore = 0.0F;

        System.out.println("The Scores of the players are: ");
        for (Player player : Players) {
            System.out.println(player);
            float currentScore = player.getScore();
            if (currentScore > maxScore) {
                maxScore = currentScore;
            }
        }
        System.out.println("The Winners of the Auction are: ");
        for (Player player : Players) {
            if (player.getScore() == maxScore) {
                System.out.println(player);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Diamond Auction");
        DiamondAuction auction = new DiamondAuction();
        auction.setNoOfPlayers();
        auction.startBidding();
        auction.announceWinner();

    }

}