package com.malicia.mrg.view.sqlite;

import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import static com.malicia.mrg.model.photo.exifreader.ExifReader.printImageTags;


public class ShowResultsetInJtable {
    private final String bigTitle;
    private final String title;
    private SQLiteJDBCDriverConnection sql;

    final static int
            FIT  = 0,
            FILL = 1;

    public ShowResultsetInJtable(SQLiteJDBCDriverConnection sql, String bigTitle, String title) {
        this.sql = sql;
        this.bigTitle = bigTitle;
        this.title = title;
    }

    public JTable invoke(int ExitMode) throws SQLException {


        JFrame frame = new JFrame(bigTitle);

        JTable table = new JTable(buildTableModel(sql.rs));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        //table.setDefaultRenderer(BufferedImage.class, new ImageRenderer());
        for(int i=0; i< table.getColumnCount(); i++){
            if (table.getColumnName(i).compareTo("loadimage") == 0){
                table.getColumnModel().getColumn(i).setCellRenderer(new ImageCellRenderer());
            }
            if (table.getColumnName(i).compareTo("exif") == 0){
                table.getColumnModel().getColumn(i).setCellRenderer(new ExifCellRenderer());
            }
        }

//        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table.getDefaultRenderer(String.class);
//        renderer.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblHeading = new JLabel(title);
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));

        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);

        frame.setDefaultCloseOperation(ExitMode);
        frame.setSize(1024, 500);
        frame.setVisible(true);

        return table;
    }

    public final static class ImageCellRenderer extends DefaultTableCellRenderer {

        private BufferedImage Imgtmp;

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {

            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            JLabel label = (JLabel)component;
            String cheminImage = String.valueOf(value);

            try {
                File f = new File(cheminImage);
                if(f.exists() && !f.isDirectory()) {
                    Imgtmp = ImageIO.read(f);
                    if (Imgtmp == null) {
                        label.setIcon(null);
                        table.setRowHeight(row, table.getRowHeight());
                    } else {
                        ImageIcon icon = new ImageIcon(getScaledImages(Imgtmp, FIT));

                        if (icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                            label.setIcon(icon);
                            table.setRowHeight(row, icon.getIconHeight());
                        } else {
                            label.setIcon(null);
                            table.setRowHeight(row, table.getRowHeight());
                        }
                        label.setText(""); // on efface le texte
                    }
                } else {
                    label.setText(""); // on efface le texte
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            return component;
        }
    }

    public final static class ExifCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {

            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            JLabel label = (JLabel)component;
            String cheminImage = String.valueOf(value);

            label.setText(printImageTags(cheminImage));

            return component;
        }
    }

    private static BufferedImage getScaledImages(BufferedImage in, int type)
    {
        int WIDTH = 240;
        int HEIGHT = 240;


        BufferedImage out = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = out.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        double width  = in.getWidth();
        double height = in.getHeight();
        double xScale = WIDTH  / width;
        double yScale = HEIGHT / height;
        double scale = 1.0;
        switch(type)
        {
            case FIT:
                scale = Math.min(xScale, yScale);  // scale to fit
                break;
            case FILL:
                scale = Math.max(xScale, yScale);  // scale to fill
        }
        double x = (WIDTH - width * scale)/2;
        double y = (HEIGHT - height * scale)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(scale, scale);
        g2.drawRenderedImage(in, at);
        g2.dispose();

        return out;
    }

    private DefaultTableModel buildTableModel(ResultSet rs )
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {

                vector.add(rs.getObject(columnIndex));

            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }



}

//class ImageRenderer extends DefaultTableCellRenderer
//{
//    public Component getTableCellRendererComponent(JTable table,
//                                                   Object value,
//                                                   boolean isSelected,
//                                                   boolean hasFocus,
//                                                   int row, int column)
//    {
//        super.getTableCellRendererComponent(table, value, isSelected,
//                hasFocus, row, column);
//        setIcon(new ImageIcon((BufferedImage)value));
//        setHorizontalAlignment(JLabel.CENTER);
//        setText("");
//        return this;
//    }
//}


