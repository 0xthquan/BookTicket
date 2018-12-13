import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class Server1 implements ActionListener {

	private JFrame frame;
	private final int cols = 7;
	private final int rows = 5;
	private int[][] database;
	private JButton[][] btnSeats;
	private JButton btnBook;
	private JTextArea taProcess;
	private JScrollPane scroll;
	private JLabel lblBanner;
	private JLabel lblProcess;
	private JLabel lblStatusConnect;
	private JPanel pnScreen;
	private JLabel lblScreen;
	private JPanel panel_3;
	private JPanel panel_2;
	private JPanel panel_1;
	private ServerSocket serverSocket;
	private Socket socket;
	private final int serverID = 1;
	private final int numOfServer = 3;
	private final int[] portSocketServer = {7777, 8888, 9999};
	private int lamport = 0;
	private ArrayList<Message> queueREQ;
	private int numOfACQ = 0;
	private int numOfBLK = 0;
	private int numOfUOK = 0;
	private int xCur = -1;
	private int yCur = -1;
	private int statusTicketBlock = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Server1 window = new Server1();
		window.frame.setVisible(true);
		window.listenServer();
	}

	/**
	 * Create the application.
	 */
	public Server1() {
		initialize();
		initDatabase();
		queueREQ = new ArrayList<>();
		btnSeats = new JButton[rows][cols];
		displaySeats();
	}
	
	// Khởi tạo database
	public void initDatabase(){
		database = new int[rows][cols];
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				database[i][j]=0;
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(5, 100, 480, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Server"+serverID);

		pnScreen = new JPanel();
		pnScreen.setBackground(new Color(220, 20, 60));
		pnScreen.setForeground(Color.RED);
		pnScreen.setBounds(86, 40, 294, 30);
		frame.getContentPane().add(pnScreen);

		lblScreen = new JLabel("Screen");
		lblScreen.setForeground(new Color(255, 255, 255));
		pnScreen.add(lblScreen);
		lblScreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblScreen.setFont(new Font("Britannic Bold", Font.BOLD, 20));

		panel_3 = new JPanel();
		//panel_3.setBackground(new Color(224, 255, 255));
		panel_3.setBounds(398, 91, 56, 205);
		panel_3.setLayout(new GridLayout(5, 1));
		frame.getContentPane().add(panel_3);

		panel_2 = new JPanel();
		//panel_2.setBackground(new Color(224, 255, 255));
		panel_2.setBounds(86, 91, 294, 205);
		panel_2.setLayout(new GridLayout(5, 5));
		frame.getContentPane().add(panel_2);

		panel_1 = new JPanel();
		//panel_1.setBackground(new Color(224, 255, 255));
		panel_1.setBounds(10, 91, 56, 205);
		panel_1.setLayout(new GridLayout(5, 1));
		frame.getContentPane().add(panel_1);

		btnBook = new JButton("Book");
		btnBook.setBounds(291, 304, 89, 23);
		btnBook.addActionListener(this);
		frame.getContentPane().add(btnBook);

		lblStatusConnect = new JLabel("SERVER "+serverID);
		lblStatusConnect.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStatusConnect.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatusConnect.setForeground(Color.BLACK);
		lblStatusConnect.setBounds(145, 15, 172, 14);
		frame.getContentPane().add(lblStatusConnect);
		
		taProcess = new JTextArea();
		taProcess.setBounds(10, 338, 224, 189);
		taProcess.setEditable(false);
		frame.getContentPane().add(taProcess);
		
		scroll = new JScrollPane(taProcess);
		scroll.setBounds(10, 338, 224, 189);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroll);

		lblProcess = new JLabel("Process");
		lblProcess.setHorizontalAlignment(SwingConstants.CENTER);
		lblProcess.setBounds(70, 323, 89, 14);
		frame.getContentPane().add(lblProcess);

		lblBanner = new JLabel("");
		lblBanner
				.setIcon(new ImageIcon(
						"F:\\image.jpg"));
		lblBanner.setHorizontalAlignment(SwingConstants.CENTER);
		lblBanner.setBounds(246, 338, 208, 189);
		frame.getContentPane().add(lblBanner);
	}
	
	// Hiển thị chỗ ngồi
	public void displaySeats(){
		for (int i = 0; i < btnSeats.length; i++) {
			for (int j = 0; j < 1; j++) {
				btnSeats[i][j] = new JButton(String.valueOf(j + 1));
				btnSeats[i][j].addActionListener(this);
				btnSeats[i][j].setBackground(database[i][j]==0?Color.LIGHT_GRAY:Color.RED);
				btnSeats[i][j].setForeground(Color.WHITE);
				btnSeats[i][j].setFont(new Font("Tahoma", Font.BOLD, 10));
				btnSeats[i][j].setText(String.valueOf((char) (i + 65)) + (j + 1));
				panel_1.add(btnSeats[i][j]);
			}
			for (int j = 1; j < 6; j++) {
				btnSeats[i][j] = new JButton(String.valueOf(j + 1));
				btnSeats[i][j].addActionListener(this);
				btnSeats[i][j].setBackground(database[i][j]==0?Color.LIGHT_GRAY:Color.RED);
				btnSeats[i][j].setForeground(Color.WHITE);
				btnSeats[i][j].setFont(new Font("Tahoma", Font.BOLD, 10));
				btnSeats[i][j].setText(String.valueOf((char) (i + 65)) + (j + 1));
				panel_2.add(btnSeats[i][j]);
			}
			for (int j = 6; j < 7; j++) {
				btnSeats[i][j] = new JButton(String.valueOf(j + 1));
				btnSeats[i][j].addActionListener(this);
				btnSeats[i][j].setBackground(database[i][j]==0?Color.LIGHT_GRAY:Color.RED);
				btnSeats[i][j].setForeground(Color.WHITE);
				btnSeats[i][j].setFont(new Font("Tahoma", Font.BOLD, 10));
				btnSeats[i][j].setText(String.valueOf((char) (i + 65)) + (j + 1));
				panel_3.add(btnSeats[i][j]);
			}
		}
	}
	
	public void updateDisplaySeat(int x, int y){
		btnSeats[x][y].setBackground(database[x][y]==1?Color.RED:Color.LIGHT_GRAY);
	}
	
	// Mở port và lắng nghe các server khác kết nối tới
	public void listenServer(){
		try {
			serverSocket = new ServerSocket(portSocketServer[serverID-1]);
			while(true){
				this.socket = serverSocket.accept();
				try {
					receiveMessage();
				} catch (Exception e) {
					System.out.println("Error: receiveMessage()");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				if (e.getSource() == btnSeats[i][j]) {
					if(database[i][j]==2){// Nếu vé đang bị khóa
						JOptionPane.showMessageDialog(frame, "Vé đang được giao dịch!");
					}else{
						if(i!=xCur||j!=yCur){
							//this.database[i][j] = this.database[i][j] == 1 ? 0 : 1;
							btnSeats[i][j].setBackground(btnSeats[i][j].getBackground()==Color.LIGHT_GRAY?Color.RED:Color.LIGHT_GRAY);
							
							if(xCur!=-1&&yCur!=-1){
								//this.database[xCur][yCur] = this.database[xCur][yCur] == 1 ? 0 : 1;
								btnSeats[xCur][yCur].setBackground(btnSeats[xCur][yCur].getBackground()==Color.LIGHT_GRAY?Color.RED:Color.LIGHT_GRAY);				
							}
							
							xCur = i;
							yCur = j;
						}
					}		
				}
			}
		}
		
		if(e.getSource() == btnBook){
			btnBook.setEnabled(false);
			// Send REQ
			if(xCur==-1&&yCur==-1){
				JOptionPane.showMessageDialog(frame, "Bạn chưa chọn vé!");
			}
			else{
				if(database[xCur][yCur]==2){// Nếu vé đang bị khóa
					JOptionPane.showMessageDialog(frame, "Vé đang được giao dịch!");
				}
				else{
					this.lamport++;
					sendAll("REQ", 0, 0, this.lamport, this.serverID, xCur, yCur);	
				}
			}
			
		}
	}
	
	// Gởi thông điệp đến tất cả các trạm
	public void sendAll(String msg, int lamport_J, int serverID_J, int lamport_I, int serverID_I, int x, int y){
		Message message = new Message(msg, lamport_J, serverID_J, lamport_I, serverID_I, x, y);
		
		for(int k=0; k<numOfServer; k++){
			try {
				Socket s = new Socket("localhost", portSocketServer[k]);
				ObjectOutputStream out =  new ObjectOutputStream(s.getOutputStream());
				out.writeObject(message);
				out.flush();
				s.close();
			} catch (Exception e) {
				System.out.println("Error: sendAll()");
				e.printStackTrace();
			} 
		}
	}
	
	// Gởi thông điệp đến trạm chỉ định
	public void sendOne(String msg, int lamport_J, int serverID_J, int lamport_I, int serverID_I, int x, int y){
		Message message = new Message(msg, lamport_J, serverID_J, lamport_I, serverID_I, x, y);
		
		try {
			Socket s = new Socket("localhost", portSocketServer[serverID_I-1]);
			ObjectOutputStream out =  new ObjectOutputStream(s.getOutputStream());
			out.writeObject(message);
			out.flush();
			s.close();
		} catch (Exception e) {
			System.out.println("Error: sendAll()");
			e.printStackTrace();
		} 
	}
	
	// Nhận thông điệp và trả lời
	public void receiveMessage() throws Exception{
		ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
		Message message = (Message) in.readObject();
		String msg = message.getMsg();
		int lamport_I = message.getLamport_I();
		int serverID_I = message.getServerID_I();
		int lamport_J = message.getLamport_J();
		int serverID_J = message.getServerID_J();
		int x = message.getX();
		int y = message.getY();
		
		switch (msg) {
		case "REQ":
			updateLamport(lamport_I);
			//System.out.println("REQ "+lamport_I+"|"+serverID_I+"|"+(x+1)+(y+1));
			taProcess.append("REQ "+lamport_I+"|"+serverID_I+"|"+(x+1)+(y+1)+"\n");
			queueREQ.add(message);
			sortREQ();

			// Nếu REQ đầu hàng đợi của Server nào thì Server đó ko được gởi ACQ
			if(queueREQ.get(0).getServerID_I()!=this.serverID){
				//sendOne("ACQ", this.lamport, this.serverID, lamport_I, serverID_I, x, y);
				sendOne("ACQ", this.lamport, this.serverID, lamport_I, serverID_I, x, y);
			}
			break;
		case "ACQ":
			updateLamport(lamport_J);
			//System.out.println("ACQ "+lamport_J+"|"+serverID_J+"|"+lamport_I+"|"+serverID_I);
			taProcess.append("ACQ "+lamport_J+"|"+serverID_J+"|"+lamport_I+"|"+serverID_I+"\n");
			numOfACQ++;
			if(numOfACQ==numOfServer-1){
				numOfACQ=0;
				// Gởi thông điệp yêu cầu khóa đến tất cả các trạm
				sendAll("BLK", 0, 0, lamport_I, serverID_I, x, y);
			}
			break;
		case "REL":
			updateLamport(lamport_J);	
			//System.out.println("REL "+lamport_J+"|"+serverID_J+"|"+lamport_I+"|"+serverID_I);
			taProcess.append("REL "+lamport_J+"|"+serverID_J+"|"+lamport_I+"|"+serverID_I+"\n");
			queueREQ.remove(0);
            updateDisplaySeat(x, y);		
            
            btnBook.setEnabled(true);
            
            for(int i=0; i<queueREQ.size(); i++){
            	Message m = (Message) queueREQ.get(i);
            	int l_I = m.getLamport_I();
            	int s_I = m.getServerID_I();
            	int xR = m.getX();
            	int yR = m.getY();
            	
            	if(this.serverID!=s_I){
            		sendOne("ACQ", this.lamport, this.serverID, l_I, s_I, xR, yR);
            	}
            }
            
            if(this.serverID==serverID_I){
            	numOfACQ = 0;
            	numOfBLK = 0;
            	numOfUOK = 0;
            	xCur = -1;
            	yCur = -1;
            	statusTicketBlock = -1;
            }
			break;
		case "BLK":
			// Tiến hành khóa
			if(blockTicket(x, y)){
				sendOne("BOK", 0, 0, lamport_I, serverID_I, x, y);
			}
			else{
				sendOne("BFA", 0, 0, lamport_I, serverID_I, x, y);
			}					
			break;
		case "BOK":
			numOfBLK++;
			if(numOfBLK==numOfServer){
				numOfBLK=0;
				// Gởi thông điệp yêu cầu cập nhật dữ liệu
				sendAll("UPD", 0, 0, lamport_I, serverID_I, x, y);
			}
			break;
		case "UPD":
			// Tiến hành cập nhật dữ liệu
			if(updateTicket(x, y)){
				this.lamport++;
				sendOne("UOK", 0, 0, lamport_I, serverID_I, x, y);
			}
			else{
				this.lamport++;
				sendOne("UFA", 0, 0, lamport_I, serverID_I, x, y);
			}					
			break;
		case "UOK":
			numOfUOK++;
			if(numOfUOK==numOfServer){
				numOfUOK=0;
				
				sendAll("REL", this.lamport, this.serverID, lamport_I, serverID_I, x, y);
				//JOptionPane.showMessageDialog(frame, "Giao dịch thành công!");
			}
			break;
		}
			
	}
	
	// Khóa vé
	public boolean blockTicket(int xFirst, int yFirst){
		// Kiểm tra hàng đợi xem có yêu cầu nào trùng vé với yêu cầu đầu hàng đợi ko
		for(int i=1; i<queueREQ.size(); i++){
			Message m = (Message) queueREQ.get(i);
			int serverID_I = m.getServerID_I();
			int x = m.getX();
			int y = m.getY();
			if(xFirst==x&&yFirst==y){// Nếu trùng
				if(this.serverID==serverID_I){// Nếu trùng là của mình
					JOptionPane.showMessageDialog(frame, "Vé đang được giao dịch!");
					queueREQ.remove(i);
					
					numOfACQ = 0;// Reset ACQ
				}
				else{// Có trùng nhưng ko phải của mình
					queueREQ.remove(i);
				}
			}
		}
		
		// Sau khi kiểm tra, tiến hành khóa
		statusTicketBlock = database[xFirst][yFirst];
		database[xFirst][yFirst] = 2;// 2:khóa, 1:đã đặt, 0:chưa đặt
		btnSeats[xFirst][yFirst].setBackground(Color.YELLOW);
		
		return true;
	}
	
	// Cập nhật dữ liệu
	public boolean updateTicket(int x, int y){
		database[x][y] = statusTicketBlock==1?0:1;
		btnSeats[x][y].setBackground(database[x][y]==1?Color.RED:Color.LIGHT_GRAY);
		
		return true;
	}
		
	// Sắp xếp các yêu cầu trong hàng đợi
	public void sortREQ(){
		for(int i=0; i<queueREQ.size(); i++){
			for(int j=i+1; j<queueREQ.size(); j++){
				Message msg1 = (Message) queueREQ.get(i);
				Message msg2 = (Message) queueREQ.get(j);
				int serverID1 = msg1.getServerID_I();
				int serverID2 = msg2.getServerID_I();
				if(msg1.getLamport_I()>msg2.getLamport_I() || (msg1.getLamport_I()==msg2.getLamport_I()&&serverID1<serverID2)){
					queueREQ.set(i, msg2);
					queueREQ.set(j, msg1);
				}
			}
		}
	}
	
	// Cập nhật đồng hồ logic Lamport
	public void updateLamport(int lamportREQ){
		if(lamportREQ>this.lamport){
			this.lamport = lamportREQ + 1;
		}
		else{
			this.lamport = this.lamport + 1;
		}
	}	
}
