package cn.hzx.frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import cn.hzx.frame.TankClient;

public class BackGround {
	int x,y;
	TankClient tc;
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
	private static Image wallImages = tk.getImage(BackGround.class.getClassLoader().getResource("images/bg.jpg"));
	public BackGround(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}	
	public void draw(Graphics g) {
		g.drawImage(wallImages, x, y, null);
	}
}
