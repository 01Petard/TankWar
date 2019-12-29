package cn.hzx.model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;

import cn.hzx.frame.TankClient;

public class Basement {
	int x,y;
	TankClient tc;
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
	private static Image wallImages = tk.getImage(Basement.class.getClassLoader().getResource("images/basement.png"));
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Basement(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(live)g.drawImage(wallImages, x, y, null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,wallImages.getWidth(null),wallImages.getHeight(null));
	}
	
	/**
	 * 基地如果与子弹相撞，两者都死
	 * @param missiles
	 * @return
	 */
	public boolean beHitWithMissile(Missile e) {
		if(this.live && this.getRect().intersects(e.getRect()) && e.isGood()==false) {
			this.live = false;
			tc.myTank.setLive(false);
			e.setLive(false);
			return true;
		}
		return false;
	}
	public boolean beHitWithMissiles(List<Missile> missiles) {
		for(int i=0;i<missiles.size();i++) {
			if(beHitWithMissile(missiles.get(i)))
				return true;//打中了返回true
		}
		return false;//没打中返回false
	}
}
