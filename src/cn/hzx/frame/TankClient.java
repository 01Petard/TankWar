package cn.hzx.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import cn.hzx.model.Basement;
import cn.hzx.model.Blood;
import cn.hzx.model.Explode;
import cn.hzx.model.Missile;
import cn.hzx.tank.Direction;
import cn.hzx.tank.Tank;
import cn.hzx.util.AudioPlayer;
import cn.hzx.util.AudioUtil;
import cn.hzx.util.PropertyMgr;
import cn.hzx.wall.Forest;
import cn.hzx.wall.Ocean;
import cn.hzx.wall.Steel;
import cn.hzx.wall.Wall;



public class TankClient extends Frame{

	/**
	 * ̹�˴�ս��ʽ��4.1
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 960;
	public static final int GAME_HEIGHT = 800;
	
	public Tank myTank = new Tank(230,700,true,Direction.STOP,this);
	public Blood b = new Blood();
	public Basement bm = new Basement(450,740,this);//myTank���Դ������أ������������
	public BackGround bg = new BackGround(0,0,this);
	
	public ArrayList<Missile> missiles = new ArrayList<Missile>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Tank> tanks = new ArrayList<Tank>();
	public List<Wall> walls = new ArrayList<Wall>();
	public List<Steel> steels = new ArrayList<Steel>();
	public List<Forest> forests = new ArrayList<Forest>();
	public List<Ocean> oceans = new ArrayList<Ocean>();
	
	Image offScreanImage = null;
	
	public void init()//��ʼ��ǽ��˳������ǣ����顢ɭ�֡�����ǽ
	{
		for(int i=0;i<2;i++) 
		{
			steels.add(new Steel(420+i*60,260,this));
			steels.add(new Steel(180+i*60,560,this));
			steels.add(new Steel(GAME_WIDTH-(180+(i+1)*60),560,this));
		}//�������

		for(int i=0;i<4;i++) {
			if(!(i<4))break;
			forests.add(new Forest(180+i*60,260,this));
			forests.add(new Forest(GAME_WIDTH-(180+(i+1)*60),260,this));
			forests.add(new Forest(180+i*60,320,this));
			forests.add(new Forest(GAME_WIDTH-(180+(i+1)*60),320,this));
			if(i>=2) {
				forests.add(new Forest(180+i*60,380,this));
				forests.add(new Forest(GAME_WIDTH-(180+(i+1)*60),380,this));
			}
			forests.add(new Forest(180+i*60,440,this));
			forests.add(new Forest(GAME_WIDTH-(180+(i+1)*60),440,this));
			forests.add(new Forest(180+i*60,500,this));
			forests.add(new Forest(GAME_WIDTH-(180+(i+1)*60),500,this));
		}//���ɭ��
		for(int i=0;i<2;i++) {
			if(!(i<2))break;
			oceans.add(new Ocean(180+i*60,380,this));
			oceans.add(new Ocean(GAME_WIDTH-(180+(i+1)*60),380,this));
		}//��Ӻ���
		for(int i=0;i<3;i++) 
		{
			if(i==0) {
				walls.add(new Wall(390,740,this));
				walls.add(new Wall(510,740,this));
				walls.add(new Wall(450,680,this));
				walls.add(new Wall(390,680,this));
				walls.add(new Wall(510,680,this));
			}//��������Χ��һȦǽ
			steels.add(new Steel(i*60,200,this));
			steels.add(new Steel(GAME_WIDTH-(i+1)*60,200,this));
			walls.add(new Wall(i*60,260,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,260,this));
			walls.add(new Wall(i*60,320,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,320,this));
			walls.add(new Wall(i*60,380,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,380,this));
			walls.add(new Wall(i*60,380,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,380,this));
			walls.add(new Wall(i*60,440,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,440,this));
			walls.add(new Wall(i*60,500,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,500,this));
			walls.add(new Wall(i*60,560,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,560,this));
			walls.add(new Wall(i*60,620,this));
			walls.add(new Wall(GAME_WIDTH-(i+1)*60,620,this));
			walls.add(new Wall(420,320,this));
			walls.add(new Wall(480,320,this));
			walls.add(new Wall(420,GAME_HEIGHT-(i+5)*60,this));
			walls.add(new Wall(480,GAME_HEIGHT-(i+5)*60,this));
			walls.add(new Wall(300+i*60,560,this));
			walls.add(new Wall(GAME_WIDTH-(300+(i+1)*60),560,this));
		}//���ǽ
	}
	
	public void paint(Graphics g) {
		bg.draw(g);
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString("����ͳ�ƣ�", 10, 50);
		g.drawString("missiles count:"+missiles.size(), 10, 65);//��ӡ�ڵ�����
		g.drawString("explodes count:"+explodes.size(), 10, 80);//��ӡ��ը����
		g.drawString("tanks count:"+tanks.size(), 10, 95);//��ӡ�з�̹������
		g.drawString("mytanks's life:"+myTank.getLife(), 10, 110);//��ӡ�ҷ�̹��Ѫ��
		g.drawString("������ʾ��", 10, 710);
		g.drawString("�����������ƶ�", 10, 725);
		g.drawString("A������", 10, 740);
		g.drawString("S����������", 10, 755);
		g.drawString("R������", 10, 770);
		g.drawString("made by ����У", 10, 785);
		g.setColor(c);
		bm.beHitWithMissiles(missiles);
		bm.draw(g);
		for(int i=0;i<oceans.size();i++) {
			Ocean o = oceans.get(i);
			o.beHitWithMissiles(missiles);
			o.draw(g);
		}//�������Ȼ�������Ϊ����̹�˴�����ӵ��ں����ϣ������ᱻ���󸲸�
		for(int i=0;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWalls(walls);//�ӵ��޷���ǽ�����ǰ�ǽ�ƻ���
			m.hitSteels(steels);//�ӵ��޷�����ǽ
			m.hitBasement(bm);
			if(myTank.isLive()) {	
				m.draw(g);
			}//myTank���Ż��ӵ������˾Ͳ���
		}//���ӵ�
		for(int i=0;i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}//����ը
		myTank.draw(g);//������Tank�е�draw����
		myTank.collidesWithTanks(tanks);//���Լ��޷���������̹��
		myTank.collidesWithWalls(walls);//���Լ��޷�����ǽ
		myTank.collidesWithSteels(steels);
		myTank.collidesWithOceans(oceans);
		myTank.eat(b);//���Լ���Ѫ��
		b.draw(g);
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithTanks(tanks);
			t.collidesWithWalls(walls);
			t.collidesWithSteels(steels);
			t.collidesWithOceans(oceans);
			t.draw(g);
		}//������̹��
		for(int i=0;i<walls.size();i++) {
			Wall w = walls.get(i);
			if(w.beHitWithMissiles(missiles))
			{
				walls.remove(w);
				i--;
			}
			w.draw(g);
		}//��ǽ
		for(int i=0;i<steels.size();i++) {
			Steel s = steels.get(i);
			s.beHitWithMissiles(missiles);
			s.draw(g);
		}//����ǽ
		for(int i=0;i<forests.size();i++) {
			Forest f = forests.get(i);
			f.beHitWithMissiles(missiles);
			f.draw(g);
		}//��ɭ�֣�ɭ�ֱ�̹��������Ϊ�˸���ס̹�˵��ӵ�
		if(!myTank.isLive()) {
			bm.setLive(true);
			printInfo(g,"Game Over",80,300,350,Color.black);//��ӡ����Game Over
			printInfo(g,"���ɱ��"+Missile.killcount+"���з�̹��",70,150,450,Color.black);//��ӡ�ɼ�
			return;
		}
		//���ɱ�����е��˺󣬾ͻ��ӡ������
		if(myTank.isLive()&&tanks.size()==0) {
			printInfo(g,"��ϲ�㣬���ʤ����",80,150,400,Color.red);
			return;
		}
	}
	/**
	 * ��Ϸ�д�ӡ���ֵķ���
	 * @param g
	 * @param str
	 * @param size
	 * @param x
	 * @param y
	 * @param color
	 */
	public void printInfo(Graphics g,String str,int size,int x,int y,Color color){
		Color c= g.getColor();
		g.setColor(color);
		Font f = new Font("����",Font.BOLD,size);
		g.setFont(f);
		g.drawString(str,x,y);
		g.setColor(c);
	}
	/**
	 * ����˫������������update��paint֮ǰ�ķ�������update��סpaint����֮ǰ��һ������ı����������������
	 */
	public void update(Graphics g) {
		if(offScreanImage==null){
			offScreanImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);//�������ⱳ��ͼ����С�ʹ���һ��
		}
		Graphics gOffScrean = offScreanImage.getGraphics();//�õ����ⱳ��ͼ�Ļ���
		Color c = gOffScrean.getColor();
		gOffScrean.setColor(Color.BLACK);//���ñ���ɫ
		gOffScrean.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScrean.setColor(c);
		paint(gOffScrean);//��paint������һ��
		g.drawImage(offScreanImage, 0, 0, null);
	}
	/**
	 * ��������
	 */
	public void launchFrame() {
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		for(int i=0;i<initTankCount;i++) {
			tanks.add(new Tank(50 + 70*(i+1), 50,false,Direction.D,this));
		}//����ʮ������
		this.setLocation(150,100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int closeCode = JOptionPane.showConfirmDialog(TankClient.this, "ȷ���˳���Ϸ��", "��ʾ��",JOptionPane.YES_NO_OPTION);// ����ѡ��Ի��򣬲���¼�û�ѡ��
				if (closeCode == JOptionPane.YES_OPTION) {// ���ѡ��ȷ��
					System.exit(0);// �رճ���
				}
			}
		});
		this.setResizable(false);
		this.setTitle("̹�˴�ս");
		this.setBackground(Color.orange);
		setVisible(true);
		new Thread(new PaintThread()).start();
		this.addKeyListener(new KeyMonitor());
		new AudioPlayer(AudioUtil.START).new AudioThread().start();//��Ϸ��ʼ�����ű�����Ч���½�һ����Ч�̣߳����ڲ�����Ч��
		init();
	}
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	/**
	 * �ػ��߳�
	 * @author 15203
	 *
	 */
	private class PaintThread implements Runnable{
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ���̼�����
	 */
	private class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);//���������Tank�е�KeyPressed����
		}
		public void keyReleased(KeyEvent e) {
			myTank.KeyReleased(e);//���������Tank�е�KeyReleased����
		}
	}
}
