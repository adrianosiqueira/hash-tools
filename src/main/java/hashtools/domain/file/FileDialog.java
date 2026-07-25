package hashtools.domain.file;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FileDialog {

    private String title;
    private FileExtension defaultExtension;



    public FileDialog() {
        this.title = "";
        this.defaultExtension = FileExtension.ALL;
    }



    public FileDialog withTitle(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }

    public FileDialog withDefaultExtension(FileExtension extension) {
        Objects.requireNonNull(extension);
        this.defaultExtension = extension;
        return this;
    }



    public Optional<EnhancedFile> openForReading() {
        return this.openForReading(null);
    }

    public Optional<EnhancedFile> openForReading(Window owner) {
        return this.openDialog(
            this.createDialog()::showOpenDialog,
            owner
        );
    }

    public Optional<EnhancedFile> openForWriting() {
        return this.openForWriting(null);
    }

    public Optional<EnhancedFile> openForWriting(Window owner) {
        return this.openDialog(
            this.createDialog()::showSaveDialog,
            owner
        );
    }



    private FileChooser createDialog() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));

        chooser
            .getExtensionFilters()
            .setAll(FileExtension.getAll(defaultExtension));

        return chooser;
    }

    private Optional<EnhancedFile> openDialog(Function<Window, File> openFunction, Window owner) {
        CompletableFuture<File> futureFile = new CompletableFuture<>();

        Runnable selectFile = () -> {
            File file = openFunction.apply(owner);
            futureFile.complete(file);
        };



        if (Platform.isFxApplicationThread()) {
            selectFile.run();
        } else {
            Platform.runLater(selectFile);
        }



        try {
            File file = futureFile.get();

            return Optional
                .ofNullable(file)
                .map(EnhancedFile::file);
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
