package com.yedidya.magiccardtrick;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // UI Elements
    private Spinner[] numberSpinners = new Spinner[4];
    private Spinner[] suitSpinners = new Spinner[4];
    private Button calculateButton;
    private TextView resultTitle;
    private ImageView resultCardImage;
    private View resultContainer;

    // Card data
    private String[] numbers = {"", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private String[] suits = {"", "hearts", "diamonds", "clubs", "spades"};
    private HashMap<String, Double> suitValues = new HashMap<>();

    // Selected cards
    private Card[] selectedCards = new Card[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the suit values
        suitValues.put("clubs", 0.4);
        suitValues.put("hearts", 0.3);
        suitValues.put("spades", 0.2);
        suitValues.put("diamonds", 0.1);

        // Initialize the card array
        for (int i = 0; i < 4; i++) {
            selectedCards[i] = new Card("", "");
        }

        // Set up UI elements
        setupUIElements();
        setupSpinners();
        setupCalculateButton();
    }

    private void setupUIElements() {
        // Initialize number spinners
        numberSpinners[0] = findViewById(R.id.spinner_card1_number);
        numberSpinners[1] = findViewById(R.id.spinner_card2_number);
        numberSpinners[2] = findViewById(R.id.spinner_card3_number);
        numberSpinners[3] = findViewById(R.id.spinner_card4_number);

        // Initialize suit spinners
        suitSpinners[0] = findViewById(R.id.spinner_card1_suit);
        suitSpinners[1] = findViewById(R.id.spinner_card2_suit);
        suitSpinners[2] = findViewById(R.id.spinner_card3_suit);
        suitSpinners[3] = findViewById(R.id.spinner_card4_suit);

        // Initialize button and result views
        calculateButton = findViewById(R.id.button_calculate);
        resultTitle = findViewById(R.id.text_result_title);
        resultCardImage = findViewById(R.id.image_result_card);
        resultContainer = findViewById(R.id.layout_result_container);

        // Hide result container initially
        resultContainer.setVisibility(View.GONE);
    }

    private void setupSpinners() {
        // Load arrays from resources instead of hardcoded values
        String[] numberItems = getResources().getStringArray(R.array.card_numbers);
        String[] suitItems = getResources().getStringArray(R.array.card_suits);
        String[] suitValues = getResources().getStringArray(R.array.suit_values);

        // Create adapters for number spinners with proper styling
        ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, numberItems);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create adapters for suit spinners
        ArrayAdapter<String> suitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, suitItems);
        suitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters and listeners for all spinners
        for (int i = 0; i < 4; i++) {
            numberSpinners[i].setAdapter(numberAdapter);
            suitSpinners[i].setAdapter(suitAdapter);

            final int index = i;

            // Set number spinner listeners
            numberSpinners[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        // First item is the prompt, set an empty value
                        selectedCards[index].setNumber("");
                    } else {
                        selectedCards[index].setNumber(numbers[position]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

            // Set suit spinner listeners
            suitSpinners[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        // First item is the prompt, set an empty value
                        selectedCards[index].setSuit("");
                    } else {
                        // Use the actual suit value, not the display emoji
                        selectedCards[index].setSuit(suitValues[position]);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
        }
    }
    private void setupCalculateButton() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all cards are selected
                for (Card card : selectedCards) {
                    if (card.getNumber().isEmpty() || card.getSuit().isEmpty()) {
                        // Show error message
                        Utilities.showToast(MainActivity.this, "Please select all card values and suits");
                        return;
                    }
                }

                // Calculate the magic card
                Card resultCard = calculateChosenCard(selectedCards);

                // Display the result
                displayResult(resultCard);
            }
        });
    }

    private void displayResult(Card card) {
        // Set the result title
        resultTitle.setText(R.string.the_magic_card_is);

        // Convert short card names to full names for resource lookup
        String cardNumber = card.getNumber();
        String fullCardName;

        switch(cardNumber) {
            case "A": fullCardName = "ace"; break;
            case "J": fullCardName = "jack"; break;
            case "Q": fullCardName = "queen"; break;
            case "K": fullCardName = "king"; break;
            default: fullCardName = cardNumber;
        }

        // Get the resource ID for the card image
        String resourceName;
        if (fullCardName.equals("2") || fullCardName.equals("3") || fullCardName.equals("4") ||
                fullCardName.equals("5") || fullCardName.equals("6") || fullCardName.equals("7") ||
                fullCardName.equals("8") || fullCardName.equals("9") || fullCardName.equals("10")) {
            resourceName = "card_" + fullCardName + "_of_" + card.getSuit().toLowerCase();
        } else {
            resourceName = fullCardName + "_of_" + card.getSuit().toLowerCase();
        }

        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        // Log the resource name for debugging
        android.util.Log.d("MagicCardTrick", "Looking for resource: " + resourceName);

        // If resource not found, use fallback text-based display
        if (resourceId == 0) {
            // Fallback handling - show a toast with the missing resource name
            Utilities.showToast(MainActivity.this, "Card image not found: " + resourceName);

            // Create a text-based representation of the card
            TextView textCard = new TextView(this);
            textCard.setText(card.getNumber() + (card.getSuit().equals("hearts") || card.getSuit().equals("diamonds") ? "♥♦" : "♣♠"));
            textCard.setTextSize(36);
            textCard.setTextColor(card.getSuit().equals("hearts") || card.getSuit().equals("diamonds") ?
                    getResources().getColor(android.R.color.holo_red_dark) :
                    getResources().getColor(android.R.color.black));

            // Add the text view to the result container
            // This requires modifying the layout to include a container for either the image or text
        } else {
            resultCardImage.setImageResource(resourceId);
        }

        // Make result container visible with animation
        resultContainer.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        resultContainer.startAnimation(animation);
    }

    private Card calculateChosenCard(Card[] cards) {
        // Convert cards to numerical values
        double[] cardValues = new double[4];
        for (int i = 0; i < 4; i++) {
            Card card = cards[i];
            double numVal;

            // Convert card number to numerical value
            if (card.getNumber().equals("A")) {
                numVal = 1;
            } else if (card.getNumber().equals("J")) {
                numVal = 11;
            } else if (card.getNumber().equals("Q")) {
                numVal = 12;
            } else if (card.getNumber().equals("K")) {
                numVal = 13;
            } else {
                numVal = Integer.parseInt(card.getNumber());
            }

            // Add suit value
            double suitVal = suitValues.get(card.getSuit());
            cardValues[i] = numVal + suitVal;
        }

        // Calculation logic based on card order
        double val1 = cardValues[1];
        double val2 = cardValues[2];
        double val3 = cardValues[3];

        // Sort the values
        double[] sortedValues = new double[3];
        sortedValues[0] = Math.min(Math.min(val1, val2), val3);
        sortedValues[2] = Math.max(Math.max(val1, val2), val3);

        // Find the middle value
        if (val1 != sortedValues[0] && val1 != sortedValues[2]) {
            sortedValues[1] = val1;
        } else if (val2 != sortedValues[0] && val2 != sortedValues[2]) {
            sortedValues[1] = val2;
        } else {
            sortedValues[1] = val3;
        }

        // Determine what to add based on relative ordering
        int numToAdd;
        if (val1 == sortedValues[2]) {
            numToAdd = val2 > val3 ? 6 : 5;
        } else if (val1 == sortedValues[1]) {
            numToAdd = val2 > val3 ? 4 : 3;
        } else {
            numToAdd = val2 > val3 ? 2 : 1;
        }

        // Calculate result number
        int resultNum = (int)(cardValues[0] + numToAdd);
        // Ensure the number wraps between 1 and 13 correctly
        resultNum = ((resultNum - 1) % 13) + 1;

        // Convert back to card representation
        String resultNumber;
        if (resultNum == 1) {
            resultNumber = "A";
        } else if (resultNum == 11) {
            resultNumber = "J";
        } else if (resultNum == 12) {
            resultNumber = "Q";
        } else if (resultNum == 13) {
            resultNumber = "K";
        } else {
            resultNumber = String.valueOf(resultNum);
        }

        // Return a new Card with calculated value and the suit of the first card
        return new Card(resultNumber, cards[0].getSuit());
    }
}