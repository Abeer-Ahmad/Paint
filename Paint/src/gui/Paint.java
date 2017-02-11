package gui;
 
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
 
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.MouseInputAdapter;

import dynamicLinkage.Plugin;
import dynamicLinkage.ResourceLoader;
import gui.filters.ExtensionFilter;
import gui.filters.JARFilter;
import parsers.json.JSONReader;
import parsers.json.JSONWriter;
import parsers.xml.XMLReader;
import parsers.xml.XMLWriter;
import shapes.Circle;
import shapes.Line;
import shapes.Oval;
import shapes.Rectangle;
import shapes.Shape;
import shapes.Square;
 
public class Paint {
 
    // Interface
    JFrame frame;
    private PaintArea paintArea;
    private JPanel shapeButtons;
    private JMenuBar menuBar;
    private JToolBar toolBar;
 
    // Program Flow
    private Stack<ArrayList<Shape>> done = new Stack<ArrayList<Shape>>();
    private Stack<ArrayList<Shape>> unDone = new Stack<ArrayList<Shape>>();
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private ArrayList<Shape> tempShape = new ArrayList<Shape>();
    private ArrayList<Point> points = new ArrayList<Point>();
    private int pointsNum = 0; // set by the constructor of the shape
    private int pointsCnt = 0; // number of points entered to check if painting completed
    private int selectedInd = -1; // determines the index of selected shape >> indication to selection
    private int selectedCursor = -1; // determines the mode of the resize
    private int xR, yR; // moving reference coordinates
    private final Color onStartlayout = Color.BLACK;
    private final Color onStartfill = Color.WHITE;
    private final BasicStroke onStartThick = new BasicStroke(3.0f);
    private boolean moving = false;
    public boolean downloaded = false;
   
    // Background Operations
    private ArrayList<JButton> paintShapes = new ArrayList<JButton>(); // array of supported shapes
    private ArrayList<JMenu> paintMenus = new ArrayList<JMenu>(); // array of supported menus
    private ArrayList<JButton> paintTools = new ArrayList<JButton>(); // array of supported tools
    private String[] shapeNames = { "Line", "Square", "Rectangle", "Oval", "Circle", "Triangle" };
    private String[] menuNames = { "File", "Extentions" };
    private String[] toolNames = { "Undo", "Redo", "Modify", "Delete", "Outline Color", "Fill Color" };
    private String[] toolDescription = { "Undo", "Redo", "Move/Resize",
            "Delete shape", "Change outline color", "Change fill color" };
 
    // Class Instance
   private  Plugin plugin = new Plugin();
   private XMLWriter xw = new XMLWriter();
   private XMLReader xr = new XMLReader(this);
   private JSONWriter jw = new JSONWriter();
   private JSONReader jr = new JSONReader(this);
   private  ResourceLoader rl = new ResourceLoader();
   private final JFileChooser fc = new JFileChooser();
   private final JFileChooser fjar = new JFileChooser();
    private ExtensionFilter extFilter = new ExtensionFilter("Only XML or JSON", new String[] { ".xml", ".json" });
    private JARFilter jarExt = new JARFilter("Only jars");
   
 
    public Paint() throws IOException {
        prepareGUI();
        this.frame.setVisible(true);
    }
 
