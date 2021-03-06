package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.FileManager;
import model.FileManagerInterface;


/**
 * Created by SH on 2016-05-08.
 */
public class TabPaneSceneController {

    @FXML private TabPane tabPane;
    @FXML private BorderPane leftEditor, rightEditor;

    private Label leftPathLabel, rightPathLabel;
    private Label leftIsEditedLabel, rightIsEditedLabel;

    //    * 추후 확장 예정 *
//    private Tab currentTab;
//    private List<Tab> originalTabs;
//    private Map<Integer, Tab> tapTransferMap;
//    private String[] stylesheets;
//
//    private boolean isAlwaysOnTop = true;


//    public TabPaneSceneController(){
//        originalTabs = new ArrayList<>();
//        stylesheets = new String[]{};
//        tapTransferMap = new HashMap<>();
//    }

    @FXML // FXML 로딩이 완료되면 호출되는 콜백함수
    public void initialize(){
        Platform.runLater(()->{
            _initLabelReference();
            _initIsEditedSignLabelReference();
            _syncIsEditedSignWithBooleanProperty();
            _syncLabelTextWithPath();
            _syncListScrollWithCompareProperty();
            _syncEditorsWithListProperty();
            _syncEditorsScrollBar(true);
        });
    }

    // * 추후 확장 예정*
    //    // getter & setter
//    public void setStylesheets(String... stylesheets) {
//        this.stylesheets = stylesheets;
//    }

//    @FXML // 탭을 드래그하기 시작하면 수행되는 액션
//    private void onTabPaneDragDetected(MouseEvent event){
//
//        if (event.getSource() instanceof TabPane) {
//            Pane rootPane = (Pane) tabPane.getScene().getRoot();
//
//            rootPane.setOnDragOver((DragEvent event1) -> {
//                event1.acceptTransferModes(TransferMode.ANY);
//                event1.consume();
//            });
//
//            currentTab = tabPane.getSelectionModel().getSelectedItem();
//            SnapshotParameters snapshotParams = new SnapshotParameters();
//            snapshotParams.setTransform(Transform.scale(0.4, 0.4));
//
//            WritableImage snapshot = currentTab.getContent().snapshot(snapshotParams, null);
//            Dragboard db = tabPane.startDragAndDrop(TransferMode.MOVE);
//
//            ClipboardContent clipboardContent = new ClipboardContent();
//            clipboardContent.put(DataFormat.PLAIN_TEXT, "소공 2팀");
//
//            db.setDragView(snapshot, 40, 40);
//            db.setContent(clipboardContent);
//        }
//
//        event.consume();
//    }
//
//    @FXML // 탭을 드래그완료 했을때 수행되는 액션
//    private void onTabPaneDragDone(DragEvent event){
//        _openTabInStage(currentTab);
//        tabPane.setCursor(Cursor.DEFAULT);
//        event.consume();
//    }


//    private void _init(){
//        originalTabs.addAll(tabPane.getTabs());
//
//        for (int i = 0; i < tabPane.getTabs().size(); i++) {
//            tapTransferMap.put(i, tabPane.getTabs().get(i));
//        }
//
//        tabPane.getTabs().stream().forEach(t -> {
//            t.setClosable(false);
//        });
//    }

//    private void _openTabInStage(final Tab tab) {
//        if(tab == null) return;
//
//        int originalTab = originalTabs.indexOf(tab);
//        tapTransferMap.remove(originalTab);
//        Pane content = (Pane) tab.getContent();
//        if (content == null) {
//            throw new IllegalArgumentException("Can not detach Tab '" + tab.getText() + "': content is empty (null).");
//        }
//        tab.setContent(null);
//        final Scene scene = new Scene(content, content.getPrefWidth(), content.getPrefHeight());
//        scene.getStylesheets().addAll(stylesheets);
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.setTitle(tab.getText());
//        stage.setAlwaysOnTop(isAlwaysOnTop);
//
//        Point2D p = MouseRobot.getMousePosition();
//
//        stage.setX(p.getX());
//        stage.setY(p.getY());
//        stage.setOnCloseRequest((WindowEvent t) -> {
//            stage.close();
//            tab.setContent(content);
//            int originalTabIndex = originalTabs.indexOf(tab);
//            tapTransferMap.put(originalTabIndex, tab);
//            int index = 0;
//            SortedSet<Integer> keys = new TreeSet<>(tapTransferMap.keySet());
//            for (Integer key : keys) {
//                Tab value = tapTransferMap.get(key);
//                if(!tabPane.getTabs().contains(value)){
//                    tabPane.getTabs().add(index, value);
//                }
//                index++;
//            }
//            tabPane.getSelectionModel().select(tab);
//        });
//
//        stage.setOnShown((WindowEvent t) -> tab.getTabPane().getTabs().remove(tab));
//
//        stage.show();
//    }

