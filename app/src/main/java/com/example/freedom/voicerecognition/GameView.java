package com.example.freedom.voicerecognition;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGameView();
        addCards(card_width, card_width);
    }

    public GameView(Context context) {
        super(context);
        initGameView();
        addCards(card_width, card_width);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
        addCards(card_width, card_width);
    }

    private void initGameView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new View.OnTouchListener(){

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)) {
                            if(offsetX < -5) {
                                swipeLeft();
                            } else if(offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if(offsetY < -5) {
                                swipeUp();
                            } else if(offsetY > 5) {
                                swipeDown();
                            }
                        }

                        break;
                }

                return true;
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {

        Card c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame() {

        MainActivity.getMainActivity().clearScore();

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {

        emptyPoints.clear();

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    public void swipeLeft() {

        boolean move = false;

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {

                for(int x1 = x+1; x1 < 4; x1++) {
                    if(cardsMap[x1][y].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }
    public void swipeRight() {

        boolean move = false;

        for(int y = 0; y < 4; y++) {
            for(int x = 3; x >= 0; x--) {

                for(int x1 = x-1; x1 >= 0; x1--) {
                    if(cardsMap[x1][y].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }
    public void swipeUp() {

        boolean move = false;

        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {

                for(int y1 = y+1; y1 < 4; y1++) {
                    if(cardsMap[x][y1].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }
    public void swipeDown() {

        boolean move = false;

        for(int x = 0; x < 4; x++) {
            for(int y = 3; y >= 0; y--) {

                for(int y1 = y-1; y1 >= 0; y1--) {
                    if(cardsMap[x][y1].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }

    private void checkFinish() {

        boolean finish = true;

        ALL:
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() == 0 ||
                        x>0 && cardsMap[x][y].equals(cardsMap[x-1][y]) ||
                        x<3 && cardsMap[x][y].equals(cardsMap[x+1][y]) ||
                        y>0 && cardsMap[x][y].equals(cardsMap[x][y-1]) ||
                        y<3 && cardsMap[x][y].equals(cardsMap[x][y+1])) {

                    finish = false;
                    break ALL;
                }
            }
        }

        if(finish) {
            new AlertDialog.Builder(getContext())
                    .setTitle("游戏结束！！！")
                    .setMessage("最终得分为 # " + MainActivity.getMainActivity().printScore() + " #")
                    .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }})
                    .setCancelable(false)
                    .show();
        }
    }

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private int card_width = MainActivity.getMainActivity().min_height_weight();
    EditText et_input;
}
