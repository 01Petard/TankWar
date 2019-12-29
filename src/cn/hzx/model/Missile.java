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
	public static final int XSPEED=10,YSPEED=10;//子弹的速度
	public static final int WIDTH=10,HEIGHT=10;//子弹的宽度和高度
	int x,y;
	Direction dir;//子弹具有方向的属性，是坦克中的一个属性
	private boolean good;
	public boolean isGood() {
		return good;
	}
	private boolean live = true;
	private TankClient tc;
	
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
		private static Toolkit tk = Toolkit.getDefaultToolkit();
		//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
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
			tc.missiles.remove(this);//如果子弹碰撞了，就移除子弹
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
			live = false;//子弹碰到边界了，就判定为死了
//			tc.missiles.remove(this);//这种做法直接就去掉了炮弹自己，不用在TankClient中做操作了
		}
		
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * 获取子弹矩形的方法
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public static int killcount = 0;
	public boolean hitTank(Tank t) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good!=t.isGood() && tc.myTank.isLive()==true) {
			if(t.isGood()) {//如果是好坦克就每次受到攻击，减20血
				t.setLife(t.getLife()-20);
				new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
				if(t.getLife() <= 0) {
					t.setLive(false);
					new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();
				}
			}else {//如果是坏坦克每次受到攻击，直接就死了
				new AudioPlayer(AudioUtil.BLAST).new AudioThread().start();
				t.setLive(false);
				killcount++;
			}
			
			this.live = false;
			Explode e = new Explode(x,y,tc);//子弹打中坦克时，在子弹的位置出来一个爆炸的效果，也就是实例化一个爆炸
			tc.explodes.add(e);//添加爆炸效果
			return true;//子弹和坦克相交了，return true
		}
		return false;//没有就return false
	}
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	/**
	 * 子弹如果击中了墙，那么子弹就死了，子弹消失，就不画了
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
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	/**
	 * 子弹如果击中了铁墙，那么子弹就死了，子弹消失，就不画了
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
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
	/**
	 * 子弹如果击中了基地，那么子弹就死了，子弹消失，就不画了
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
