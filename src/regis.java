import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.UnknownHostException;

public class regis extends JFrame{
    private JPanel mainRegis;
    private JLabel Username;
    private JLabel Password;
    private JLabel ConPassword;
    private JLabel Name;
    private JPanel picJPanel;
    private JPanel buttonJPanel;
    private JButton exit;
    private JButton enterButton;
    private JButton attachButton;
    private JTextPane picpush;
    private JTextField username1;
    private JTextField password1;
    private JTextField conpassword1;
    private JTextField name1;
    private JTextField email1;

    public static byte[] LoadImage(String filePath) throws Exception {
        File file = new File(filePath);
        int size = (int)file.length();
        byte[] buffer = new byte[size];
        FileInputStream in = new FileInputStream(file);
        in.read(buffer);
        in.close();
        return buffer;
    }



    public JPanel getmainRegis(){
        return  mainRegis;
    }
    public regis()  {
        JFrame frame = new JFrame();
        frame.setContentPane(this.getmainRegis());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setTitle("OXGame");
        frame.setVisible(true);




        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser choser =new JFileChooser();
                choser.showOpenDialog(null);
                File f =choser.getSelectedFile();
                String filename = f.getAbsolutePath();
                picpush.setText(filename);
                       }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              frame.dispose();
            }
        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MongoClientURI uri  = new MongoClientURI("mongodb://user01:user01@ds247852.mlab.com:47852/oxgame");
                    MongoClient client = new MongoClient(uri);
                    DB  db =  client.getDB("oxgame");
                    DBCollection Players = db.getCollection("Player");



                    String username = username1.getText().toString();
                    String username2=username;
                    String password = password1.getText().toString();
                    String password2=password;
                    String cPassword = conpassword1.getText().toString();
                    String email = email1.getText().toString();
                    String email2=email;
                    String name = name1.getText().toString();
                    String name2=name;
                    String pic = picpush.getText().toString();


                    byte[] imageBytes = LoadImage(pic);
                    GridFS fs = new GridFS( db );
                    GridFSInputFile in = fs.createFile( imageBytes );
                    in.save();
                    String pic2 = in.getId().toString();


                    DBObject dfName = Players.findOne(new BasicDBObject("Name",name));
                    DBObject dfUsername = Players.findOne(new BasicDBObject("Username",username));
                    DBObject dfEmail = Players.findOne(new BasicDBObject("Email",email));

                    if(dfUsername!=null){
                        JOptionPane.showMessageDialog(null,"ชื่อผู้ใช้ มีผู้ใช้แล้ว!!","",JOptionPane.INFORMATION_MESSAGE);
                    } else if(dfEmail!=null){
                        JOptionPane.showMessageDialog(null,"อีเมล์ มีผู้ใช้แล้ว!!","",JOptionPane.INFORMATION_MESSAGE);
                    }else if(dfName!=null){
                        JOptionPane.showMessageDialog(null,"ชื่อในเกมส์ มีผู้ใช้แล้ว!!","",JOptionPane.INFORMATION_MESSAGE);
                    }else if(password.equals(password2)){
                        JOptionPane.showMessageDialog(null,"ใส่รหัสผ่านไม่ตรงกัน!! กรุณาใส่รหัสผ่านให้ตรงกัน!!","",JOptionPane.INFORMATION_MESSAGE);
                    } else if(!username2.equals("")&&!password2.equals("")&&!cPassword.equals("")&&!email2.equals("")&&!name2.equals("")&&!email2.equals("")&&!pic2.equals("")){

                        BasicDBObject user = new BasicDBObject();
                        user.put("PlayerID", 0);
                        user.put("Username", username2);
                        user.put("Password", password2);
                        user.put("E-mail", email2);
                        user.put("Name", name2);
                        user.put("NumberOfPlays", 0);
                        user.put("Win", 0);
                        user.put("Draw", 0);
                        user.put("Lost", 0);
                        user.put("Level", 0);
                        user.put("Pic", pic2);

                        Players.insert(user);
                        JOptionPane.showMessageDialog(null,"สมัครสมาชิกเรียบร้อยแล้ว^^","",JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();

                    }else{
                        JOptionPane.showMessageDialog(null,"ข้อมูลไม่ครบ!! กรุณาใส่ข้อมูลให้ครบ!!","",JOptionPane.INFORMATION_MESSAGE);

                    }

                } catch (UnknownHostException e1) {
                    JOptionPane.showMessageDialog(null,"ชื่อผู้ใช้ หรือ อีเมล์ หรือ ชื่อในเกมส์ มีผู้ใช้แล้ว!!","",JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
    }





}
