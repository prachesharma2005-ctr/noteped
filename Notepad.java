import java.io.*;  
import java.util.Date;  
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  
import javax.swing.event.*;  
  
/************************************/  
class FileOperation  
{  
Notepad npd;  
  
boolean saved;  
boolean newFileFlag;  
String fileName;  
String applicationTitle="Notepad - JavaTpoint";  
  
File fileRef;  
JFileChooser chooser;  
/////////////////////////////  
boolean isSave(){return saved;}  
void setSave(boolean saved){this.saved=saved;}  
String getFileName(){return new String(fileName);}  
void setFileName(String fileName){this.fileName=new String(fileName);}  
/////////////////////////  
FileOperation(Notepad npd)  
{  
this.npd=npd;  
  
saved=true;  
newFileFlag=true;  
fileName=new String("Untitled");  
fileRef=new File(fileName);  
this.npd.f.setTitle(fileName+" - "+applicationTitle);  
  
chooser=new JFileChooser();  
chooser.addChoosableFileFilter(new MyFileFilter(".java","Java Source Files(*.java)"));  
chooser.addChoosableFileFilter(new MyFileFilter(".txt","Text Files(*.txt)"));  
chooser.setCurrentDirectory(new File("."));  
  
}  
//////////////////////////////////////  
  
boolean saveFile(File temp)  
{  
FileWriter fout=null;  
try  
{  
fout=new FileWriter(temp);  
fout.write(npd.ta.getText());  
}  
catch(IOException ioe){updateStatus(temp,false);return false;}  
finally  
{try{fout.close();}catch(IOException excp){}}  
updateStatus(temp,true);  
return true;  
}  
////////////////////////  
boolean saveThisFile()  
{  
  
if(!newFileFlag)  
    {return saveFile(fileRef);}  
  
return saveAsFile();  
}  
////////////////////////////////////  
boolean saveAsFile()  
{  
File temp=null;  
chooser.setDialogTitle("Save As...");  
chooser.setApproveButtonText("Save Now");   
chooser.setApproveButtonMnemonic(KeyEvent.VK_S);  
chooser.setApproveButtonToolTipText("Click me to save!");  
  
do  
{  
if(chooser.showSaveDialog(this.npd.f)!=JFileChooser.APPROVE_OPTION)  
    return false;  
temp=chooser.getSelectedFile();  
if(!temp.exists()) break;  
if(   JOptionPane.showConfirmDialog(  
    this.npd.f,"<html>"+temp.getPath()+" already exists.<br>Do you want to replace it?<html>",  
    "Save As",JOptionPane.YES_NO_OPTION  
                )==JOptionPane.YES_OPTION)  
    break;  
}while(true);  
  
  
return saveFile(temp);  
}  
  
////////////////////////  
boolean openFile(File temp)  
{  
FileInputStream fin=null;  
BufferedReader din=null;  
  
try  
{  
fin=new FileInputStream(temp);  
din=new BufferedReader(new InputStreamReader(fin));  
String str=" ";  
while(str!=null)  
{  
str=din.readLine();  
if(str==null)  
break;  
this.npd.ta.append(str+"\n");  
}  
  
}  
catch(IOException ioe){updateStatus(temp,false);return false;}  
finally  
{try{din.close();fin.close();}catch(IOException excp){}}  
updateStatus(temp,true);  
this.npd.ta.setCaretPosition(0);  
return true;  
}  
///////////////////////  
void openFile()  
{  
if(!confirmSave()) return;  
chooser.setDialogTitle("Open File...");  
chooser.setApproveButtonText("Open this");   
chooser.setApproveButtonMnemonic(KeyEvent.VK_O);  
chooser.setApproveButtonToolTipText("Click me to open the selected file.!");  
  
File temp=null;  
do  
{  
if(chooser.showOpenDialog(this.npd.f)!=JFileChooser.APPROVE_OPTION)  
    return;  
temp=chooser.getSelectedFile();  
  
if(temp.exists())   break;  
  
JOptionPane.showMessageDialog(this.npd.f,  
    "<html>"+temp.getName()+"<br>file not found.<br>"+  
    "Please verify the correct file name was given.<html>",  
    "Open", JOptionPane.INFORMATION_MESSAGE);  
  
} while(true);  
  
this.npd.ta.setText("");  
  
if(!openFile(temp))  
    {  
    fileName="Untitled"; saved=true;   
    this.npd.f.setTitle(fileName+" - "+applicationTitle);  
    }  
if(!temp.canWrite())  
    newFileFlag=true;  
  
}  
////////////////////////  
void updateStatus(File temp,boolean saved)  
{  
if(saved)  
{  
this.saved=true;  
fileName=new String(temp.getName());  
if(!temp.canWrite())  
    {fileName+="(Read only)"; newFileFlag=true;}  
fileRef=temp;  
npd.f.setTitle(fileName + " - "+applicationTitle);  
npd.statusBar.setText("File : "+temp.getPath()+" saved/opened successfully.");  
newFileFlag=false;  
}  
else  
{  
npd.statusBar.setText("Failed to save/open : "+temp.getPath());  
}  
}  
///////////////////////  
boolean confirmSave()  
{  
String strMsg="<html>The text in the "+fileName+" file has been changed.<br>"+  
    "Do you want to save the changes?<html>";  
if(!saved)  
{  
int x=JOptionPane.showConfirmDialog(this.npd.f,strMsg,applicationTitle,  
JOptionPane.YES_NO_CANCEL_OPTION);  
  
if(x==JOptionPane.CANCEL_OPTION) return false;  
if(x==JOptionPane.YES_OPTION && !saveAsFile()) return false;  
}  
return true;  
}  
///////////////////////////////////////  
void newFile()  
{  
if(!confirmSave()) return;  
  
this.npd.ta.setText("");  
fileName=new String("Untitled");  
fileRef=new File(fileName);  
saved=true;  
newFileFlag=true;  
this.npd.f.setTitle(fileName+" - "+applicationTitle);  
}  
//////////////////////////////////////  
}// end defination of class FileOperation  
/************************************/  
class MyFileFilter extends javax.swing.filechooser.FileFilter {
    private String extension;
    private String description;

