import com.malicia.mrg.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.sqlite.ShowResultsetInJtable;
import javafx.fxml.FXML;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class Main {

    public static final String BIGTITLE_JTABLE = "auto match (New 2 repertoire photo) from lrcat";

    public static void main(String[] args) {

        Properties properties = null;

        try (FileReader reader = new FileReader("resource/config.properties")){
            properties = new Properties();
            properties.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Statement stmt = null;

        String RepertoireNew = properties.getProperty("RepertoireNew");
        String PasRepertoirePhoto = properties.getProperty("PasRepertoirePhoto");
        String TempsAdherence = properties.getProperty("TempsAdherence");
        String CatalogLrcat = properties.getProperty("CatalogLrcat");

        SimpleDateFormat formattertodate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formattertoyymmdd = new SimpleDateFormat("yyyyMMdd");

        final SQLiteJDBCDriverConnection sql = new SQLiteJDBCDriverConnection();
        sql.connect(CatalogLrcat);

        SqlCreateAndAlimentionTable(PasRepertoirePhoto, TempsAdherence,RepertoireNew, sql);

        CreateJTableSelectionRepertoire(sql);


    }

    private static void SqlCreateAndAlimentionTable(String pasRepertoirePhoto, String tempsAdherence, String repertoireNew, SQLiteJDBCDriverConnection sql) {
        sql.execute("DROP TABLE IF EXISTS Repertory;  ");
//
        sql.execute("CREATE TEMPORARY TABLE Repertory AS  " +
                "select e.captureTime as ortime ,  strftime('%s', DATETIME( e.captureTime,\"-"+ tempsAdherence +"\")) as mint , strftime('%s', DATETIME(e.captureTime,\"+"+ tempsAdherence +"\")) as maxt , c.absolutePath , b.pathFromRoot   " +
                " from AgLibraryFile a  " +
                "inner join AgLibraryFolder b  " +
                "on a.folder = b.id_local  " +
                "inner join AgLibraryRootFolder c  " +
                "on b.rootFolder = c.id_local  " +
                "inner join Adobe_images e  " +
                "on a.id_local = e.rootFile  " +
                "Where  b.pathFromRoot not like \"%" + pasRepertoirePhoto + "%\" " +
                " ;");

        sql.execute("DROP TABLE IF EXISTS NewPhoto;  " );

        sql.execute( "CREATE TEMPORARY TABLE NewPhoto AS  " +
                "select  strftime('%s', e.captureTime) as captureTime , c.absolutePath , b.pathFromRoot ,a.originalFilename ,e.captureTime as captureTimeOrig , aiecm.value as CameraModel " +
                "from AgLibraryFile a  " +
                "inner join AgLibraryFolder b  " +
                "on a.folder = b.id_local  " +
                "inner join AgLibraryRootFolder c  " +
                "on b.rootFolder = c.id_local  " +
                "inner join Adobe_images e  " +
                "on a.id_local = e.rootFile  " +
                "LEFT JOIN AgHarvestedExifMetadata ahem " +
                "ON e.id_local = ahem.image " +
                "LEFT JOIN AgInternedExifCameraModel aiecm " +
                "ON ahem.cameraModelRef = aiecm.id_local " +
                "Where b.pathFromRoot like \"%" + repertoireNew + "%" + "\";  ");

        sql.execute("DROP TABLE IF EXISTS SelectionRepertoire;  " );

        sql.execute( "CREATE TEMPORARY TABLE SelectionRepertoire AS  " +
                "SELECT distinct  " +
                " b.pathFromRoot || b.originalFilename as src , " +
                " a.absolutePath || a.pathFromRoot as dest " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                ";");
    }

    private static JTable CreateJTableSelectionRepertoire(final SQLiteJDBCDriverConnection sql) {

        sql.select("SELECT a.dest  " +
                ", count(*) as nb " +
                "FROM SelectionRepertoire a " +
                "group by  a.dest " +
                ";");

        JTable ListeSelectionRepertoire = null;
        try {
            ListeSelectionRepertoire = new ShowResultsetInJtable(sql, BIGTITLE_JTABLE, "Selection Repertoire").invoke(JFrame.EXIT_ON_CLOSE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final JTable finalListeSelectionRepertoire = ListeSelectionRepertoire;
        ListeSelectionRepertoire.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && finalListeSelectionRepertoire.getSelectedRow() != -1) {

                    JTable tableForOneRepertoire = CreateJtableForOneRepertoire(finalListeSelectionRepertoire.getValueAt(finalListeSelectionRepertoire.getSelectedRow(), 0).toString(), sql);

                }
//                    System.out.println(event.toString());
//                    System.out.println(ListeSelectionRepertoire.getValueAt(ListeSelectionRepertoire.getSelectedRow(), 0).toString());
            }
        });

        return ListeSelectionRepertoire;

    }
    private static JTable CreateJtableForOneRepertoire(String Dest , SQLiteJDBCDriverConnection sql) {


        sql.select("SELECT distinct  " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename , " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename as loadimage , " +
                " a.absolutePath || a.pathFromRoot as dest , " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename as exif " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                "WHERE a.absolutePath || a.pathFromRoot = \"" + Dest + "\" " +
                "order by  a.absolutePath , a.pathFromRoot " +
                " ;");


        JTable ListeForOneRepertoire = null;
        try {
            ListeForOneRepertoire = new ShowResultsetInJtable(sql, BIGTITLE_JTABLE,"Liste @new -> !repertoire") .invoke(JFrame.DISPOSE_ON_CLOSE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final JTable finalListeForOneRepertoire = ListeForOneRepertoire;

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem viewItem = new JMenuItem("View");
        JMenuItem unmatchItem = new JMenuItem("UnMatch");
        JMenuItem opendestItem = new JMenuItem("Open Destination");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem moveallItem = new JMenuItem("-=*Move All*=-");

        viewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playElement(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString());
//                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose viewItem");
            }
        });
        unmatchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel) finalListeForOneRepertoire.getModel()).removeRow(finalListeForOneRepertoire.getSelectedRow());
