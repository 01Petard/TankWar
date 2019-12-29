package cn.hzx.model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import cn.hzx.frame.TankClient;
import cn.hzx.tank.Tank;

public class Explode {
	int x,y;
	private boolean live = true;
	
	private TankClient tc;
	
	//让Toolkit拿到默认工具包，用工具包中的方法把硬盘中的图片拿到内存中来
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//设为静态变量，防止图片在Explode重新初始化时，再从硬盘上拽一回
	private static Image[] imgs = {
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast5.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast6.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast7.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast8.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast9.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast10.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast11.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast12.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast13.gif")),
//		tk.getImage(Explode.class.getClassLoader().getResource("images/blast14.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/blast15.gif")),
	};//爆炸的图片，我减少了一点，让爆炸更迅速，后期修改的话，去掉注释就可以了
	int step = 0;
	
	private static boolean init = false;
	
	public Explode(int x,int y,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}
		
		if(!live) {
			tc.explodes.remove(this);//爆炸不活着了，就移除
			return;
		}
		if(step==imgs.length) {//如果step到最后了，就归零且不画了
			live  = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step], this.x-Tank.WIDTH, this.y-Tank.HEIGHT, null);
		step++;
	}
}
