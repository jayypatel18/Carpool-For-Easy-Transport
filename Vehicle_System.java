package vss;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.mail.*;
import java.lang.Math;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;
import java.text.*;

public class Vehicle_System {
    static void driver_login() {
        try {
            Scanner ss=new Scanner(System.in);
            System.out.print("Enter Driver ID : ");
            String eid=ss.nextLine();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate today1 = LocalDate.now();
            String tomorrow1 = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
            Statement stmt= con.createStatement();
            ResultSet rw=stmt.executeQuery("select count(did) from cars where did='"+eid+"'");
            rw.next();
            int rs1=rw.getInt(1);
            if(rs1==1) {
                ResultSet ife=stmt.executeQuery("select count(*) from booking where date='"+tomorrow1+"' and reg_no=(select reg_no from cars where did='"+eid+"')");
                ife.next();
                int ife1=ife.getInt(1);
                if(ife1>0) {
                    ResultSet nam=stmt.executeQuery("select driver_n from cars where did='"+eid+"'");
                    nam.next();
                    String ename=nam.getString(1);
                    System.out.println("************************");
                    System.out.println("Hello, "+ename);
                    System.out.println("************************");
                    System.out.println("Your Today's Trip Customers are : ");
                    System.out.println("************************");
                    System.out.println("Pick-Up : ");
                    System.out.println("************************");
                    ResultSet qu=stmt.executeQuery("select emp.name,booking.pick_up,booking.date,booking.time,emp.phone from emp,booking where booking.type='p' "+
                            "and booking.date='"+tomorrow1+"' and emp.eid=booking.eid and booking.reg_no=(select reg_no from cars where did='"+eid+"')");
                    while(qu.next()) {
                        System.out.println("Name : "+qu.getString("name"));
                        System.out.println("Mobile No. : "+qu.getString("phone"));
                        System.out.println("Pick-Up From : "+qu.getString("pick_up"));
                        System.out.println("Date : "+qu.getString("date"));
                        System.out.println("Time : "+qu.getString("time"));
                        System.out.println("************************");
                    }
                    System.out.println("Drop : ");
                    System.out.println("************************");
                    ResultSet qu1=stmt.executeQuery("select emp.name,booking.pick_up,booking.date,booking.time,emp.phone from emp,booking where booking.type='d' "
                            + "and booking.date='"+tomorrow1+"' and emp.eid=booking.eid and booking.reg_no=(select reg_no from cars where did='"+eid+"')");
                    while(qu1.next()) {
                        System.out.println("Name : "+qu1.getString("name"));
                        System.out.println("Mobile No. : "+qu1.getString("phone"));
                        System.out.println("Drop At : "+qu1.getString("pick_up"));
                        System.out.println("Date : "+qu1.getString("date"));
                        System.out.println("Time : "+qu1.getString("time"));
                        System.out.println("************************");
                    }
                }
                else {
                    System.out.println("************************");
                    System.out.println("Wohooo! You Do Not Have Any Customers Today.");
                    System.out.println("************************");
                }
            }
            else {
                System.out.println("Driver ID Not Found!!!");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    static void cancel(List l,Dictionary d,String t_date) {
        try{
            Scanner ss=new Scanner(System.in);
            System.out.print("Enter Employee ID : ");
            String eid=ss.nextLine();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate today1 = LocalDate.now();
            String tomorrow1 = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
            Statement stmt= con.createStatement();
            if(l.contains(eid)) {
                ResultSet nam=stmt.executeQuery("select name from emp where eid='"+eid+"'");
                nam.next();
                String ename=nam.getString(1);
                System.out.println();
                System.out.println("Welcome, "+ename);

                ResultSet cr=stmt.executeQuery("select count(*) from booking where eid='"+eid+"' and date='"+t_date+"'");
                cr.next();
                int pre=cr.getInt(1);
                if(pre==0) {
                    System.out.println("****************************");
                    System.out.println("Oops! You Don't have any bookings.");
                    System.out.println("****************************");
                }
                else {
                    ResultSet s12=stmt.executeQuery("select * from booking where eid='"+eid+"' and date='"+t_date+"' and type='p'");
                    while(s12.next()) {
                        System.out.println("****************************");
                        System.out.println("Pick-Up and Drop : ");
                        System.out.println("****************************");
                        System.out.println("Reg no : "+s12.getString("reg_no"));
                        System.out.println("Origin : "+s12.getString("origin"));
                        System.out.println("Pick Up : "+s12.getString("pick_up"));
                        System.out.println("Date : "+s12.getString("date"));
                        System.out.println("Pick-Up Time : "+s12.getString("time"));

                    }
                    ResultSet s13=stmt.executeQuery("select * from booking where eid='"+eid+"' and date='"+t_date+"' and type='d'");
                    while(s13.next()) {
                        System.out.println("Drop Time : "+s13.getString("time"));
                        System.out.println("****************************");
                    }
                    ResultSet r12=stmt.executeQuery("select reg_no from booking where eid='"+eid+"' and date='"+t_date+"' and type='d'");
                    r12.next();
                    String rid=r12.getString(1);
                    stmt.executeUpdate("delete from booking where eid='"+eid+"' and date='"+t_date+"'");
                    ResultSet rr=stmt.executeQuery("select seats from daily_basis where reg_no='"+rid+"' and date='"+t_date+"'");
                    rr.next();
                    int new_seat=rr.getInt(1);
                    int news=new_seat+1;
                    stmt.executeUpdate("update daily_basis set seats="+news+" where rid='"+rid+"' and Date='"+t_date+"'");
                    System.out.println("Woohoo! Your Booking Cancelled Successfully .");
                }
            }
            else {
                System.out.println("Invalid Employee ID");
            }

        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    static void show_ticket(List l,Dictionary d,String t_date) {
        try {
            Scanner ss=new Scanner(System.in);
            System.out.print("Enter Employee ID : ");
            String eid=ss.nextLine();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate today1 = LocalDate.now();
            String tomorrow1 = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
            Statement stmt= con.createStatement();
            if(l.contains(eid)) {
                ResultSet nam=stmt.executeQuery("select name from emp where eid='"+eid+"'");
                nam.next();
                String ename=nam.getString(1);
                System.out.println();
                System.out.println("Welcome, "+ename);
                ResultSet s12=stmt.executeQuery("select * from booking where eid='"+eid+"' and date='"+t_date+"' and type='p'");
                ResultSet s13=stmt.executeQuery("select * from booking where eid='"+eid+"' and date='"+t_date+"' and type='d'");
                while(s12.next()) {
                    System.out.println("****************************");
                    System.out.println("Pick-Up : ");
                    System.out.println("****************************");
                    System.out.println("Reg no : "+s12.getString("reg_no"));
                    System.out.println("Origin : "+s12.getString("origin"));
                    System.out.println("Pick Up : "+s12.getString("pick_up"));
                    System.out.println("Date : "+s12.getString("date"));
                    System.out.println("Time : "+s12.getString("time"));
                    System.out.println("****************************");
                }
                while(s13.next()) {
                    System.out.println("Drop : ");
                    System.out.println("****************************");
                    System.out.println("Reg no : "+s13.getString("reg_no"));
                    System.out.println("Origin : "+s13.getString("origin"));
                    System.out.println("Drop : "+s13.getString("pick_up"));
                    System.out.println("Date : "+s13.getString("date"));
                    System.out.println("Time : "+s13.getString("time"));
                    System.out.println("****************************");
                }
            }
            else {
                System.out.println("Invalid Employee ID");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    static void last_s(String eid,String regno,String o1,String o,String d,String time1){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            Statement stmt= con.createStatement();
            Scanner ss=new Scanner(System.in);
            stmt.executeUpdate("insert into booking values('"+eid+"','"+regno+"','"+o1+"','Sachivalay','"+o+"','"+d+"','"+time1+"','p')");
            stmt.executeUpdate("insert into booking values('"+eid+"','"+regno+"','Sachivalay','"+o1+"','"+o+"','"+d+"','18:00:00','d')");
            ResultSet sea=stmt.executeQuery("select seats from daily_basis where reg_no='"+regno+"' and date='"+d+"'");
            sea.next();
            int seats_11=sea.getInt(1);
            int seats_1=seats_11-1;
            stmt.executeUpdate("update daily_basis set seats="+seats_1+" where reg_no='"+regno+"' and Date='"+d+"'");

            System.out.println("Your Route Has Been Booked ");
        }
        catch(Exception e) {
            System.out.println(e);
        }}
    static void select_r(Dictionary d,String eid,String origin,String Date){
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            Statement stmt= con.createStatement();
            Scanner ss=new Scanner(System.in);
            System.out.print("Enter Your Choice : ");
            int choice=ss.nextInt();

            ResultSet p=stmt.executeQuery("select CARS.Reg_no,Cars.Driver_n,cars.car,cars.o_time,routes.places,routes.origin from cars,routes where"
                    + " cars.rid=routes.rid and reg_no='"+d.get(choice)+"'");
            while(p.next()) {
                String regno1=p.getString("Reg_no");
                String origin1=p.getString("origin");
                String time1=p.getString("o_time");
                System.out.println("********************************************");
                System.out.println("Information About Your Selected Route : ");
                System.out.println("Employee ID : "+eid);
                System.out.println("Registration No. : "+p.getString("Reg_no"));
                System.out.println("Driver : "+p.getString("driver_n"));
                System.out.println("Pick-Up From : "+origin);
                System.out.println("Origin : "+p.getString("origin"));
                System.out.println("Vehicle : "+p.getString("car"));
                System.out.println("Via Places : "+p.getString("places"));
                System.out.println("Time of Pick-Up : "+p.getString("o_time"));
                System.out.println("Time of Drop : 18:00:00");
                System.out.println("Date : "+Date);
                System.out.println("********************************************");
                last_s(eid,regno1,origin1,origin,Date,time1);
            }

        }catch(Exception e) {
            System.out.println(e);
        }
    }
    static void show_rs(String a,Dictionary d,String eid,String Date) {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            Statement stmt= con.createStatement();
            String sql="SELECT DISTINCT CARS.REG_NO,CARS.DRIVER_N,CARS.O_TIME,CARS.CAR,CARS.ORIGIN,ROUTES.PLACES FROM CARS,ROUTES,daily_basis WHERE ROUTES.ORIGIN='"+a+"' or"
                    + " ROUTES.PLACES LIKE '%"+a+"%' and cars.rid=routes.rid and daily_basis.rid=cars.rid and daily_basis.rid=routes.rid and daily_basis.seats>0"
                    + " and daily_basis.date='"+Date+"'";
            ResultSet r=stmt.executeQuery(sql);

            while(r.next()) {
                if(r.getString("Reg_no")=="") {
                    System.out.println("No Vehicles for Given Route!");
                }
                else{
                    int numb=d.size()+1;
                    d.put(numb,r.getString("Reg_no"));
                    String time_of_o=r.getString("o_time");
                    System.out.println("********************************************");
                    System.out.println("Route Number : "+numb);
                    System.out.println("Registration No. : "+r.getString("Reg_no"));
                    System.out.println("Driver : "+r.getString("driver_n"));
                    System.out.println("Time : "+r.getString("o_time"));
                    System.out.println("Origin : "+r.getString("origin"));
                    System.out.println("Vehicle : "+r.getString("car"));
                    System.out.println("Via Places : "+r.getString("places"));
                    System.out.println("Date : "+Date);
                }
            }
            System.out.println("********************************************");
            select_r(d,eid,a,Date);
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }
    static void login(List l,Dictionary d,String t_date) {try {
        Scanner ss=new Scanner(System.in);
        System.out.print("Enter Employee ID : ");
        String eid=ss.nextLine();
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate today1 = LocalDate.now();
        String tomorrow1 = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
        Statement stmt= con.createStatement();
        if(l.contains(eid)) {
            ResultSet nam=stmt.executeQuery("select name from emp where eid='"+eid+"'");
            nam.next();
            String ename=nam.getString(1);
            System.out.println();
            System.out.println("Welcome, "+ename);

            ResultSet cr=stmt.executeQuery("select count(*) from booking where eid='"+eid+"' and date='"+t_date+"'");
            cr.next();
            int pre=cr.getInt(1);
            if(pre==0) {

                System.out.print("Enter From Place : ");
                String origin=ss.nextLine();
                System.out.println("Select Route Number From Below Mentioned Routes to Book That Vehicle : ");
                System.out.println("");
                show_rs(origin,d,eid,t_date);
            }
            else {
                System.out.println("****************************");
                System.out.println("Sorry!You Have Already Booked Your Cab for Tomorrow.");
                System.out.println("****************************");
            }
        }
        else {
            System.out.println("Invalid Employee ID");
        }

    }
    catch(Exception e) {
        System.out.println(e);
    }

    }
    static void create_acc() {try {
        Scanner ss=new Scanner(System.in);
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
        Statement stmt= con.createStatement();
        ResultSet r=stmt.executeQuery("Select count(eid) as num from emp");
        r.next();
        String eidnew=null;
        int a=r.getInt(1)+1;
        if(a>0 && a<10) {
            eidnew="S00"+a;
        }
        else if(a>9 && a<100) {
            eidnew="S0"+a;
        }
        else if(a>99) {
            eidnew="S"+a;
        }
        System.out.println("**************************");
        System.out.print("Enter Name : ");
        String newname=ss.nextLine();
        System.out.print("Enter Your Email-Id : ");
        String emailnew=ss.nextLine();
        System.out.print("Enter Your 10-Digit Mobile Number : ");
        String phnew="+91"+ss.nextLine();
        System.out.println("**************************");
        System.out.println("Your EID : "+eidnew);
        System.out.println("Your Name : "+newname);
        System.out.println("Your EMAIL-ID : "+emailnew);
        System.out.println("Your MOBILE : "+phnew);
        stmt.executeUpdate("insert into emp values('"+eidnew+"','"+emailnew+"','"+phnew+"','"+newname+"')");
        System.out.println("**************************");
        System.out.println("Account Created Successfully");
        System.out.println("Now You Can Login Once Again");
    }
    catch(Exception e) {
        System.out.println(e);
    }
    }
    public static void main(String[] args) {
        try {
            Dictionary d=new Hashtable();
            List<String> l=new ArrayList<>(Arrays.asList());
            Scanner ss=new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_sharing","root","OOPproject@1");
            Statement stmt= con.createStatement();
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate today1 = LocalDate.now();
            String tomorrow1 = (today.plusDays(1)).format(DateTimeFormatter.ISO_DATE);
            ResultSet exe=stmt.executeQuery("select eid from emp");
            while(exe.next()) {
                l.add(exe.getString("eid"));
            }
            ResultSet dail=stmt.executeQuery("select count(rid) from daily_basis where date='"+tomorrow1+"'");
            dail.next();
            if(dail.getInt(1)>0) {
                System.out.print("");
            }
            else {
                stmt.executeUpdate("insert into daily_basis values('GJ18AX1912','R1KS0830',7,'Innova','"+tomorrow1+"');");
                stmt.executeUpdate("insert into daily_basis values('GJ18GI3763','R1KS0845',8,'Tavera','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BQ1272','R1KS0900',4,'Zest','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX1293','R2SS0800',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX3872','R2SS0810',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX7374','R2SS0820',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BQ1382','R2SS0830',4,'Zest','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18UI3646','R2SS0845',8,'Eeco','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BR6453','R3VS0800',7,'Enjoy','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18UI3727','R3VS0810',8,'Eeco','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18GI2877','R3VS0820',8,'Tavera','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BE6563','R3VS0830',8,'Marazzo','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX2838','R3VS0840',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX2939','R4CS0815',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX5583','R4CS0825',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX2948','R4CS0835',7,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BQ3847','R4CS0845',4,'Zest','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AX4828','R5DS0815',7,'Enjoy','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BQ5647','R5DS0830',4,'Zest','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18BT4746','RNURS101',8,'Enjoy','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18XT4736','RNURS102',8,'Innova','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18AT6564','RNURS103',4,'Zest','"+tomorrow1+"')");
                stmt.executeUpdate("insert into daily_basis values('GJ18XT2309','RNURS104',8,'Innova','"+tomorrow1+"')");
            }
            System.out.println("******************************");
            System.out.println("Select One Of Below Options : ");
            System.out.println("1.Login And Select Route");
            System.out.println("2.Create Account");
            System.out.println("3.Show Ticket");
            System.out.println("4.Cancel Booking");
            System.out.println("5.Driver Login");
            System.out.println("6.Exit");
            System.out.println("******************************");
            System.out.print("Choice -> ");
            int choice=ss.nextInt();
            if(choice==1) {
                login(l,d,tomorrow1);
                main(null);
            }
            else if(choice==2) {
                create_acc();
                main(null);
            }
            else if(choice==3) {
                show_ticket(l,d,tomorrow1);
                main(null);
            }
            else if(choice==4) {
                cancel(l,d,tomorrow1);
                main(null);
            }
            else if(choice==5) {
                driver_login();
                main(null);
            }
            else if(choice==6) {
                System.exit(0);
            }
            else {
                System.out.println("Oops!Invalid Input!");
                System.out.println("Please Enter Valid Input Now : ");
                main(null);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
