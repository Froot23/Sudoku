package com.company;

import java.util.*;
import java.util.concurrent.atomic.*;

public class sodoko {

    private AtomicInteger[][] normal;
    private AtomicInteger[][] special;
    private boolean[][] addable;
    private int x;
    private int y;

    public sodoko(int diff)
    {
        x = 0;
        y = 0;
        addable = new boolean[9][9];
        normal = new AtomicInteger[9][9];
        special = new AtomicInteger[9][9];
        for(int i =0; i < normal.length; i++)
            for(int j =0; j<normal[i].length;j++)
                normal[i][j] = new AtomicInteger();
        reference();
        fill(0,0);
        print(normal);
        System.out.println();
        playBoard(diff);
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
        for (int i =0; i< normal.length; i++) {
            if(i%3 == 0 && i != 0)
                System.out.println("------------------------");
            for (int j =0; j< normal[i].length; j++) {
                if(j%3 ==0 && j !=0)
                    System.out.print(" | ");
                if(i == x && j == y)
                    System.out.print("\033[1;101m");

                if(addable[i][j]) {
                    System.out.print("\033[1;96m");
                    System.out.print(normal[i][j].get() == 0? "_" : "" + normal[i][j]);
                }
                else
                    System.out.print("" + normal[i][j]);
                System.out.print("\033[m" + " ");
            }
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

        public void playBoard(int diff)
        {
            for(int i = 0; i < diff; i++)
            {
                ArrayList<Integer> choice = choices();
                int rand = (int) (Math.random()*choice.size());
                int random = (int)(Math.random()*9);
                while(special[choice.get(rand)][random].get() == 0)
                    random = (int) (Math.random() * 9);
                special[choice.get(rand)][random].set(0);
            }
            for(int i =0; i < normal.length; i++)
            {
                for(int j =0; j < normal[i].length; j++)
                {
                    if(normal[i][j].get() == 0)
                        addable[i][j] = true;
                }
            }
        }

        private int nonZero(AtomicInteger[] list)
        {
            int count = 0;
            for(AtomicInteger num : list)
                if(num.get() != 0)
                    count++;
            return count;
        }

        private ArrayList<Integer> choices()
        {
            ArrayList<Integer> choices = new ArrayList<Integer>();
            int max = 0;
            for(int i =0; i < special.length; i++)
            {
                if(nonZero(special[i]) > max)
                    max = nonZero(special[i]);
            }
            for(int i =0; i < special.length; i++)
            {
                if(max - nonZero(special[i]) <= 2)
                    choices.add(i);
            }
            return choices;
        }

        public void play()
        {

            Scanner tanner = new Scanner(System.in);
            boolean run = true;
            while(run) {
                try {
                    String temp = tanner.nextLine();
                    if (temp.equals("w")) {
                        if (x == 0)
                            x= 8;
                        else
                            x--;
                    } else if (temp.equals("a")) {
                        if (y == 0)
                            y = 8;
                        else
                            y--;
                    } else if (temp.equals("s")) {
                        if (x == 8)
                            x = 0;
                        else
                            x++;
                    } else if (temp.equals("d")) {
                        if (y == 8)
                            y = 0;
                        else
                            y++;
                    } else if (Integer.parseInt(temp) >= 1 && Integer.parseInt(temp) <= 9) {
                        if(addable[x][y]) {
                            normal[x][y].set(Integer.parseInt(temp));
                        }
                        else
                            System.out.println("Already number here");
                        boolean good = true;
                        for(int i =0; i<normal.length; i++)
                            for(int j =0; j< normal[i].length; j++)
                                if (normal[i][j].get() == 0 && good)
                                    good = false;
                        if(good)
                         if(check()) {
                             System.out.println("POGGGGGG");
                             run = false;
                         }
                         else
                             System.out.println("BRUHHHHH");
                    } else if (temp.equals("q"))
                    {
                        run = false;
                    }
                    print(normal);
                } catch (Exception e) {
                    System.out.println("Wrong number or character");
                }
            }
        }

        public boolean check()
        {
            for(int i =0; i < normal.length; i++)
            {
                HashSet row = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
                HashSet box = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
                HashSet col = new HashSet(Arrays.asList(1,2,3,4,5,6,7,8,9));
                 for(int j =0; j< normal[i].length; j++)
                 {
                     row.remove(normal[i][j].get());
                     box.remove(special[i][j].get());
                     col.remove(normal[j][i].get());
                 }
             if(row.size() != 0 || box.size() != 0 || col.size() != 0)
                 return false;
            }
            return true;
        }
}
// \033[1;101m
//\033[1;96m