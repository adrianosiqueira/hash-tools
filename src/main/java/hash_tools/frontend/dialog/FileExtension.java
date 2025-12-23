package hash_tools.frontend.dialog;

import javafx.stage.FileChooser;

import java.util.ResourceBundle;

public record FileExtension(
    String description,
    String... extensions
) {
    public static final FileExtension ALL = new FileExtension("All", "*");
    public static final FileExtension CHECKSUM = new FileExtension("Checksums", "*.md5", "*.sha1", "*.sha224", "*.sha256", "*.sha384", "*.sha512", "*.txt");
    public static final FileExtension COMPRESSED = new FileExtension("Compresseds", "*.7z", "*.ace", "*.alz", "*.arc", "*.arj", "*.bzip2", "*.egg", "*.gz", "*.gzip", "*.rar", "*.tar", "*.zip");
    public static final FileExtension DISK_IMAGE = new FileExtension("Disc Images", "*.adf", "*.bin", "*.cue", "*.dmg", "*.img", "*.iso", "*.nrg");
    public static final FileExtension DOCUMENT = new FileExtension("Documents", "*.doc", "*.docx", "*.odp", "*.ods", "*.odt", "*.pdf", "*.ppt", "*.pptx", "*.rtf", "*.txt", "*.xls", "*.xlsx");
    public static final FileExtension MARKDOWN = new FileExtension("Markdown", "*.markdown", "*.MARKDOWN", "*.md", "*.MD");
    public static final FileExtension MARKUP = new FileExtension("Markup", "*.cfml", "*.gml", "*.htm", "*.html", "*.kml", "*.xaml", "*.xhtml", "*.xml", "*.yaml");
    public static final FileExtension MUSIC = new FileExtension("Musics", "*.aac", "*.aiff", "*.mp3", "*.oga", "*.ogg", "*.wav", "*.wma");
    public static final FileExtension PICTURE = new FileExtension("Pictures", "*.gif", "*.jpg", "*.jpeg", "*.png", "*.svg", "*.tiff", "*.webp");
    public static final FileExtension RUNNABLE = new FileExtension("Runnables", "*.apk", "*.appimage", "*.AppImage", "*.exe", "*.jar", "*.msi", "*.run");
    public static final FileExtension SCRIPT = new FileExtension("Scripts", "*.bash", "*.bat", "*.dat", "*.js", "*.php", "*.sh", "*.vbs", "*.zsh");
    public static final FileExtension SOURCE_CODE = new FileExtension("Source Codes", "*.c", "*.cpp", "*.java", "*.pas", "*.py");
    public static final FileExtension STYLESHEET = new FileExtension("Stylesheet", "*.css", "*.less", "*.sass", "*.scss", "*.styl", "*.xslt");
    public static final FileExtension VIDEO = new FileExtension("Videos", "*.3gp", "*.avi", "*.flv", "*.mkv", "*.mp4", "*.mpeg", "*.mpg", "*.ogv", "*.rmvb", "*.webm", "*.wmv");


    public static final FileExtension ALL = new FileExtension("All", "*.*");
    public static final FileExtension CHECKSUM = new FileExtension("Checksum", "*.*");



    public FileChooser.ExtensionFilter toFilter(ResourceBundle resources) {
        return new FileChooser.ExtensionFilter(
            resources.getString(description),
            extensions
        );
    }

    public FileChooser.ExtensionFilter toFilter() {
        return new FileChooser.ExtensionFilter(
            description,
            extensions
        );
    }
}
