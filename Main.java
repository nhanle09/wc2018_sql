import java.sql.*;
import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Main {
	public static String game_type;
	public static String country_name;
	public static Connection _connect;
	
	public static void main(String[] args) {
		String dbaccount, dbpassword, dbhost;
		dbhost = "localhost:3306/soccer?autoReconnect=true&useSSL=false";
		dbaccount = "root";
		dbpassword = "root";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver could not be loaded");
		}
		try {
			_connect = DriverManager.getConnection(""
					+ "jdbc:mysql://" + dbhost, dbaccount, dbpassword);
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
		}
		
		JFrame main_window = new JFrame();
		JFrame game_window = new JFrame();
		JFrame player_window = new JFrame();
		
		main_window.setTitle("2018 World Cup");
		main_window.setSize(300, 150);
		main_window.setLayout(new GridLayout(2, 1));
		main_window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent closingEvent) {
				System.exit(0);}});
		
		game_window.setTitle("Game Result");
		game_window.setSize(400, 200);
		
		player_window.setTitle("Player Information");
		player_window.setSize(500, 450);
		
		String match_col[] = {"Team 1", "Score", "Score", "Team 2"};
		String player_col[] = {"Player Name", "Number", "Position"};
		String gtype[] = {"A","B","C","D","E","F","G","H","X","Q","S","L"};
		String pname[] = {"Argentina", "Australia", "Belgium", "Brazil", "Colombia", 
				"Costa Rica", "Croatia", "Denmark", "Egypt", "England", "France", 
				"Germany", "Iceland", "Iran", "Japan", "Mexico", "Morocco", 
				"Nigeria", "Panama", "Peru", "Poland", "Portugal", "Russia", 
				"Saudi Arabia", "Serbia", "South Korea", "Spain", 
				"Sweden", "Switzerland", "Tunisia", "Uruguay"};

		// Game
		JPanel game_button_panel = new JPanel();
		JLabel game_prompt_label = new JLabel("Select Game Type: ");
		JComboBox<String> game_list = new JComboBox<>(gtype);
		JButton game_ok_button = new JButton("OK");
		
		DefaultTableModel game_table = new DefaultTableModel(match_col, 0);
		JTable game_result_table = new JTable(game_table);
		JScrollPane game_scroll_table = new JScrollPane(game_result_table);
		
		
		// Player
		JPanel player_button_panel = new JPanel();
		JLabel country_prompt_label = new JLabel("Select Country: ");
		JComboBox<String> player_list = new JComboBox<>(pname);
		JButton player_ok_button = new JButton("OK");
		
		DefaultTableModel player_table = new DefaultTableModel(player_col, 0);
		JTable player_result_table = new JTable(player_table);
		JScrollPane player_scroll_table = new JScrollPane(player_result_table);
		
		game_button_panel.add(game_prompt_label);
		game_button_panel.add(game_list);
		game_button_panel.add(game_ok_button);
		
		player_button_panel.add(country_prompt_label);
		player_button_panel.add(player_list);
		player_button_panel.add(player_ok_button);
		
		
		main_window.add(game_button_panel);
		main_window.add(player_button_panel);
		game_window.add(game_scroll_table);
		player_window.add(player_scroll_table);
		main_window.setVisible(true);
		
		// Game
		game_ok_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ex) {
				game_type = (String) game_list.getSelectedItem();
				PreparedStatement _statement;
				ResultSet _result;
				try { _statement = _connect.prepareStatement("select L.cname, team1s, team2s, R.cname "
						+ "from country as L, country as R, game "
						+ "where gtype='" + game_type + "' and team1=L.cid and team2=R.cid");
					try { _result = _statement.executeQuery();
						try { while (_result.next()) {
								Object[] objs = {_result.getString(1), _result.getString(2),
										_result.getString(3), _result.getString(4)};
								game_table.addRow(objs);
								}
							} catch (SQLException e) {System.out.println("Error in Next()");}
						} catch (SQLException e) {System.out.println("Error in Executing Query");}
					} catch (SQLException e) {System.out.println("Error in PreparedStatement");}
					game_window.setVisible(true);
					main_window.setVisible(false);
					try { _connect.close();
					} catch (SQLException e) {System.out.println("Error in close");}
				}
			});
		
		// Players
		player_ok_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ex) {
				country_name = (String) player_list.getSelectedItem();
				PreparedStatement _statement;
				ResultSet _result;
				try { _statement = _connect.prepareStatement("select pname, pno, position from country, player "
						+ "where country.cname='" + country_name + "' and country.cid=player.cid");
					try { _result = _statement.executeQuery();
						try { while (_result.next()) {
								Object[] objs = {_result.getString(1), _result.getString(2),
										_result.getString(3)};
								player_table.addRow(objs);
								}
							} catch (SQLException e) {System.out.println("Error in Next()");}
						} catch (SQLException e) {System.out.println("Error in Executing Query");}
					} catch (SQLException e) {System.out.println("Error in PreparedStatement");}
					player_window.setVisible(true);
					main_window.setVisible(false);
					try { _connect.close();
					} catch (SQLException e) {System.out.println("Error in close");}
				}
			});
		
	}
}