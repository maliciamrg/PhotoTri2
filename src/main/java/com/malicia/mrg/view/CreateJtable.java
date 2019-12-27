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

public class CreateJtable {
    public static JTable CreateJTableSelectionRepertoire(final String BIGTITLE_JTABLE, final ResultSet rs) {



        JTable ListeSelectionRepertoire = null;
        try {
            ListeSelectionRepertoire = new ShowResultsetInJtable( BIGTITLE_JTABLE, "Selection Repertoire").invoke(JFrame.EXIT_ON_CLOSE,rs);
        } catch (SQLException e) {
            logger.log("context" , e);
        }

        final JTable finalListeSelectionRepertoire = ListeSelectionRepertoire;
        ListeSelectionRepertoire.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && finalListeSelectionRepertoire.getSelectedRow() != -1) {

                    ListeNewFromOneRepertoire(finalListeSelectionRepertoire.getValueAt(finalListeSelectionRepertoire.getSelectedRow(), 0).toString());
                    JTable tableForOneRepertoire = CreateJtableForOneRepertoire(  BIGTITLE_JTABLE, rs);

                }
//                    System.out.println(event.toString());
//                    System.out.println(ListeSelectionRepertoire.getValueAt(ListeSelectionRepertoire.getSelectedRow(), 0).toString());
            }
        });

        return ListeSelectionRepertoire;

    }

    private static JTable CreateJtableForOneRepertoire(String BIGTITLE_JTABLE , ResultSet rs) {

        JTable ListeForOneRepertoire = null;
        try {
            ListeForOneRepertoire = new ShowResultsetInJtable(BIGTITLE_JTABLE,"Liste @new -> !repertoire") .invoke(JFrame.DISPOSE_ON_CLOSE , rs );
        } catch (SQLException e) {
            logger.log("context" , e);
        }

        final JTable finalListeForOneRepertoire = ListeForOneRepertoire;

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
//                JOptionPane.showMessageDialog(finalListeForOneRepertoire, "Right-click performed on table and choose moveItem");
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

        ListeForOneRepertoire.setComponentPopupMenu(popupMenu);

        return ListeForOneRepertoire;

    }

    private static void ListeNewFromOneRepertoire(String Dest) {
        SQLiteJDBCDriverConnection.select("SELECT distinct  " +
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
    }

    private static void ListeGroupNewPhoto(String tempsAdherence, String urltexte, String BIGTITLE_JTABLE) {
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

            new ShowResultsetInJtable( BIGTITLE_JTABLE,"group @new") .invoke(JFrame.EXIT_ON_CLOSE , rs);

        } catch (SQLException e) {
            logger.log("context" , e);
        }
    }

    @FXML
    static void openFolder(String urltexte) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(urltexte) );
        } catch (IOException e) {
            logger.log("context" , e);
        }
    }

    @FXML
    static void playElement(String urltexte) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(urltexte) );
        } catch (IOException e) {
            logger.log("context" , e);
        }
    }
}
