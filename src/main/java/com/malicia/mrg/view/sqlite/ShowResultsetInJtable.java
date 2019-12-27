package com.malicia.mrg.view.sqlite;

import static com.malicia.mrg.model.photo.exifreader.ExifReader.printOriDateTime;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class ShowResultsetInJtable {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    private final String bigTitle;
    private final String title;

    static final int FIT  = 0;
    static final int FILL = 1;

    public ShowResultsetInJtable(String bigTitle, String title) {
        this.bigTitle = bigTitle;
        this.title = title;
    }

    public JTable invoke(int exitMode , ResultSet rs ) throws SQLException {


        JFrame frame = new JFrame(bigTitle);

        JTable table = new JTable(buildTableModel(rs));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        for(int i=0; i< table.getColumnCount(); i++){
            if (table.getColumnName(i).compareTo("loadimage") == 0){
                table.getColumnModel().getColumn(i).setCellRenderer(new ImageCellRenderer());
            }
            if (table.getColumnName(i).compareTo("exif") == 0){
                table.getColumnModel().getColumn(i).setCellRenderer(new ExifCellRenderer());
            }
        }

        JLabel lblHeading = new JLabel(title);
        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));

        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);

        frame.setDefaultCloseOperation(exitMode);
        frame.setSize(1024, 500);
        frame.setVisible(true);

        return table;
    }

    public static final class ImageCellRenderer extends DefaultTableCellRenderer {

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
                    BufferedImage imgtmp = ImageIO.read(f);
                    if (imgtmp == null) {
                        label.setIcon(null);
                        table.setRowHeight(row, table.getRowHeight());
                    } else {
                        ImageIcon icon = new ImageIcon(getScaledImages(imgtmp, FIT));

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
                LOGGER.severe( e.getMessage());
            }



            return component;
        }


        private static BufferedImage getScaledImages(BufferedImage in, int type)
        {
            final int WIDTH = 240;
            final int HEIGHT = 240;


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

    }

    public static final class ExifCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {

            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            JLabel label = (JLabel)component;
            String cheminImage = String.valueOf(value);

            label.setText(printOriDateTime(cheminImage));

            return component;
        }
    }



    private DefaultTableModel buildTableModel(ResultSet rs )
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {

                vector.add(rs.getObject(columnIndex));

            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }



}



