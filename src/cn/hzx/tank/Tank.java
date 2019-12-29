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
	private int step = r.nextInt(12)+3;//step����̹���ƶ��Ĳ����������ƶ�3��
	
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
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
				tc.tanks.remove(this);//̹�˲����ŵĻ�Ҳ�Ƴ���
			}
			return;
		}
		if(good) bb.draw(g);//ֻ���õ�̹�˻�Ѫ��
		
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
		this.oldX = x;//������һ��̹�����ڵ�λ�ã�ʹ̹�˼�ʹײ��ǽ���Ժ�Ҳ�����޷��ƶ�
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
		if(this.dir!=Direction.STOP) {//����Ͳ�����̹��һ��,��ͣ�͸��ƶ�����һ����ͣ�˾ͳ���֮ͣǰ�ķ���
			this.ptDir = this.dir;
		}
		
		if(x<0)x=0;
		if(y<30)y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH)x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT)y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		//�û���̹������ƶ�
		if(!good) {
			Direction[] dirs = Direction.values();//˵�ĳ����Ļ������ǰ�D,L,R,U,STOPת��Ϊ��ֵ
			if(step==0) {
				step=r.nextInt(12)+3;
				int rn = r.nextInt(dirs.length);//�������һ��0~dirs.length-1��һ����
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38)this.fire();
		}
	}
	/**
	 * ��̹�˵�λ�ñ���֮ǰ��λ�ã��������Լ����ײ
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
			this.setLive(true);//����
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
	 * �ж�̹�˷���
	 */
	void locateDirection(){
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	Missile fire() {//��Ϊ�����𡱻����ӵ������Է���ֵ�ǡ��ӵ���
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
	 * ̹���޷���ǽ�����ǲ���ǽ����ǽ�Ѿ�����
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
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	/**
	 * ̹���޷�����ǽ
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
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	/**
	 * ̹���޷�������
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
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(this!=t) {//��˼����˵,����̹�˲���ͬһ��̹��
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
			g.drawRect(x, y-10, WIDTH, 10);//Ѫ����
			int w = WIDTH*life/100;//ʵ��Ѫ���Ŀ��
			g.fillRect(x, y-10, w, 10);//ʵ��Ѫ��
			g.setColor(c);
		}
		
	}
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);//̹�˳���Ѫ���Ѫ�������
			return true;
		}
		return false;
	}
	
	
}
