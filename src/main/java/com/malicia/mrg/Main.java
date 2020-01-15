package com.malicia.mrg;

import com.malicia.mrg.model.PropertiesParameters;
import com.malicia.mrg.model.RequeteSql;
import com.malicia.mrg.model.sqlite.SQLiteJDBCDriverConnection;
import com.malicia.mrg.view.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;



public class Main {

    private static final  Logger LOGGER;
    private static String absolutePathFirst;
    private static JFrame f2;
    private static app napp;


    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static final String BIGTITLE_JTABLE = "auto match (New 2 repertoire photo) from lrcat";

    public static void main(String[] args) {

        initializeMe();

        GenerateGui();

//        moveNewToGrpPhotos();
//
//        delEmptyDirectory();

    }

    private static ImageIcon getImageiconResized(URL imagesJpg) {
        LOGGER.finest(imagesJpg.toString());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //this is your screen size
        ImageIcon imageIcon = new ImageIcon(imagesJpg); //imports the image
        imageIcon.getImage().flush();
        int fact = imageIcon.getIconHeight() / (screenSize.height / 10);
        ImageIcon imageIcon2 = null;
        if (fact > 0) {
            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(imageIcon.getIconWidth() / fact, imageIcon.getIconHeight() / fact, Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon2 = new ImageIcon(newimg);
            imageIcon2.getImage().flush();
        }
        return imageIcon2;
    }

    private static void GenerateGui() {

//        napp.setImageOne(new JLabel(getImageiconResized("D:\\50_Phototheque\\!!Legacy\\&Antony Travaux\\&Antony Travaux 2008\\2008.06.01_20.14.02____001-3-3-1.png")));
//        f2.setContentPane(napp.getPanelMain());
//
        f2.setContentPane(napp.getJpane());
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f2.pack();
        f2.setVisible(true);



        //JLabel nl = new JLabel(getImageiconResized("two.png"));
        JLabel nl = new JLabel("two.png");
        nl.setVisible(true);

        JLabel n2 = napp.getLb1();
        n2.setText("text 2");
        n2.setIcon(getImageiconResized(GrpPhoto.class.getClassLoader().getResource("two.png")));

//        n2.setVisible(false);

//        napp.setLb1(nl);
//        napp.lb1=nl;
        f2.pack();
        f2.revalidate();   // revalidate all the frame components
        f2.repaint();



//        f2.add(nl);
//
//        JLabel lblStanFunc = new JLabel("Die Funktionssyntax lautet:");
//        lblStanFunc.setBounds(800, 40, 300, 30);
//        lblStanFunc.setVisible(true);
//        f2.add(lblStanFunc);
    }

    private static void delEmptyDirectory() {
        if (!PropertiesParameters.getDryRun()) {
            File directory = new File(absolutePathFirst + PropertiesParameters.getRepertoireNew() + "/");
            deleteEmptyDir(directory);
            int nbdel = 0;
            do {
                nbdel = RequeteSql.sqlDeleteRepertory();
                LOGGER.fine("logical delete:" + String.format("%04d", nbdel));
            }
            while (nbdel > 0);
        }
    }

    private static void initializeMe() {
        MyLogger.setup();

        LOGGER.info("Start");

        PropertiesParameters.initPropertiesParameters();

        SQLiteJDBCDriverConnection.connect(PropertiesParameters.getCatalogLrcat());

        absolutePathFirst = RequeteSql.getabsolutePathFirst();

        napp = new app();
        f2 = new JFrame("Selection");
    }

    private static void moveNewToGrpPhotos() {
        RequeteSql.sqlCombineAllGrouplessInGroupByPlageAdherance(PropertiesParameters.getPasRepertoirePhoto(), PropertiesParameters.getTempsAdherence(), PropertiesParameters.getRepertoireNew());

        List<GrpPhoto> groupDePhoto = regroupeByNewGroup(PropertiesParameters.getKidsModelList());
        List<GrpPhoto> groupDePhotoExecpt = exceptNewGroup(groupDePhoto, PropertiesParameters.getKidsModelList());
        if (movetoNewGroup(true, groupDePhotoExecpt)) {
            movetoNewGroup(PropertiesParameters.getDryRun(), groupDePhotoExecpt);
//            movetoNewGroup(false,groupDePhoto);
        } else {
            LOGGER.info("movetoNewGroup KO, nothig nmove");
        }
    }

    private static List<GrpPhoto> exceptNewGroup(List<GrpPhoto> groupDePhoto, List<String> KidsModelList) {
        List<GrpPhoto> excptgdp = new ArrayList();
        GrpPhoto Bazaz = new GrpPhoto( PropertiesParameters.getBazar(), absolutePathFirst, PropertiesParameters.getRepertoireNew()+ "/");
        GrpPhoto NoDate = new GrpPhoto("@NoDate", absolutePathFirst, PropertiesParameters.getRepertoireNew()+ "/");
        GrpPhoto Kidz = new GrpPhoto("@Kidz", absolutePathFirst, PropertiesParameters.getRepertoireNew()+ "/");


        Iterator<GrpPhoto> groupDePhotoIterator = groupDePhoto.iterator();

        while (groupDePhotoIterator.hasNext()) {
            GrpPhoto gdp = groupDePhotoIterator.next();
            if (KidsModelList.contains(gdp.getCameraModelGrp())) {
                Kidz.add(gdp.getEle());
            } else {
                if (gdp.getnbele() <= 5) {
                    Bazaz.add(gdp.getEle());
                } else {
                    if (gdp.dateNull()){
                        NoDate.add(gdp.getEle());
                    } else {
                        excptgdp.add(gdp) ;
                    }
                }
            }
        }
        excptgdp.add(Bazaz);
        excptgdp.add(NoDate);
        excptgdp.add(Kidz);
        return excptgdp;
    }

    private static  List<GrpPhoto> regroupeByNewGroup(List<String> kidsModelList) {

//            constitution des groupe

        ResultSet rs = RequeteSql.sqlGroupGrouplessByPlageAdherance(PropertiesParameters.getTempsAdherence());

        GrpPhoto gp = new GrpPhoto();

        List<GrpPhoto> ggp = new ArrayList();

        try {
            boolean first = true;

            while (rs.next()) {


                // Now we can fetch the data by column name, save and use them!
                String CameraModel = rs.getString("CameraModel");
                if (!kidsModelList.contains(CameraModel)){
                    CameraModel = " ";
                };
                long captureTime = rs.getLong("captureTime");
                long mint = rs.getLong("mint");
                long maxt = rs.getLong("maxt");
                String src = rs.getString("src");
                String absolutePath = rs.getString("absolutePath");

                if (first) {
                    gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, PropertiesParameters.getRepertoireNew() + "/");
                } else {
                    if (!gp.add(CameraModel, captureTime, mint, maxt, src)) {
                        ggp.add(gp);
                        gp = new GrpPhoto();
                        gp.addfirst(CameraModel, captureTime, mint, maxt, src, absolutePath, PropertiesParameters.getRepertoireNew() + "/");
                    }
                }
                //}
//                LOGGER.info("\tCameraModel: " + CameraModel +
//                        ", captureTime: " + captureTime +
//                        ", src : " + src);


                first = false;
            }
            ggp.add(gp);


//            LOGGER.info("Nb row lues=> " + nbrow);
//            LOGGER.info("Nb row grp => " + nbele);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ggp;
    }

    private static boolean movetoNewGroup(boolean dryRun, List<GrpPhoto> ggp) {
//       Execution du deplacement

        LOGGER.fine("Printing result...");
        int nbele = 0;

        Hashtable codeRetourAction = new Hashtable();

        int nbrow = 0;
        for (int i = 0; i < ggp.size(); i++) {
            GrpPhoto gptemp = ggp.get(i);
            nbrow += gptemp.getnbele();

            Hashtable hashRet = gptemp.groupAndMouveEle(dryRun);
            LOGGER.finer("GrpPhoto:"+gptemp.toString());
            LOGGER.finer(" hashRet:"+hashRet.toString());
            mergeHashtable (codeRetourAction ,hashRet);
        }



        LOGGER.info((dryRun?"dryRun =>":"") +  codeRetourAction.toString());
        nbele = (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DO) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_SAME) + (int) codeRetourAction.get(GrpPhoto.OK_MOVE_DRY_RUN);
        return (nbrow == nbele);
    }


