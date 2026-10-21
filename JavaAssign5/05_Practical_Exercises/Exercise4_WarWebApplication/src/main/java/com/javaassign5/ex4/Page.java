package com.javaassign5.ex4;

/** Shared HTML layout for the servlet pages. */
final class Page {

    private Page() { }

    static String wrap(String title, String body) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>" + title + " - Student Portal</title>"
                + "<link rel='stylesheet' href='css/style.css'></head>"
                + "<body><main>" + body + "</main></body></html>";
    }
}
