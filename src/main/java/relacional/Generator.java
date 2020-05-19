package relacional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Generator {

	private static Connection connection;
	private Random r = new Random();
	private String db; 
	private String[] text = ("Thesunha dsetandsohadhi sdreams Thera inpeltedthew indshieldasthe darknessengulfedus  The quick brown fox jumps over the lazy dog " + "He stepped gingerly onto the bridge knowing that enchantment awaited on the other side " + "She couldnt decide of the glass was half empty or half full so she drank it " + "The secret ingredient to his wonderful life was crime " + "He liked to play with words in the bathtub " + "I ate a sock because people on the Internet told me to " + "When he encountered maize for the first time, he thought it incredibly corny " + "They did nothing as the raccoon attacked the lady’s bag of food " + "He quietly entered the museum as the super bowl started " + "He took one look at what was under the table and noped the hell out of there " + "As the years pass by, we all know owners look more and more like their dogs " + "Seek success, but always be prepared for random cats " + "The view from the lighthouse excited even the most seasoned traveler " + "When money was tight, hed get his lunch money from the local wishing well " + "He didn’t want to go to the dentist, yet he went anyway " + "He always wore his sunglasses at night " + "Before he moved to the inner city, he had always believed that security complexes were psychological " + "Be careful with that butter knife " + "Buried deep in the snow, he hoped his batteries were fresh in his avalanche beacon " + "The snow-covered path was no help in finding his way out of the back-country " + "He walked into the basement with the horror movie from the night before playing in his head " + "Id rather be a bird than a fish " + "I am my aunts sisters daughter " + "I purchased a baby clown from the Russian terrorist black market " + "Dan took the deep dive down the rabbit hole " + "We should play with legos at camp " + "Three generations with six decades of life experience " + "They say that dogs are mans best friend, but this cat was setting out to sabotage that theory " + "A kangaroo is really just a rabbit on steroids " + "Youve been eyeing me all day and waiting for your move like a lion stalking a gazelle in a savannah " + "The minute she landed she understood the reason this was a fly-over state " + "The trick to getting kids to eat anything is to put catchup on it " + "Fluffy pink unicorns are a popular status symbol among macho men " + "Greetings from the galaxy MACS0647-JD, or what we call home " + "The waitress was not amused when he ordered green eggs and ham " + "He dreamed of eating green apples with worms " + "Iguanas were falling out of the trees " + "She did not cheat on the test, for it was not the right thing to do " + "Everybody should read Chaucer to improve their everyday vocabulary " + "The old apple revels in its authority " + "The waves were crashing on the shore; it was a lovely sight " + "The Guinea fowl flies through the air with all the grace of a turtle " + "Its much more difficult to play tennis with a bowling ball than it is to bowl with a tennis ball " + "I met an interesting turtle while the song on the radio blasted away " + "There was no telling what thoughts would come from the machine " + "Garlic ice-cream was her favorite " + "Her life in the confines of the house became her new normal ").split(" ");
	private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVXYWZ";


	public Generator(String db) {
		this.db = db;
		connect(db);
	}



	private static void connect(String db)  {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://85.244.169.93:3306/"+db+"?useLegacyDatetimeCode=false&serverTimezone=UTC", "menoita", "R00tBDP@ss");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	} 



	@SuppressWarnings("deprecation")
	public void createDiaSemana(int generations) throws SQLException {
		List<String> weekDays = List.of("segunda-feira","terça-feira","quarta-feira","quinta-feira","sexta-feira","sabado","domingo");
		String query = "INSERT INTO `"+db+"`.`dia_semana` (`dia_semana`, `hora_ronda`) VALUES (?, ?);";

		PreparedStatement statement = connection.prepareStatement(query);

		for (int i = 0; i < generations; i++) {
			try {
				statement.setString(1, weekDays.get(r.nextInt(weekDays.size())));
				statement.setTime(2, new Time(r.nextInt(24), r.nextInt(60), r.nextInt(60)));
				statement.executeUpdate();
			}catch (Exception e) {
				i--;
				e.printStackTrace();
			}
		}
		statement.close();
	}



	private String getWord(int i) {
		String w = "";
		for (int j = 0; j < i; j++) 
			w+= alphabet.toCharArray()[r.nextInt(alphabet.length())];
		return w;
	}



	public void createMedicoesSensores(int generations) throws SQLException {
		if(db.equals("dctidata_29")) {
			String query =  "INSERT INTO `"+db+"`.`medicoes_sensores` (`id_medicao`, `valor_medicao`, `data_hora_medicao`, `tipo_sensor`) VALUES (?, ?, ?, ?);";

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select max(id_medicao) from "+db+".medicoes_sensores;");
			rs.next();
			int id = rs.getInt(1);
			stm.close();	

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				id++;
				try {
					statement.setInt(1, id);
					statement.setInt(2,r.nextInt(100));
					statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)));
					statement.setString(4,getWord(3));
					statement.executeUpdate();
				}catch (Exception e) {
					id--;
					i--;
					e.printStackTrace();
				}
			}

		}else if(db.equals("sid_php")){
			String query = "INSERT INTO `"+db+"`.`medicao_sensores` (`medicao_id`, `valor_medicao`, `tipo_sensor`, `data_hora_medicao`) VALUES (?, ?, ?, ?);";
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select max(medicao_id) from "+db+".medicao_sensores;");
			rs.next();
			int id = rs.getInt(1);
			stm.close();	

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				id++;
				try {
					statement.setInt(1, id);
					statement.setInt(2,r.nextInt(100));
					statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)));
					statement.setString(3,getWord(3));
					statement.executeUpdate();
				}catch (Exception e) {
					id--;
					i--;
					e.printStackTrace();
				}
			}
		}
	}




	public void createAlertas(int generations) throws SQLException {
		if(db.equals("dctidata_29")) {
			String query = "INSERT INTO `"+db+"`.`alerta` ( `tipo_alerta`, `data_hora_alerta`, `descricao`, `medicao_id`) VALUES (?, ?, ?, ?)";

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select max(id_medicao) from "+db+".medicoes_sensores;");
			rs.next();
			int maxid = rs.getInt(1);
			stm.close();	

			if(maxid<1) {
				return;
			}

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					statement.setString(1, getWord(3));
					statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)));
					statement.setString(3, getPhrase(r.nextInt(5) + 5));
					statement.setInt(4,r.nextInt(maxid+1));
					statement.executeUpdate();
				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}

		}
		else if(db.equals("sid_php")){

		}
	}


	private String getPhrase(int i) {
		String w = "";
		for (int j = 0; j < i; j++) 
			w+= text[r.nextInt(text.length)]+" ";
		return w;
	}



	public void createRondaExtra(int generations) throws SQLException {
		if(db.equals("dctidata_29")) {
			String query = "INSERT INTO `"+db+"`.`ronda_extra` (`data_hora`, `user_id`) VALUES (?, ?);";
			List<Integer> ids = new ArrayList<>();

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from "+db+"."+ (db.equals("dctidata_29") ? "user" :"utilizador"));

			while(rs.next()) 
				ids.add(rs.getInt("user_id"));

			stm.close();	


			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)));
					statement.setInt(2, ids.get(r.nextInt(ids.size())));
					statement.executeUpdate();
				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}
		}else if(db.equals("sid_php")){
			String query = "INSERT INTO `"+db+"`.`ronda_extra` (`email`, `data_hora`) VALUES (?, ?);";
			List<String> ids = new ArrayList<>();

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from "+db+"."+ (db.equals("dctidata_29") ? "user" :"utilizador"));

			while(rs.next()) 
				ids.add(rs.getString("email"));

			stm.close();	


			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(i)));
					statement.setString(1, ids.get(r.nextInt(ids.size())));
					statement.executeUpdate();
				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}
		}
	}


	public void createRondaPlaneada(int generations) throws SQLException {
		if(db.equals("dctidata_29")) {
			String query = "INSERT INTO `"+db+"`.`ronda_planeada` (`user_id`, `dia_semana`, `hora_ronda`) VALUES (?, ?, ?);";
			List<Integer> ids = new ArrayList<>();
			List<RondaPlaneada> rp = new ArrayList<>();

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from "+db+"."+ (db.equals("dctidata_29") ? "user" :"utilizador"));
			while(rs.next()) 
				ids.add(rs.getInt(4));
			rs.close();

			ResultSet rs1 = stm.executeQuery("SELECT * FROM `"+db+"`.`dia_semana`;");
			while(rs1.next()) 
				rp.add(new RondaPlaneada(rs1.getTime(2),rs1.getString(1)));

			stm.close();	

			System.out.println(rp.size());
			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					statement.setInt(1, ids.get(r.nextInt(ids.size())));
					statement.setString(2, rp.get(r.nextInt(rp.size())).getDiaSemana());
					statement.setTime(3, rp.get(r.nextInt(rp.size())).getTime());
					System.out.println(statement);
					statement.executeUpdate();

				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}
		}else if(db.equals("sid_php")){
			String query = "INSERT INTO `"+db+"`.`ronda_planeada` (`email`, `dia_semana`, `hora_ronda`) VALUES (?, ?, ?);";
			List<String> ids = new ArrayList<>();
			List<RondaPlaneada> rp = new ArrayList<>();

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from "+db+"."+ (db.equals("dctidata_29") ? "user" :"utilizador"));
			while(rs.next()) 
				ids.add(rs.getString("email"));
			rs.close();

			ResultSet rs1 = stm.executeQuery("SELECT * FROM `"+db+"`.`dia_semana`;");
			while(rs1.next()) 
				rp.add(new RondaPlaneada(rs1.getTime(2),rs1.getString(1)));

			stm.close();	


			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					statement.setString(1, ids.get(r.nextInt(ids.size())));
					statement.setString(2, rp.get(r.nextInt(rp.size())).getDiaSemana());
					statement.setTime(3, rp.get(r.nextInt(rp.size())).getTime());
					statement.executeUpdate();
				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void updateDiaSemana(int generations) throws SQLException {
		if(db.equals("sid_php")) {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM sid_php.dia_semana order by hora_ronda desc limit 1;");

			String dia_semana = "domingo";
			String dia_semana_tmp = "domingo";
			Time hora  = new Time(r.nextInt(24),r.nextInt(60),r.nextInt(60));
			Time hora_tmp  = new Time(0,0,0);

			rs.next();
			hora_tmp = rs.getTime(2);
			dia_semana_tmp = rs.getString(1);
			rs.close();


			String query ="UPDATE sid_php.dia_semana SET dia_semana = ? , hora_ronda = ? WHERE dia_semana = ?  AND hora_ronda = ?; ";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				statement.setString(1, dia_semana_tmp);
				statement.setTime(2, hora_tmp);
				statement.setString(3, dia_semana);
				statement.setTime(4, hora);
				statement.executeUpdate();

				String tmp = dia_semana_tmp;
				dia_semana_tmp = dia_semana;
				dia_semana = tmp;

				Time ttmp = hora_tmp;
				hora_tmp = hora;
				hora = ttmp;

			}

		}else if(db.equals("dctidata_29")) {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM dctidata_29.dia_semana limit 1;");

			String dia1 = "sabado";
			String dia2 = "domingo";

			rs.next();
			Time hora = rs.getTime(2);
			String dia_semana = rs.getString(1);
			rs.close();


			String query ="UPDATE dctidata_29.dia_semana SET dia_semana = ? WHERE (dia_semana = ?) and (hora_ronda =?);";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					if(dia1.equals("dia_semana")) {
						statement.setString(1, dia2);
						statement.setString(2, dia_semana);
						statement.setTime(3, hora);
						dia_semana = dia2;
					}else {
						statement.setString(1, dia1);
						statement.setString(2, dia_semana);
						statement.setTime(3, hora);
						dia_semana = dia1;
					}
					statement.executeUpdate();
				}catch (Exception e) {
					i--;
					e.printStackTrace();
				}
			}
		}
	}	


	public void updateUtilizadores(int generations) throws SQLException {
		if(db.equals("sid_php")) {

			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM sid_php.utilizador limit 1;");

			rs.next();
			String email = rs.getString(1);
			rs.close();

			String query ="UPDATE sid_php.utilizador SET nome = ? WHERE email = ? ; ";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				String nome = getPhrase(r.nextInt(3)+1);
				statement.setString(1, nome);
				statement.setString(2, email);
				statement.executeUpdate();
			}
		}else if(db.equals("dctidata_29")) {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM dctidata_29.user limit 1;");

			rs.next();
			String email = rs.getString(1);
			rs.close();

			String query ="UPDATE dctidata_29.user SET nome_utilizador = ? WHERE email = ? ; ";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				try {
					String nome = getPhrase(r.nextInt(1)+1);
					statement.setString(1, nome);
					statement.setString(2, email);
					statement.executeUpdate();
				}catch (Exception e) {
					e.printStackTrace();
					i--;
				}
			}
		}
	}	


	@SuppressWarnings("deprecation")
	public void updateRondaPlaneada(int generations) throws SQLException {
		if(db.equals("sid_php")) {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM sid_php.ronda_planeada limit 1;");

			rs.next();
			String diasemana = rs.getString(2);
			Time tempo = rs.getTime(3);
			rs.close();

			String query ="UPDATE sid_php.ronda_planeada SET dia_semana = ? , hora_ronda = ? WHERE dia_semana = ? and hora_ronda = ? ;";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				Time tmp = null;
				if(i % 2 == 0) {
					tmp = new Time(tempo.getHours(), tempo.getMinutes(), tempo.getSeconds()+1);
					statement.setString(1, diasemana);
					statement.setTime(4, tempo);
					statement.setString(3, diasemana);
					statement.setTime(2, tmp);
				}else {
					tmp = new Time(tempo.getHours(), tempo.getMinutes(), tempo.getSeconds()-1);
					statement.setString(1, diasemana);
					statement.setTime(4, tempo);
					statement.setString(3, diasemana);
					statement.setTime(2, tmp);
				}
				tempo = tmp;
				statement.executeUpdate();
			}
		}else if (db.equals("dctidata_29")) {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM dctidata_29.ronda_planeada limit1;");

			rs.next();
			int userid = rs.getInt(1);
			String diasemana = rs.getString(2);
			Time tempo = rs.getTime(3);
			rs.close();

			ResultSet rs1 = stm.executeQuery("SELECT * FROM dctidata_29.user limit 2;");
			rs1.next();
			int user1 = rs1.getInt("user_id");
			rs1.next();
			int user2 = rs1.getInt("user_id");
			rs1.close();

			String query ="UPDATE dctidata_29.ronda_planeada SET user_id = ? WHERE dia_semana = ? and hora_ronda = ? ;";

			PreparedStatement statement = connection.prepareStatement(query);

			for (int i = 0; i < generations; i++) {
				if(userid != user1) {
					statement.setInt(1, user1);
					statement.setString(2, diasemana);
					statement.setTime(3, tempo);
					userid = user1;
				}else {
					statement.setInt(1, user2);
					statement.setString(2, diasemana);
					statement.setTime(3, tempo);
					userid = user2;
				}
				statement.executeUpdate();
			}
		}
	}



	public static void main(String[] args) {
		try {
			Generator g = new Generator("dctidata_29");
			//Generator g = new Generator("sid_php");

			g.createDiaSemana(10);
			g.createMedicoesSensores(10);
			g.createRondaExtra(10);
			g.updateUtilizadores(10);
			g.updateRondaPlaneada(10);


			//			g.createMedicoesSensores(1000);
			//			g.createAlertas(1000);
			//			g.createRondaExtra(1000);
			//			g.createRondaPlaneada(1000);
			//			g.updateUtilizadores(30000);
			//			g.createAlertas(4000);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
