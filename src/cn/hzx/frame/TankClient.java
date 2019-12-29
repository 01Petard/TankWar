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
	 * 坦克大战正式版4.1
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 960;
	public static final int GAME_HEIGHT = 800;
	
	public Tank myTank = new Tank(230,700,true,Direction.STOP,this);
	public Blood b = new Blood();
	public Basement bm = new Basement(450,740,this);//myTank可以穿过基地，不会打死基地
	public BackGround bg = new BackGround(0,0,this);
	
	public ArrayList<Missile> missiles = new ArrayList<Missile>();
	public List<Explode> explodes = new ArrayList<Explode>();
	public List<Tank> tanks = new ArrayList<Tank>();
	public List<Wall> walls = new ArrayList<Wall>();
	public List<Steel> steels = new ArrayList<Steel>();
	public List<Forest> forests = new ArrayList<Forest>();
	public List<Ocean> oceans = new ArrayList<Ocean>();
	
	Image offScreanImage = null;
	
	public void init()//初始化墙的顺序必须是：铁块、森林、海洋、墙
	{
		for(int i=0;i<2;i++) 
		{
			steels.add(new Steel(420+i*60,260,this));
			steels.add(new Steel(180+i*60,560,this));
			steels.add(new Steel(GAME_WIDTH-(180+(i+1)*60),560,this));
		}//添加铁块

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
		}//添加森林
		for(int i=0;i<2;i++) {
			if(!(i<2))break;
			oceans.add(new Ocean(180+i*60,380,this));
			oceans.add(new Ocean(GAME_WIDTH-(180+(i+1)*60),380,this));
		}//添加海洋
		for(int i=0;i<3;i++) 
		{
			if(i==0) {
				walls.add(new Wall(390,740,this));
				walls.add(new Wall(510,740,this));
				walls.add(new Wall(450,680,this));
				walls.add(new Wall(390,680,this));
				walls.add(new Wall(510,680,this));
			}//给基地周围加一圈墙
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
		}//添加墙
	}
	
	public void paint(Graphics g) {
		bg.draw(g);
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString("数据统计：", 10, 50);
		g.drawString("missiles count:"+missiles.size(), 10, 65);//打印炮弹数量
		g.drawString("explodes count:"+explodes.size(), 10, 80);//打印爆炸数量
		g.drawString("tanks count:"+tanks.size(), 10, 95);//打印敌方坦克数量
		g.drawString("mytanks's life:"+myTank.getLife(), 10, 110);//打印我方坦克血量
		g.drawString("操作提示：", 10, 710);
		g.drawString("↑↓←→：移动", 10, 725);
		g.drawString("A：开火", 10, 740);
		g.drawString("S：超级开火", 10, 755);
		g.drawString("R：复活", 10, 770);
		g.drawString("made by 黄泽校", 10, 785);
		g.setColor(c);
		bm.beHitWithMissiles(missiles);
		bm.draw(g);
		for(int i=0;i<oceans.size();i++) {
			Ocean o = oceans.get(i);
			o.beHitWithMissiles(missiles);
			o.draw(g);
		}//画海洋，先画海洋是为了让坦克打出的子弹在海洋上，而不会被海洋覆盖
		for(int i=0;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWalls(walls);//子弹无法穿墙，除非把墙破坏掉
			m.hitSteels(steels);//子弹无法穿铁墙
			m.hitBasement(bm);
			if(myTank.isLive()) {	
				m.draw(g);
			}//myTank活着画子弹，死了就不画
		}//画子弹
		for(int i=0;i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}//画爆炸
		myTank.draw(g);//调用了Tank中的draw方法
		myTank.collidesWithTanks(tanks);//让自己无法穿过其他坦克
		myTank.collidesWithWalls(walls);//让自己无法穿过墙
		myTank.collidesWithSteels(steels);
		myTank.collidesWithOceans(oceans);
		myTank.eat(b);//让自己吃血块
		b.draw(g);
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithTanks(tanks);
			t.collidesWithWalls(walls);
			t.collidesWithSteels(steels);
			t.collidesWithOceans(oceans);
			t.draw(g);
		}//画坏的坦克
		for(int i=0;i<walls.size();i++) {
			Wall w = walls.get(i);
			if(w.beHitWithMissiles(missiles))
			{
				walls.remove(w);
				i--;
			}
			w.draw(g);
		}//画墙
		for(int i=0;i<steels.size();i++) {
			Steel s = steels.get(i);
			s.beHitWithMissiles(missiles);
			s.draw(g);
		}//画铁墙
		for(int i=0;i<forests.size();i++) {
			Forest f = forests.get(i);
			f.beHitWithMissiles(missiles);
			f.draw(g);
		}//画森林，森林比坦克晚画，是为了覆盖住坦克的子弹
		if(!myTank.isLive()) {
			bm.setLive(true);
			printInfo(g,"Game Over",80,300,350,Color.black);//打印死亡Game Over
			printInfo(g,"你击杀了"+Missile.killcount+"辆敌方坦克",70,150,450,Color.black);//打印成绩
			return;
		}
		//如果杀光所有敌人后，就会打印结束语
		if(myTank.isLive()&&tanks.size()==0) {
			printInfo(g,"恭喜你，获得胜利！",80,150,400,Color.red);
			return;
		}
	}
	/**
	 * 游戏中打印大字的方法
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
		Font f = new Font("黑体",Font.BOLD,size);
		g.setFont(f);
		g.drawString(str,x,y);
		g.setColor(c);
	}
	/**
	 * 利用双缓冲解决屏闪，update是paint之前的方法，让update截住paint，在之前画一张虚拟的背景，解决屏闪问题
	 */
	public void update(Graphics g) {
		if(offScreanImage==null){
			offScreanImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);//设置虚拟背景图，大小和窗口一样
		}
		Graphics gOffScrean = offScreanImage.getGraphics();//拿到虚拟背景图的画笔
		Color c = gOffScrean.getColor();
		gOffScrean.setColor(Color.BLACK);//设置背景色
		gOffScrean.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScrean.setColor(c);
		paint(gOffScrean);//用paint方法画一遍
		g.drawImage(offScreanImage, 0, 0, null);
	}
	/**
	 * 开启窗口
	 */
	public void launchFrame() {
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		for(int i=0;i<initTankCount;i++) {
			tanks.add(new Tank(50 + 70*(i+1), 50,false,Direction.D,this));
		}//生成十个敌人
		this.setLocation(150,100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int closeCode = JOptionPane.showConfirmDialog(TankClient.this, "确定退出游戏？", "提示！",JOptionPane.YES_NO_OPTION);// 弹出选择对话框，并记录用户选择
				if (closeCode == JOptionPane.YES_OPTION) {// 如果选择确定
					System.exit(0);// 关闭程序
				}
			}
		});
		this.setResizable(false);
		this.setTitle("坦克大战");
		this.setBackground(Color.orange);
		setVisible(true);
		new Thread(new PaintThread()).start();
		this.addKeyListener(new KeyMonitor());
		new AudioPlayer(AudioUtil.START).new AudioThread().start();//游戏开始，播放背景音效。新建一个音效线程，用于播放音效。
		init();
	}
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	/**
	 * 重画线程
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
	 * 键盘监听类
	 */
	private class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);//这里调用了Tank中的KeyPressed方法
		}
		public void keyReleased(KeyEvent e) {
			myTank.KeyReleased(e);//这里调用了Tank中的KeyReleased方法
		}
	}
}
