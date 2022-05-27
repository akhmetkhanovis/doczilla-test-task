import java.io.File;
import java.util.List;

public class Demo {
    private static final String ROOT_PREFIX = "task-first\\src\\main\\resources\\Root Folder\\";
    private static final String DEPENDENCY_PREFIX = "require";
    private static final String FILE_EXT = ".txt";

    public static void main(String[] args) {

        FileUtils utils = new FileUtils(ROOT_PREFIX, DEPENDENCY_PREFIX, FILE_EXT);
        List<FileUtils.FileProxy> fileProxies = utils.scanFolder();
        System.out.println("Root Folder content:");
        fileProxies.forEach(System.out::println);

        List<File> files = utils.sortFiles();
        System.out.println("Sorted files according to their dependencies");
        files.forEach(System.out::println);

        String globalFile = "global";
        File merged = utils.mergeFilesContent(globalFile);

        System.out.println("merging all files to " + merged.getPath());


    }
}
