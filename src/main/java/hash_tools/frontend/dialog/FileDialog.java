package hash_tools.frontend.dialog;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileDialog {

    private String title;
    private ResourceBundle resources;
    private FileExtension defaultExtension;
    private Window ownerWindow;



    public FileDialog title(String title) {
        this.title = title;
        return this;
    }

    public FileDialog resources(ResourceBundle resources) {
        this.resources = resources;
        return this;
    }

    public FileDialog defaultExtension(FileExtension defaultExtension) {
        this.defaultExtension = defaultExtension;
        return this;
    }

    public FileDialog ownerWindow(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        return this;
    }



    public Optional<Path> openFile() {
        File file = this
            .createFileChooser()
            .showOpenDialog(ownerWindow);

        return Optional
            .ofNullable(file)
            .map(File::toPath)
            .map(Path::toAbsolutePath);
    }



    private FileChooser createFileChooser() {
        String title = Optional
            .ofNullable(resources)
            .map(r -> r.getString(this.title))
            .orElse(this.title);

        List<FileChooser.ExtensionFilter> filters = FileExtension
            .getAllExtensions()
            .stream()
            .map(this::toTranslatedFilter)
            .collect(Collectors.toCollection(ArrayList::new));

        Optional
            .ofNullable(defaultExtension)
            .map(this::toTranslatedFilter)
            .ifPresent(filter -> {
                Predicate<FileChooser.ExtensionFilter> filtersAreEquals = f -> Comparator
                    .comparing(FileChooser.ExtensionFilter::getDescription)
                    .compare(filter, f) == 0;


                /*
                 * filters.remove() does not work because the filter.equals()
                 * always returns false, so filters.contains() always fails.
                 * Hence the necessity to manually search the extension by its
                 * description.
                 */
                filters.removeIf(filtersAreEquals);
                filters.addFirst(filter);
            });

        File initialDirectory = Optional
            .of("user.home")
            .map(System::getProperty)
            .map(File::new)
            .orElse(null);


        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(initialDirectory);
        chooser.getExtensionFilters().setAll(filters);
        return chooser;
    }



    private FileChooser.ExtensionFilter toTranslatedFilter(FileExtension extension) {
        return Optional
            .ofNullable(resources)
            .map(extension::toFilter)
            .orElseGet(extension::toFilter);
    }
}
