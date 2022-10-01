package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Bot implements java.util.function.Supplier<Map<String, String>>{
    static class Cell{
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int[][] getStringTo2d(String s) {
        int[][] g = new int[3][3];
        for (int i = 0, k = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++, k++) {
                if (s.charAt(k) == '0') g[i][j] = 0;
                else g[i][j] = (s.charAt(k) - '0') * 2 - 3;
            }
        }
        return g;
    }

    private Map<String, String> nextMove(String input) {
        String[] strs = input.split("#");
        Map<String, String> res = new HashMap<>();
        int[][] g = getStringTo2d(strs[0]);
        String flag = strs[1];
        int t = "true".equals(flag) ? -1 : 1;
        int bestVal = Integer.MIN_VALUE, r = -1, c = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (g[i][j] == 0) {
                    g[i][j] = t;
                    int val = miniMax(g, 0, t, false);
                    g[i][j] = 0;
                    if (val > bestVal){
                        bestVal = val;
                        r = i;
                        c = j;
                    }
                }
            }
        }
        res.put("nextr", Integer.valueOf(r).toString());
        res.put("nextc", Integer.valueOf(c).toString());
        return res;
    }

    private int check_isFinshed(int[][] g, int flag) {
        int winVal = flag == -1 ? -3 : 3;
        boolean draw = true;
        for (int i = 0; i < 3; i++) {
            int s1 = 0, s2 = 0;
            for (int j = 0; j < 3; j++) {
                if (g[i][j] == 0) draw = false;
                s1 += g[i][j];
                s2 += g[j][i];
            }
            if (s1 == winVal || s2 == winVal) return 1;
            else if (s1 == -winVal || s2 == -winVal) return -1;
        }
        int s3 = 0, s4 = 0;
        for (int i = 0; i < 3; i++) {
            s3 += g[i][i];
            s4 += g[i][2 - i];
        }
        if (s3 == winVal || s4 == winVal) return 1;
        else if (s3 == -winVal || s4 == -winVal) return -1;
        if (draw) return 0;
        return 10;
    }

    private int miniMax(int[][] g, int depth, int flag, boolean isMe) {
        int k = check_isFinshed(g, flag);
        if (k != 10) {
            return k;
        }
        if (!isMe) {
            int worseVal = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (g[i][j] == 0) {
                        g[i][j] = -flag;
                        worseVal = Math.min(miniMax(g, depth + 1, flag, true), worseVal);
                        g[i][j] = 0;
                    }
                }
            }
            return worseVal;
        } else {
            int bestVal = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (g[i][j] == 0) {
                        g[i][j] = flag;
                        bestVal = Math.max(miniMax(g, depth + 1, flag, false), bestVal);
                        g[i][j] = 0;
                    }
                }
            }
            return bestVal;
        }

    }

    @Override
    public Map<String, String> get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
