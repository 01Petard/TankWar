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
	
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
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
	};//��ը��ͼƬ���Ҽ�����һ�㣬�ñ�ը��Ѹ�٣������޸ĵĻ���ȥ��ע�;Ϳ�����
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
			tc.explodes.remove(this);//��ը�������ˣ����Ƴ�
			return;
		}
		if(step==imgs.length) {//���step������ˣ��͹����Ҳ�����
			live  = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step], this.x-Tank.WIDTH, this.y-Tank.HEIGHT, null);
		step++;
	}
}
