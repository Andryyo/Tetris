package com.example.tetris;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Tetris_view extends View {

	Rect bounds;
	private final byte rows = 12;
	private final byte columns = 20;
	private int Score;
	private Timer timer = null;
	private Figure main_figure;
	private Figure next_figure;
	private byte new_figure;
	private byte[][] pool;
	private boolean action=false; 
	private boolean game_is_over;
	private boolean pause = true;
	private int block_height;
	private int block_width;
	private Paint paint;
	public Tetris_view(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}


	public void init()
	{
		timer  = new Timer();
		Move task = new Move();
		pool = new byte[rows+8][columns+8];
		game_is_over = false;
		new_figure = 0;
		Score = 0;
		for (int i=4;i<rows+4;i++)
			for (int j=4;j<columns+4;j++)
				pool[i][j] = 0;
		for (int j=4;j<columns+6;j++)
			{
				pool[3][j] = 1;
				pool[rows+4][j] = 1; 
			}
		for (int i=0;i<rows+4;i++)
				pool[i][3] = 1;
		next_figure = new Figure();
		main_figure = new Figure();
		paint = new Paint();
		bounds = new Rect();
		timer.schedule(task, 300, 400);
	}	
	public void onDraw(Canvas canvas)
	{
		canvas.getClipBounds(bounds);
		block_height = bounds.height()/columns;
		block_width = bounds.width()/(rows+4);
		paint.setColor(Color.BLACK);
		canvas.drawColor(Color.WHITE);
		for (int i=4;i<rows+4;i++)
			for (int j=4;j<columns+4;j++)			
				if (pool[i][j]==1) 
					canvas.drawRect((i-4)*block_width+1, bounds.height()-(j-3)*block_height+1, 
							(i-3)*block_width-1, bounds.height()-(j-4)*block_height-1, paint);
		for (int i=0;i<4;i++) 
			canvas.drawRect((main_figure.data[i][0]+main_figure.x-4)*block_width+1, 
					bounds.height()-(main_figure.data[i][1]-3+main_figure.y)*block_height+1,
					(main_figure.data[i][0]-3+main_figure.x)*block_width-1, 
					bounds.height()-(main_figure.data[i][1]-4+main_figure.y)*block_height-1, paint);
		paint.setColor(Color.GRAY);
		canvas.drawRect(block_width*rows, 0, bounds.width(), bounds.height(), paint);
		paint.setColor(Color.BLACK);
		for (int i=0;i<4;i++)
				canvas.drawRect((rows+next_figure.data[i][0])*block_width+1, 
						bounds.height()-(columns+next_figure.data[i][1]-4)*block_height+1, 
						(rows+1+next_figure.data[i][0])*block_width-1, 
						bounds.height()-(columns-5+next_figure.data[i][1])*block_height-1, paint);
		paint.setColor(Color.GREEN);
		paint.setTextSize(block_height);
		canvas.drawText(Integer.toString(Score), rows*block_width, 7*block_height, paint);
		if (game_is_over)	canvas.drawText("Game Over", 10, bounds.height()/2, paint);
		if (pause)	canvas.drawText("Pause", 10, bounds.height()/2, paint);
	}
	public boolean onTouchEvent(MotionEvent me)
	{
		if ((me.getAction()==MotionEvent.ACTION_DOWN)&&(!game_is_over))
		{
			action = true;
			float x = me.getX();
			float y = me.getY();
			int height = bounds.height();
			int width = bounds.width();
			if (y<3*height/4)
			{
				if (x<=width/5)
					main_figure.move_left(pool);
				else if (x>=4*width/5)
					main_figure.move_right(pool); 
				else
					main_figure.rotate(pool);
			}
				else 
					if (new_figure == 0) main_figure.drop(pool); 
			action = false;
		invalidate();
		}
		return true;
	}
	private class Move extends TimerTask {
		public void run()
		{
			while (action)
				;
			if (main_figure.down(pool) == false)
			{
				if (new_figure == 0)
					new_figure = 2;
				else new_figure--;
			}
			if (new_figure == 1) 
			{
				main_figure.print(pool);
				delete();
				main_figure = new Figure(next_figure);
				next_figure = new Figure();
				new_figure = 0;
			}
			postInvalidate();
		}
	}
	private void delete()
	{
			action = true;
			byte x = 0;
			boolean combo; 
			for (int j=4;j<columns+4;j++)
			{
				combo = true;
				for (int i=4;i<rows+4;i++)
					if (pool[i][j] == 0) combo = false;
				if (combo)
				{
					for (int k=j;k<columns+4;k++)
						for (int l=4;l<rows+4;l++)
							pool[l][k] = pool[l][k+1];
					x++;
					j--;
				}
			}			
			switch (x)
			{
			case 1:Score+=100; break;
			case 2:Score+=300; break;
			case 3:Score+=700; break;
			case 4:Score+=1500; break;
			}
			action = false;
			postInvalidate();
	}	
	
	public void Game_Over()
	{
		timer.cancel();
		game_is_over = true;
		postInvalidate();
	}
	void Pause()
	{
		if (!pause)
		{
			timer.cancel();
			pause = true;
			postInvalidate();
		}
		else
		{
			timer.cancel();
			timer = new Timer();
			Move task = new Move();
			timer.schedule(task, 0, 400);
			pause = false;
		}
	}
	private class Figure {
		private byte data[][];
		private int x,y;
		public Figure()
		{
			x = rows/2+2;
			y = columns+3;
			Random r = new Random();
			byte type = (byte)r.nextInt(7);
			switch (type)
			{
			case 0: data = new byte[][] {{1,0},{1,1},{1,2},{1,3}}; break;
			case 1: data = new byte[][] {{0,3},{0,2},{1,2},{2,2}}; break;
			case 2: data = new byte[][] {{1,1},{2,1},{3,1},{3,2}}; break;
			case 3: data = new byte[][] {{1,1},{1,2},{2,1},{2,2}}; break;
			case 4: data = new byte[][] {{0,1},{1,1},{1,2},{2,2}}; break;
			case 5: data = new byte[][] {{0,1},{1,1},{2,1},{1,2}}; break;
			case 6: data = new byte[][] {{0,2},{1,2},{1,1},{2,1}}; break;
			}
			if (crossing(pool))
			{
				Game_Over();
			}
		}
		public Figure(Figure copy)
		{
			data = new byte[4][2];
			for (int i=0;i<4;i++)
			{
			data[i][0] = copy.data[i][0];
			data[i][1] = copy.data[i][1];
			}
			x = copy.x;
			y = copy.y;
		}
		public boolean rotate(byte[][] pool)
		{
			byte buf;
			Figure figure = new Figure(this);
			for (byte i = 0;i<4;i++)
			{
				buf = figure.data[i][0];
				figure.data[i][0] = figure.data[i][1];
				figure.data[i][1] = (byte) (3-buf);
			}
			if (!figure.crossing(pool))
				for (byte i = 0;i<4;i++)
				{
					buf = data[i][0];
					data[i][0] = data[i][1];
					data[i][1] = (byte) (3-buf);
				}
			else return false;
			return true;
		}
		public boolean move_left(byte[][]pool)
		{
			x--;
			if (crossing(pool))
				x++;
			else return true;
			return false;
		}
		public boolean move_right(byte[][] pool)
		{
			x++;
			if (crossing(pool))
				x--;
			else return true;
			return false;
		}
		public boolean down(byte[][] pool)
		{
			Figure figure = new Figure(this);
			figure.y--;
			if (figure.crossing(pool))
			{
				if (!figure.move_left(pool))
				{
					if (!figure.move_right(pool))
					{
						new_figure = 1;
						return true;
					}
				}
				return false;
			}
			else y--;
			return true;
		}
		public void drop(byte[][] pool)
		{
			do
				y--;
			while (!crossing(pool));
			y++;
		}
		public boolean crossing(byte[][] pool)
		{
			for (int i=0;i<4;i++)
					if (pool[data[i][0]+x][data[i][1]+y]==1) return true;
			return false;
		}
		public void print(byte[][] pool)
		{
			for (int i=0;i<4;i++)
				pool[data[i][0]+x][data[i][1]+y]=1;
		}
	}
}