package com.gducpm.phlimbo;

import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MediaPlayer bgm;
    View buttonContainer;

    public int[][] movesets = {
            // Original positions are array indexes

            // Group-of-four diagonal cross
            // Bias: 1
            // 0 4 -> 5 1
            // 1 5    4 0
            // 2 6    7 3
            // 3 7    6 2
            {5, 4, 7, 6, 1, 0, 3, 2},

            // Group-of-two vertical swap
            // Bias: 1
            // 0 4 -> 1 5
            // 1 5    0 4
            // 2 6    3 7
            // 3 7    2 6
            {1, 0, 3, 2, 5, 4, 7, 6},

            // Group-of-four vertical swap
            // Bias: 3
            // 0 4 -> 2 6
            // 1 5    3 7
            // 2 6    0 4
            // 3 7    1 5
            {2, 3, 0, 1, 6, 7, 4, 5},
            {2, 3, 0, 1, 6, 7, 4, 5},
            {2, 3, 0, 1, 6, 7, 4, 5},

            // Group-of-four horizontal swap
            // Bias: 2
            // 0 4 -> 4 0
            // 1 5    5 1
            // 2 6    6 2
            // 3 7    7 3
            {4, 5, 6, 7, 0, 1, 2, 3},
            {4, 5, 6, 7, 0, 1, 2, 3},

            // Clockwise orbit
            // Bias: 3
            // 0 4 -> 1 0
            // 1 5    2 4
            // 2 6    3 5
            // 3 7    7 6
            {1, 2, 3, 7, 0, 4, 5, 6},
            {1, 2, 3, 7, 0, 4, 5, 6},
            {1, 2, 3, 7, 0, 4, 5, 6},

            // Counterclockwise orbit
            // Bias: 3
            // 0 4 -> 4 5
            // 1 5    0 6
            // 2 6    1 7
            // 3 7    2 3
            {4, 0, 1, 2, 5, 6, 7, 3},
            {4, 0, 1, 2, 5, 6, 7, 3},
            {4, 0, 1, 2, 5, 6, 7, 3}
    };

    MaterialButton[] buttons;
    MaterialButton correctButton;

    void initButtons() {
        buttons = new MaterialButton[]{
                findViewById(R.id.btn0),
                findViewById(R.id.btn1),
                findViewById(R.id.btn2),
                findViewById(R.id.btn3),
                findViewById(R.id.btn4),
                findViewById(R.id.btn5),
                findViewById(R.id.btn6),
                findViewById(R.id.btn7)
        };
        int correctIdx = (int) (Math.random() * buttons.length);
        correctButton = buttons[correctIdx];
    }

    void applyMove(int[] move) {
        int n = buttons.length;
        float[] startX = new float[n];
        float[] startY = new float[n];
        for (int i = 0; i < n; i++) {
            startX[i] = buttons[i].getX();
            startY[i] = buttons[i].getY();
        }
        MaterialButton[] newPositions = new MaterialButton[8];
        for (int i = 0; i < n; i++) {
            int target = move[i];
            buttons[i].animate()
                    .x(startX[target])
                    .y(startY[target])
                    .setDuration(260)
                    .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                    .start();
            newPositions[target] = buttons[i];
        }
        buttons = newPositions;
    }

    void chaosLoop(int times, int delayMs, int mode) {
        if (times <= 0 && mode == 1)
            { chaosLoop(8, 300, 2); return; }
        if (times <= 0 && mode == 2) return;
        int r;
        if (mode == 1) {
            r = (int) (Math.random() * movesets.length);
        } else {
            r = 7 + (int) (Math.random() * 6);
        }
        applyMove(movesets[r]);
        buttonContainer.postDelayed(() -> chaosLoop(times - 1, delayMs, mode), delayMs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bgm = MediaPlayer.create(this, R.raw.limbo);
        bgm.setLooping(false);
        bgm.setVolume(1f, 1f);
        initButtons();
        buttonContainer = findViewById(R.id.buttonContainer);
        correctButton.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.limbo_green)
                )
        );
        bgm.start();
        buttonContainer.postDelayed(() -> chaosLoop(16, 300, 1), 4200);
        buttonContainer.postDelayed(() -> correctButton.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.limbo_red)
                )
        ), 3000);
        buttonContainer.postDelayed(() -> {
            buttons[0].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_blue)
                    )
            );
            buttons[1].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_red)
                    )
            );
            buttons[2].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_green)
                    )
            );
            buttons[3].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_yellow)
                    )
            );
            buttons[4].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_lime)
                    )
            );
            buttons[5].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_purple)
                    )
            );
            buttons[6].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_cyan)
                    )
            );
            buttons[7].setBackgroundTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.limbo_pink)
                    )
            );
            for (Button button:buttons) {
                button.setOnClickListener(v -> {
                    if (button == correctButton){
                        Toast.makeText(this, "Good!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    } else {
                        Toast.makeText(this, "You suck", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                });
            }
        }, 14000);

    }
}