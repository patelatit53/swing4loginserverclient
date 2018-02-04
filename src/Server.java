import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server Signing ON");
        ServerSocket ss = new ServerSocket(9081);
        for (int i = 0; i < 10; i++) {
            Socket soc = ss.accept();
            Login l = new Login(soc);
            l.start();
        }
        ss.close();
        System.out.println("Server Singing OFF");
    }
}


class Login extends Thread {

    private static Connection conn = null;
    private static Statement st;
    private Socket soc;

    public static Properties p = new Properties();

    static {
        try {
            FileReader fr = new FileReader("user.properties");
            p.load(fr);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Login(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        try {
            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(
                            soc.getInputStream()
                    )
            );
            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    soc.getOutputStream()
                            )
                    ), true
            );
            String name = nis.readLine();
            String pwd = nis.readLine();
            boolean flag = true;
            while (flag) {
                if (validate(name, pwd)) {
                    nos.println("true");
                    flag = false;
                } else {
                    nos.println("false");
                    name = nis.readLine();
                    pwd = nis.readLine();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    boolean validate(String name, String pwd) throws Exception {
        Set<Object> keys = p.keySet();
        //Displaying keys
        System.out.println("keys = " + keys.toString() + "\n");
        if (keys.contains(name)) {
            String password = p.getProperty(name);
            if (password.equals(pwd)) {
                return true;
            }
        }
        return false;
    }
}

