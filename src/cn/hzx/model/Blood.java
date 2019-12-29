package cn.hzx.model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import cn.hzx.frame.TankClient;
import cn.hzx.wall.Ocean;

public class Blood {
	int x,y,w,h;
	TankClient tc;
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
	private static Image bloodImages = tk.getImage(Ocean.class.getClassLoader().getResource("images/blood.gif"));
	int step = 0;
	
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * Ѫ���ƶ���·��
	 */
	private int[][] pos= {
			{740,700},{745,700},{750,700},{755,700},{760,700},
			{760,700},{760,705},{760,710},{760,715},{760,720},
			{760,720},{755,720},{750,720},{745,720},{740,720},
			{740,720},{740,715},{740,710},{740,705},{740,700},
	                  };
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	public void draw(Graphics g) {
		if(!live)return;
		g.drawImage(bloodImages, x, y, null);
		move();
	}

	private void move() {
		step++;
		if(step==pos.length) {
			step=0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
}
