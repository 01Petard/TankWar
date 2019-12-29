package cn.hzx.wall;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import cn.hzx.frame.TankClient;
import cn.hzx.model.Missile;

public class Steel {
	int x,y;
	TankClient tc;
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
	private static Image wallImages = tk.getImage(Steel.class.getClassLoader().getResource("images/steel.gif"));
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public Steel(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		g.drawImage(wallImages, x, y, null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,wallImages.getWidth(null),wallImages.getHeight(null));
	}
	
	/**
	 * 铁墙如果与子弹相撞，铁墙不会死，而子弹会死
	 * @param missiles
	 * @return
	 */
	public boolean beHitWithMissile(Missile e) {
		if(this.live && this.getRect().intersects(e.getRect())) {
			e.setLive(false);
			return true;
		}
		return false;
	}
	public boolean beHitWithMissiles(java.util.List<Missile> missiles) {
		for(int i=0;i<missiles.size();i++) {
			if(beHitWithMissile(missiles.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
}
