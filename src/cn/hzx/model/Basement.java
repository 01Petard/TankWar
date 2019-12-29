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
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
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
	 * ����������ӵ���ײ�����߶���
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
				return true;//�����˷���true
		}
		return false;//û���з���false
	}
}
