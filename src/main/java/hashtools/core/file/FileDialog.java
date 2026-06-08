package hashtools.core.file;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class FileDialog {

    private String title;
    private FileExtension extension;



    public FileDialog() {
        this.title = "";
        this.extension = FileExtension.ALL;
    }



    public FileDialog withTitle(String title) {
        this.title = Objects.requireNonNullElse(title, "");
        return this;
    }

    public FileDialog withDefaultExtension(FileExtension extension) {
        this.extension = Objects.requireNonNullElse(extension, FileExtension.ALL);
        return this;
    }



    public EnhancedFile openForReading() {
        return this.openForReading(null);
    }

    public EnhancedFile openForReading(Window owner) {
        return this.openDialog(
            this.createDialog()::showOpenDialog,
            owner
        );
    }

    public EnhancedFile openForWriting() {
        return this.openForWriting(null);
    }

    public EnhancedFile openForWriting(Window owner) {
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
            .setAll(FileExtension.getAll(extension));

        return chooser;
    }

    private EnhancedFile openDialog(Function<Window, File> openFunction, Window owner) {
        AtomicReference<File> fileReference = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        Runnable selectFile = () -> {
            File file = openFunction.apply(owner);
            fileReference.set(file);
            latch.countDown();
        };



        if (Platform.isFxApplicationThread()) {
            selectFile.run();
        } else {
            Platform.runLater(selectFile);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return EnhancedFile.create(fileReference);
    }
}