    private static void mergeHashtable(Hashtable dReturnEle, Hashtable groupAndMouveEle) {
        Set<String> keys = groupAndMouveEle.keySet();
        for(String key: keys){
            if (key.compareTo(GrpPhoto.LISTE_ERREUR )!=0) {
                if (dReturnEle.containsKey(key)) {
                    int val = (int) dReturnEle.get(key) + (int) groupAndMouveEle.get(key);
                    dReturnEle.put(key, val);
                } else {
                    dReturnEle.put(key, groupAndMouveEle.get(key));
                }
//            System.out.println("Value of "+key+" is: "+groupAndMouveEle.get(key));
            }
        }

    }

    public static boolean deleteEmptyDir(File dir) {
        boolean returnVal = false;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            boolean success = true;
            for (int i = 0; i < children.length; i++){
                success &= deleteEmptyDir(new File(dir, children[i]));
            }

            if (success) {
                // The directory is now empty directory free so delete it
                LOGGER.fine("delete repertory:" + dir.toString());
                returnVal = dir.delete();

            }

        } else {
            returnVal = false;
        }
        return returnVal;
    }


    public static void changeimage() {
        JLabel n2 = napp.getLb1();
        JLabel h1 = napp.getHidden1() ;
        if (n2.getText().compareTo("text 2")==0){
            n2.setText("text 3");
            n2.setIcon(getImageiconResized(GrpPhoto.class.getClassLoader().getResource("one.png")));
            h1.setText("hidden1");
        }else {
            n2.setText("text 2");
            n2.setIcon(getImageiconResized(GrpPhoto.class.getClassLoader().getResource("two.png")));
            h1.setText("hidden2");
        }

        f2.pack();
        f2.revalidate();   // revalidate all the frame components
        f2.repaint();

    }

    public static void popUpName() {
        JLabel h1 = napp.getHidden1() ;
        JOptionPane.showMessageDialog(f2,h1.getText());
    }
}
