package com.company;

import java.util.*;
import java.util.concurrent.atomic.*;

public class sodoko {

    private AtomicInteger[][] normal;
    private AtomicInteger[][] special;

    public sodoko()
    {
        normal = new AtomicInteger[9][9];
        special = new AtomicInteger[9][9];
        for(int i =0; i < normal.length; i++)
            for(int j =0; j<normal[i].length;j++)
                normal[i][j] = new AtomicInteger();
        reference();
        normal[5][5].set(9);
        fill();
        print(normal);
        print(special);
    }
    //TODO atomic int
    private void reference()
    {
        for(int i =0; i < special.length; i++)
        {
            for(int j = 0; j < special[i].length; j++)
            {
                special[i][j] = normal[j/3 + i/3][j%3 + i%3*3];
            }
        }
    }

    public void print(AtomicInteger[][] mat) {
        for (AtomicInteger[] row : mat) {
            for (AtomicInteger num : row)
                System.out.print("" + num + " ");
            System.out.println();
        }
    }
    //TODO maybe use intersection and union for speed
    public void fill()
    {
        HashSet choices = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
        for(int i =0; i < normal.length;i++)
        {
            for(int j = 0; j < normal.length;j++)
            {
                //HashSet row = new HashSet(Arrays.asList((normal[i])));
                //HashSet spe = new HashSet(Arrays.asList(special[i]));
                HashSet col = new HashSet();
                for(int k = 0; i < normal.length;k++)
                    col.add(normal[k][j]);
                choices.removeAll(Arrays.asList((normal[i]));
                choices.removeAll(Arrays.asList(special[i]));
                choices.removeAll(col);
                int rand = (int) (Math.random() * choices.size());
                //TODO iterator rand
                choices = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
                }

            }
        }
    }

    private boolean checkRow(int num, int col)
    {
        for(int i = 0; i < normal.length;i++)
            if(normal[i][col].get() == num)
                return false;
        return true;
    }

    private boolean checkCol(int num, int row)
    {
        for(int i = 0; i < normal.length;i++)
            if(normal[row][i].get() == num)
                return false;
        return true;
    }

    private boolean checkSpe(int num, int col)
    {
        for(int i = 0; i < normal.length;i++)
            if(special[i][col].get() == num)
                return false;
        return true;
    }
}