//                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose unmatchItem");
            }
        });;
        opendestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose addActionListener");
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new File(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString()).delete();
                ((DefaultTableModel) finalListeForOneRepertoire.getModel()).removeRow(finalListeForOneRepertoire.getSelectedRow());
//                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose deleteItem");
            }
        });
        moveallItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose moveallItem");
            }
        });

        popupMenu.add(viewItem);
        popupMenu.add(unmatchItem);
        popupMenu.add(opendestItem);
        popupMenu.add(deleteItem);
        popupMenu.add(moveallItem);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = finalListeForOneRepertoire.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), finalListeForOneRepertoire));
                        if (rowAtPoint > -1) {
                            finalListeForOneRepertoire.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });

        ListeForOneRepertoire.setComponentPopupMenu(popupMenu);

        return ListeForOneRepertoire;

    }


    private static void ListeGroupNewPhoto(String tempsAdherence, SQLiteJDBCDriverConnection sql, String urltexte) {
        sql.execute("DROP TABLE IF EXISTS GroupNewPhoto;  " );

        sql.execute( "CREATE TEMPORARY TABLE GroupNewPhoto AS  " +
                "select a.* , '0' as numeroGroup  , strftime('%s', DATETIME( a.captureTimeOrig,\"+"+ tempsAdherence +"\")) as captureTimeAdherence " +
                "from NewPhoto a  " +
                "order by a.CameraModel , a.capturetime ; ");

        try {

            sql.select("SELECT distinct  " +
                    " * FROM GroupNewPhoto a  " +
                    ";");

            long captureTime =0;
            long captureTimeAdherence =  0;
            long numeroGroup = 0;
            long captureTimePrevious = 0;
            long captureTimeAdherencePrevious = 0;
            long numeroGroupPrevious = 0;
            while (sql.rs.next()) {

                captureTime = sql.rs.getLong("captureTime");
                captureTimeAdherence =sql.rs.getLong("captureTimeAdherence");

                if (captureTimePrevious < captureTime && captureTime < captureTimeAdherencePrevious ){
                    numeroGroup = numeroGroupPrevious;
                } else {
                    numeroGroup = numeroGroupPrevious;
                    numeroGroup++;
                }

                sql.execute("UPDATE GroupNewPhoto  " +
                        " set numeroGroup = \"" + numeroGroup + "\"  " +
                        " where absolutePath = \"" + sql.rs.getString("absolutePath") + "\"  " +
                        "and pathFromRoot = \"" + sql.rs.getString("pathFromRoot") + "\"  " +
                        "and originalFilename = \"" + sql.rs.getString("originalFilename") + "\"  " +
                        " ");

                captureTimePrevious=captureTime;
                captureTimeAdherencePrevious= captureTimeAdherence;
                numeroGroupPrevious=numeroGroup;

            }

            sql.select("SELECT distinct  " +
                    " * FROM GroupNewPhoto a  " +
                    ";");

            new ShowResultsetInJtable( sql, BIGTITLE_JTABLE,"group @new") .invoke(JFrame.EXIT_ON_CLOSE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    static void openFolder(String urltexte) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(urltexte) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    static void playElement(String urltexte) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(urltexte) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void ListeExifNew(String repertoirePhoto, SQLiteJDBCDriverConnection sql) {
        sql.select("SELECT AgLibraryFile.id_local, AgHarvestedExifMetadata.image, AgLibraryFile.originalFilename, Adobe_images.aspectRatioCache, " +
                "Adobe_images.bitDepth, Adobe_images.captureTime, Adobe_images.colorLabels, Adobe_images.fileFormat, Adobe_images.fileHeight, " +
                "Adobe_images.fileWidth, Adobe_images.orientation, AgHarvestedExifMetadata.aperture, AgInternedExifCameraModel.value, " +
                "AgHarvestedExifMetadata.dateDay, AgHarvestedExifMetadata.dateMonth, AgHarvestedExifMetadata.dateYear, AgHarvestedExifMetadata.flashFired, " +
                "AgHarvestedExifMetadata.focalLength, AgHarvestedExifMetadata.gpsLatitude, AgHarvestedExifMetadata.gpsLongitude, AgHarvestedExifMetadata.isoSpeedRating, " +
                "AgInternedExifLens.value, AgHarvestedExifMetadata.shutterSpeed, Adobe_AdditionalMetadata.xmp, Adobe_imageProperties.propertiesString, " +
                "AgLibraryRootFolder.absolutePath, AgLibraryFolder.pathFromRoot, [AgLibraryRootFolder].[absolutePath] & [AgLibraryFolder].[pathFromRoot] AS absolutePath_pathFromRoot " +
                "FROM (((((((AgLibraryFile " +
                "LEFT JOIN Adobe_images " +
                "ON AgLibraryFile.id_local = Adobe_images.rootFile) " +
                "LEFT JOIN AgHarvestedExifMetadata " +
                "ON Adobe_images.id_local = AgHarvestedExifMetadata.image) " +
                "LEFT JOIN Adobe_imageProperties " +
                "ON Adobe_images.id_local = Adobe_imageProperties.image) " +
                "LEFT JOIN Adobe_AdditionalMetadata " +
                "ON Adobe_images.id_local = Adobe_AdditionalMetadata.image) " +
                "LEFT JOIN AgInternedExifCameraModel " +
                "ON AgHarvestedExifMetadata.cameraModelRef = AgInternedExifCameraModel.id_local) " +
                "LEFT JOIN AgInternedExifLens " +
                "ON AgHarvestedExifMetadata.lensRef = AgInternedExifLens.id_local) " +
                "LEFT JOIN AgLibraryFolder " +
                "ON AgLibraryFile.folder = AgLibraryFolder.id_local) " +
                "LEFT JOIN AgLibraryRootFolder " +
                "ON AgLibraryFolder.rootFolder = AgLibraryRootFolder.id_local " +
                "Where  AgLibraryFolder.pathFromRoot like \"" + repertoirePhoto + "%\" " +
                "ORDER BY AgLibraryFile.id_local ;");

        try {
            new ShowResultsetInJtable(sql, BIGTITLE_JTABLE,"@new") .invoke(JFrame.EXIT_ON_CLOSE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
