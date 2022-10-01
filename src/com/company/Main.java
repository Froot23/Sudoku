package com.company;

import java.util.concurrent.atomic.*;

public class Main {

    public static void main(String[] args) {
	    sodoko thing = new sodoko();
        AtomicInteger jack = new AtomicInteger(1);
        AtomicInteger bob = jack;
        jack.set(4);
        System.out.println(bob);
    }


}
