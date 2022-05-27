import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private final String rootPrefix;
    private final String dependencyPrefix;
    private final String fileExt;
    private List<FileProxy> filesList;

    public FileUtils(String rootPrefix, String dependencyPrefix, String fileExt) {
        this.rootPrefix = rootPrefix;
        this.dependencyPrefix = dependencyPrefix;
        this.fileExt = fileExt;
        this.filesList = new ArrayList<>();
    }

    public List<File> getSortedFiles() {
        scanFolder();
        return sortFiles();
    }

    public List<FileProxy> scanFolder() {
        try (Stream<Path> walk = Files.walk(Paths.get(rootPrefix))) {
            filesList = walk.filter(Files::isRegularFile)
                    .map(x -> new FileProxy(new File(String.valueOf(x)), new ArrayList<>()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseDependencies();
        return filesList;
    }

    public File mergeFilesContent(String filename) {
        List<File> files = getSortedFiles();
        File global = new File(rootPrefix + filename + fileExt);
        try (PrintWriter writer = new PrintWriter(global)) {
            for (File source : files) {
                writer.write(">>>" + source.getPath() + "<<<\n\n");
                try (Scanner scanner = new Scanner(source)) {
                    String line;
                    while (scanner.hasNext()) {
                        line = scanner.nextLine();
                        writer.write(line + "\n");
                    }
                }
                writer.write("\n\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return global;
    }

    private void checkCyclicDependencies() {
        for (FileProxy fileProxy : filesList) {
            File primary = fileProxy.getFile();
            for (FileProxy temp : filesList) {
                File dependency = temp.getFile();
                if (fileProxy.getDependencies().contains(dependency) && temp.getDependencies().contains(primary)) {
                    String error = "Cyclically dependent files are found:\n" +
                            fileProxy + "\nand\n" +
                            temp;
                    throw new RuntimeException(error);
                }
            }
        }
    }

    public List<File> sortFiles() {
        List<File> sorted = new ArrayList<>();
        for (FileProxy fileProxy : filesList) {
            File fileMain = fileProxy.getFile();
            List<File> dependencies = fileProxy.getDependencies();
            if (!dependencies.isEmpty()) {
                for (File file : dependencies) {
                    if (!sorted.contains(file)) {
                        sorted.add(file);
                    }
                }
            }

            if (!sorted.contains(fileMain)) {
                sorted.add(fileMain);
            }
        }
        return sorted;
    }

    private Optional<File> findByPath(String path) {
        for (FileProxy fileProxy : filesList) {
            File file = fileProxy.getFile();
            if (file.getPath().equals(path)) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

    private void parseDependencies() {
        for (FileProxy fileProxy : filesList) {
            try (Scanner scanner = new Scanner(fileProxy.getFile())) {
                String line;
                while (scanner.hasNext()) {
                    line = scanner.nextLine();
                    if (line.startsWith(dependencyPrefix)) {
                        String dependency = line
                                .replace(dependencyPrefix, "")
                                .replaceAll("[’'‘]", "")
                                .replace("/", "\\")
                                .trim();
                        dependency += fileExt;
                        String dependencyPath = rootPrefix + dependency;
                        fileProxy.addDependency(findByPath(dependencyPath)
                                .orElseThrow(() -> new FileNotFoundException("Can't find the file in this path")));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        checkCyclicDependencies();
    }

    class FileProxy {
        private final File file;
        private final List<File> dependencies;

        public FileProxy(File file, List<File> dependencies) {
            this.file = file;
            this.dependencies = dependencies;
        }

        public File getFile() {
            return file;
        }

        public List<File> getDependencies() {
            return dependencies;
        }

        public void addDependency(File file) {
            dependencies.add(file);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("file=")
                    .append(file.getPath())
                    .append(" [dependencies: ");

            if (!dependencies.isEmpty()) {
                for (File file : dependencies) {
                    sb.append(file.getPath().replace(rootPrefix, ""))
                            .append("; ");
                }
                sb.replace(sb.lastIndexOf("; "), sb.length(), "");
            }

            sb.append("]");
            return sb.toString();
        }
    }
}
