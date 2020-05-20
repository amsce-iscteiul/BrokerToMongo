package mongo;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.util.*;
import java.io.*;
import javax.swing.*;


@SuppressWarnings("deprecation")
public class CloudToMongo implements MqttCallback {
    MqttClient mqttclient;
    static MongoClient mongoClient;
    static DB db;
    static DBCollection mongocol;
    static String cloud_server = new String();
    static String cloud_topic = new String();
    static String mongo_host = new String();
    static String mongo_database = new String();
    static String mongo_collection = new String();

    
    
    
    
    public static void main(String[] args) {

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("src/main/resources/cloudToMongo.ini"));
            cloud_server = p.getProperty("cloud_server");
            cloud_topic = p.getProperty("cloud_topic");
            mongo_host = p.getProperty("mongo_host");
            mongo_database = p.getProperty("mongo_database");
            mongo_collection = p.getProperty("mongo_collection");
        } catch (Exception e) {

            System.out.println("Error reading CloudToMongo.ini file " + e);
            JOptionPane.showMessageDialog(null, "The CloudToMongo.inifile wasn't found.", "CloudToMongo", JOptionPane.ERROR_MESSAGE);
        }
        new CloudToMongo().connecCloud();
        new CloudToMongo().connectMongo();
        
        
        
    }

    
    
    
    
    public void connecCloud() {
		int i;
        try {
			i = new Random().nextInt(100000);
            mqttclient = new MqttClient(cloud_server, "CloudToMongo_"+String.valueOf(i)+"_"+cloud_topic);
            mqttclient.connect();
            mqttclient.setCallback(this);
            mqttclient.subscribe(cloud_topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    public void connectMongo() {
		mongoClient = new MongoClient(new MongoClientURI(mongo_host));
		db = mongoClient.getDB(mongo_database);
        mongocol = db.getCollection(mongo_collection);
    }

    
    
    
    
    @Override
    public void messageArrived(String topic, MqttMessage c) throws Exception {
        try {
                DBObject document_json;
                document_json = filterSensor(c);
                
                Cursor cursor = mongocol.find((DBObject) JSON.parse(document_json.toString()));
                if(!cursor.hasNext()) {
                	mongocol.insert(document_json);
                	System.out.println("ADDED: " + document_json);
                }else {
                	System.out.println("Duplicado detetado: " + document_json);
                }
                
        } catch (Exception e) {
        	//e.printStackTrace();
            System.out.println(e);
        }
    }

    
    
    
    
    private DBObject filterSensor(MqttMessage c) {
    	String aux = c.toString().replace("\"\"", "\",\"");
    	JSONObject jsonObj = new JSONObject(aux);	
    	if(!jsonObj.has("cell")) 
    		jsonObj.put("cell", new Random().nextInt(500));
    	if(!jsonObj.has("mov"))
    		jsonObj.put("mov", new Random().nextInt(100) < 90 ? 0:1);
    	
    	return (DBObject) JSON.parse(jsonObj.toString()) ;
    }

    
    
    
    
    @Override
    public void connectionLost(Throwable cause) {}

    
    
    
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {}
}