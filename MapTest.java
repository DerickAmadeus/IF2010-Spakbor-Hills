public class MapTest {
    public static void main(String[] args) {
        java.io.File folder = new java.io.File("Map");
        java.io.FilenameFilter filter = (dir, name) -> name.endsWith(".java");
        java.io.File[] files = folder.listFiles(filter);

        if (files != null) {
            for (java.io.File file : files) {
                System.out.println("Found file: " + file.getName());
            }
        } else {
            System.out.println("No files found in the folder or folder does not exist.");
        }

        for (java.io.File file : files) {
            try {
            System.out.println("Compiling: " + file.getName());
            Process process = Runtime.getRuntime().exec("javac " + file.getPath());
            process.waitFor();

            if (process.exitValue() == 0) {
                System.out.println("Successfully compiled: " + file.getName());
            } else {
                System.out.println("Failed to compile: " + file.getName());
            }
            } catch (Exception e) {
            System.out.println("Error while compiling " + file.getName() + ": " + e.getMessage());
            }
        }
        
    }
}
