package com.malicia.mrg.view;

import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.sqlite.ShowResultsetInJtable;
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
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class CreateJtable {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private CreateJtable() {

        throw new IllegalStateException("Utility class");
    }

    public static JTable createJTableSelectionRepertoire(final String BIGTITLE_JTABLE, final ResultSet rs) {



        JTable listeSelectionRepertoire = null;
        try {
            listeSelectionRepertoire = new ShowResultsetInJtable( BIGTITLE_JTABLE, "Selection Repertoire").invoke(JFrame.EXIT_ON_CLOSE,rs);
        } catch (SQLException e) {
            LOGGER.severe( e.getMessage());
        }

        final JTable finalListeSelectionRepertoire = listeSelectionRepertoire;
        listeSelectionRepertoire.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && finalListeSelectionRepertoire.getSelectedRow() != -1) {

                    listeNewFromOneRepertoire(finalListeSelectionRepertoire.getValueAt(finalListeSelectionRepertoire.getSelectedRow(), 0).toString());
                    createJtableForOneRepertoire(  BIGTITLE_JTABLE, rs);

                }
                LOGGER.finer(event.toString());
                LOGGER.finer(finalListeSelectionRepertoire.getValueAt(finalListeSelectionRepertoire.getSelectedRow(), 0).toString());
            }
        });

        return listeSelectionRepertoire;

    }

    private static JTable createJtableForOneRepertoire(String bigtitleJtable , ResultSet rs) {

        JTable listeForOneRepertoire = null;
        try {
            listeForOneRepertoire = new ShowResultsetInJtable(bigtitleJtable,"Liste @new -> !repertoire") .invoke(WindowConstants.DISPOSE_ON_CLOSE , rs );
        } catch (SQLException e) {
            LOGGER.severe( e.getMessage());
        }

        final JTable finalListeForOneRepertoire = listeForOneRepertoire;

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem viewItem = new JMenuItem("View");
        JMenuItem unmatchItem = new JMenuItem("UnMatch");
        JMenuItem opendestItem = new JMenuItem("Open Destination");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem moveItem = new JMenuItem("Move");
        JMenuItem moveallItem = new JMenuItem("-=*Move All*=-");

        viewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playElement(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString());
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose viewItem");
            }
        });
        unmatchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultTableModel) finalListeForOneRepertoire.getModel()).removeRow(finalListeForOneRepertoire.getSelectedRow());
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose unmatchItem");
            }
        });
        opendestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose addActionListener");
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!new File(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString()).delete()) {
                    LOGGER.info("deleté =" + finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString() );
                }
                ((DefaultTableModel) finalListeForOneRepertoire.getModel()).removeRow(finalListeForOneRepertoire.getSelectedRow());
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose deleteItem");
            }
        });
        moveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File afile =new File(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 0).toString());
                if(afile.renameTo(new File(finalListeForOneRepertoire.getValueAt( finalListeForOneRepertoire.getSelectedRow(), 2).toString() + afile.getName()))){
                    JOptionPane.showMessageDialog(finalListeForOneRepertoire, "File is moved successful!");
                }else{
                    JOptionPane.showMessageDialog(finalListeForOneRepertoire, "File is failed to move!");
                }
                ((DefaultTableModel) finalListeForOneRepertoire.getModel()).removeRow(finalListeForOneRepertoire.getSelectedRow());
                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose moveItem");
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
        popupMenu.add(moveItem);
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

        listeForOneRepertoire.setComponentPopupMenu(popupMenu);

        return listeForOneRepertoire;

    }

    private static void listeNewFromOneRepertoire(String dest) {
        SQLiteJDBCDriverConnection.select("SELECT distinct  " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename , " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename as loadimage , " +
                " a.absolutePath || a.pathFromRoot as dest , " +
                " a.absolutePath || b.pathFromRoot || b.originalFilename as exif " +
                "FROM Repertory a  " +
                "inner join NewPhoto b  " +
                "on b.captureTime between a.mint and a.maxt " +
                "WHERE a.absolutePath || a.pathFromRoot = \"" + dest + "\" " +
                "order by  a.absolutePath , a.pathFromRoot " +
                " ;");
    }

    private static void listeGroupNewPhoto(String tempsAdherence, String bigtitleJtable) {
        SQLiteJDBCDriverConnection.execute("DROP TABLE IF EXISTS GroupNewPhoto;  " );

        SQLiteJDBCDriverConnection.execute( "CREATE TEMPORARY TABLE GroupNewPhoto AS  " +
                "select a.* , '0' as numeroGroup  , strftime('%s', DATETIME( a.captureTimeOrig,\"+"+ tempsAdherence +"\")) as captureTimeAdherence " +
                "from NewPhoto a  " +
                "order by a.CameraModel , a.capturetime ; ");

        try {

            ResultSet rs = SQLiteJDBCDriverConnection.select("SELECT distinct  " +
                    " * FROM GroupNewPhoto a  " +
                    ";");

            long captureTime =0;
            long captureTimeAdherence =  0;
            long numeroGroup = 0;
            long captureTimePrevious = 0;
            long captureTimeAdherencePrevious = 0;
            long numeroGroupPrevious = 0;
            while (rs.next()) {

                captureTime = rs.getLong("captureTime");
                captureTimeAdherence =rs.getLong("captureTimeAdherence");

                if (captureTimePrevious < captureTime && captureTime < captureTimeAdherencePrevious ){
                    numeroGroup = numeroGroupPrevious;
                } else {
                    numeroGroup = numeroGroupPrevious;
                    numeroGroup++;
                }

                SQLiteJDBCDriverConnection.execute("UPDATE GroupNewPhoto  " +
                        " set numeroGroup = \"" + numeroGroup + "\"  " +
                        " where absolutePath = \"" + rs.getString("absolutePath") + "\"  " +
                        "and pathFromRoot = \"" + rs.getString("pathFromRoot") + "\"  " +
                        "and originalFilename = \"" + rs.getString("originalFilename") + "\"  " +
                        " ");

                captureTimePrevious=captureTime;
                captureTimeAdherencePrevious= captureTimeAdherence;
                numeroGroupPrevious=numeroGroup;

            }

            SQLiteJDBCDriverConnection.select("SELECT distinct  " +
                    " * FROM GroupNewPhoto a  " +
                    ";");

            new ShowResultsetInJtable( bigtitleJtable,"group @new") .invoke(JFrame.EXIT_ON_CLOSE , rs);

        } catch (SQLException e) {
            LOGGER.severe( e.getMessage());
        }
    }

    @FXML
    static void playElement(String urltexte) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(urltexte) );
        } catch (IOException e) {
            LOGGER.severe( e.getMessage());
        }
    }
}
