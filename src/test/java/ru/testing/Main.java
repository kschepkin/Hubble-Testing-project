package ru.testing;

import data.Constants;
import helpers.HacHelper;

import static controllers.PropertyLoader.*;

public class Main {

    public static void main (String args []) {
        String name = loadProperty(CURRENT_USER);
        String phone = loadProperty(name + PHONE);
    }
}
