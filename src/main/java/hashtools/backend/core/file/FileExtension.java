package hashtools.backend.core.file;

import javafx.stage.FileChooser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum FileExtension {

    ALL("All", "*"),
    COMPRESSED("Compresseds", "*.7z", "*.ace", "*.alz", "*.arc", "*.arj", "*.bzip2", "*.egg", "*.gz", "*.gzip", "*.rar", "*.tar", "*.zip"),
    DISK_IMAGE("Disc Images", "*.adf", "*.bin", "*.cue", "*.dmg", "*.img", "*.iso", "*.nrg"),
    DOCUMENT("Documents", "*.doc", "*.docx", "*.odp", "*.ods", "*.odt", "*.pdf", "*.ppt", "*.pptx", "*.rtf", "*.txt", "*.xls", "*.xlsx"),
    HASH("Hashes", "*.md5", "*.sha1", "*.sha224", "*.sha256", "*.sha384", "*.sha512", "*.txt"),
    MARKDOWN("Markdown", "*.markdown", "*.MARKDOWN", "*.md", "*.MD"),
    MARKUP("Markup", "*.cfml", "*.gml", "*.htm", "*.html", "*.kml", "*.xaml", "*.xhtml", "*.xml", "*.yaml"),
    MUSIC("Musics", "*.aac", "*.aiff", "*.mp3", "*.oga", "*.ogg", "*.wav", "*.wma"),
    PICTURE("Pictures", "*.gif", "*.jpg", "*.jpeg", "*.png", "*.svg", "*.tiff", "*.webp"),
    RUNNABLE("Runnables", "*.apk", "*.appimage", "*.AppImage", "*.exe", "*.jar", "*.msi", "*.run"),
    SCRIPT("Scripts", "*.bash", "*.bat", "*.dat", "*.js", "*.php", "*.sh", "*.vbs", "*.zsh"),
    SOURCE_CODE("Source Codes", "*.c", "*.cpp", "*.java", "*.pas", "*.py"),
    STYLESHEET("Stylesheet", "*.css", "*.less", "*.sass", "*.scss", "*.styl", "*.xslt"),
    VIDEO("Videos", "*.3gp", "*.avi", "*.flv", "*.mkv", "*.mp4", "*.mpeg", "*.mpg", "*.ogv", "*.rmvb", "*.webm", "*.wmv");



    private final String displayName;
    private final String[] extensions;



    FileExtension(String displayName, String... extensions) {
        this.displayName = displayName;
        this.extensions = extensions;
    }



    public static FileChooser.ExtensionFilter[] getAll(FileExtension defaultExtension) {
        List<FileExtension> extensions = new ArrayList<>(List.of(values()));
        extensions.remove(defaultExtension);
        extensions.addFirst(defaultExtension);

        return extensions
            .stream()
            .map(FileExtension::getFilter)
            .toArray(FileChooser.ExtensionFilter[]::new);
    }



    public boolean containsExtension(String extension) {
        if (extension == null) {
            return false;
        }

        return Stream
            .of(extensions)
            .map(s -> s.substring(2))
            .anyMatch(extension::equalsIgnoreCase);
    }



    private FileChooser.ExtensionFilter getFilter() {
        return new FileChooser.ExtensionFilter(
            displayName,
            extensions
        );
    }
}