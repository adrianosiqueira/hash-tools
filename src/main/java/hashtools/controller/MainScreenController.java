package hashtools.controller;

import hashtools.condition.KeyboardKeyIsActionKey;
import hashtools.condition.KeyboardKeyIsHomeKey;
import hashtools.condition.MouseButtonIsPrimary;
import hashtools.domain.Operation;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController extends Controller {

    @FXML
    private Pane
        pnlMainMenu,
        pnlChecker,
        pnlComparator,
        pnlGenerator;


    private void bindPnlRootVisibility() {
        BooleanBinding shouldBeVisible = pnlChecker
            .visibleProperty().not()
            .and(pnlComparator.visibleProperty().not())
            .and(pnlGenerator.visibleProperty().not());

        pnlMainMenu
            .visibleProperty()
            .bind(shouldBeVisible);
    }

    @FXML
    private void closeScreen(KeyEvent event) {
        // TODO Remove this method when all the three run screens are done
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsHomeKey(event),
            new OpenMainMenu()
        ));
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        bindPnlRootVisibility();
    }


    @FXML
    private void pnlCheckerKeyPressed(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlCheckerKeyReleased(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new DisarmNode((Node) event.getSource())
        ));

        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new OpenCheckerScreen()
        ));
    }

    @FXML
    private void pnlCheckerMouseClicked(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new OpenCheckerScreen()
        ));
    }

    @FXML
    private void pnlCheckerMousePressed(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlCheckerMouseReleased(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new DisarmNode((Node) event.getSource())
        ));
    }


    @FXML
    private void pnlComparatorKeyPressed(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlComparatorKeyReleased(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new DisarmNode((Node) event.getSource())
        ));

        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new OpenComparatorScreen()
        ));
    }

    @FXML
    private void pnlComparatorMouseClicked(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new OpenComparatorScreen()
        ));
    }

    @FXML
    private void pnlComparatorMousePressed(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlComparatorMouseReleased(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new DisarmNode((Node) event.getSource())
        ));
    }


    @FXML
    private void pnlGeneratorKeyPressed(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlGeneratorKeyReleased(KeyEvent event) {
        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new DisarmNode((Node) event.getSource())
        ));

        threadPool.execute(() -> performOperation(
            new KeyboardKeyIsActionKey(event),
            new OpenGeneratorScreen()
        ));
    }

    @FXML
    private void pnlGeneratorMouseClicked(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new OpenGeneratorScreen()
        ));
    }

    @FXML
    private void pnlGeneratorMousePressed(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new ArmNode((Node) event.getSource())
        ));
    }

    @FXML
    private void pnlGeneratorMouseReleased(MouseEvent event) {
        threadPool.execute(() -> performOperation(
            new MouseButtonIsPrimary(event),
            new DisarmNode((Node) event.getSource())
        ));
    }


    private final class OpenCheckerScreen implements Operation {
        @Override
        public void perform() {
            System.out.println(getClass().getSimpleName());
            pnlChecker.setVisible(true);
        }
    }

    private final class OpenComparatorScreen implements Operation {
        @Override
        public void perform() {
            System.out.println(getClass().getSimpleName());
            pnlComparator.setVisible(true);
        }
    }

    private final class OpenGeneratorScreen implements Operation {
        @Override
        public void perform() {
            System.out.println(getClass().getSimpleName());
            pnlGenerator.setVisible(true);
        }
    }

    private final class OpenMainMenu implements Operation {
        @Override
        public void perform() {
            // TODO Remove this class when all the three run screens are done.
            pnlChecker.setVisible(false);
            pnlComparator.setVisible(false);
            pnlGenerator.setVisible(false);
        }
    }
}