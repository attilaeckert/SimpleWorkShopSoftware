package com.simpleworkshopsoftware;
/**
 * Launcher class for the Simple Workshop Software application.
 * This class exists to resolve issues with executable JAR files
 * It serves as a simple entry point that delegates to the original main(App) class.
 *
 * @author Attila Eckert
 * @version 1.0
 * @since 12/27/2024
 */
public class Main {
    public static void main(String[] args) {
        App.main(args);
    }
}