    public MyFileFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().toLowerCase().endsWith(extension);
    }

    public String getDescription() {
        return description;
    }
}
/************************************/  
class FindDialog extends JDialog implements ActionListener {
    private JTextArea textArea;
    private JTextField findField, replaceField;
    private JButton findButton, replaceButton, replaceAllButton, cancelButton;
    private JCheckBox matchCaseCheckBox;
    private boolean isFind = true;
    private int lastSearchIndex = 0;

    public FindDialog(JTextArea ta) {
        this.textArea = ta;
        setTitle("Find");
        setModal(false);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Find what
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Find what:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        findField = new JTextField(20);
        mainPanel.add(findField, gbc);

        // Replace with
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Replace with:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        replaceField = new JTextField(20);
        mainPanel.add(replaceField, gbc);

        // Match case checkbox
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        matchCaseCheckBox = new JCheckBox("Match case");
        mainPanel.add(matchCaseCheckBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        findButton = new JButton("Find Next");
        replaceButton = new JButton("Replace");
        replaceAllButton = new JButton("Replace All");
        cancelButton = new JButton("Cancel");

        findButton.addActionListener(this);
        replaceButton.addActionListener(this);
        replaceAllButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(findButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    public void showDialog(JFrame parent, boolean findOnly) {
        this.isFind = findOnly;
        if (findOnly) {
            setTitle("Find");
            replaceField.setEnabled(false);
            replaceButton.setEnabled(false);
            replaceAllButton.setEnabled(false);
        } else {
            setTitle("Replace");
            replaceField.setEnabled(true);
            replaceButton.setEnabled(true);
            replaceAllButton.setEnabled(true);
        }
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findButton) {
            findNextWithSelection();
        } else if (e.getSource() == replaceButton) {
            replace();
        } else if (e.getSource() == replaceAllButton) {
            replaceAll();
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    public void findNextWithSelection() {
        String searchText = findField.getText();
        if (searchText.isEmpty()) {
            return;
        }

        String content = textArea.getText();
        if (!matchCaseCheckBox.isSelected()) {
            content = content.toLowerCase();
            searchText = searchText.toLowerCase();
        }

        int index = content.indexOf(searchText, lastSearchIndex);
        if (index >= 0) {
            textArea.setSelectionStart(index);
            textArea.setSelectionEnd(index + searchText.length());
            lastSearchIndex = index + 1;
        } else {
            lastSearchIndex = 0;
            JOptionPane.showMessageDialog(this, "Cannot find \"" + findField.getText() + "\"", 
                "Find", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void replace() {
        String selectedText = textArea.getSelectedText();
        String searchText = findField.getText();
        
        if (selectedText != null) {
            String compareSelected = selectedText;
            String compareSearch = searchText;
            
            if (!matchCaseCheckBox.isSelected()) {
                compareSelected = compareSelected.toLowerCase();
                compareSearch = compareSearch.toLowerCase();
            }
            
            if (compareSelected.equals(compareSearch)) {
                textArea.replaceSelection(replaceField.getText());
            }
        }
        findNextWithSelection();
    }

    private void replaceAll() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        
        if (searchText.isEmpty()) {
            return;
        }

        String content = textArea.getText();
        String newContent;
        
        if (matchCaseCheckBox.isSelected()) {
            newContent = content.replace(searchText, replaceText);
        } else {
            newContent = content.replaceAll("(?i)" + searchText, replaceText);
        }
        
        textArea.setText(newContent);
        lastSearchIndex = 0;
        JOptionPane.showMessageDialog(this, "Replace All completed", 
            "Replace", JOptionPane.INFORMATION_MESSAGE);
    }
}
/************************************/  
class FontChooser extends JDialog {
    private JList<String> fontList, styleList, sizeList;
    private JTextField fontField, styleField, sizeField;
    private JLabel previewLabel;
    private Font selectedFont;
    private boolean okPressed = false;

    private String[] fontNames;
    private String[] styleNames = {"Regular", "Bold", "Italic", "Bold Italic"};
    private String[] sizeNames = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};

    public FontChooser(Font initialFont) {
        this.selectedFont = initialFont;
        setModal(true);
        setTitle("Font Chooser");
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Get available fonts
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontNames = ge.getAvailableFontFamilyNames();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with font, style, size
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        // Font panel
        JPanel fontPanel = new JPanel(new BorderLayout(5, 5));
        fontPanel.add(new JLabel("Font:"), BorderLayout.NORTH);
        fontField = new JTextField(initialFont.getFamily());
        fontPanel.add(fontField, BorderLayout.CENTER);
        fontList = new JList<>(fontNames);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setSelectedValue(initialFont.getFamily(), true);
        fontList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    fontField.setText(fontList.getSelectedValue());
                    updatePreview();
                }
            }
        });
        fontPanel.add(new JScrollPane(fontList), BorderLayout.SOUTH);

        // Style panel
        JPanel stylePanel = new JPanel(new BorderLayout(5, 5));
        stylePanel.add(new JLabel("Style:"), BorderLayout.NORTH);
        styleField = new JTextField(getStyleName(initialFont.getStyle()));
        stylePanel.add(styleField, BorderLayout.CENTER);
        styleList = new JList<>(styleNames);
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleList.setSelectedValue(getStyleName(initialFont.getStyle()), true);
        styleList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    styleField.setText(styleList.getSelectedValue());
                    updatePreview();
                }
            }
        });
        stylePanel.add(new JScrollPane(styleList), BorderLayout.SOUTH);

        // Size panel
        JPanel sizePanel = new JPanel(new BorderLayout(5, 5));
        sizePanel.add(new JLabel("Size:"), BorderLayout.NORTH);
        sizeField = new JTextField(String.valueOf(initialFont.getSize()));
        sizePanel.add(sizeField, BorderLayout.CENTER);
        sizeList = new JList<>(sizeNames);
        sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sizeList.setSelectedValue(String.valueOf(initialFont.getSize()), true);
        sizeList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    sizeField.setText(sizeList.getSelectedValue());
                    updatePreview();
                }
            }
        });
        sizePanel.add(new JScrollPane(sizeList), BorderLayout.SOUTH);

        topPanel.add(fontPanel);
        topPanel.add(stylePanel);
        topPanel.add(sizePanel);

        // Preview panel
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewLabel = new JLabel("AaBbYyZz", JLabel.CENTER);
        previewLabel.setFont(initialFont);
        previewPanel.add(previewLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                selectedFont = createFont();
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = false;
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(previewPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private String getStyleName(int style) {
        switch (style) {
            case Font.BOLD:
                return "Bold";
            case Font.ITALIC:
                return "Italic";
            case Font.BOLD | Font.ITALIC:
                return "Bold Italic";
            default:
                return "Regular";
        }
    }

    private int getStyleValue(String styleName) {
        switch (styleName) {
            case "Bold":
                return Font.BOLD;
            case "Italic":
                return Font.ITALIC;
            case "Bold Italic":
                return Font.BOLD | Font.ITALIC;
            default:
                return Font.PLAIN;
        }
    }

    private void updatePreview() {
        previewLabel.setFont(createFont());
    }

    public Font createFont() {
        String fontName = fontField.getText();
        String styleName = styleField.getText();
        int style = getStyleValue(styleName);
        int size = 12;
        
        try {
            size = Integer.parseInt(sizeField.getText());
        } catch (NumberFormatException e) {
            size = 12;
        }
        
        return new Font(fontName, style, size);
    }

    public boolean showDialog(JFrame parent, String title) {
        setTitle(title);
        setLocationRelativeTo(parent);
        okPressed = false;
        setVisible(true);
        return okPressed;
    }
}
/************************************/  
class LookAndFeelMenu {
    public static void createLookAndFeelMenuItem(JMenu viewMenu, JFrame frame) {
        JMenu lafMenu = new JMenu("Look and Feel");
        
        UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        ButtonGroup lafGroup = new ButtonGroup();
        
        String currentLAF = UIManager.getLookAndFeel().getClass().getName();
        
        for (UIManager.LookAndFeelInfo laf : lafs) {
            JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(laf.getName());
            lafItem.setSelected(laf.getClassName().equals(currentLAF));
            lafGroup.add(lafItem);
            
            lafItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(laf.getClassName());
                        SwingUtilities.updateComponentTreeUI(frame);
                        frame.pack();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, 
                            "Could not set Look and Feel: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            lafMenu.add(lafItem);
        }
        
        viewMenu.add(lafMenu);
    }
}
/************************************/  
public class Notepad  implements ActionListener, MenuConstants  
{  
  
JFrame f;  
JTextArea ta;  
JLabel statusBar;  
  
private String fileName="Untitled";  
private boolean saved=true;  
String applicationName="Javapad";  
  
String searchString, replaceString;  
int lastSearchIndex;  
  
FileOperation fileHandler;  
FontChooser fontDialog=null;  
FindDialog findReplaceDialog=null;   
JColorChooser bcolorChooser=null;  
JColorChooser fcolorChooser=null;  
JDialog backgroundDialog=null;  
JDialog foregroundDialog=null;  
JMenuItem cutItem,copyItem, deleteItem, findItem, findNextItem,  
replaceItem, gotoItem, selectAllItem;  
/****************************/  
Notepad()  
{  
f=new JFrame(fileName+" - "+applicationName);  
ta=new JTextArea(30,60);  
statusBar=new JLabel("||       Ln 1, Col 1  ",JLabel.RIGHT);  
f.add(new JScrollPane(ta),BorderLayout.CENTER);  
f.add(statusBar,BorderLayout.SOUTH);  
f.add(new JLabel("  "),BorderLayout.EAST);  
f.add(new JLabel("  "),BorderLayout.WEST);  
createMenuBar(f);  
//f.setSize(350,350);  
f.pack();  
f.setLocation(100,50);  
f.setVisible(true);  
f.setLocation(150,50);  
f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
  
fileHandler=new FileOperation(this);  
  
/////////////////////  
  
ta.addCaretListener(  
new CaretListener()  
{  
public void caretUpdate(CaretEvent e)  
{  
int lineNumber=0, column=0, pos=0;  
  
try  
{  
pos=ta.getCaretPosition();  
lineNumber=ta.getLineOfOffset(pos);  
column=pos-ta.getLineStartOffset(lineNumber);  
}catch(Exception excp){}  
if(ta.getText().length()==0){lineNumber=0; column=0;}  
statusBar.setText("||       Ln "+(lineNumber+1)+", Col "+(column+1));  
}  
});  
//////////////////  
DocumentListener myListener = new DocumentListener()  
{  
public void changedUpdate(DocumentEvent e){fileHandler.saved=false;}  
public void removeUpdate(DocumentEvent e){fileHandler.saved=false;}  
public void insertUpdate(DocumentEvent e){fileHandler.saved=false;}  
};  
ta.getDocument().addDocumentListener(myListener);  
/////////  
WindowListener frameClose=new WindowAdapter()  
{  
public void windowClosing(WindowEvent we)  
{  
if(fileHandler.confirmSave())System.exit(0);  
}  
};  
f.addWindowListener(frameClose);  
//////////////////  
/* 
ta.append("Hello dear hello hi"); 
ta.append("\nwho are u dear mister hello"); 
ta.append("\nhello bye hel"); 
ta.append("\nHello"); 
ta.append("\nMiss u mister hello hell"); 
fileHandler.saved=true; 
*/  
}  
////////////////////////////////////  
void goTo()  
{  
int lineNumber=0;  
try  
{  
lineNumber=ta.getLineOfOffset(ta.getCaretPosition())+1;  
String tempStr=JOptionPane.showInputDialog(f,"Enter Line Number:",""+lineNumber);  
if(tempStr==null)  
    {return;}  
lineNumber=Integer.parseInt(tempStr);  
ta.setCaretPosition(ta.getLineStartOffset(lineNumber-1));  
}catch(Exception e){}  
}  
///////////////////////////////////  
public void actionPerformed(ActionEvent ev)  
{  
String cmdText=ev.getActionCommand();  
////////////////////////////////////  
if(cmdText.equals(fileNew))  
    fileHandler.newFile();  
else if(cmdText.equals(fileOpen))  
    fileHandler.openFile();  
////////////////////////////////////  
else if(cmdText.equals(fileSave))  
    fileHandler.saveThisFile();  
////////////////////////////////////  
else if(cmdText.equals(fileSaveAs))  
    fileHandler.saveAsFile();  
////////////////////////////////////  
else if(cmdText.equals(fileExit))  
    {if(fileHandler.confirmSave())System.exit(0);}  
////////////////////////////////////  
else if(cmdText.equals(filePrint))  
JOptionPane.showMessageDialog(  
    Notepad.this.f,  
    "Get ur printer repaired first! It seems u dont have one!",  
    "Bad Printer",  
    JOptionPane.INFORMATION_MESSAGE  
    );  
////////////////////////////////////  
else if(cmdText.equals(editCut))  
    ta.cut();  
////////////////////////////////////  
else if(cmdText.equals(editCopy))  
    ta.copy();  
////////////////////////////////////  
else if(cmdText.equals(editPaste))  
    ta.paste();  
////////////////////////////////////  
else if(cmdText.equals(editDelete))  
    ta.replaceSelection("");  
////////////////////////////////////  
else if(cmdText.equals(editFind))  
{  
if(Notepad.this.ta.getText().length()==0)  
    return; // text box have no text  
if(findReplaceDialog==null)  
    findReplaceDialog=new FindDialog(Notepad.this.ta);  
findReplaceDialog.showDialog(Notepad.this.f,true);//find  
}  
////////////////////////////////////  
else if(cmdText.equals(editFindNext))  
{  
if(Notepad.this.ta.getText().length()==0)  
    return; // text box have no text  
  
if(findReplaceDialog==null)  
    statusBar.setText("Use Find option of Edit Menu first !!!!");  
else  
    findReplaceDialog.findNextWithSelection();  
}  
////////////////////////////////////  
else if(cmdText.equals(editReplace))  
{  
if(Notepad.this.ta.getText().length()==0)  
    return; // text box have no text  
  
if(findReplaceDialog==null)  
    findReplaceDialog=new FindDialog(Notepad.this.ta);  
findReplaceDialog.showDialog(Notepad.this.f,false);//replace  
}  
////////////////////////////////////  
else if(cmdText.equals(editGoTo))  
{  
if(Notepad.this.ta.getText().length()==0)  
    return; // text box have no text  
goTo();  
}  
////////////////////////////////////  
else if(cmdText.equals(editSelectAll))  
    ta.selectAll();  
////////////////////////////////////  
else if(cmdText.equals(editTimeDate))  
    ta.insert(new Date().toString(),ta.getSelectionStart());  
////////////////////////////////////  
else if(cmdText.equals(formatWordWrap))  
{  
JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();  
ta.setLineWrap(temp.isSelected());  
}  
////////////////////////////////////  
else if(cmdText.equals(formatFont))  
{  
if(fontDialog==null)  
    fontDialog=new FontChooser(ta.getFont());  
  
if(fontDialog.showDialog(Notepad.this.f,"Choose a font"))  
    Notepad.this.ta.setFont(fontDialog.createFont());  
}  
////////////////////////////////////  
else if(cmdText.equals(formatForeground))  
    showForegroundColorDialog();  
////////////////////////////////////  
else if(cmdText.equals(formatBackground))  
    showBackgroundColorDialog();  
////////////////////////////////////  
  
else if(cmdText.equals(viewStatusBar))  
{  
JCheckBoxMenuItem temp=(JCheckBoxMenuItem)ev.getSource();  
statusBar.setVisible(temp.isSelected());  
}  
////////////////////////////////////  
else if(cmdText.equals(helpAboutNotepad))  
{  
JOptionPane.showMessageDialog(Notepad.this.f,aboutText,"Dedicated 2 u!",  
JOptionPane.INFORMATION_MESSAGE);  
}  
else  
    statusBar.setText("This "+cmdText+" command is yet to be implemented");  
}//action Performed  
////////////////////////////////////  
void showBackgroundColorDialog()  
{  
if(bcolorChooser==null)  
    bcolorChooser=new JColorChooser();  
if(backgroundDialog==null)  
    backgroundDialog=JColorChooser.createDialog  
        (Notepad.this.f,  
        formatBackground,  
        false,  
        bcolorChooser,  
        new ActionListener()  
        {public void actionPerformed(ActionEvent evvv){  
            Notepad.this.ta.setBackground(bcolorChooser.getColor());}},  
        null);        
  
backgroundDialog.setVisible(true);  
}  
////////////////////////////////////  
void showForegroundColorDialog()  
{  
if(fcolorChooser==null)  
    fcolorChooser=new JColorChooser();  
if(foregroundDialog==null)  
    foregroundDialog=JColorChooser.createDialog  
        (Notepad.this.f,  
        formatForeground,  
        false,  
        fcolorChooser,  
        new ActionListener()  
        {public void actionPerformed(ActionEvent evvv){  
            Notepad.this.ta.setForeground(fcolorChooser.getColor());}},  
        null);        
  
foregroundDialog.setVisible(true);  
}  
  
///////////////////////////////////  
JMenuItem createMenuItem(String s, int key,JMenu toMenu,ActionListener al)  
{  
JMenuItem temp=new JMenuItem(s,key);  
temp.addActionListener(al);  
toMenu.add(temp);  
  
return temp;  
}  
////////////////////////////////////  
JMenuItem createMenuItem(String s, int key,JMenu toMenu,int aclKey,ActionListener al)  
{  
JMenuItem temp=new JMenuItem(s,key);  
temp.addActionListener(al);  
temp.setAccelerator(KeyStroke.getKeyStroke(aclKey,ActionEvent.CTRL_MASK));  
toMenu.add(temp);  
  
return temp;  
}  
////////////////////////////////////  
JCheckBoxMenuItem createCheckBoxMenuItem(String s,  
 int key,JMenu toMenu,ActionListener al)  
{  
JCheckBoxMenuItem temp=new JCheckBoxMenuItem(s);  
temp.setMnemonic(key);  
temp.addActionListener(al);  
temp.setSelected(false);  
toMenu.add(temp);  
  
return temp;  
}  
////////////////////////////////////  
JMenu createMenu(String s,int key,JMenuBar toMenuBar)  
{  
JMenu temp=new JMenu(s);  
temp.setMnemonic(key);  
toMenuBar.add(temp);  
return temp;  
}  
/*********************************/  
void createMenuBar(JFrame f)  
{  
JMenuBar mb=new JMenuBar();  
JMenuItem temp;  
  
JMenu fileMenu=createMenu(fileText,KeyEvent.VK_F,mb);  
JMenu editMenu=createMenu(editText,KeyEvent.VK_E,mb);  
JMenu formatMenu=createMenu(formatText,KeyEvent.VK_O,mb);  
JMenu viewMenu=createMenu(viewText,KeyEvent.VK_V,mb);  
JMenu helpMenu=createMenu(helpText,KeyEvent.VK_H,mb);  
  
createMenuItem(fileNew,KeyEvent.VK_N,fileMenu,KeyEvent.VK_N,this);  
createMenuItem(fileOpen,KeyEvent.VK_O,fileMenu,KeyEvent.VK_O,this);  
createMenuItem(fileSave,KeyEvent.VK_S,fileMenu,KeyEvent.VK_S,this);  
createMenuItem(fileSaveAs,KeyEvent.VK_A,fileMenu,this);  
fileMenu.addSeparator();  
temp=createMenuItem(filePageSetup,KeyEvent.VK_U,fileMenu,this);  
temp.setEnabled(false);  
createMenuItem(filePrint,KeyEvent.VK_P,fileMenu,KeyEvent.VK_P,this);  
fileMenu.addSeparator();  
createMenuItem(fileExit,KeyEvent.VK_X,fileMenu,this);  
  
temp=createMenuItem(editUndo,KeyEvent.VK_U,editMenu,KeyEvent.VK_Z,this);  
temp.setEnabled(false);  
editMenu.addSeparator();  
cutItem=createMenuItem(editCut,KeyEvent.VK_T,editMenu,KeyEvent.VK_X,this);  
copyItem=createMenuItem(editCopy,KeyEvent.VK_C,editMenu,KeyEvent.VK_C,this);  
createMenuItem(editPaste,KeyEvent.VK_P,editMenu,KeyEvent.VK_V,this);  
deleteItem=createMenuItem(editDelete,KeyEvent.VK_L,editMenu,this);  
deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));  
editMenu.addSeparator();  
findItem=createMenuItem(editFind,KeyEvent.VK_F,editMenu,KeyEvent.VK_F,this);  
findNextItem=createMenuItem(editFindNext,KeyEvent.VK_N,editMenu,this);  
findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));  
replaceItem=createMenuItem(editReplace,KeyEvent.VK_R,editMenu,KeyEvent.VK_H,this);  
gotoItem=createMenuItem(editGoTo,KeyEvent.VK_G,editMenu,KeyEvent.VK_G,this);  
editMenu.addSeparator();  
selectAllItem=createMenuItem(editSelectAll,KeyEvent.VK_A,editMenu,KeyEvent.VK_A,this);  
createMenuItem(editTimeDate,KeyEvent.VK_D,editMenu,this)  
.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));  
  
