package db;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import utilities.MySqlConnect;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
/**
 * Этот класс надо переделать, как только появятся контроллеры
 *
 * */
public class DataBaseHelper {
    //Все в проперти
    private final static String sqlInsertObjects = "insert into objects(model_files_id, class, name) values(?, ?, ?)";
    private final static String sqlInsertObject = "insert into object(objects_id, class, name) values(?, ?, ?)";
    private final static String sqlInsertAttribute = "insert into attribute(object_id, name, unit, dataType, description) values(?, ?, ?, ?, ?)";
    private final static String sqlInsertData = "insert into data(attribute_id, component_name, value) values(?, ?, ?)";
    private final static String sqlInsertModel = "insert into model_files(modeltype_id, pathToMainFile) values(?, ?)";
    private final static String sqlLastId = "select last_insert_id()";

    public static int getLastInsertId(Connection conn) {
        try {
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sqlLastId);
            rs.next();

            return  rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insert(){

        MySqlConnect mySqlConnect = new MySqlConnect();

        try {
            //парсинг xml
            SAXBuilder saxBuilder = new SAXBuilder();
            File xmlFile = new File("src\\main\\resources\\HydrotreaterUnitSimulation.xml");
            Document document = saxBuilder.build(xmlFile);
            Element rootNode = document.getRootElement();

            Connection conn = mySqlConnect.connect();

            //вставка модели
            PreparedStatement statement = conn.prepareStatement(sqlInsertModel);
            statement.setInt(1, 1);
            statement.setString(2, xmlFile.getAbsolutePath());
            statement.execute();
            //последний вставленный индекс
            int modelId = getLastInsertId(conn);

            List<Element> objectsList = rootNode.getChildren("Objects");
            for (Element objects : objectsList) {
                System.out.println("Objects Name: "
                        + objects.getAttributeValue("Name"));
                //вставка Objects
                PreparedStatement statementObjects = conn.prepareStatement(sqlInsertObjects);
                //statementObjects.setInt(1, modelId);
                statementObjects.setString(2, objects.getAttributeValue("ClassName"));
                statementObjects.setString(3, objects.getAttributeValue("Name"));
                statementObjects.execute();

                //последний вставленный индекс
                int objectsId = getLastInsertId(conn);

                List<Element> objectList = objects.getChildren("Object");
                for (Element object : objectList) {
                    System.out.println("\tObject ClassName: "
                            + object.getAttributeValue("Class") + "\tName: "
                            + object.getAttributeValue("Name"));
                    //вставка Objects
                    PreparedStatement statementObject = conn.prepareStatement(sqlInsertObject);
                    statementObject.setInt(1, objectsId);
                    statementObject.setString(2, object.getAttributeValue("Class"));
                    statementObject.setString(3, object.getAttributeValue("Name"));
                    statementObject.execute();

                    //последний вставленный индекс
                    int objectId = getLastInsertId(conn);

                    List<Element> attributeList = object.getChildren("Attribute");
                    for (Element attribute : attributeList) {
                        System.out.println("\t\tAttribute Name: "
                                + attribute.getAttributeValue("Name") + "\tUnit: "
                                + attribute.getAttributeValue("Unit")+ "\tDescription: "
                                + attribute.getAttributeValue("Description") + "\tDataType: "
                                + attribute.getAttributeValue("DataType"));
                        //вставка Attribute
                        PreparedStatement statementAttribute = conn.prepareStatement(sqlInsertAttribute);
                        statementAttribute.setInt(1, objectId);
                        statementAttribute.setString(2, attribute.getAttributeValue("Name"));
                        statementAttribute.setString(3, attribute.getAttributeValue("Unit"));
                        statementAttribute.setString(4, attribute.getAttributeValue("DataType"));
                        statementAttribute.setString(5, attribute.getAttributeValue("Description"));
                        statementAttribute.execute();

                        //последний вставленный индекс
                        int attributeId = getLastInsertId(conn);

                        List<Element> dataList = attribute.getChildren("Data");
                        for (Element data : dataList) {
                            System.out.println("\t\t\tData ComponentName: "
                                    + data.getAttributeValue("ComponentName") + "\tValue: "
                                    + data.getAttributeValue("Value"));
                            //вставка Data
                            PreparedStatement statementData = conn.prepareStatement(sqlInsertData);
                            statementData.setInt(1, attributeId);
                            statementData.setString(2, data.getAttributeValue("ComponentName"));
                            statementData.setString(3, data.getAttributeValue("Value"));
                            statementData.execute();
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch(JDOMException ex) {
            ex.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            mySqlConnect.disconnect();
        }
    }
}
