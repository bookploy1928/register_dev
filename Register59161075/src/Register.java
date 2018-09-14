import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import Check.OpenFileFilter;
import Check.ImagePanel;


public class Register {
    private JTextField usertext;
    private JTextField passtext;
    private JTextField re_passtext;
    private JButton cancelButton;
    private JButton registerButton;
    private JPanel ImagePanel;
    private JButton browseButton;



    public Register() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usertext.getText().trim().length() > 0 && passtext.getText().trim().length() > 0 && re_passtext.getText().trim().length() > 0) {
                    try {
                        MongoClientURI uri = new MongoClientURI("mongodb://gameox:q1azwsxedc3@ds251902.mlab.com:51902/game_ox");
                        MongoClient client = new MongoClient(uri);
                        System.out.println("1");
                        DB db = client.getDB("game_ox");
                        try {
                            db.getCollection("username");
                        } catch (Exception exp) {
                            db.createCollection("username", null);
                        }
                        DBCollection collection = db.getCollection("username");
                        DBObject dfId = collection.findOne(new BasicDBObject("_id", Tusername.getText()));
                        DBObject dfEmail = collection.findOne(new BasicDBObject("Email", Temail.getText()));
                        BasicDBObject document = new BasicDBObject();
                        document.put("_id", usertext.getText());
                        document.put("Password", passtext.getText().toString());
                        String newFileName = Tusername.getText();
                        byte[] imageBytes = LoadImage(imagelink);
                        GridFS gfsPhoto = new GridFS(db, "photo");
                        GridFSInputFile gfsFile = gfsPhoto.createFile(imageBytes);
                        gfsFile.setFilename(newFileName);
                        gfsFile.save();
                        DBCursor cursor = gfsPhoto.getFileList();
                        while (cursor.hasNext()) {
                            System.out.println(cursor.next());
                        }

                        collection.insert(document);
                        JOptionPane.showMessageDialog(null, "สมัครสมาชิกเรียบร้อยแล้ว^^", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch(Exception ex) {

                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new OpenFileFilter("jpg","Photo in JPG format"));
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showOpenDialog(Register.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    try {
                        BufferedImage buffimg = ImageIO.read(f);
                        ImagePanel.setImage(buffimg);
                    } catch (IOException eX) {
                        eX.printStackTrace();
                    }
                }
            }
        });
    }
}
