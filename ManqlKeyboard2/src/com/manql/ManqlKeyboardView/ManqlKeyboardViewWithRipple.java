package com.manql.ManqlKeyboardView;

import java.util.Random;

import com.example.android.softkeyboard.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.View;

class Point {
	Point() {
		x = y = 0;
	}
	
	Point(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	int x;
	int y;
}
public class ManqlKeyboardViewWithRipple extends View implements Runnable {

	boolean m_isRunning = false;
	boolean m_isRain = false;
	
	boolean m_waveFlag = false;
	static final int MeshSize = 2;
	static final int MeshShift = 1;	// (MeshSize / 2 = MeshShift, just for speed up)
	
	Point p1, p2, p3, p4;
	Point pRowStart, pRowEnd, p, rowStartInc, rowEndInc, pInc;
	
	int m_width;
	int m_height;
	int m_meshWidth;
	int m_meshHeight;

	short[] m_buf1;
	short[] m_buf2;
	
	short[] m_bufDiffX;
	short[] m_bufDiffY;
	
	int[] m_bitmap1;
	int[] m_bitmap2;

	Thread m_thread;

	int m_preX;
	int m_preY;
	
	Random random;

	
	public ManqlKeyboardViewWithRipple(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ManqlKeyboardViewWithRipple(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public ManqlKeyboardViewWithRipple(Context context) {
		super(context);
		init();
	}
	
	private void init()
	{

		random = new Random();
		
		Bitmap image_original = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.keyboard_design_revised);
		Bitmap image = Bitmap.createScaledBitmap(image_original, 320, 183, true);
		
		m_width = image.getWidth();
		m_height = image.getHeight();
		
		Log.i("LOG", "width" + m_width + " height"+ m_height);
		
		m_meshWidth = m_width / MeshSize + 1;;
		m_meshHeight = m_height / MeshSize + 1;;
		
    	p = new Point();
    	p1 = new Point();
    	p2 = new Point();
    	p3 = new Point();
    	p4 = new Point();
    	pRowStart = new Point();
    	rowStartInc = new Point();
    	pRowEnd = new Point();
    	rowEndInc = new Point();
    	pInc = new Point();
		
    	m_buf1 = new short[m_meshWidth * (m_meshHeight)];
		m_buf2 = new short[m_meshWidth * (m_meshHeight)];
		m_bufDiffX = new short[m_meshWidth * (m_meshHeight)];
		m_bufDiffY = new short[m_meshWidth * (m_meshHeight)];
		
		m_bitmap1 = new int[m_width * m_height];
		m_bitmap2 = new int[m_width * m_height];

		image.getPixels(m_bitmap1, 0, m_width, 0, 0, m_width, m_height);

		start();
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(m_bitmap2, 0, m_width, 0, 0, m_width, m_height,
				false, null);
	}
	
	public void processKeyDown(int keyCode, KeyEvent event)
	{
		int action = event.getAction();
		
		if (KeyEvent.ACTION_DOWN == action) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_0:
				m_isRain = !m_isRain;
				break;
			}
		}
	}

