package org.cscb525;

import org.cscb525.configure.SessionFactoryUtil;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();
    }
}