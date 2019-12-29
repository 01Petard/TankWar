package cn.hzx.model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hzx.frame.TankClient;
import cn.hzx.tank.Direction;
import cn.hzx.tank.Tank;
import cn.hzx.util.AudioPlayer;
import cn.hzx.util.AudioUtil;
import cn.hzx.wall.Steel;
import cn.hzx.wall.Wall;


public class Missile {
	public static final int XSPEED=10,YSPEED=10;//�ӵ����ٶ�
	public static final int WIDTH=10,HEIGHT=10;//�ӵ��Ŀ�Ⱥ͸߶�
	int x,y;
	Direction dir;//�ӵ����з�������ԣ���̹���е�һ������
	private boolean good;
	public boolean isGood() {
		return good;
	}
	private boolean live = true;
	private TankClient tc;
	
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
		private static Toolkit tk = Toolkit.getDefaultToolkit();
		//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
		private static Image[] missileImages = null;
		private static Map<String,Image> imgs = new HashMap<String,Image>();
		static {
			missileImages  =new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
			};
			imgs.put("L", missileImages[0]);
			imgs.put("U", missileImages[1]);
			imgs.put("R", missileImages[2]);
			imgs.put("D", missileImages[3]);
		}
	

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y,boolean good,Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);//����ӵ���ײ�ˣ����Ƴ��ӵ�
			return;
		}
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		default:
			break;
		}
		
		move();
	}
	
	private void move() {
		
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
		default:
			break;
		}
		
		if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT) {
			live = false;//�ӵ������߽��ˣ����ж�Ϊ����
//			tc.missiles.remove(this);//��������ֱ�Ӿ�ȥ�����ڵ��Լ���������TankClient����������
		}
		
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * ��ȡ�ӵ����εķ���
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public static int killcount = 0;
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good!=t.isGood() && tc.myTank.isLive()==true) {
			if(t.isGood()) {//����Ǻ�̹�˾�ÿ���ܵ���������20Ѫ
				t.setLife(t.getLife()-20);
				new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
				if(t.getLife() <= 0) {
					t.setLive(false);
					new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();
				}
			}else {//����ǻ�̹��ÿ���ܵ�������ֱ�Ӿ�����
				new AudioPlayer(AudioUtil.BLAST).new AudioThread().start();
				t.setLive(false);
				killcount++;
			}
			
			this.live = false;
			Explode e = new Explode(x,y,tc);//�ӵ�����̹��ʱ�����ӵ���λ�ó���һ����ը��Ч����Ҳ����ʵ����һ����ը
			tc.explodes.add(e);//��ӱ�ըЧ��
			return true;//�ӵ���̹���ཻ�ˣ�return true
		}
		return false;//û�о�return false
	}
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i)))
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	/**
	 * �ӵ����������ǽ����ô�ӵ������ˣ��ӵ���ʧ���Ͳ�����
	 * @param w
	 * @return
	 */
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()) {
			this.live = false;
			return true;
		}
		return false;
	}
	public boolean hitWalls(List<Wall> walls) {
		for(int i=0;i<walls.size();i++) {
			if(hitWall(walls.get(i)))
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	/**
	 * �ӵ������������ǽ����ô�ӵ������ˣ��ӵ���ʧ���Ͳ�����
	 * @param w
	 * @return
	 */
	public boolean hitSteel(Steel s) {
		if(this.live && this.getRect().intersects(s.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	public boolean hitSteels(List<Steel> steels) {
		for(int i=0;i<steels.size();i++) {
			if(hitSteel(steels.get(i)))
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
	/**
	 * �ӵ���������˻��أ���ô�ӵ������ˣ��ӵ���ʧ���Ͳ�����
	 * @param w
	 * @return
	 */
	public boolean hitBasement(Basement bm) {
		if(this.live && this.getRect().intersects(bm.getRect()) && this.isGood()==false) {
			this.live = false;
			return true;
		}
		return false;
	}
		
}
