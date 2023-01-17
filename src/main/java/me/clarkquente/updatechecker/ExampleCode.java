package me.clarkquente.updatechecker;

public class ExampleCode {

    public static void main(String[] args) {
        final String path = System.getProperty("user.dir") + "\\builds";

        final UpdateChecker updateChecker = new UpdateChecker(path, "1.0.0", "ClarkQuente", "Update-Checker", true);
        updateChecker.checkVersion();
    }
}