    private void _initLabelReference(){
        this.leftPathLabel = (Label)leftEditor.lookup("#filePath");
        this.rightPathLabel = (Label)rightEditor.lookup("#filePath");
    }

    private void _initIsEditedSignLabelReference(){
        this.leftIsEditedLabel = (Label)leftEditor.lookup("#isEditedSign");
        this.rightIsEditedLabel = (Label)rightEditor.lookup("#isEditedSign");
    }

    private void _syncIsEditedSignWithBooleanProperty(){
        FileManager.getFileManagerInterface().isEditedProperty(FileManagerInterface.SideOfEditor.Left).addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.leftIsEditedLabel.setText("*");
            } else {
                this.leftIsEditedLabel.setText("");
            }
        });

        FileManager.getFileManagerInterface().isEditedProperty(FileManagerInterface.SideOfEditor.Right).addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.rightIsEditedLabel.setText("*");
            } else {
                this.rightIsEditedLabel.setText("");
            }
        });
    }

    private void _syncLabelTextWithPath(){
        this.leftPathLabel.textProperty().bind(FileManager.getFileManagerInterface().filePathProperty(FileManagerInterface.SideOfEditor.Left));
        this.rightPathLabel.textProperty().bind(FileManager.getFileManagerInterface().filePathProperty(FileManagerInterface.SideOfEditor.Right));
    }

    private void _syncEditorsWithListProperty(){
        HighlightEditorInterface leftEditor = (HighlightEditorInterface)this.leftEditor.lookup("#editor");
        HighlightEditorInterface rightEditor = (HighlightEditorInterface)this.rightEditor.lookup("#editor");

        leftEditor.getHighlightListView().itemsProperty().bind(FileManager.getFileManagerInterface().listProperty(FileManagerInterface.SideOfEditor.Left));
        rightEditor.getHighlightListView().itemsProperty().bind(FileManager.getFileManagerInterface().listProperty(FileManagerInterface.SideOfEditor.Right));
    }

    private void _syncListScrollWithCompareProperty(){

        FileManager.getFileManagerInterface().isCompareProperty().addListener((observable, oldValue, newValue) -> {

            HighlightEditorInterface leftEditor = (HighlightEditorInterface) this.leftEditor.lookup("#editor");
            HighlightEditorInterface rightEditor = (HighlightEditorInterface) this.rightEditor.lookup("#editor");

            ScrollBar leftScroll = (ScrollBar) leftEditor.getHighlightListView().lookup(".scroll-bar:vertical");
            ScrollBar rightScroll = (ScrollBar) rightEditor.getHighlightListView().lookup(".scroll-bar:vertical");

            if( newValue ) {
                leftScroll.valueProperty().bindBidirectional(rightScroll.valueProperty());
                _syncEditorsScrollBar(false);
            } else {
                leftScroll.valueProperty().unbindBidirectional(rightScroll.valueProperty());
                _syncEditorsScrollBar(true);
            }

        });

    }

    private void _syncEditorsScrollBar(boolean b){

        HighlightEditorInterface leftEditor = (HighlightEditorInterface)this.leftEditor.lookup("#editor");
        HighlightEditorInterface rightEditor = (HighlightEditorInterface)this.rightEditor.lookup("#editor");

        TextArea leftTextArea = leftEditor.getTextArea();
        TextArea rightTextArea = rightEditor.getTextArea();

        ListView leftListView = leftEditor.getHighlightListView();
        ListView rightListView = rightEditor.getHighlightListView();

        // textarea scrolling
        ScrollBar leftVerticalScroll = (ScrollBar) leftTextArea.lookup(".scroll-bar:vertical");
        ScrollBar rightVerticalScroll = (ScrollBar) rightTextArea.lookup(".scroll-bar:vertical");

        // listview scroll property
        ScrollBar listLeftVerticalScroll = (ScrollBar) leftListView.lookup(".scroll-bar:vertical");
        ScrollBar listRightVerticalScroll = (ScrollBar) rightListView.lookup(".scroll-bar:vertical");

        if(b) {
            leftVerticalScroll.valueProperty().bindBidirectional(listLeftVerticalScroll.valueProperty());
            rightVerticalScroll.valueProperty().bindBidirectional(listRightVerticalScroll.valueProperty());
        } else {
            leftVerticalScroll.valueProperty().unbindBidirectional(listLeftVerticalScroll.valueProperty());
            rightVerticalScroll.valueProperty().unbindBidirectional(listRightVerticalScroll.valueProperty());
        }

    }

}