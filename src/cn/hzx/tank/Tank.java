package cn.hzx.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.hzx.frame.TankClient;
import cn.hzx.model.Blood;
import cn.hzx.model.Missile;
import cn.hzx.util.AudioPlayer;
import cn.hzx.util.AudioUtil;
import cn.hzx.wall.Ocean;
import cn.hzx.wall.Steel;
import cn.hzx.wall.Wall;

public class Tank {

	public static final int XSPEED=6,YSPEED=6;
	public static final int WIDTH = 60,HEIGHT = 60;
	
	private BloodBar bb = new BloodBar();
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	private int life = 100;
	public int getLife() {
		return life;
	}
	public void setLife(int life){
		this.life = life;
	}

	TankClient tc;
	
	private boolean good;
	public boolean isGood() {
		return good;
	}
	private boolean bad;
	public boolean isBad() {
		return bad;
	}
	private int x,y;
	private int oldX,oldY;
	
	private static Random r = new Random();
	private boolean bL=false,bU=false,bR=false,bD=false;
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.U;
	private int step = r.nextInt(12)+3;//step代表坦克移动的步数，至少移动3步
	
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
	private static Image[] tankImages = null;
	private static Map<String,Image> imgs = new HashMap<String,Image>();
	static {
		tankImages  = new Image[] {
			tk.getImage(Tank.class.getClassLoader().getResource("images/p1tankL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p1tankU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p1tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p1tankD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p2tankL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p2tankU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p2tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/p2tankD.gif")),
		};
		imgs.put("myL", tankImages[0]);
		imgs.put("myU", tankImages[1]);
		imgs.put("myR", tankImages[2]);
		imgs.put("myD", tankImages[3]);
		imgs.put("enemyL", tankImages[4]);
		imgs.put("enemyU", tankImages[5]);
		imgs.put("enemyR", tankImages[6]);
		imgs.put("enemyD", tankImages[7]);
	}
	
	
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc) {
		this(x,y,good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);//坦克不活着的话也移除了
			}
			return;
		}
		if(good) bb.draw(g);//只给好的坦克画血条
		
		if(good) {
			switch(ptDir) {
			case L:
				g.drawImage(imgs.get("myL"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("myU"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("myR"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("myD"), x, y, null);
				break;
			default:
				break;
			}
		}else {
			switch(ptDir) {
			case L:
				g.drawImage(imgs.get("enemyL"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("enemyU"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("enemyR"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("enemyD"), x, y, null);
				break;
			default:
				break;
			}
		}
		
		move();
	}
	
	void move() {
		this.oldX = x;//记载上一步坦克所在的位置，使坦克即使撞到墙了以后也不会无法移动
		this.oldY = y;
		switch(dir) {
		case L:
			x-=XSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case D:
			y+=YSPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir!=Direction.STOP) {//让炮筒方向跟坦克一致,不停就跟移动方向一样，停了就朝向停之前的方向
			this.ptDir = this.dir;
		}
		
		if(x<0)x=0;
		if(y<30)y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH)x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT)y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		//让坏的坦克随机移动
		if(!good) {
			Direction[] dirs = Direction.values();//说的抽象点的话，就是把D,L,R,U,STOP转换为数值
			if(step==0) {
				step=r.nextInt(12)+3;
				int rn = r.nextInt(dirs.length);//随机产生一个0~dirs.length-1的一个数
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38)this.fire();
		}
	}
	/**
	 * 让坦克的位置保持之前的位置，这样可以检测碰撞
	 */
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	public void KeyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}
	public void KeyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_A:
			fire();
			break;
		case KeyEvent.VK_S:
			superfire();
			break;
		case KeyEvent.VK_R:
			this.setLife(100);
			this.setLive(true);//复活
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locateDirection();
	}

	/**
	 * 判断坦克方向
	 */
	void locateDirection(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	Missile fire() {//因为“开火”会打出子弹，所以返回值是“子弹”
		AudioPlayer fire=new AudioPlayer(AudioUtil.FIRE);
		fire.new AudioThread().start();
		if(!live)return null;
		int x = this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y = this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir) {
		if(!live)return null;
		int x = this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y = this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	private void superfire() {
		Direction[] dirs = Direction.values();
		for(int i=0;i<4;i++) {
			fire(dirs[i]);
		}
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,tankImages[0].getWidth(null),tankImages[0].getHeight(null));
	}
	
	/**
	 * 坦克无法穿墙，除非不与墙碰或墙已经死了
	 * @param w
	 * @return
	 */
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithWalls(java.util.List<Wall> walls) {
		for(int i=0;i<walls.size();i++) {
			if(collidesWithWall(walls.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	/**
	 * 坦克无法穿铁墙
	 * @param w
	 * @return
	 */
	public boolean collidesWithSteel(Steel s) {
		if(this.live && this.getRect().intersects(s.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithSteels(java.util.List<Steel> steels) {
		for(int i=0;i<steels.size();i++) {
			if(collidesWithSteel(steels.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	/**
	 * 坦克无法穿海洋
	 * @param w
	 * @return
	 */
	public boolean collidesWithOcean(Ocean o) {
		if(this.live && this.getRect().intersects(o.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	public boolean collidesWithOceans(java.util.List<Ocean> oceans) {
		for(int i=0;i<oceans.size();i++) {
			if(collidesWithOcean(oceans.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(this!=t) {//意思就是说,两辆坦克不是同一辆坦克
				if(this.live && this.getRect().intersects(t.getRect())) {
				this.stay();
				t.stay();
				return true;
				}
			}
		}
		return false;
	}
	
	private class BloodBar{
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, WIDTH, 10);//血条框
			int w = WIDTH*life/100;//实际血量的宽度
			g.fillRect(x, y-10, w, 10);//实际血量
			g.setColor(c);
		}
		
	}
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);//坦克吃了血块后，血块就死了
			return true;
		}
		return false;
	}
	
	
}
