package com.yedidya.magiccardtrick;

/**
 * Represents a playing card with a number (or face value) and suit.
 */
public class Card {
    private String number;
    private String suit;

    /**
     * Constructs a new card with the given number and suit.
     *
     * @param number The card number or face value (A, 2-10, J, Q, K)
     * @param suit The card suit (hearts, diamonds, clubs, spades)
     */
    public Card(String number, String suit) {
        this.number = number;
        this.suit = suit;
    }

    /**
     * Gets the card's number or face value.
     *
     * @return The card number as a string
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the card's number or face value.
     *
     * @param number The number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the card's suit.
     *
     * @return The card suit as a string
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Sets the card's suit.
     *
     * @param suit The suit to set
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * Creates a string representation of the card.
     *
     * @return A string in the format "number of suit"
     */
    @Override
    public String toString() {
        return number + " of " + suit;
    }
}