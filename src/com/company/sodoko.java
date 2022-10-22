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
        fill(0,0);
        print(normal);
        //print(special);
    }
    //TODO atomic int
    private void reference()
    {
        for(int i =0; i < special.length; i++)
        {
            for(int j = 0; j < special[i].length; j++)
            {
                special[i][j] = normal[j/3 + i/3*3][j%3 + i%3*3];
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
    public boolean fill(int i, int j)
    {
//        for(int i =0; i < normal.length;i++)
//        {
//            for(int j = 0; j < normal.length;j++)
//            {
//                HashSet choices = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
//                //HashSet row = new HashSet(Arrays.asList((normal[i])));
//                //HashSet spe = new HashSet(Arrays.asList(special[i]));
//                HashSet temp = new HashSet();
//                for(AtomicInteger num: normal[i])
//                    temp.add(num.get());
//                choices.removeAll(temp);
//                temp.clear();
//                for(AtomicInteger num: special[j/3 + (i/3)*3])
//                    temp.add(num.get());
//                choices.removeAll(temp);
//                temp.clear();
//                for(int k = 0; k < normal.length;k++)
//                    temp.add(normal[k][j].get());
//                choices.removeAll(temp);
//                int rand = (int) (Math.random() * choices.size());
//                //TODO iterator rand
//                normal[i][j].set((Integer)(choices.toArray()[rand]));
//                System.out.println("\033[2J");
//                print(normal);
//                }
//
//            }
        //TODO BACKTRACKING
        HashSet choices = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
        HashSet temp = new HashSet();
        for(AtomicInteger num: normal[i])
            temp.add(num.get());
        choices.removeAll(temp);
        temp.clear();
        for(AtomicInteger num: special[j/3 + (i/3)*3])
            temp.add(num.get());
        choices.removeAll(temp);
        temp.clear();
        for(int k = 0; k < normal.length;k++)
            temp.add(normal[k][j].get());
        choices.removeAll(temp);
        while(choices.size() != 0)
        {
            int rand = (int) (Math.random() * choices.size());
            Integer num = (Integer)(choices.toArray()[rand]);
            normal[i][j].set(num);
            choices.remove(num);
            if((i == 8 && j == 8) || fill(i+(j+1)/9, (j+1)%9))
                return true;
        }
        normal[i][j].set(0);
        return false;
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