    private void prepareGUI() throws IOException {
        frame = new JFrame("PaintBrush");
        paintArea = new PaintArea();
        shapeButtons = new JPanel();
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(200, 30)); // modification
        frame.setBounds(0, 0, 1000, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
       
        frame.setIconImage(ImageIO.read(rl.load("Paint Brush.png"))); 
        // setting the menu bar
        for (int i = 0; i < menuNames.length; i++) {
            paintMenus.add(new JMenu(menuNames[i]));
            paintMenus.get(i).setName(menuNames[i]);
            menuBar.add(paintMenus.get(i));    
        }
       
        // setting file menu bar
        JMenuItem newfile = new JMenuItem("new");
        newfile.setName("new");
        newfile.addActionListener(new Menu());
        paintMenus.get(0).add(newfile);
 
        JMenuItem save = new JMenuItem("save");
        save.setName("save");
        save.addActionListener(new Menu());
        paintMenus.get(0).add(save);
 
        JMenuItem load = new JMenuItem("load");
        load.setName("load");
        load.addActionListener(new Menu());
        paintMenus.get(0).add(load);
 
        JMenuItem triangle = new JMenuItem("Triangle");
        triangle.setName("Triangle");
        triangle.addActionListener(new PlugIN());
        paintMenus.get(1).add(triangle);
        frame.setJMenuBar(menuBar);
       
        // setting shapes buttons
        shapeButtons.setLayout(new GridLayout(0, 1));
        shapeButtons.setBackground(Color.WHITE);
        for (int i = 0; i < shapeNames.length; i++) {
            ImageIcon icon = new ImageIcon(ImageIO.read(rl.load( shapeNames[i] + ".png")));
            paintShapes.add(new JButton(icon));
            paintShapes.get(i).setBackground(Color.WHITE);
            paintShapes.get(i).setName(shapeNames[i]);
            paintShapes.get(i).setToolTipText("Draw " + shapeNames[i]);
            paintShapes.get(i).addActionListener(new onClick());
            if (i!=5)
            shapeButtons.add(paintShapes.get(i));
        }
        paintShapes.get(5).setVisible(false);
        shapeButtons.setBounds(0, 0, 80, 650);
        frame.add(shapeButtons);
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
        	@Override
        	public void windowClosing(java.awt.event.WindowEvent windowevent){
        		
        		 if (JOptionPane.showConfirmDialog(frame, 
        		            "Do you want to save your artwork ?", "We shall miss you", 
        		            JOptionPane.YES_NO_OPTION,
        		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
        	            try {
        	                int selectedFile = fc.showSaveDialog(paintArea);
        	                if (selectedFile == JFileChooser.APPROVE_OPTION) {
        	                    File newFile = fc.getSelectedFile();
        	                    if (extFilter.extension(newFile).equals(extFilter.xml)) {
        	                        saveXML(newFile);
        	                    } else if (extFilter.extension(newFile).equals(extFilter.json)) {
        	                        saveJSON(newFile);
        	                    } else {
        	                        JOptionPane.showMessageDialog(null, "Enter Valid Filename");
        	                    }
        	                }
        	                
        	            } catch (Exception e) {
        	                JOptionPane.showMessageDialog(null, "Enter Valid Filename");
        	                return;
        	            }
        		            System.exit(0);
        		        }
        	}
        	
        	
        	
        });
        // setting tool bar
        toolBar = new JToolBar("Paint Tools");
        toolBar.setLayout(new FlowLayout());
        for (int i = 0; i < toolNames.length; i++) {
            ImageIcon icon = new ImageIcon(ImageIO.read(rl.load( toolNames[i] + ".png"))); 
            paintTools.add(new JButton(icon)); 
            paintTools.get(i).setName(toolNames[i]);
            paintTools.get(i).setToolTipText(toolDescription[i]);
            paintTools.get(i).addActionListener(new Option());
            toolBar.add(paintTools.get(i));
        }
        paintArea.add(toolBar);
        frame.add(paintArea);	
       
        // setting file chooser
 
        fc.setCurrentDirectory(new File("."));
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileFilter(extFilter);
        fc.addChoosableFileFilter(extFilter);
 
        fjar.setCurrentDirectory(new File("."));
        fjar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fjar.setFileFilter(jarExt);
       
