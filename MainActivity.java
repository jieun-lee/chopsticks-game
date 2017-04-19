package com.example.android.chopsticksgame;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String PLAYER_1 = "Player 1's turn:";
    public static final String PLAYER_2 = "Player 2's turn:";
    public static final String NEW_TURN = " select one of your hands to begin.";
    public static final String ONE_SELECTED = "Tap opponent's hand to transfer points, or tap your own hand to change the ratio.";
    public static final String SELF_TAP = "Use the buttons on the left and right to change up the ratio.";
    public static final String BAD_REQUEST = "Bad request! Try again:";

    public static final int COLOR_TURN = Color.YELLOW;
    public static final int COLOR_NORMAL = Color.WHITE;

    // indicates which player's turn it is
    private int turn;

    // indicates which hand of current player is selected
    // 0: unselected
    // 1: left
    // 2: right
    private int selectedHand;

    // indicates how many fingers the player has left on each hand
    // 0 indicates the hand is dead
    private int oneLeft;
    private int oneRight;
    private int twoLeft;
    private int twoRight;

    private LinearLayout messagePanel;
    private TextView messageBox;
    private Button okButton;

    // reference to the corresponding text view
    private TextView oneLeftView;
    private TextView oneRightView;
    private TextView twoLeftView;
    private TextView twoRightView;

    private TextView oneText;
    private TextView twoText;

    private boolean changeRatioMode;
    private int tempLeft;
    private int tempRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagePanel = (LinearLayout) findViewById(R.id.message_panel);
        messageBox = (TextView) findViewById(R.id.message_box);

        // set the references to the corresponding text views
        oneLeftView = (TextView) findViewById(R.id.player_1_left);
        oneRightView = (TextView) findViewById(R.id.player_1_right);
        twoLeftView = (TextView) findViewById(R.id.player_2_left);
        twoRightView = (TextView) findViewById(R.id.player_2_right);

        oneText = (TextView) findViewById(R.id.player_1_text);
        twoText = (TextView) findViewById(R.id.player_2_text);

        okButton = (Button) findViewById(R.id.ok_button);

        setup();
    }

    // initialize values for a new game
    private void setup() {
        // set turn
        turn = 1;
        oneText.setBackgroundColor(COLOR_TURN);
        messagePanel.setRotation(0);
        messageBox.setText(PLAYER_1 + NEW_TURN);
        updateViewEnabled(true, true, false, false);

        // no hand is selected
        selectedHand = 0;
        setBoldText(1, false);
        setBoldText(2, false);
        setBoldText(3, false);
        setBoldText(4, false);

        okButton.setEnabled(false);

        changeRatioMode = false;

        // set all values to 1 and update all hands
        oneLeft = 1;
        oneRight = 1;
        twoLeft = 1;
        twoRight = 1;
        updateHand(1);
        updateHand(2);
        updateHand(3);
        updateHand(4);
    }

    // sets selected hand to have bold text
    // for int hand, 1 represents oneLeft, and 4 represents twoRight
    private void setBoldText(int hand, boolean isBold) {
        switch (hand) {
            case 1:
                if (isBold) {
                    oneLeftView.setTypeface(null, Typeface.BOLD);
                } else {
                    oneLeftView.setTypeface(null, Typeface.NORMAL);
                } break;

            case 2:
                if (isBold) {
                    oneRightView.setTypeface(null, Typeface.BOLD);
                } else {
                    oneRightView.setTypeface(null, Typeface.NORMAL);
                } break;

            case 3:
                if (isBold) {
                    twoLeftView.setTypeface(null, Typeface.BOLD);
                } else {
                    twoLeftView.setTypeface(null, Typeface.NORMAL);
                } break;

            case 4:
                if (isBold) {
                    twoRightView.setTypeface(null, Typeface.BOLD);
                } else {
                    twoRightView.setTypeface(null, Typeface.NORMAL);
                } break;

            default:
                break;
        }
    }

    private void updateViewEnabled(boolean oneLeft, boolean oneRight, boolean twoLeft, boolean twoRight) {
        oneLeftView.setEnabled(oneLeft);
        oneRightView.setEnabled(oneRight);
        twoLeftView.setEnabled(twoLeft);
        twoRightView.setEnabled(twoRight);
    }

    // sets the text view and updates the hand with the correct number
    // for int hand, 1 represents oneLeft, and 4 represents twoRight
    private void updateHand(int hand) {
        switch(hand) {
            case 1:
                if (oneLeft == 0) {
                    oneLeftView.setText("");
                } else {
                    oneLeftView.setText("" + oneLeft);
                } break;

            case 2:
                if (oneRight == 0) {
                    oneRightView.setText("");
                } else {
                    oneRightView.setText("" + oneRight);
                } break;

            case 3:
                if (twoLeft == 0) {
                    twoLeftView.setText("");
                } else {
                    twoLeftView.setText("" + twoLeft);
                } break;

            case 4:
                if (twoRight == 0) {
                    twoRightView.setText("");
                } else {
                    twoRightView.setText("" + twoRight);
                } break;

            default:
                break;
        }
    }

    // switch players and rotate the message panel accordingly
    private void switchTurn() {
        // nothing is selected
        selectedHand = 0;

        if (turn == 1) {
            // switch from player 1 to player 2
            setBoldText(1, false);
            setBoldText(2, false);
            turn = 2;
            oneText.setBackgroundColor(COLOR_NORMAL);
            twoText.setBackgroundColor(COLOR_TURN);
            messagePanel.setRotation(180);
            messageBox.setText(PLAYER_2 + NEW_TURN);
            updateViewEnabled(false, false, true, true);
        } else {
            // switch from player 2 to player 1
            setBoldText(3, false);
            setBoldText(4, false);
            turn = 1;
            oneText.setBackgroundColor(COLOR_TURN);
            twoText.setBackgroundColor(COLOR_NORMAL);
            messagePanel.setRotation(0);
            messageBox.setText(PLAYER_1 + NEW_TURN);
            updateViewEnabled(true, true, false, false);
        }
    }

    // click player one's left hand
    public void clickPlayerOneLeft(View v) {

        // if it's the current player's turn
        if (turn == 1) {

            // if change ratio mode, increase / decrease
            if (changeRatioMode) {
                // if the ratio can be changed, change the temporary values
                if (tempLeft < 5 && tempRight > 0) {
                    tempLeft++;
                    tempRight--;
                    oneLeftView.setText("" + tempLeft);
                    oneRightView.setText("" + tempRight);
                }
            }

            // selection mode
            else {
                // no hand selected, set this hand as selected
                if (selectedHand == 0) {

                    // current hand is dead; can't do anything
                    if (oneLeft == 0) {
                        return;
                    }

                    // current hand becomes active
                    else {
                        selectedHand = 1;
                        setBoldText(1, true);
                        messageBox.setText(ONE_SELECTED);
                        // disable current hand, enable the others
                        updateViewEnabled(false, true, true, true);
                    }
                }

                // right hand is already selected
                // turn change ratio mode on
                else {
                    changeRatioMode = true;
                    tempLeft = oneLeft;
                    tempRight = oneRight;
                    setBoldText(1, true);
                    updateViewEnabled(true, true, false, false);
                    okButton.setEnabled(true);
                    messageBox.setText(SELF_TAP);
                }
            }
        }

        // if it's not the current player's turn
        else {

            // oneLeft is dead already
            if (oneLeft == 0) {
                return;
            } else {

                // transfer points
                if (selectedHand == 1) {
                    // player two's left hand is selected
                    oneLeft = oneLeft + twoLeft;
                } else {
                    // player two's right hand is selected
                    oneLeft = oneLeft + twoRight;
                }

                // if oneLeft is dead, set to 0
                if (oneLeft >= 5) {
                    oneLeft = 0;
                }
                updateHand(1);
                switchTurn();
            }
        }
    }

    // click player one's right hand
    public void clickPlayerOneRight(View v) {

        // if it's the current player's turn
        if (turn == 1) {

            // if change ratio mode, increase / decrease
            if (changeRatioMode) {
                // if the ratio can be changed, change the temporary values
                if (tempRight < 5 && tempLeft > 0) {
                    tempRight++;
                    tempLeft--;
                    oneLeftView.setText("" + tempLeft);
                    oneRightView.setText("" + tempRight);
                }
            }

            // selection mode
            else {
                // no hand selected, set this hand as selected
                if (selectedHand == 0) {

                    // current hand is dead; can't do anything
                    if (oneRight == 0) {
                        return;
                    }

                    // current hand becomes active
                    else {
                        selectedHand = 2;
                        setBoldText(2, true);
                        messageBox.setText(ONE_SELECTED);
                        // disable current hand, enable the others
                        updateViewEnabled(true, false, true, true);
                    }
                }

                // left hand is already selected
                // turn change ratio mode on
                else {
                    changeRatioMode = true;
                    tempLeft = oneLeft;
                    tempRight = oneRight;
                    setBoldText(2, true);
                    updateViewEnabled(true, true, false, false);
                    okButton.setEnabled(true);
                    messageBox.setText(SELF_TAP);
                }
            }
        }

        // if it's not the current player's turn
        else {

            // oneRight is dead already
            if (oneRight == 0) {
                return;
            } else {

                // transfer points
                if (selectedHand == 1) {
                    // player two's left hand is selected
                    oneRight = oneRight + twoLeft;
                } else {
                    // player two's right hand is selected
                    oneRight = oneRight + twoRight;
                }

                // if oneRight is dead, set to 0
                if (oneRight >= 5) {
                    oneRight = 0;
                }
                updateHand(2);
                switchTurn();
            }
        }
    }

    // click player two's left hand
    public void clickPlayerTwoLeft(View v) {

        // if it's the current player's turn
        if (turn == 2) {

            // if change ratio mode, increase / decrease
            if (changeRatioMode) {
                // if the ratio can be changed, change the temporary values
                if (tempLeft < 5 && tempRight > 0) {
                    tempLeft++;
                    tempRight--;
                    twoLeftView.setText("" + tempLeft);
                    twoRightView.setText("" + tempRight);
                }
            }

            // selection mode
            else {
                // no hand selected, set this hand as selected
                if (selectedHand == 0) {

                    // current hand is dead; can't do anything
                    if (twoLeft == 0) {
                        return;
                    }

                    // current hand becomes active
                    else {
                        selectedHand = 1;
                        setBoldText(3, true);
                        messageBox.setText(ONE_SELECTED);
                        // disable current hand, enable the others
                        updateViewEnabled(true, true, false, true);
                    }
                }

                // left hand is already selected
                // turn change ratio mode on
                else {
                    changeRatioMode = true;
                    tempLeft = twoLeft;
                    tempRight = twoRight;
                    setBoldText(3, true);
                    updateViewEnabled(false, false, true, true);
                    okButton.setEnabled(true);
                    messageBox.setText(SELF_TAP);
                }
            }
        }

        // if it's not the current player's turn
        else {

            // twoLeft is dead already
            if (twoLeft == 0) {
                return;
            } else {

                // transfer points
                if (selectedHand == 1) {
                    // player one's left hand is selected
                    twoLeft = twoLeft + oneLeft;
                } else {
                    // player one's right hand is selected
                    twoLeft = twoLeft + oneRight;
                }

                // if twoLeft is dead, set to 0
                if (twoLeft >= 5) {
                    twoLeft = 0;
                }
                updateHand(3);
                switchTurn();
            }
        }
    }

    // click player two's right hand
    public void clickPlayerTwoRight(View v) {

        // if it's the current player's turn
        if (turn == 2) {

            // if change ratio mode, increase / decrease
            if (changeRatioMode) {
                // if the ratio can be changed, change the temporary values
                if (tempRight < 5 && tempLeft > 0) {
                    tempRight++;
                    tempLeft--;
                    twoLeftView.setText("" + tempLeft);
                    twoRightView.setText("" + tempRight);
                }
            }

            // selection mode
            else {
                // no hand selected, set this hand as selected
                if (selectedHand == 0) {

                    // current hand is dead; can't do anything
                    if (twoRight == 0) {
                        return;
                    }

                    // current hand becomes active
                    else {
                        selectedHand = 2;
                        setBoldText(4, true);
                        messageBox.setText(ONE_SELECTED);
                        // disable current hand, enable the others
                        updateViewEnabled(true, true, true, false);
                    }
                }

                // left hand is already selected
                // turn change ratio mode on
                else {
                    changeRatioMode = true;
                    tempLeft = twoLeft;
                    tempRight = twoRight;
                    setBoldText(4, true);
                    updateViewEnabled(false, false, true, true);
                    okButton.setEnabled(true);
                    messageBox.setText(SELF_TAP);
                }
            }
        }

        // if it's not the current player's turn
        else {

            // twoRight is dead already
            if (twoRight == 0) {
                return;
            } else {

                // transfer points
                if (selectedHand == 1) {
                    // player one's left hand is selected
                    twoRight = twoRight + oneLeft;
                } else {
                    // player one's right hand is selected
                    twoRight = twoRight + oneRight;
                }

                // if twoRight is dead, set to 0
                if (twoRight >= 5) {
                    twoRight = 0;
                }
                updateHand(4);
                switchTurn();
            }
        }
    }

    // click to increase number of fingers on left hand
    public void okButtonPressed(View v) {
        // will only be enabled if changeRatioMode is on
        // check for validity

        // player 1's turn
        if (turn == 1)  {
            // if left equals left, right will be the same as well
            // if left equals right, right will be the old left
            // these are invalid cases
            if (tempLeft == oneLeft || tempLeft == oneRight) {
                // invalid selection, return to the beginning
                messageBox.setText(BAD_REQUEST + NEW_TURN);
                selectedHand = 0;
                setBoldText(1, false);
                setBoldText(2, false);
                updateViewEnabled(true, true, false, false);
            } else {
                // valid selection
                oneLeft = tempLeft;
                oneRight = tempRight;
                okButton.setEnabled(false);
                switchTurn();
            }
            updateHand(1);
            updateHand(2);
        }
        // player 2's turn
        else {
            // same logic as above
            if (tempLeft == twoLeft || tempLeft == twoRight) {
                // invalid selection, return to the beginning
                messageBox.setText(BAD_REQUEST + NEW_TURN);
                selectedHand = 0;
                setBoldText(3, false);
                setBoldText(4, false);
                updateViewEnabled(false, false, true, true);
            } else {
                // valid selection
                twoLeft = tempLeft;
                twoRight = tempRight;
                okButton.setEnabled(false);
                switchTurn();
            }
            updateHand(3);
            updateHand(4);
        }
        changeRatioMode = false;
    }
}
