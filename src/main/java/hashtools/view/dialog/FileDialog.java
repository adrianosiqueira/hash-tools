package hashtools.view.dialog;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class FileDialog {

    private static final String DEFAULT_TITLE = "Select a file";
    private static final String DEFAULT_INITIAL_DIRECTORY = System.getProperty("user.home");



    private String title;
    private String initialDirectory;
    private FileExtension selectedExtension;



    public FileDialog() {
        this.setTitle(null);
        this.setInitialDirectory(null);
        this.setSelectedExtension(null);
    }



    public FileDialog setTitle(String title) {
        this.title = Optional
            .ofNullable(title)
            .orElse(DEFAULT_TITLE);

        return this;
    }

    public FileDialog setInitialDirectory(String initialDirectory) {
        this.initialDirectory = Optional
            .ofNullable(initialDirectory)
            .orElse(DEFAULT_INITIAL_DIRECTORY);

        return this;
    }

    public FileDialog setSelectedExtension(FileExtension selectedExtension) {
        this.selectedExtension = Optional
            .ofNullable(selectedExtension)
            .orElse(FileExtension.ALL);

        return this;
    }



    public Optional<Path> showOpenDialog(Window owner) {
        return this.createAndShowDialog(fileChooser ->
            fileChooser.showOpenDialog(owner)
        );
    }

    public Optional<Path> showSaveDialog(Window owner) {
        return this.createAndShowDialog(fileChooser ->
            fileChooser.showSaveDialog(owner)
        );
    }

    private Optional<Path> createAndShowDialog(Function<FileChooser, File> function) {
        File initialDirectory = this
            .sanitizeInitialDirectory()
            .toFile();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(this.title);
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser
            .getExtensionFilters()
            .setAll(FileExtension.getAll(selectedExtension));



        File file = function.apply(fileChooser);

        return Optional
            .ofNullable(file)
            .map(File::toPath)
            .map(Path::toAbsolutePath);
    }

    private Path sanitizeInitialDirectory() {
        Path providedDirectory = Path.of(initialDirectory);

        return Files.isDirectory(providedDirectory)
            ? providedDirectory
            : Path.of(DEFAULT_INITIAL_DIRECTORY);
    }
}