        GetCoor dragAction = new GetCoor();
        paintArea.addMouseListener(dragAction);
        paintArea.addMouseMotionListener(dragAction);
 
    }
 
    // file menu actions
    public void fileMenu(String option) {
        switch (option) {
        case "save": {
            reset();
            try {
                int selectedFile = fc.showSaveDialog(paintArea);
                if (selectedFile == JFileChooser.APPROVE_OPTION) {
                    File newFile = fc.getSelectedFile();
                    if (extFilter.extension(newFile).equals(extFilter.xml)) {
                        saveXML(newFile);
                    } else if (extFilter.extension(newFile).equals(extFilter.json)) {
                        saveJSON(newFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter Valid Filename");
                    }
                }
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Enter Valid Filename");
                return;
            }
        }
       
        case "load": {
            int selectedFile = fc.showOpenDialog(paintArea);
            try {
                if (selectedFile == JFileChooser.APPROVE_OPTION) {
                    File newFile = fc.getSelectedFile();
                    if (extFilter.extension(newFile).equals(extFilter.xml)) {
                        loadXML(newFile);
                    } else if (extFilter.extension(newFile).equals(extFilter.json)) {
                        loadJSON(newFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter Valid Filename");
                    }
                    reset();
                    done.clear();
                    unDone.clear();
                    done.push(copyShapes(shapes));
                    paintArea.drawGrid(shapes, tempShape, selectedInd);
                }
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "File not Found");
                return;
            }
        }
       
        case "new": {
            reset();
            shapes.clear();
            done.clear();
            unDone.clear();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
            return;
        }
        }
    }
 
    // tool bar actions
    public void applyAction(String option) {
        switch (option) {
        case "Undo": {
            reset();
            undo();
            return;
        }
        case "Redo": {
            reset();
            redo();
            return;
        }
        case "Modify": {
            modify();
            return;
        }
        case "Delete": {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure");
            if (response == JOptionPane.YES_OPTION)
                delete();
            return;
        }
        case "Outline Color": {
            changeOutlineClr();
            return;
        }
        case "Fill Color": {
            changeFillClr();
            return;
        }
        }
    }
 
    // Undo Action
    public void undo() {
        if (done.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Cannot undo more!", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        unDone.push(copyShapes(done.pop()));
        if (done.isEmpty()) {
            shapes = new ArrayList<Shape>();
        } else {
            shapes = copyShapes(done.peek());
        }
        paintArea.drawGrid(shapes, tempShape, selectedInd);
    }
 
    // Redo Action
    public void redo() {
        if (unDone.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Cannot redo more!", "Warning", JOptionPane.PLAIN_MESSAGE);
        } else {
            done.push(copyShapes(unDone.pop()));
            shapes = copyShapes(done.peek());
            paintArea.drawGrid(shapes, tempShape, selectedInd);
        }
    }
 
    // Modify Action
    public void modify() {
        if (selectedInd >= 0)
            moving = true;
    }
 
    // Delete Action
    public void delete() {
        if (selectedInd >= 0) {
            shapes.remove(selectedInd);
            done.push(copyShapes(shapes));
            reset();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
        } else {
            reset();
            Toolkit.getDefaultToolkit().beep();
        }
    }
 
    // Change Outline Color Action
    public void changeOutlineClr() {
        if (selectedInd >= 0) {
            Color layoutClr = JColorChooser.showDialog(frame, "Choose Color", Color.white);
            tempShape.get(0).setLayoutClr(layoutClr);
            shapes.remove(selectedInd);
            shapes.add((Shape) (chooseShape(tempShape.get(0).getFillClr(),
                    new BasicStroke(3.0f), tempShape.get(0).getLayoutClr(),
                    tempShape.get(0).getVertices(), tempShape.get(0).getClass()
                            .getSimpleName())));
            done.push(copyShapes(shapes));
            reset();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
        } else {
            reset();
            Toolkit.getDefaultToolkit().beep();
        }
    }
 
    // Change Fill Color Action
    public void changeFillClr() {
        if (selectedInd >= 0) {
            Color fillClr = JColorChooser.showDialog(frame, "Choose Color", Color.white);
            tempShape.get(0).setFillClr(fillClr);
            shapes.remove(selectedInd);
            shapes.add((Shape) (chooseShape(tempShape.get(0).getFillClr(),
                    new BasicStroke(3.0f), tempShape.get(0).getLayoutClr(),
                    tempShape.get(0).getVertices(), tempShape.get(0).getClass()
                            .getSimpleName())));
            done.push(copyShapes(shapes));
            reset();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
        } else {
            reset();
            Toolkit.getDefaultToolkit().beep();
        }
    }
 
    // Save_XML
    public void saveXML(final File newSheet) {
        xw.writeXML(newSheet, copyShapes(shapes));
    }
 
    // Load_XML
    public void loadXML(final File loadSheet) {
        ArrayList<Shape> shapesXML = new ArrayList<Shape>(copyShapes(xr.readXML(loadSheet)));
        if (shapesXML != null) {
            shapes = copyShapes(shapesXML);
        }
    }
 
    // Save_JSON
    public void saveJSON(final File newSheet) {
        jw.toJSON(copyShapes(shapes));
        jw.write(newSheet);
    }
 
    //Load_JSON
    public void loadJSON(final File loadSheet) {
        shapes = new ArrayList<Shape>(copyShapes(jr.read(loadSheet)));
    }
 
    // creating new shape
    public Shape chooseShape(Color fillColor, BasicStroke thickness,
            Color LayoutColor, Point[] dim, String shapeName) {
        switch (shapeName) {
        case "Line": {
            return new Line(fillColor, thickness, LayoutColor, dim);
        }
        case "Square": {
            return new Square(fillColor, thickness, LayoutColor, dim);
        }
 
        case "Rectangle": {
            return new Rectangle(fillColor, thickness, LayoutColor, dim);
        }
 
        case "Triangle": {
            if (downloaded) {
                return plugin.createTriangle(fillColor, thickness, LayoutColor, dim);
            }
            break;
        }
 
        case "Oval": {
            return new Oval(fillColor, thickness, LayoutColor, dim);
        }
        case "Circle": {
            return new Circle(fillColor, thickness, LayoutColor, dim);
        }
        }
        return null;
    }
 
    // update points while moving or dragging
    public void updateNextPoint(int index, int x, int y) {
        if (index < points.size()) {
            points.get(index).x = x;
            points.get(index).y = y;
        } else {
            System.out.print("index not found");
        }
    }
 
    // Tools ActionListener
    class Option implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton option = (JButton) e.getSource();
            applyAction(option.getName());
        }
    }
 
    // File Menu Items ActionListener
    class Menu implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem menu = (JMenuItem) e.getSource();
            fileMenu(menu.getName());
        }
    }
 
    // Plug_in ActionListener
    class PlugIN implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
            if (!downloaded) {
                int selectedFile = fjar.showOpenDialog(paintArea);
                try {
                    if (selectedFile == JFileChooser.APPROVE_OPTION) {
                        File newFile = fjar.getSelectedFile();
                        if (jarExt.extension(newFile).equals(jarExt.jar)) {
                        	plugin.loadPlugin(newFile);
                            downloaded = true;
                            shapeButtons.add(paintShapes.get(5));
                            paintShapes.get(5).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "select proper file");
                        }
                    }
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "Couldn't find file");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Already downloaded");
            }
        }
    }
 
    // Shape Buttons ActionListener
    class onClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();
            paintArea.drawGrid(shapes, tempShape, selectedInd);
            JButton btnClicked = (JButton) e.getSource();
            Point[] initalPoints = null; // flag to initialize
            tempShape.add(chooseShape(onStartfill, onStartThick, onStartlayout,
                    initalPoints, btnClicked.getName()));
            pointsNum = tempShape.get(0).getPointsNum();
        }
    }
 
    class GetCoor extends MouseInputAdapter {
        public void mousePressed(MouseEvent e) {
            if (moving) {
                int temp = selectedInd;
                selectedInd = getSelectedInd(e.getX(), e.getY());
                xR = e.getX();
                yR = e.getY();
                selectedCursor = tempShape.get(0).getCursor(new Point(xR, yR));
                if (selectedInd >= 0 || selectedCursor >= 0) {
                    // one of the drag points selected
                    if (selectedCursor > -1) {
                        tempShape.get(0).resizeShape(selectedCursor, new Point(xR, yR));
                    }
                    // cursor selected out of shape
                    selectedInd = temp;
                    paintArea.drawGrid(shapes, tempShape, selectedInd);
                } else {
                    reset();
                }
            }
        }
 
        @Override
        public void mouseDragged(MouseEvent e) {
            if (moving) {
                if (selectedCursor >= 0) {
                    tempShape.get(0).resizeShape(selectedCursor, new Point(e.getX(), e.getY()));
                } else {
                    int deltaX = e.getX() - xR;
                    int deltaY = e.getY() - yR;
                    tempShape.get(0).move(deltaX, deltaY);
                    xR = e.getX();
                    yR = e.getY();
                }
                paintArea.drawGrid(shapes, tempShape, selectedInd);
            }
        }
 
        @Override
        public void mouseReleased(MouseEvent e) {
            if (pointsNum == 0 && moving && tempShape.size() != 0) {
                drawNew();
                paintArea.drawGrid(shapes, tempShape, selectedInd);
                moving = false;
            }
        }
 
        @Override
        public void mouseMoved(MouseEvent e) {
            // There's draw button Selected
            if (pointsNum > 0 && pointsCnt > 0) {
                // shapes drawn with two points
                if (pointsCnt == pointsNum - 1) {
                    if (tempShape.size() != 1) {
                        while (tempShape.size() != 1) {
                            tempShape.remove(tempShape.size() - 1);
                        }
                    }
                    if (points.size() == pointsNum) {
                        updateNextPoint(pointsNum - 1, e.getX(), e.getY());
                    } else {
                        points.add(new Point(e.getX(), e.getY()));
                    }
                    tempShape.get(0).updateVertices(points, pointsCnt - 1,
                            pointsCnt);
                } else {
                    // polygons
                    if (points.size() == pointsCnt + 1) {
                        updateNextPoint(pointsCnt, e.getX(), e.getY());
                    } else {
                        points.add(new Point(e.getX(), e.getY()));
                        Line temp = new Line(onStartfill, onStartThick,
                                onStartlayout, new Point[] { new Point(0, 0),
                                        new Point(0, 0) });
                        tempShape.add(temp);
                    }
                    tempShape.get(pointsCnt).updateVertices(points, pointsCnt - 1, pointsCnt);
                }
                paintArea.drawGrid(shapes, tempShape, selectedInd);
            }
        }
 
        @Override
        public void mouseClicked(MouseEvent e) {
            if (pointsNum != 0 && pointsCnt == pointsNum - 1) {
                // any point except the first one
                updateNextPoint(pointsCnt, e.getX(), e.getY());
                tempShape.get(0).updateVertices(points, pointsCnt - 1, pointsCnt);
                shapes.add((Shape) (chooseShape(onStartfill, onStartThick,
                        onStartlayout, tempShape.get(0).getVertices(),
                        tempShape.get(0).getClass().getSimpleName())));
 
                done.push(copyShapes(shapes));
                reset();
            } else if (pointsNum != pointsCnt) {
                // first point
                pointsCnt++;
                if (points.size() == 0) {
                    points.add(new Point(e.getX(), e.getY()));
                } else {
                    updateNextPoint(pointsCnt - 1, e.getX(), e.getY());
                }
            } else {
                selectedInd = getSelectedInd(e.getX(), e.getY());
                if (selectedInd >= 0) {
                    tempShape.clear();
                    float[] dash = { 5F, 5F };
                    tempShape
                            .add((Shape) (chooseShape(shapes.get(selectedInd)
                                    .getFillClr(), new BasicStroke(2F,
                                    BasicStroke.CAP_SQUARE,
                                    BasicStroke.JOIN_MITER, 3F, dash, 0F),
                                    shapes.get(selectedInd).getLayoutClr(),
                                    shapes.get(selectedInd).getVertices(),
                                    shapes.get(selectedInd).getClass()
                                            .getSimpleName())));
 
                } else {
                    reset();
                }
            }
            paintArea.drawGrid(shapes, tempShape, selectedInd);
        }
    }
 
    // resets all data related to to paint area
    public void reset() {
        tempShape.clear();
        pointsNum = 0;
        pointsCnt = 0;
        points.clear();
        selectedInd = -1;
        selectedCursor = -1;
        moving = false;
    }
 
    // returns the index of selected shape
    public int getSelectedInd(int x, int y) {
        for (int i = shapes.size() - 1; i >= 0; --i) {
            if (shapes.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }
 
    // release from selection
    public void drawNew() {
        if (tempShape.size() > 0) {
            shapes.remove(selectedInd);
            shapes.add((Shape) (chooseShape(tempShape.get(0).getFillClr(),
                    new BasicStroke(3.0f), tempShape.get(0).getLayoutClr(),
                    tempShape.get(0).getVertices(), tempShape.get(0).getClass()
                            .getSimpleName())));
            tempShape.clear();
            selectedInd = -1;
            selectedCursor = -1;
            done.push(copyShapes(shapes));
        }
    }
 
    // convert array to array_list
    public ArrayList<Point> toArrayList(Point[] dim) {
        ArrayList<Point> newz = new ArrayList<Point>();
        for (int i = 0; i < dim.length; i++) {
            newz.add(new Point(dim[i]));
        }
        return newz;
    }
 
    // make a new copy of shapes array_list
    public ArrayList<Shape> copyShapes(ArrayList<Shape> original) {
        ArrayList<Shape> copy = new ArrayList<Shape>();
        for (int i = 0; i < original.size(); i++) {
            copy.add((Shape) (chooseShape(original.get(i).getFillClr(),
                    original.get(i).getThickness(), original.get(i)
                            .getLayoutClr(), original.get(i).getVertices(),
                    original.get(i).getClass().getSimpleName())));
        }
        return copy;
    }
   
    public ArrayList<Shape> getShapes() {
        return shapes;
    }
    
    public Class<?> loadClasses() {
    	return  plugin.getLoadedClasses();
    }
}