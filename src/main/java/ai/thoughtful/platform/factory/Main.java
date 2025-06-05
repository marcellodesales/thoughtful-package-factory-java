package ai.thoughtful.platform.factory;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program starting...");

        try {
            System.out.println("About to call StackType.sort()");
            String stdPkg = StackType.sort(1, 3, 5, 55.5);
            System.out.println("Method returned: '" + stdPkg + "'");
            System.out.println("Result is null? " + (stdPkg == null));
            System.out.println("Result is empty? " + (stdPkg != null && stdPkg.isEmpty()));

        } catch (Exception e) {
            System.err.println("Exception caught: " + e.getClass().getSimpleName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Program finished.");
    }
}