createCheckBoxMenuItem(formatWordWrap,KeyEvent.VK_W,formatMenu,this);  
  
createMenuItem(formatFont,KeyEvent.VK_F,formatMenu,this);  
formatMenu.addSeparator();  
createMenuItem(formatForeground,KeyEvent.VK_T,formatMenu,this);  
createMenuItem(formatBackground,KeyEvent.VK_P,formatMenu,this);  
  
createCheckBoxMenuItem(viewStatusBar,KeyEvent.VK_S,viewMenu,this).setSelected(true);  
/************For Look and Feel***/  
LookAndFeelMenu.createLookAndFeelMenuItem(viewMenu,this.f);  
  
  
temp=createMenuItem(helpHelpTopic,KeyEvent.VK_H,helpMenu,this);  
temp.setEnabled(false);  
helpMenu.addSeparator();  
createMenuItem(helpAboutNotepad,KeyEvent.VK_A,helpMenu,this);  
  
MenuListener editMenuListener=new MenuListener()  
{  
   public void menuSelected(MenuEvent evvvv)  
    {  
    if(Notepad.this.ta.getText().length()==0)  
    {  
    findItem.setEnabled(false);  
    findNextItem.setEnabled(false);  
    replaceItem.setEnabled(false);  
    selectAllItem.setEnabled(false);  
    gotoItem.setEnabled(false);  
    }  
    else  
    {  
    findItem.setEnabled(true);  
    findNextItem.setEnabled(true);  
    replaceItem.setEnabled(true);  
    selectAllItem.setEnabled(true);  
    gotoItem.setEnabled(true);  
    }  
    if(Notepad.this.ta.getSelectionStart()==ta.getSelectionEnd())  
    {  
    cutItem.setEnabled(false);  
    copyItem.setEnabled(false);  
    deleteItem.setEnabled(false);  
    }  
    else  
    {  
    cutItem.setEnabled(true);  
    copyItem.setEnabled(true);  
    deleteItem.setEnabled(true);  
    }  
    }  
   public void menuDeselected(MenuEvent evvvv){}  
   public void menuCanceled(MenuEvent evvvv){}  
};  
editMenu.addMenuListener(editMenuListener);  
f.setJMenuBar(mb);  
}  
/*************Constructor**************/  
////////////////////////////////////  
public static void main(String[] s)  
{  
new Notepad();  
}  
}  
/**************************************/  
interface MenuConstants  
{  
final String fileText="File";  
final String editText="Edit";  
final String formatText="Format";  
final String viewText="View";  
final String helpText="Help";  
  
final String fileNew="New";  
final String fileOpen="Open...";  
final String fileSave="Save";  
final String fileSaveAs="Save As...";  
final String filePageSetup="Page Setup...";  
final String filePrint="Print";  
final String fileExit="Exit";  
  
final String editUndo="Undo";  
final String editCut="Cut";  
final String editCopy="Copy";  
final String editPaste="Paste";  
final String editDelete="Delete";  
final String editFind="Find...";  
final String editFindNext="Find Next";  
final String editReplace="Replace";  
final String editGoTo="Go To...";  
final String editSelectAll="Select All";  
final String editTimeDate="Time/Date";  
  
final String formatWordWrap="Word Wrap";  
final String formatFont="Font...";  
final String formatForeground="Set Text color...";  
final String formatBackground="Set Pad color...";  
  
final String viewStatusBar="Status Bar";  
  
final String helpHelpTopic="Help Topic";  
final String helpAboutNotepad="About Javapad";  
  
final String aboutText="Your Javapad";  
}  