	public void processTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int)(event.getX());
		int y = (int)(event.getY());

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			dropStone(x, y, 25, 10);
			m_preX = x;
			m_preY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			bresenhamDrop(m_preX, m_preY, x, y, 4, 40);
			m_preX = x;
			m_preY = y;
			break;
		}
	}

	public void start() {
		m_isRunning = true;
		m_thread = new Thread(this);
		m_thread.start();
	}

	public void stop() {
		m_isRunning = false;
	}

	public void resume() {
		m_isRunning = true;
	}

	public void destroy() {
		stop();

		m_thread.interrupt();
	}

	public void run() {

		while (m_isRunning) {
			try {
				Thread.sleep(10);
			}
			catch(Exception e) {
				e.printStackTrace();
			};
			
			if (m_isRain) {
				int x = 10 + random.nextInt() % (m_width - 20);
				int y = 10 + random.nextInt() % (m_height - 20);
				dropStone(x, y, 3, 80);
			}

			rippleSpread();

			rippleRender();

			postInvalidate();
		}
	}

	// ìº?????????¨êº¼ë¥???¬æ§¨ï¼?¿½???å¡?????êº¼ë?ëµ¨ë?å¯§ê???????êº¼ë?ï¼??
	// 		X0' =ï¼?1 + X2 + X3 + X4ï¼? 2 - X0
	//  +----x3----+
	//  +    |     +
	//  +    |     +
	//	x1---x0----x2
	//  +    |     +
	//  +    |     +
	//  +----x4----+
	//
    void rippleSpread()
    {
    	m_waveFlag = false;

    	int i = 0, offset = 0;
    	m_buf2[0] = (short) (((m_buf1[1] + m_buf1[m_meshWidth]) >> 1) - m_buf2[0]);

        // first column
        for (int y = 1; y < m_meshHeight - 1; ++y) {
            i += m_meshWidth;
            m_buf2[i] = (short) (((m_buf1[i + 1] + m_buf1[i - m_meshWidth]
                    + m_buf1[i + m_meshWidth]) >> 1) - m_buf2[i]);

            m_buf2[i] -= (m_buf2[i] >> 5);
            m_waveFlag |= (m_buf2[i] != 0);
        }

        // first row
        for (i = 1; i < m_meshWidth - 1; ++i) {
        	m_buf2[i] = (short) (((m_buf1[i - 1] + m_buf1[i + 1]
                    + m_buf1[i + m_meshWidth]) >> 1) - m_buf2[i]);

        	m_buf2[i] -= (m_buf2[i] >> 5);
        	m_waveFlag |= (m_buf2[i] != 0);
        }
        
    	for (int y = 1; y < m_meshHeight - 1; ++y) {
    		offset += m_meshWidth;
    		for (int x = 1; x < m_meshWidth - 1; ++x) {
    			i = offset + x;

                // êº¼ì???¿½:ï¿½è?????·ë??¨êº¼ë¥?µ¨?¨å?ê³???¼ë???º¼ë¥?
                // X0' =ï¼?1 + X2 + X3 + X4ï¼? 2 - X0
                //
    			m_buf2[i] = (short)(((m_buf1[i - 1] + m_buf1[i + 1]
                        + m_buf1[i - m_meshWidth]
                        + m_buf1[i + m_meshWidth]) >> 1)
                        - m_buf2[i]);

                // êº¼ì???? 1/32
                //
                m_buf2[i] -= (m_buf2[i] >> 5);
                
    			m_waveFlag |= (m_buf2[i] != 0);
    		}
    	}
    	
    	if (m_waveFlag){
    		m_waveFlag = false;
    		offset = 0;
	    	for (int y = 1; y < m_meshHeight - 1; ++y) {
	    		offset += m_meshWidth;
	    		for (int x = 1; x < m_meshWidth - 1; ++x) {
	    			i = offset + x;
	    			m_bufDiffX[i] = (short)((m_buf2[i + 1] - m_buf2[i - 1]) >> 3);
	    			m_bufDiffY[i] = (short)((m_buf2[i + m_meshWidth] - m_buf2[i - m_meshWidth]) >> 3);
	    		
	    			m_waveFlag |= (m_bufDiffX[i] != 0 || m_bufDiffY[i] != 0);
	    		}
	    	}
    	}
    	
    	//?¥ë»£êº¼ì????ë»????
    	short[] temp = m_buf1;
    	m_buf1 = m_buf2;
    	m_buf2 = temp;
    }

	void rippleRender()
	{	
		int px = 0, py = 0, dx = 0, dy = 0;
		int index = 0, offset = 0, pyOffset = 0;
		
		for (int j = 1; j < m_meshHeight; ++j) {
			offset += m_meshWidth;
			for (int i = 1; i < m_meshWidth; ++i) {		
				index = offset + i;
				p1.x = m_bufDiffX[index - m_meshWidth - 1];
				p1.y = m_bufDiffY[index - m_meshWidth - 1];
				p2.x = m_bufDiffX[index - m_meshWidth];
				p2.y = m_bufDiffY[index - m_meshWidth];
				p3.x = m_bufDiffX[index - 1];
				p3.y = m_bufDiffY[index - 1];
				p4.x = m_bufDiffX[index];
				p4.y = m_bufDiffY[index];
				
				pRowStart.x = p1.x << MeshShift;
				pRowStart.y = p1.y << MeshShift;
				rowStartInc.x = p3.x - p1.x;
				rowStartInc.y = p3.y - p1.y; 
				
				pRowEnd.x = p2.x << MeshShift;
				pRowEnd.y = p2.y << MeshShift;
				rowEndInc.x = p4.x - p2.x;
				rowEndInc.y = p4.y - p2.y;
	
				py = (j - 1) << MeshShift;
				for (int y = 0; y < MeshSize; ++y) {
					p.x = pRowStart.x;
					p.y = pRowStart.y;
					
					// scaled by MeshSize times
					pInc.x = (pRowEnd.x - pRowStart.x) >> MeshShift;
					pInc.y = (pRowEnd.y - pRowStart.y) >> MeshShift;
					
					px = (i - 1) << MeshShift;
					pyOffset = py * m_width;
	    			for (int x = 0; x < MeshSize; ++x) {
	    				dx = px + p.x >> MeshShift;
	    				dy = py + p.y >> MeshShift;
	    				
	    				if ((dx >= 0) && (dy >= 0) && (dx < m_width) && (dy < m_height) ) {
	    					m_bitmap2[pyOffset + px] = m_bitmap1[(dy * m_width) + dx];
	        			}
	        			else {
	        				m_bitmap2[pyOffset + px] = m_bitmap1[pyOffset + px];
	        			}
	
	        			p.x += pInc.x;
	        			p.y += pInc.y;
	        			++px;
	    			}
	    			
	    			pRowStart.x += rowStartInc.x;
	    			pRowStart.y += rowStartInc.y;
	    			pRowEnd.x += rowEndInc.x;
	    			pRowEnd.y += rowEndInc.y;
	    			++py;
				}
			}
		}
	}

	// æ§¨ì?è¿??å½?º¼ï¼??ì³¬ê·¹ä¼??å½??æ«????º¼?½ï?ì½±ì?ï¥??è¹¶ë??¨å?æ«?°»???åº«ï?
	// è¿???¨êº¼?½ë???¬¼ëµ¨ì?ì¢????º«?¨ê??ºëµ¨???åº«ë?????¼å?ë°????
	// ä¹?³¬??¤«??§£êº¼ì????ë»????bufï¼???????º«????¨ë????å¯§ëª¸ë§??"??°§??ï¼?
	// ???  buf[x, y] = -n???ë²??ï¤??n ?¨ë????ï¼?2 ~ 128ï¼??????¹ë????
	// stoneSize 	: êº¼é?ê³??
	// stoneWeight 	: êº¼é?ì½??
	//
    void dropStone(int x, int y, int stoneSize, int stoneWeight) {
    	// ?¸ë?éº??è§???³í?ìº¥ë????
    	if ((x + stoneSize) > m_width || (y + stoneSize) > m_height
    			|| (x - stoneSize) < 0 || (y - stoneSize) < 0) {
    		return;
    	}

    	int mx = 0, my = 0;
      	int value = stoneSize * stoneSize;
    	short weight = (short)-stoneWeight;
		for (int posx = x - stoneSize; posx < x + stoneSize;) {
		    for (int posy = y - stoneSize; posy < y + stoneSize;) {
		        if ((posx - x) * (posx - x) + (posy - y) * (posy - y) < value) {
		        	my = posy >> MeshShift;
		        	mx = posx >> MeshShift;
		            m_buf1[m_meshWidth * my  + mx] = weight;
		        }
		        
		        posy += MeshSize;
		    }
		    
		    posx += MeshSize;
		}

		resume();
	}

    void dropStoneLine(int x, int y, int stoneSize, int stoneWeight) {
	    // ?¸ë?éº??è§???³í?ìº¥ë????
	    if ((x + stoneSize) > m_width || (y + stoneSize) > m_height
	        || (x - stoneSize) < 0 || (y - stoneSize) < 0) {
	        return;
	    }
	    
	    int mx = 0, my = 0;
	    for (int posx = x - stoneSize; posx < x + stoneSize; ++posx) {
		    for (int posy = y - stoneSize; posy < y + stoneSize; ++posy) {
	            my = posy >> MeshShift;
	        	mx = posx >> MeshShift;
	            m_buf1[m_meshWidth * my  + mx] = -600;
	        }
	    }

	    resume();
	}

    // xs, ys : ??¿¦???xe, ye : ??²º???size : êº¼é?ê³??ï¼?eight : êº¼é?ì½??
    void bresenhamDrop(int xs, int ys, int xe, int ye, int size, int weight) {
	    int dx = xe - xs;
	    int dy = ye - ys;
	    dx = (dx >= 0) ? dx : -dx;
	    dy = (dy >= 0) ? dy : -dy;

	    if (dx == 0 && dy == 0) {
	    	dropStoneLine(xs, ys, size, weight);
	    }
	    else if (dx == 0) {
	    	int yinc = (ye - ys != 0) ? 1 : -1;
	        for(int i = 0; i < dy; ++i){
	        	dropStoneLine(xs, ys, size, weight);
	            ys += yinc;
	        }
	    }
	    else if (dy == 0) {
	    	int xinc = (xe - xs != 0) ? 1 : -1;
	        for(int i = 0; i < dx; ++i){
	        	dropStoneLine(xs, ys, size, weight);
	            xs += xinc;
	        }
	    }
	    else if (dx > dy) {
	        int p = (dy << 1) - dx;
	        int inc1 = (dy << 1);
	        int inc2 = ((dy - dx) << 1);
	        int xinc = (xe - xs != 0) ? 1 : -1;
	        int yinc = (ye - ys != 0) ? 1 : -1;

	        for(int i = 0; i < dx; ++i) {
	        	dropStoneLine(xs, ys, size, weight);
	            xs += xinc;
	            if (p < 0) {
	                p += inc1;
	            }
	            else {
	                ys += yinc;
	                p += inc2;
	            }
	        }
	    }
	    else {
	        int p = (dx << 1) - dy;
	        int inc1 = (dx << 1);
	        int inc2 = ((dx - dy) << 1);
	        int xinc = (xe - xs != 0) ? 1 : -1;
	        int yinc = (ye - ys != 0) ? 1 : -1;

	        for(int i = 0; i < dy; ++i) {
	        	dropStoneLine(xs, ys, size, weight);
	            ys += yinc;
	            if (p < 0) {
	                p += inc1;
	            }
	            else {
	                xs += xinc;
	                p += inc2;
	            }
	        }
	    }

	    resume();
	}
}
