package cn.hzx.frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import cn.hzx.frame.TankClient;

public class BackGround {
	int x,y;
	TankClient tc;
	//��Toolkit�õ�Ĭ�Ϲ��߰����ù��߰��еķ�����Ӳ���е�ͼƬ�õ��ڴ�����
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϊ��̬��������ֹͼƬ��Explode���³�ʼ��ʱ���ٴ�Ӳ����קһ��
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
