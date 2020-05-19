package mongo;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.mongodb.*;
import com.mongodb.util.JSON;

import org.bson.*;

import java.util.*;
import java.util.Vector;
import java.io.File;
import java.io.*;
import javax.swing.*;


public class SensorToMongo implements MqttCallback {
    MqttClient mqttclient;
    static MongoClient mongoClient;
    static DB db;
    static DBCollection mongocol;
    static String sensor_server = new String();
    static String sensor_client = new String();
    static String sensor_topic = new String();
	static String sensor_name = new String();
    static String mongo_connection = new String();
    static String mongo_database = new String();
    static String mongo_collection = new String();
    static String sensor_format_json = new String();

    public static void main(String[] args) {

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("src/main/resources/SensorToMongo.ini"));
            sensor_server = p.getProperty("sensor_server");
            sensor_client = p.getProperty("sensor_client");
            sensor_topic = p.getProperty("sensor_topic");
            mongo_connection = p.getProperty("mongo_server");
            mongo_database = p.getProperty("mongo_database");
            mongo_collection = p.getProperty("mongo_collection");
            sensor_name = p.getProperty("sensor_name");
            sensor_format_json = p.getProperty("sensor_format_json");
        } catch (Exception e) {

            System.out.println("Error reading mongo_setup.ini file " + e);
            JOptionPane.showMessageDialog(null, "The mongo_setup.inifile wasn't found.", "Sensor To Mongo", JOptionPane.ERROR_MESSAGE);
        }
        new SensorToMongo().connecSensor();
        new SensorToMongo().connectMongo();
    }

    public void connecSensor() {
        try {
            mqttclient = new MqttClient(sensor_server, sensor_client);
            mqttclient.connect();
            mqttclient.setCallback(this);
            mqttclient.subscribe(sensor_topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectMongo() {
		mongoClient = new MongoClient("localhost",27017);
        mongoClient = new MongoClient(new MongoClientURI(mongo_connection));
        MongoClientURI uri = new MongoClientURI("mongodb://user:passwd@localhost:27017/?authSource=test");
//        Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
        db = mongoClient.getDB(mongo_database);
        mongocol = db.getCollection(mongo_collection);	
    }

    @Override
    public void messageArrived(String topic, MqttMessage c)
            throws Exception {
        try {
            if (sensor_format_json.equals("true")) {
                DBObject document_json;
                document_json = (DBObject) JSON.parse(c.toString());
                mongocol.insert(document_json);
            } else {
                BasicDBObject basic_document = new BasicDBObject();
                basic_document.append(sensor_name, c.toString());
                mongocol.insert(basic_document);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}
