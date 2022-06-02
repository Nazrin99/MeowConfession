package Program.Sandbox;

import java.io.*;
import java.sql.*;

public class objectSerialization implements Serializable {
    private String name;
    private int age;

    public objectSerialization(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

class Test{
    public static void main(String[] args) throws IOException {
        objectSerialization obj1 = new objectSerialization("Nazrin", 20);
        objectSerialization obj2 = null;
        ByteArrayOutputStream bos;
        ObjectOutputStream oos;
        ByteArrayInputStream bis;
        ObjectInputStream ois;

        byte[] objectArray = null;

        try{
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj1);
            objectArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test VALUES(?)");
            preparedStatement.setBytes(1, objectArray);
            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/confession", "root", "MeowConfession");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                objectArray = resultSet.getBytes(1);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            bis = new ByteArrayInputStream(objectArray);
            ois = new ObjectInputStream(bis);
            obj2 = (objectSerialization) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(obj1.getName().equalsIgnoreCase(obj2.getName()));
        System.out.println(obj1.getAge() == obj2.getAge());


    }
}
